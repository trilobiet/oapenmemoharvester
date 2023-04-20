package org.oapen.memoproject.dataingestion.metadata;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.oapen.memoproject.dataingestion.jpa.PersistenceService;
import org.oapen.memoproject.dataingestion.jpa.entities.ExportChunk;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Implementation of ChunkIngester. Chunks are either read from a large file containing
 * all chunks (ingestAll) or from a web request per chunk (ingestForHandles) 
 * containing only chunks for a provided list of handles.
 * 
 * After retrieving the chunks they are sent to a PersistenceService instance to be persisted.
 * 
 * @author acdhirr
 *
 */
@Component
public class ChunksIngesterService implements ChunksIngester {
	
	/* Default value, re-download file after x days (usually there is no need for this other
	 * then during development: the file will be downloaded just once).
	 */
	private int daysExpiration = 1;
	private int batchSize = 1000;
	
	private final ExportsDownloader downloader;
	private final PersistenceService persistenceService;

	private static final Logger logger = LoggerFactory.getLogger(ChunksIngesterService.class);

	/**
	 * Constructor 
	 * 
	 * @param persistenceService
	 * @param downloader
	 */
	public ChunksIngesterService(PersistenceService persistenceService, ExportsDownloader downloader) {
		
		this.persistenceService = persistenceService;
		this.downloader = downloader;
	}
	
	/** 
	 * 
	 * @return number of days after which a new download will be initiated (for ingestAll())
	 * (default = 5)
	 */
	public int getDaysExpiration() {
		return daysExpiration;
	}

	/**
	 * @param daysExpiration sets number of days after which a new download will be initiated (for ingestAll())
	 */
	public void setDaysExpiration(int daysExpiration) {
		this.daysExpiration = daysExpiration;
	}

	/**
	 * @return number of records to keep in memory before flushing out to the persistence service
	 * (default is 1000).
	 */
	public int getBatchSize() {
		return batchSize;
	}

	/**
	 * @param batchSize sets number of records to keep in memory before flushing out to the persistence service
	 */
	public void setBatchSize(int batchSize) {
		this.batchSize = batchSize;
	}
	
	
	/**
	 * for every ExportType (4) run ingestAll and combine the resulting 
	 * lists of ingested handles to a single list of unique handles
	 */
	@Override
	public List<String> ingestAll() throws IngestException {
		
		Set<String> hndls = new HashSet<>();
		
		for (ExportType type: downloader.getExportTypes() ) {
			
			logger.info("Ingesting for {}...", type);
			List<String> lst = ingestAll(type);
			hndls.addAll(lst);
		}
		
		return new ArrayList<>(hndls);
	}
	

	@Override
	public List<String> ingestAll(ExportType type) throws IngestException {

		List<String> ingestedHandles = new ArrayList<>();
		
		File file = getDownloadedFile(type);
		
		if (this.isNewDownloadNeeded(file)) file = downloadFile(type);
		
		FileChunker fc = new FileChunker(file,type);
		Set<ExportChunk> batch = new HashSet<>(batchSize);

		try {
			
			fc.chunkify(
					
				// Consumer to handle chunk	
				c -> {
				
					ExportChunkable chunkable = getChunkable(type,c);
					
					if (chunkable.isValid()) {
	
						ExportChunk exportChunk = new ExportChunk();
						exportChunk.setType( chunkable.getType().name() );
						exportChunk.setContent(	chunkable.getContent() );
						exportChunk.setHandleTitle( chunkable.getHandle().get() );
						batch.add(exportChunk);
					}

					if (batch.size() > batchSize) {
						// save and clear batch
						ingestedHandles.addAll( saveBatch(batch) );
						batch.clear(); 
					}
					
				}, 
			
				// Number of lines to skip (first line is header for csv/tsv)
				type.skipLines() 
			);
			
		} catch (FileNotFoundException e) {
			
			throw new IngestException(e);
		}
		
		// final batch
		ingestedHandles.addAll(saveBatch(batch));
		
		return ingestedHandles;
	}
	
	/**
	 * for every ExportType (4) run ingestAll and combine the resulting 
	 * lists of ingested handles to a single list of unique handles
	 */
	@Override
	public List<String> ingestForHandles(List<String> handles) {
		
		Set<String> hndls = downloader.getExportTypes().stream()
			.flatMap(type -> ingestForHandles(handles, type).stream())
			.collect(Collectors.toSet());

		return new ArrayList<>(hndls);
	}

	
	@Override
	public List<String> ingestForHandles(List<String> handles, ExportType type) {
		
		Set<ExportChunk> completedChunks = new HashSet<>();
		
		handles.forEach(handle -> {
			
			// First get from the ExportChunk the URL value that points to the content to be downloaded 
			Optional<ExportChunk> oc = persistenceService.getExportChunks(handle).stream()
				.filter(chunk -> chunk.getType().toLowerCase().equals(type.lowerCaseName()))
				.findFirst();
			
			if (oc.isPresent()) {
				try {
					ExportChunk c = oc.get();
					URL url = new URL(c.getUrl());
					// Download the content containing the chunk
					String chunk = downloader.getAsString(url);
					c.setContent(chunk);
					// And update the chunk TO
					completedChunks.add(c);
				}
				catch (IOException e) {
					// Apparently there's not a valid url in the contents field, so just skip it
					logger.warn("Could not ingest {} chunk for {}.", type, handle);
				} 
			}
		});
		
		// Save the completed chunks
		List<ExportChunk> savedChunks = persistenceService.saveExportChunks(completedChunks);
		
		// Return their title_handle values as a list
		return savedChunks.stream()
			.map(chunk -> chunk.getHandleTitle())
			.collect(Collectors.toList());
	}
	
	
	
	// Save a batch of ExportChunks and clear the batch
	private List<String> saveBatch(Set<ExportChunk> batch) {
		
		List<String> handles = new ArrayList<>();
		
		if (!batch.isEmpty()) {
			List<ExportChunk> q = persistenceService.saveExportChunks(batch);
			q.forEach(cq -> handles.add(cq.getHandleTitle()));
		}	
		
		return handles;
	}
	
	
	// Get downloaded file from file system
	private File getDownloadedFile(ExportType type) {
		
		String path = downloader.getDirectory() + "/exports." + type.lowerCaseName();
		return new File(path);
	}
	
	
	// Save a new download to file system
	private File downloadFile(ExportType type) throws IngestException {
		
		try {
			downloader.download(type);
			return getDownloadedFile(type);
		} catch (IOException e) {
			throw new IngestException(e);
		}
	}
	
	// factory for ExportTypes
	private ExportChunkable getChunkable(ExportType type, String content) {
		
		switch (type) { 
			case MARCXML: return new MARCXMLChunk(content);
			case ONIX: return new ONIXChunk(content);
			case RIS: return new RISChunk(content);
			case KBART: return new KBARTChunk(content);
			default: return null;
		}		
		
	}
	
	// Check if the downloaded file is not too old
	private boolean isNewDownloadNeeded(File file) {
		
		if (!file.exists()) return true;
		else if (file.exists()) {
			long lastModified = file.lastModified();
			LocalDate downloadDate = Instant.ofEpochMilli(lastModified).atZone(ZoneId.systemDefault()).toLocalDate();
			LocalDate nowDate = LocalDate.now();
			if (ChronoUnit.DAYS.between(downloadDate, nowDate) > daysExpiration) return true;
		}			
		return false;
	}

	
	@Override
	public String toString() {
		return "ChunksIngesterService [daysExpiration=" + daysExpiration + ", batchSize=" + batchSize + ", downloader="
				+ downloader + ", persistenceService=" + persistenceService + "]";
	}
	


}

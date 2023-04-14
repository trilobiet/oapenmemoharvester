package org.oapen.memoproject.dataingestion.metadata;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.oapen.memoproject.dataingestion.jpa.PersistenceService;
import org.oapen.memoproject.dataingestion.jpa.entities.ExportChunk;
import org.springframework.stereotype.Component;

@Component
public class ChunksIngesterService implements ChunksIngester {
	
	// Default value, re-download file after x days
	private int daysExpiration = 5;
	private int batchSize = 1000;
	
	private final Downloader downloader;
	private final Map<String,URL> exportsUrls;
	private final PersistenceService persistenceService;
	
	public ChunksIngesterService(PersistenceService persistenceService, Map<String,URL> exportsUrls, Downloader downloader) {
		
		this.persistenceService = persistenceService;
		this.exportsUrls = exportsUrls;
		this.downloader = downloader;
	}
	
	// private static final Logger logger = LoggerFactory.getLogger(ChunksIngesterService.class);


	public int getDaysExpiration() {
		return daysExpiration;
	}

	public void setDaysExpiration(int daysExpiration) {
		this.daysExpiration = daysExpiration;
	}

	public int getBatchSize() {
		return batchSize;
	}

	public void setBatchSize(int batchSize) {
		this.batchSize = batchSize;
	}
	
	
	@Override
	// for every type (4) run ingestAll and combine the resulting 
	// lists of ingested handles to a single list of unique handles
	public List<String> ingestAll() throws IngestException {
		
		Set<String> hndls = new HashSet<>();
		
		for (String type: exportsUrls.keySet() ) {
			List<String> lst = ingestAll(ExportType.valueOf(type));
			hndls.addAll(lst);
		}
		
		return new ArrayList<>(hndls);
	}
	

	@Override
	// TODO introduce IngestException
	public List<String> ingestAll(ExportType type) throws IngestException {

		List<String> ingestedHandles = new ArrayList<>();
		
		File file = getDownloadedFile(type);
		
		if (this.isNewDownloadNeeded(file)) file = reDownloadFile(type);
		
		FileChunker fc = new FileChunker(file,type);
		Set<ExportChunk> batch = new HashSet<>(batchSize);
		fc.chunkify(c -> {
			
			ExportChunkable chunkable = getChunkable(type,c);
			
			if (chunkable.isValid()) {

				ExportChunk exportChunk = new ExportChunk();
				exportChunk.setType( chunkable.getType().name() );
				exportChunk.setContent(	chunkable.getContent() );
				exportChunk.setHandleTitle( chunkable.getHandle().get() );
				batch.add(exportChunk);
			}
			
			if (batch.size() > batchSize) ingestedHandles.addAll(saveBatch(batch));
		});
		
		// final batch
		ingestedHandles.addAll(saveBatch(batch));
		
		return ingestedHandles;
	}
	
	
	@Override
	// for every type (4) run ingestForhandles and combine the resulting 
	// lists of ingested handles to a single list of unique handles
	public List<String> ingestForHandles(List<String> handles) {

		Set<String> hndls = exportsUrls.keySet().stream()
			.flatMap(type -> ingestForHandles(handles, ExportType.valueOf(type)).stream())
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
					URL url = new URL(c.getContent());
					// Download the content containing the chunk
					String chunk = downloader.getAsString(url);
					c.setContent(chunk);
					// And update the chunk TO
					completedChunks.add(c);
				}
				catch (IOException e) {
					// Apparently there's not a valid url in the contents field, so just skip it
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
			batch.clear(); // a side effect
		}	
		
		return handles;
	}
	
	
	// Get downloaded file from file system
	private File getDownloadedFile(ExportType type) {
		
		String path = downloader.getDirectory() + "/exports." + type.lowerCaseName();
		return new File(path);
	}
	
	
	// Save a new download to file system
	private File reDownloadFile(ExportType type) throws IngestException {
		
		URL url = exportsUrls.get(type.name());
		
		try {
			downloader.download(url);
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
		
		if (file.exists()) {
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
				+ downloader + ", exportsUrls=" + exportsUrls + ", persistenceService=" + persistenceService + "]";
	}
	


}

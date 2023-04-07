package org.oapen.memoproject.dataingestion.metadata;

import java.io.File;
import java.io.IOException;
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

import org.oapen.memoproject.dataingestion.Orchestrator;
import org.oapen.memoproject.dataingestion.jpa.PersistenceService;
import org.oapen.memoproject.dataingestion.jpa.entities.ExportChunk;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ChunksIngesterService implements ChunksIngester {
	
	// Default value, re-download file after x days
	private int daysExpiration = 5;
	private int batchSize = 1000;
	private Downloader downloader;
	
	@Value("${app.url.exports}")
	private Map<String,String> exportsUrls;
	
	private PersistenceService persistenceService;
	
	public ChunksIngesterService(PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}

	public ChunksIngesterService(PersistenceService persistenceService, Map<String,String> exportsUrls, Downloader downloader) {
		this.persistenceService = persistenceService;
		this.exportsUrls = exportsUrls;
		this.downloader = downloader;
	}
	
	private static final Logger logger = 
		LoggerFactory.getLogger(Orchestrator.class);


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
	
	public Downloader getDownloader() {
		return downloader;
	}

	public void setDownloader(Downloader downloader) {
		this.downloader = downloader;
	}

	@Override
	public List<String> ingestAll(ExportType type) {

		logger.info("Ingesting chunks from downloads");
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
	

	@Override
	public List<String> ingestForHandles(List<String> handles, ExportType type) {
		
		Set<ExportChunk> completedChunks = new HashSet<>();
		
		handles.forEach(handle -> {
			
			Optional<ExportChunk> oc = persistenceService.getExportChunks(handle).stream()
				.filter(chunk -> chunk.getType().toLowerCase().equals(type.lowerCaseName()))
				.findFirst();
			
			if (oc.isPresent()) {
				
				ExportChunk c = oc.get();
				String url = c.getContent();
				try {
					String chunk = downloader.getAsString(url);
					c.setContent(chunk);
					completedChunks.add(c);
				}
				catch (IOException e) {
					
				} 
			}
		});
		
		List<ExportChunk> savedChunks = persistenceService.saveExportChunks(completedChunks);
		
		return savedChunks.stream()
			.map(chunk -> chunk.getHandleTitle())
			.collect(Collectors.toList());
	}
	
	
	// Get downloaded file from file system
	private File getDownloadedFile(ExportType type) {
		
		String path = downloader.getDirectory() + "/exports." + type.lowerCaseName();
		return new File(path);
	}
	
	
	// Save a new download to file system
	private File reDownloadFile(ExportType type) {
		
		String url = exportsUrls.get(type.lowerCaseName());
		
		try {
			downloader.download(url);
		} catch (IOException e) {
			logger.error("Could not download new export file from " + type + " to " + downloader.getDirectory());
		}
		
		return getDownloadedFile(type);
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

}

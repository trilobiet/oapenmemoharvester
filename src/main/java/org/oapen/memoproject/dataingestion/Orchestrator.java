package org.oapen.memoproject.dataingestion;

import java.net.MalformedURLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.oapen.memoproject.dataingestion.appstatus.AppStatus;
import org.oapen.memoproject.dataingestion.appstatus.PropertiesAppStatusService;
import org.oapen.memoproject.dataingestion.harvest.HarvestException;
import org.oapen.memoproject.dataingestion.harvest.ListRecordsURLComposer;
import org.oapen.memoproject.dataingestion.harvest.OAIHarvesterImp;
import org.oapen.memoproject.dataingestion.harvest.RecordListHandler;
import org.oapen.memoproject.dataingestion.jpa.PersistenceService;
import org.oapen.memoproject.dataingestion.metadata.ChunksIngester;
import org.oapen.memoproject.dataingestion.metadata.IngestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
/**
 * Main Controller for a (daily, n-daily, weekly etc.) harvesting cycle.
 *  
 * @author acdhirr
 *
 */
public class Orchestrator implements CommandLineRunner {
	
	@Value("${app.path.app-status}")
	private String propFileName;
	
	@Value("${app.path.oaipath}")
	private String oaiPath;

	private static final Logger logger = 
			LoggerFactory.getLogger(Orchestrator.class);
	
	private AppStatus status;
	private ListRecordsURLComposer urlComposer;
	private OAIHarvesterImp harvester;
	
	@Autowired
	RecordListHandler recordListHandler;
	
	@Autowired
	PersistenceService persistenceService;
	
	@Autowired
	ChunksIngester chunksIngesterService;
	
	public Orchestrator() {}


	@Override
	public void run(String... args) throws MalformedURLException {

		status = new PropertiesAppStatusService(propFileName);
		urlComposer = new ListRecordsURLComposer(oaiPath);
		harvester = new OAIHarvesterImp(urlComposer, recordListHandler);
		
		// Set the RstHandler to write each resumption token to status, so we can pick up from there in case of failure 
		harvester.setRstHandler(rst -> {
			status.setResumptionToken(rst.token);
		});
		
		List<String> harvestedHandles = new ArrayList<>();
		List<String> ingestedHandles = new ArrayList<>();
		
		logger.info("\n======================= Starting Harvest & Ingest Cycle =======================");
		logger.info(status.toString());
		
		/* 
		 * Start harvesting. Always start from lastHarvestDay, even if there is a resumptionToken
		 * in the status because of an earlier interrupted harvest. We need the full list of 
		 * handles to download ALL updated export chunks later on, which may not be available on an
		 * exceptional condition forcing the whole cycle to stop, but still leaving a ResumptionToken. 
		 */
		logger.info("Harvesting from date {}", status.getLastHarvestDay());
		harvestedHandles = harvestFromlastHarvestDay();
		
		// at least some handles have been ingested
		if (!harvestedHandles.isEmpty()) {
			
			logger.info("Harvested {} titles", harvestedHandles.size());
			
			// update status file
			status.setLastHarvestDay(LocalDate.now());
			status.setResumptionToken("");
		}
		else {
			logger.info("Nothing to harvest");
		}
		
		// Now continue with export chunks: first see if the downloaded bulk is ingested
		if (!status.isExportChunksDownloadsIngested()) {
			
			logger.info("Ingesting export chunks from downloaded files");
			ingestedHandles = ingestChunksFromDownload();
			
			status.setExportChunksDownloadsIngested(true);
			// consider the download to be the first ingestion
			status.setLastChunkIngestionDay(LocalDate.now()); 
		}	
		// otherwise get the export chunks only for the titles that have just been ingested
		else if (!harvestedHandles.isEmpty()) {
			
			logger.info("Ingesting export chunks from web requests");
			ingestedHandles = ingestChunksFromHandleList(harvestedHandles);
		}	
		
		if (!ingestedHandles.isEmpty()) {
			
			logger.info("Ingested export chunks for {} handles (effective number maybe lower due to deleted titles).", ingestedHandles.size());
			status.setLastChunkIngestionDay(LocalDate.now());
		}
		
		logger.info("\n======================= Finished Harvest & Ingest Cycle =======================");
	}
	
	
	private List<String> harvestFromlastHarvestDay() {
	
		LocalDate fromDate = status.getLastHarvestDay();
		LocalDate untilDate = LocalDate.now().minusDays(7);
		
		try { 
			
			List<String> handles = harvester.harvest(fromDate, untilDate);
			logger.info("Harvested from date " + fromDate);
			return handles;
		} 
		catch (HarvestException e) { 
			
			logger.error(e.getMessage());
			return new ArrayList<>();
		}
	}


	private List<String> ingestChunksFromDownload() {
		
		try { 
			
			List<String> ingestedHandles = chunksIngesterService.ingestAll();
			logger.info("Ingested export chunks from downloads");
			return ingestedHandles;
		} 
		catch (IngestException e) {
			
			logger.error(e.getMessage());
			return new ArrayList<>();
		}
	}

	
	private List<String> ingestChunksFromHandleList(List<String> handles) {
		
		try {

			List<String> ingestedHandles = chunksIngesterService.ingestForHandles(handles);
			logger.info("Ingested export chunks from web requests");
			return ingestedHandles;
		} 
		catch (IngestException e) {
			
			logger.error(e.getMessage());
			return new ArrayList<>();
		}
	}
	
	

}

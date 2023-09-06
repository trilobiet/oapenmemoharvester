package org.oapen.memoproject.dataingestion;

import java.net.MalformedURLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.oapen.memoproject.dataingestion.appstatus.AppStatus;
import org.oapen.memoproject.dataingestion.appstatus.PropertiesAppStatusService;
import org.oapen.memoproject.dataingestion.harvest.HarvestException;
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

	@Value("${app.harvest.daysBack}")
	private int daysBackUntil;
	
	private static final Logger logger = 
			LoggerFactory.getLogger(Orchestrator.class);
	
	private AppStatus status;
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
		//urlComposer = new ListRecordsURLComposer(oaiPath);
		harvester = new OAIHarvesterImp(oaiPath, recordListHandler);
		
		// Set the RstHandler to write each resumption token to status (only for information) 
		harvester.setRstHandler(rst -> {
			status.setResumptionToken(rst.token);
		});
		
		List<String> harvestedHandles = new ArrayList<>();
		List<String> ingestedHandles = new ArrayList<>();
		
		// from and until in OAI are inclusive!
		// http://www.openarchives.org/OAI/openarchivesprotocol.html#Datestamp
		LocalDate fromDate = status.getLastHarvestDay().plusDays(1);
		
		daysBackUntil = args.length > 0? Integer.parseInt(args[0]): daysBackUntil;
		
		LocalDate untilDate = LocalDate.now().minusDays( daysBackUntil );
		
		if ( untilDate.isBefore(fromDate) ) {
			
			logger.warn("===> 'until' argument {} days back from now is still before lastHarvestDay+1", daysBackUntil);
		}
		else {	
		
			logger.info("\n======================= Starting Harvest & Ingest Cycle =======================");
			logger.info(status.toString());
			logger.info("daysBackUntil = {}", daysBackUntil);
			
			/* 
			 * Start harvesting. Always start from lastHarvestDay, even if there is a resumptionToken
			 * in the status because of an earlier interrupted harvest. We need the full list of 
			 * handles to download ALL updated export chunks later on, which may not be available on an
			 * exceptional condition forcing the whole cycle to stop, but still leaving a ResumptionToken. 
			 */
			logger.info("Harvesting from {} until {}", fromDate, untilDate);
		
			harvestedHandles = harvestFromlastHarvestDay(fromDate, untilDate);
			
			// at least some handles have been ingested
			if (!harvestedHandles.isEmpty())  
				logger.info("Harvested {} titles", harvestedHandles.size());
			else 
				logger.info("Nothing found to harvest");
			
			// Now continue with export chunks: first see if the downloaded bulk is ingested
			if (!status.isExportChunksDownloadsIngested()) {
				
				logger.info("Ingesting export chunks from downloaded files");
				ingestedHandles = ingestChunksFromDownload();
				
				status.setExportChunksDownloadsIngested(true);
			}	
			// otherwise get the export chunks only for the titles that have just been ingested
			else if (!harvestedHandles.isEmpty()) {
				
				logger.info("Ingesting export chunks from web requests");
				ingestedHandles = ingestChunksFromHandleList(harvestedHandles);
			}	
			
			if (!ingestedHandles.isEmpty()) {
				
				/* Uupdate status file ONLY when chunks have also been ingested succesfully. In case
				 * they're not we must reharvest to get the handles for the chunks not yet ingested.
				 */
				status.setLastHarvestDay(untilDate);
				status.setResumptionToken("");
				status.setLastChunkIngestionDay(LocalDate.now()); // also gets set when chunks are coming from downloads
				
				logger.info("Ingested export chunks for {} handles (effective number may be lower due to deleted titles).", ingestedHandles.size());
			}
			else {
				logger.info("No export chunks were ingested");
			}
			
			logger.info("\n======================= Finished Harvest & Ingest Cycle =======================");
			
		}	
	}
	
	
	private List<String> harvestFromlastHarvestDay(LocalDate fromDate, LocalDate untilDate) {
	
		try { 
			
			List<String> handles = harvester.harvest(fromDate, untilDate);
			logger.info("Harvested from {} until {}", fromDate, untilDate);
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

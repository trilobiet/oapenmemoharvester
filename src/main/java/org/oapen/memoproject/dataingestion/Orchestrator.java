package org.oapen.memoproject.dataingestion;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.oapen.memoproject.dataingestion.appstatus.AppStatus;
import org.oapen.memoproject.dataingestion.appstatus.PropertiesAppStatusService;
import org.oapen.memoproject.dataingestion.harvest.HarvestException;
import org.oapen.memoproject.dataingestion.harvest.ListRecordsURLComposer;
import org.oapen.memoproject.dataingestion.harvest.OAIHarvester;
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
public class Orchestrator implements CommandLineRunner {
	
	@Value("${app.path.app-status}")
	private String propFileName;
	
	@Value("${app.path.oaipath}")
	private String oaiPath;

	private static final Logger logger = 
			LoggerFactory.getLogger(Orchestrator.class);
	
	private AppStatus status;
	private ListRecordsURLComposer urlComposer;
	private OAIHarvester harvester;
	
	@Autowired
	RecordListHandler recordListHandler;
	
	@Autowired
	PersistenceService persistenceService;
	
	@Autowired
	ChunksIngester chunksIngesterService;
	
	public Orchestrator() {}


	@Override
	public void run(String... args) {

		status = new PropertiesAppStatusService(propFileName);
		urlComposer = new ListRecordsURLComposer(oaiPath);
		harvester = new OAIHarvesterImp(urlComposer, recordListHandler);
		
		// System.out.println(chunksIngesterService);
		
		List<String> handles;
		
		logger.info(status.toString());
		
		if (!status.getResumptionToken().isBlank())
			// Continue from a resumption token
			handles = harvestFromResumptionToken();
		else 
			// Continue from the day we left off
			handles = harvestFromlastHarvestDay();

		// at least some handles have been ingested
		if (!handles.isEmpty()) {
			
			// update status file
			status.setLastHarvestDay(LocalDate.now());
			status.setResumptionToken("");
		}
		
		// Now continue with export chunks: first see if the downloaded bulk is ingested
		if (!status.isExportChunksDownloadsIngested()) 
			ingestChunksFromDownload();
		
		// otherwise get the export chunks only for the titles that have just been ingested
		else if (!handles.isEmpty()) 
			ingestChunksFromHandleList(handles);
		
	}
	
	
	private List<String> harvestFromlastHarvestDay() {
	
		List<String> handles = new ArrayList<>();
		
		LocalDate fromDate = status.getLastHarvestDay();
		LocalDate untilDate = LocalDate.now().minusDays(7);
		
		try { 
			handles = harvester.harvest(fromDate, untilDate);
			logger.info("Harvested from date " + fromDate);
		} 
		catch (HarvestException e) { 
			logger.error(e.getMessage());
		}
		
		return handles;
	}

	
	private List<String> harvestFromResumptionToken() {
		
		List<String> handles = new ArrayList<>();
		String token = status.getResumptionToken();
		
		try { 
			handles = harvester.harvest(token);
			logger.info("Harvested from resumptionToken " + token);
		} 
		catch (HarvestException e) {
			logger.error(e.getMessage());
		}

		return handles;
	}
	
	
	private void ingestChunksFromDownload() {
		
		List<String> ingestedHandles = new ArrayList<>();
		
		try { 
			ingestedHandles = chunksIngesterService.ingestAll();
			logger.info(String.format("Ingested %n chunks from downloads", ingestedHandles.size()));
		} 
		catch (IngestException e) {
			logger.error(e.getMessage());
		}
		
	}

	
	private void ingestChunksFromHandleList(List<String> handles) {
		
		List<String> ingestedHandles = new ArrayList<>();
		
		try {
			ingestedHandles = chunksIngesterService.ingestForHandles(handles);
			logger.info(String.format("Ingested %n chunks from downloads", ingestedHandles.size()));
		} catch (IngestException e) {
			logger.error(e.getMessage());
		}
		
	}
	
	

}

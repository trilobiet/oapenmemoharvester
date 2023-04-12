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
import org.oapen.memoproject.dataingestion.metadata.ChunksIngesterService;
import org.oapen.memoproject.dataingestion.metadata.ExportType;
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

	@Value("${app.url.exportsurl}")
	private String exportsUrl;
	
	@Value("${app.path.downloads}")
	private String downloadsPath;
	
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
	ChunksIngesterService chunksIngesterService;
	
	public Orchestrator() {}


	@Override
	public void run(String... args) {

		status = new PropertiesAppStatusService(propFileName);
		urlComposer = new ListRecordsURLComposer(oaiPath);
		harvester = new OAIHarvesterImp(urlComposer, recordListHandler);
		
		List<String> handles;
		
		logger.info(status.toString());
		
		if (status.getResumptionToken().isBlank()) 
			handles = harvestFromlastHarvestDay();
		else 
			handles = harvestFromResumptionToken(); 

		if (!handles.isEmpty()) {
			
			status.setLastHarvestDay(LocalDate.now());
			status.setResumptionToken("");
		}
		
		if (!status.isExportChunksDownloadsIngested()) {
			ingestChunksFromDownload();
		}
		else if (!handles.isEmpty()) {
			ingestChunksFromHandleList(handles);
		}
		
	}
	
	
	private List<String> harvestFromlastHarvestDay() {
	
		List<String> handles = new ArrayList<>();
		
		LocalDate fromDate = status.getLastHarvestDay();
		LocalDate untilDate = LocalDate.now().minusDays(7);
		
		logger.info("Harvesting from " + fromDate);
		
		try {
			handles = harvester.harvest(fromDate, untilDate);
		} catch (HarvestException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		
		return handles;
	}

	
	private List<String> harvestFromResumptionToken() {
		
		List<String> handles = new ArrayList<>();
		
		String token = status.getResumptionToken();
		
		logger.debug("Harvesting from resumptionToken " + token);
		
		try {
			handles = harvester.harvest(token);
		} catch (HarvestException e) {
			logger.error(e.getMessage());
		}

		return handles;
	}
	
	
	private void ingestChunksFromDownload() {
		
		List<String> ingestedHandles = 
			chunksIngesterService.ingestAll(ExportType.MARCXML);
		
		logger.info(
			String.format("Ingested %n chunks from downloads", ingestedHandles.size()));
		
	}

	
	private void ingestChunksFromHandleList(List<String> handles) {
		
		List<String> ingestedHandles = 
			chunksIngesterService.ingestForHandles(handles, ExportType.MARCXML);
		
		logger.info(
			String.format("Ingested %n chunks from downloads", ingestedHandles.size()));
		
	}
	
	

}

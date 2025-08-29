package org.oapen.memoproject.dataingestion;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.oapen.memoproject.dataingestion.appstatus.AppStatus;
import org.oapen.memoproject.dataingestion.appstatus.PropertiesAppStatusService;
import org.oapen.memoproject.dataingestion.harvest.HarvestException;
import org.oapen.memoproject.dataingestion.harvest.OAIHarvesterImp;
import org.oapen.memoproject.dataingestion.harvest.RecordListHandler;
import org.oapen.memoproject.dataingestion.jpa.PersistenceService;
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

	@Value("${app.harvest.delay}")
	private int delay;
	
	private static final Logger logger = 
			LoggerFactory.getLogger(Orchestrator.class);
	
	private AppStatus status;
	private OAIHarvesterImp harvester;
	
	@Autowired
	RecordListHandler recordListHandler;
	
	@Autowired
	PersistenceService persistenceService;
	
	public Orchestrator() {}

	@Override
	public void run(String... args) throws MalformedURLException, URISyntaxException {

		status = new PropertiesAppStatusService(propFileName);
		//urlComposer = new ListRecordsURLComposer(oaiPath);
		harvester = new OAIHarvesterImp(oaiPath, recordListHandler, delay);
		
		// Set the RstHandler to write each resumption token to status (only for information) 
		harvester.setRstHandler(rst -> {
			status.setResumptionToken(rst.token);
		});
		
		List<String> harvestedHandles = new ArrayList<>();
		
		// from and until in OAI are inclusive!
		// http://www.openarchives.org/OAI/openarchivesprotocol.html#Datestamp
		LocalDate fromDate = status.getLastHarvestDay().plusDays(1);
		
		System.out.println(propFileName);
		System.out.println("FROM " + fromDate);
		
		daysBackUntil = args.length > 0? Integer.parseInt(args[0]): daysBackUntil;
		
		LocalDate untilDate = LocalDate.now().minusDays( daysBackUntil );
		
		Optional<String> resumptionToken = status.getResumptionToken() == null 
			? Optional.empty() 
			: Optional.of(status.getResumptionToken().trim()); 
		
		if ( untilDate.isBefore(fromDate) ) {
			
			logger.warn("===> 'until' argument {} days back from now is still before lastHarvestDay+1", daysBackUntil);
		}
		else {	
			
			if (status.isFullHarvest()) {
				
				logger.info("\n=============================== Cleaning DB ===================================");
				logger.info("\nNo previous harvest date found. This is a full harvest. Starting with a clean database.");
				
				persistenceService.deleteAll();
				
				logger.info("\nReady cleaning up database.");	
			}
		
			logger.info("\n======================= Starting Harvest & Ingest Cycle =======================");
			logger.info(status.toString());
			logger.info("daysBackUntil = {}", daysBackUntil);
			
			// Start harvesting either from resumptionToken or last harvest day. 
			if (resumptionToken.isPresent() && !resumptionToken.get().isBlank()) {
				logger.info("Harvesting from resumptionToken {}", resumptionToken.get());
				harvestedHandles = harvestFromResumptionToken(resumptionToken.get());
			}
			else {
				logger.info("Harvesting from {} until {}", fromDate, untilDate);
				harvestedHandles = harvestFromlastHarvestDay(fromDate, untilDate);
			}
			
			// at least some handles have been ingested
			if (!harvestedHandles.isEmpty())  
				logger.info("Harvested {} titles", harvestedHandles.size());
			else 
				logger.info("Nothing found to harvest");
			
			/* Update status */
			status.setLastHarvestDay(untilDate);
			status.setResumptionToken("");
			
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

	
	private List<String> harvestFromResumptionToken(String rst) {
		
		try { 
			
			List<String> handles = harvester.harvest(rst);
			logger.info("Harvested from resumptionToken {}", rst);
			return handles;
		} 
		catch (HarvestException e) { 
			
			logger.error(e.getMessage());
			return new ArrayList<>();
		}
	}
	
}

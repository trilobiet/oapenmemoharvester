package org.oapen.memoproject.dataingestion;

import java.time.LocalDate;

import org.oapen.memoproject.dataingestion.harvest.ListRecordsFromDateUrlComposer;
import org.oapen.memoproject.dataingestion.harvest.ListRecordsURLComposer;
import org.oapen.memoproject.dataingestion.harvest.OAIHarvesterImp;
import org.oapen.memoproject.dataingestion.harvest.RecordListHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(value=1)
public class ApplicationRunner implements CommandLineRunner {
	
	@Value("${app.path.harvestedfiles}")
	private String harvestPath;
	
	@Value("${app.path.oaipath}")
	private String oaiPath;
	
	@Autowired
	RecordListHandler recordListHandler;
	

	@Override
	public void run(String... args) throws Exception {
		
		harvest();
	}

	public void harvest() throws Exception {
		
		ListRecordsURLComposer harvestUrl = new ListRecordsFromDateUrlComposer(oaiPath,LocalDate.of(2023, 3, 1));
		
		OAIHarvesterImp harvester = new OAIHarvesterImp(harvestUrl, recordListHandler);
		
		System.out.println("HARVESTING...........................");
		
		harvester.harvest();

	}
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
}


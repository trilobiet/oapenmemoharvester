package org.oapen.memoproject.dataingestion;

import java.time.LocalDate;

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
	

	@Override
	public void run(String... args) throws Exception {
		
		harvest();
	}

	public void harvest() throws Exception {
		
		OAIURLComposer harvestUrl = new XOAIFromDateUrlComposer(oaiPath,LocalDate.of(2023, 2, 14));
		
		OAIHarvester harvester = new OAIHarvester(harvestUrl);
		
		System.out.println("HARVESTING...........................");
		
		harvester.harvest(
			doc -> System.out.println(doc.getElementsByTagName("element").getLength())
		);

	}
	
	
	
}


package org.oapen.memoproject.dataingestion;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Orchestrator implements CommandLineRunner {
	
	@Value("${app.path.app-status}")
	private String propFileName;
	
	public Orchestrator() {
		
		
		
	}
	
	
	public void run() {
		
		AppStatus status = new PropertiesAppStatusService(propFileName);
		
		System.out.println(status);
		
		
	}


	@Override
	public void run(String... args) throws Exception {
		run();
		
	}
	
	

}

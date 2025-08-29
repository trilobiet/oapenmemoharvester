package org.oapen.memoproject.config;

import org.oapen.memoproject.dataingestion.harvest.RecordListHandler;
import org.oapen.memoproject.dataingestion.harvest.RecordListHandlerImp;
import org.oapen.memoproject.dataingestion.jpa.JpaPersistenceService;
import org.oapen.memoproject.dataingestion.jpa.PersistenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class AppConfig {
	
	// private static final Logger logger = LoggerFactory.getLogger(AppConfig.class);	
	
	@Autowired
	public Environment env;	
	
	@Bean
	RecordListHandler getRecordlistHandler() {
		
		// OAPEN or DOABOOKS, default OAPEN
		Domain domain = env.getProperty("app.domain", Domain.class, Domain.OAPEN);
		return new RecordListHandlerImp(getPersistenceService(), domain);
	}
	
	@Bean
	PersistenceService getPersistenceService() {
		return new JpaPersistenceService();
	}

}

	
package org.oapen.memoproject.config;

import org.oapen.memoproject.dataingestion.harvest.RecordListHandler;
import org.oapen.memoproject.dataingestion.harvest.RecordListHandlerImp;
import org.oapen.memoproject.dataingestion.jpa.JpaPersistenceService;
import org.oapen.memoproject.dataingestion.jpa.PersistenceService;
import org.oapen.memoproject.dataingestion.metadata.ChunksIngester;
import org.oapen.memoproject.dataingestion.metadata.ChunksIngesterService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
	
	@Bean
	RecordListHandler getRecordlistHandler() {
		return new RecordListHandlerImp();
	}
	
	@Bean
	PersistenceService getPersistenceService() {
		return new JpaPersistenceService();
	}
	
	@Bean
	ChunksIngester getChunkIngester() {
		return new ChunksIngesterService(getPersistenceService());
	}
	

}

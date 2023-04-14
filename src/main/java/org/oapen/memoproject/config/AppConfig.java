package org.oapen.memoproject.config;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.oapen.memoproject.dataingestion.harvest.RecordListHandler;
import org.oapen.memoproject.dataingestion.harvest.RecordListHandlerImp;
import org.oapen.memoproject.dataingestion.jpa.JpaPersistenceService;
import org.oapen.memoproject.dataingestion.jpa.PersistenceService;
import org.oapen.memoproject.dataingestion.metadata.ChunksIngester;
import org.oapen.memoproject.dataingestion.metadata.ChunksIngesterService;
import org.oapen.memoproject.dataingestion.metadata.Downloader;
import org.oapen.memoproject.dataingestion.metadata.ExportsDownloader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class AppConfig {
	
	private static final Logger logger = 
			LoggerFactory.getLogger(AppConfig.class);	
	
	@Autowired
	public Environment env;	
	
	@Bean
	RecordListHandler getRecordlistHandler() {
		return new RecordListHandlerImp();
	}
	
	@Bean
	PersistenceService getPersistenceService() {
		return new JpaPersistenceService();
	}
	
	@Bean
	Downloader getDownloader() {
		return new ExportsDownloader(env.getProperty("app.path.exportsdir"));
	}
	
	@Value("#{${app.url.exports}}")
	private Map<String,String> exportsUrls;

	@Bean
	Map<String,URL> getExportsUrls() {
		
		Map<String, URL> map = new HashMap<>();
		
		for (String key : exportsUrls.keySet()) {
			String url = exportsUrls.get(key);
			try {
				map.put(key, new URL(url));
			} catch (MalformedURLException e) {
				logger.error("===> Could not use \"{}\" as a URL because it is malformed!",url);
				return null;
			}
		}
		
		return map;
	}
	
	@Bean
	ChunksIngester getChunkIngester() {
		
		return new ChunksIngesterService(
			getPersistenceService(),
			getExportsUrls(),
			getDownloader() 
		);
	}
	

}

	
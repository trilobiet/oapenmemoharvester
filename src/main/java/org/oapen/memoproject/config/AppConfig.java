package org.oapen.memoproject.config;

import java.io.File;
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
import org.oapen.memoproject.dataingestion.metadata.ExportType;
import org.oapen.memoproject.dataingestion.metadata.ExportsDownloader;
import org.oapen.memoproject.dataingestion.metadata.ExportsDownloaderImp;
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
		return new RecordListHandlerImp(getPersistenceService());
	}
	
	@Bean
	PersistenceService getPersistenceService() {
		return new JpaPersistenceService();
	}
	
	@Bean
	ExportsDownloader getDownloader() {
		return new ExportsDownloaderImp(
			getExportsUrls(),
			new File(env.getProperty("app.path.exportsdir"))
		);
	}
	
	@Value("#{${app.url.exports}}")
	public Map<String,String> exportsUrls;

	@Bean
	Map<ExportType,URL> getExportsUrls() {
		
		Map<ExportType, URL> map = new HashMap<>();
		
		for (String key : exportsUrls.keySet()) {

			String url = exportsUrls.get(key);
			
			try {
				ExportType type = ExportType.valueOf(key);
				map.put(type, new URL(url));
			} catch (MalformedURLException e) {
				logger.warn("===> Could not use \"{}\" as a URL because it is malformed!",url);
			} catch (IllegalArgumentException e) {
				logger.warn("===> Could not use \"{}\" as an ExportType!",key);
			}
		}
		
		return map;
	}
	
	@Bean
	ChunksIngester getChunkIngester() {
		
		return new ChunksIngesterService(
			getPersistenceService(),
			getDownloader() 
		);
	}
	

}

	
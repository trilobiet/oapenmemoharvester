package org.oapen.memoproject.dataingestion;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.DefaultPropertiesPersister;

public class PropertiesAppStatusService implements AppStatus {
	
	private final String propertiesFileName; 
	private final String LAST_HARVEST_DAY = "last_harvest_day";
	private final String RESUMPTION_TOKEN = "resumption_token";
	private final String IS_EXPORTCHUNKS_DOWNLOADS_INGESTED = "is_exportchunks_downloads_ingested";
	private final String IS_EXPORTCHUNKS_UPDATES_INGESTED = "is_exportchunks_updates_ingested";
	
	private static final Logger logger = 
		LoggerFactory.getLogger(PropertiesAppStatusService.class);
	
	/**
	 * Constructs an PropertiesAppStatusService writing to and
	 * reading from local file. 
	 * @param propertiesFileName Local file path to store the status.
	 */
	public PropertiesAppStatusService(String propertiesFileName) {
		this.propertiesFileName = propertiesFileName;
	}


	@Override
	public LocalDate getLastHarvestDay() {
		
		Optional<String> d = readValue(LAST_HARVEST_DAY);
		if (d.isPresent()) return LocalDate.parse(d.get());
		else return LocalDate.ofEpochDay(0); 
	}

	@Override
	public String getResumptionToken() {

		Optional<String> r = readValue(RESUMPTION_TOKEN);
		if (r.isPresent()) return r.get();
		else return ""; 
	}

	@Override
	public boolean isExportChunksDownloadsIngested() {

		Optional<String> b = readValue(IS_EXPORTCHUNKS_DOWNLOADS_INGESTED);
		if (b.isPresent()) return b.get()=="true"?true:false;
		else return false; 
	}

	@Override
	public boolean isExportChunksUpdatesIngested() {

		Optional<String> b = readValue(IS_EXPORTCHUNKS_UPDATES_INGESTED);
		if (b.isPresent()) return b.get()=="true"?true:false;
		else return false; 
	}

	@Override
	public void setLastharvestDay(LocalDate d) {

		storeValue(LAST_HARVEST_DAY, d.toString());
	}
	
	@Override
	public void setResumptionToken(String rst) {
		
		storeValue(RESUMPTION_TOKEN, rst);
	}

	@Override
	public void setExportChunksDownloadsIngested(boolean b) {
		
		storeValue(IS_EXPORTCHUNKS_DOWNLOADS_INGESTED, b?"true":"false");
	}

	@Override
	public void setExportChunksUpdatesIngested(boolean b) {
		
		storeValue(IS_EXPORTCHUNKS_UPDATES_INGESTED, b?"true":"false");
	}
	
	
	private void storeValue(String key, String value) {
		
		File f = initFile();

		try (OutputStream out = new FileOutputStream(f)){
		    DefaultPropertiesPersister p = new DefaultPropertiesPersister();
		    Properties props = new Properties();
		    props.setProperty(key, value);
		    p.store(props, out, "OAPEN-MEMO xoai harvester application state");
		} catch (IOException e) {
			logger.warn("Could not store " + key + " at " +	f.getAbsolutePath()	);
		}
	}
	

	private Optional<String> readValue(String key) {
		
		File f = initFile();
		
		try (InputStream in = new FileInputStream(f)){
			Properties props = new Properties();
			props.load(in);
			if (props.containsKey(key))	
				return Optional.of(props.getProperty(key));
			else 
				return Optional.empty();
		} catch (IOException e) {
			logger.warn("No key '" + key + "' found at " + f.getAbsolutePath());
			return Optional.empty();
		}
	}
	

	private File initFile() {
		
		File f = new File(propertiesFileName);
		File parent = f.getParentFile();
		if (!parent.exists()) parent.mkdirs();
		if (!f.exists())
			try {
				f.createNewFile();
				logger.warn("Created new {}.", propertiesFileName);
			} catch (IOException e) {
				logger.error("Failed to create new {}.", propertiesFileName);
				logger.error(e.getMessage());
			}
		return f;
	}


	@Override
	public String toString() {
		return "PropertiesAppStatusService [propertiesFileName=" + propertiesFileName + "]";
	}
	

	
	
}

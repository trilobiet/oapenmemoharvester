package org.oapen.memoproject.dataingestion.appstatus;

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

/**
 * Stores application state in a properties file
 *  
 * @author acdhirr
 *
 */
public class PropertiesAppStatusService implements AppStatus {
	
	private final String propertiesFileName; 
	private final String LAST_HARVEST_DAY = "last_harvest_day";
	private final String RESUMPTION_TOKEN = "resumption_token";
	
	private Optional<String> lastHarvestDay; 
	private Optional<String> resumptionToken;
	
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
		
		lastHarvestDay = readValue(LAST_HARVEST_DAY);
		if (lastHarvestDay.isPresent()) return LocalDate.parse(lastHarvestDay.get());
		else return LocalDate.ofEpochDay(0); 
	}

	@Override
	public boolean isFullHarvest() {
		
		lastHarvestDay = readValue(LAST_HARVEST_DAY);
		resumptionToken = readValue(RESUMPTION_TOKEN);
		
		if (
			lastHarvestDay.isPresent() 
			|| 
			(resumptionToken.isPresent() && !resumptionToken.get().isBlank())
		) 
			return false;
		else 
			return true; 
	}

	@Override
	public String getResumptionToken() {

		resumptionToken = readValue(RESUMPTION_TOKEN);
		if (resumptionToken.isPresent()) return resumptionToken.get();
		else return ""; 
	}
	
	@Override
	public void setLastHarvestDay(LocalDate d) {
		
		lastHarvestDay = Optional.of(d.toString());
		saveProperties();
	}
	
	@Override
	public void setResumptionToken(String rst) {
		
		resumptionToken = Optional.of(rst.toString());
		saveProperties();
	}
	
	private void saveProperties() {
		
		File f = initFile();

		try (OutputStream out = new FileOutputStream(f)){
			
		    DefaultPropertiesPersister p = new DefaultPropertiesPersister();
		    Properties props = new Properties();
		    
		    lastHarvestDay.ifPresentOrElse( 
		    	prop -> props.setProperty(LAST_HARVEST_DAY, prop), 
		    	() -> props.remove(LAST_HARVEST_DAY)
		    );

		    resumptionToken.ifPresentOrElse( 
		    	prop -> props.setProperty(RESUMPTION_TOKEN, prop), 
		    	() -> props.remove(RESUMPTION_TOKEN)
		    );
		    
		    p.store(props, out, "MEMO xoai harvester application state");
		    
		} catch (IOException e) {
			logger.warn("Could not save properties at " +	f.getAbsolutePath()	);
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
		return "PropertiesAppStatusService [propertiesFileName=" + propertiesFileName 
				+ " lastHarvestDay=" + getLastHarvestDay()
				+ " resumptionToken=" + getResumptionToken()
				+ "]";
	}
	
}

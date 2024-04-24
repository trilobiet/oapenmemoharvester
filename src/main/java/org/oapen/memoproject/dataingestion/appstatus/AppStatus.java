package org.oapen.memoproject.dataingestion.appstatus;

import java.time.LocalDate;

/**
 * Reader/writer for application state. Each cycle of running the application needs
 * to pick up where the previous cycle left. 
 * 
 * @author acdhirr
 *
 */
public interface AppStatus {
	
	/**
	 * @return date of last successful harvest. 
	 */
	LocalDate getLastHarvestDay();
	
	/**
	 * @return true if this is the first run and there is no lastHarvestDay. 
	 */
	boolean isFullHarvest();

	/**
	 * @return resumptionToken, in case of an interrupted harvest
	 */
	String getResumptionToken();
	
	/**
	 * @return true, if the initial bulk of export data is ingested via large file downloads 
	 */
	boolean isExportChunksDownloadsIngested();
	
	/**
	 * 
	 * @return date of last successful chunk ingestion
	 */
	LocalDate getLastChunkIngestionDay();

	/**
	 * @param d Last successful harvest day
	 */
	void setLastHarvestDay(LocalDate d);
	
	/**
	 * @param rst ResumptionToken to be saved in case of an interrupted harvest
	 */
	void setResumptionToken(String rst);
	
	/**
	 * @param b boolean indicating whether export chunks have been downloaded via large file download and ingested
	 */
	void setExportChunksDownloadsIngested(boolean b);

	/**
	 * 
	 * @param d Last successful chunk ingestion day
	 */
	void setLastChunkIngestionDay(LocalDate d);

	
}

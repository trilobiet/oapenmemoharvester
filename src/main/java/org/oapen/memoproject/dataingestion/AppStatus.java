package org.oapen.memoproject.dataingestion;

import java.time.LocalDate;

public interface AppStatus {
	
	/**
	 * @return date of last successfull harvest. 
	 */
	LocalDate getLastHarvestDay();
	
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
	 * @return true, if after a harvest, corresponding chunks are requested and ingested
	 */
	boolean isExportChunksUpdatesIngested();
	
	/**
	 * @param ym Last successfully ingested year/month number.
	 */
	void setLastharvestDay(LocalDate d);
	
	/**
	 * @param rst ResumptionToken to be saved in case of an interrupted harvest
	 */
	void setResumptionToken(String rst);
	
	/**
	 * @param b boolean indicating whether export chunks have been downloaded via large file download and ingested
	 */
	void setExportChunksDownloadsIngested(boolean b);
	
	/**
	 * @param b boolean indicating whether, after a harvest, corresponding export chunks have been requested and ingested
	 */
	void setExportChunksUpdatesIngested(boolean b);

}

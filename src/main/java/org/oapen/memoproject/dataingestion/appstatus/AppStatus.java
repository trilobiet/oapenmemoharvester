package org.oapen.memoproject.dataingestion.appstatus;

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
	 * @param ym Last successfully ingested year/month number.
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
	
}

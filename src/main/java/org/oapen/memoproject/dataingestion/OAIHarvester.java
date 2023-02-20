package org.oapen.memoproject.dataingestion;

import java.net.URL;

import org.w3c.dom.Document;

public interface OAIHarvester {
	
	/**
	 * Start harvesting. Harvesting will continue until no more ResumptionTokens are encountered.
	 * The handler will be invoked for each harvested page. When there are x ResumptionTokens,
	 * handler will be invoked x + 1 times. 
	 * 
	 * @param handler Handler function to process each Document 
	 * @throws HarvestException
	 */
	void harvest(RecordListHandler handler) throws HarvestException;
	
	/**
	 * Load an OAI XML document 
	 * 	  
	 * @param url
	 * @return A Document
	 * @throws Exception
	 */
	Document fetchDocument(URL url) throws Exception;	
	

}


package org.oapen.memoproject.dataingestion.harvest;

import java.time.LocalDate;
import java.util.List;

public interface OAIHarvester {
	
	/**
	 * Start harvesting. Harvesting will continue until no more ResumptionTokens are encountered.
	 * The handler will be invoked for each harvested page. When there are x ResumptionTokens,
	 * handler will be invoked x + 1 times. 

	 * @return a list of harvested titles by their handle (id)
	 * @throws HarvestException
	 */
	List<String> harvest() throws HarvestException;

	/**
	 * 
	 * @param fromDate
	 * @param untilDate
	 * @return a list of harvested titles by their handle (id)
	 * @throws HarvestException
	 */
	List<String> harvest(LocalDate fromDate, LocalDate untilDate) throws HarvestException;

	/**
	 * 
	 * @param rst ResumptionToken
	 * @return a list of harvested titles by their handle (id)
	 * @throws HarvestException
	 */
	List<String> harvest(String rst) throws HarvestException;

}


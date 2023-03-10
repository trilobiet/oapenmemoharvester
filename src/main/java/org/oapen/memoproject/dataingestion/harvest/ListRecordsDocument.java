package org.oapen.memoproject.dataingestion.harvest;

import java.util.List;
import java.util.Optional;

import org.w3c.dom.Element;

/**
 * Represents an XML Document that can be split in records and has an optional resumption token.
 * 
 * @author acdhirr
 *
 */
interface ListRecordsDocument {
	
	List<Element> getRecords();
	
	Optional<ResumptionToken> getResumptionToken();

}

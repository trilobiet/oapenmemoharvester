package org.oapen.memoproject.dataingestion;

import java.util.List;
import java.util.Optional;

import org.w3c.dom.Element;

public interface ListRecordsDocument {
	
	List<Element> getRecords();
	
	Optional<ResumptionToken> getResumptionToken();

}

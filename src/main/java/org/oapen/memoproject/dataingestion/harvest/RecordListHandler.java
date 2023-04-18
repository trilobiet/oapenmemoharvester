package org.oapen.memoproject.dataingestion.harvest;

import java.util.List;

import org.w3c.dom.Element;

// Single method interface to be used in lambdas
public interface RecordListHandler {
	
	/**
	 * Process a List of Elements. 
	 * Return a List of Strings.
	 * 
	 * @param elements List of elements to be processed
	 * @return resulting list of Strings
	 */
	List<String> process(List<Element> elements);
}

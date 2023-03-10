package org.oapen.memoproject.dataingestion.harvest;

import java.util.List;

import org.w3c.dom.Element;

// Single method interface to be used in lambdas
public interface RecordListHandler {
	
	void process(List<Element> elements);
}

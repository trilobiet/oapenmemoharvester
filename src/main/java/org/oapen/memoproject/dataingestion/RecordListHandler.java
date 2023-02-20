package org.oapen.memoproject.dataingestion;

import java.util.List;

import org.w3c.dom.Element;

// Single method interface to be used in lambdas
public interface RecordListHandler {
	
	void process(List<Element> elements);
}

package org.oapen.memoproject.dataingestion;

import org.w3c.dom.Document;

// Single method interface to be used in lambdas
public interface OAIDocumentHandler {
	
	void process(Document document);
}

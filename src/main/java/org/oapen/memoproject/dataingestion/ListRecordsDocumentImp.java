package org.oapen.memoproject.dataingestion;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ListRecordsDocumentImp implements ListRecordsDocument {
	
	private final Document document;

	public ListRecordsDocumentImp(Document document) {
		this.document = document;
	}

	@Override
	public List<Element> getRecords() {
		
		NodeList nl = document.getElementsByTagName("record");
		List<Element> elements = new ArrayList<>();
		
		for (int i = 0; i < nl.getLength(); i++) {
			Element el = (Element) nl.item(i);
	        elements.add(el);
		}
		
		return elements;
	}

	@Override
	public Optional<ResumptionToken> getResumptionToken() {

		NodeList nl = document.getElementsByTagName("resumptionToken");
		
		if (nl.getLength() > 0) {
			
			Node node = nl.item(0);
			
			ResumptionToken rt = new ResumptionToken(
				node.getTextContent(),
				node.getAttributes().getNamedItem("completeListSize").getTextContent(),
				node.getAttributes().getNamedItem("cursor").getTextContent()
			);
			
			if (node.getTextContent().isEmpty()) return Optional.empty();
			else return Optional.of(rt);
		}
		else return Optional.empty();
	
	}

}

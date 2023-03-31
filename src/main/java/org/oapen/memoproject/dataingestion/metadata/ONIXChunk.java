package org.oapen.memoproject.dataingestion.metadata;

import java.util.Optional;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class ONIXChunk implements ExportChunk {
	
	private final String chunk; 
	private final Optional<String> handle;
	private final Optional<String> id;
	
	public ONIXChunk(String chunk) {
		
		this.chunk = chunk;
		handle = Optional.empty();
		id = initId();
	}
	
	private Optional<String> initId() {
		
		Document record = new XMLStringConverter().stringToXMLDocument(chunk);
		NodeList nodes = record.getElementsByTagName("RecordReference");
        
        if (nodes.getLength() > 0) {
        	
        	String id = parseId(nodes.item(0).getTextContent());
        	return Optional.of(id);
        }
        else return Optional.empty();
		
	}
	
	private String parseId(String in) {
		
		// <- OAPEN-ID_5fd15d8e-628e-4e16-9dd8-f1b07b37efa7
		// -> 5fd15d8e-628e-4e16-9dd8-f1b07b37efa7 
		if (in.length() >= 36) return in.substring(in.length()-36);
		else return in;
	}
	
	@Override
	public Optional<String> getHandle() {
		return handle;
	}
	
	@Override
	public Optional<String> getId() {
		return id;
	}
	
}









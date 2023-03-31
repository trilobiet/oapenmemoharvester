package org.oapen.memoproject.dataingestion.metadata;

import java.util.Optional;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class MARCXMLChunk implements ExportChunk {
	
	private final String chunk; 
	private final Optional<String> handle;
	private final Optional<String> id;
	
	public MARCXMLChunk(String chunk) {
		
		this.chunk = chunk;
		handle = initHandle();
		id = Optional.empty();
	}
	
	private Optional<String> initHandle() {
			
		Document record = new XMLStringConverter().stringToXMLDocument(chunk);
        NodeList nodes = record.getElementsByTagName("marc:controlfield");
        
        if (nodes.getLength() > 0) {
        	
        	String id = parseHandle(nodes.item(0).getTextContent());
        	return Optional.of(id);
        }
        else return Optional.empty();
	}
	
	private String parseHandle(String in) {
		
		// <- https://library.oapen.org/handle/20.500.12657/42289 
		// -> 20.500.12657/42289 
		if (in.length() >= 18) return in.substring(in.length()-18);
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









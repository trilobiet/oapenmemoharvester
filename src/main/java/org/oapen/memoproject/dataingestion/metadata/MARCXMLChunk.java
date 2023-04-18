package org.oapen.memoproject.dataingestion.metadata;

import java.util.Optional;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 * A MARCXML Record representation.
 * 
 * @author acdhirr
 *
 */
public class MARCXMLChunk implements ExportChunkable {
	
	private final String content; 
	private final Optional<String> handle;
	
	public MARCXMLChunk(String content) {
		
		this.content = content;
		handle = initHandle();
	}
	
	private Optional<String> initHandle() {
			
		Document record = new XMLStringConverter().stringToXMLDocument(content);
        NodeList nodes = record.getElementsByTagName("marc:controlfield");
        
        if (nodes.getLength() > 0) {
        	
        	String handle = parseHandle(nodes.item(0).getTextContent());
        	return Optional.of(handle);
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
	public String getContent() {
		return content;
	}

	@Override
	public ExportType getType() {
		return ExportType.MARCXML;
	}
	
	@Override
	public boolean isValid() {
		
		return handle.isPresent();
	}

}









package org.oapen.memoproject.dataingestion.metadata;

import java.util.Optional;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 * A ONIX XML Record representation.
 * 
 * @author acdhirr
 *
 */
public class ONIXChunk implements ExportChunkable {
	
	private final String content; 
	private final Optional<String> handle;
	
	public ONIXChunk(String content) {
		
		this.content = content;
		handle = initHandle();
	}
	
	//TODO
	private Optional<String> initHandle() {
		
		Document record = new XMLStringConverter().stringToXMLDocument(content);
		NodeList nodes = record.getElementsByTagName("ResourceLink");
        
        if (nodes.getLength() > 0) {
        	
        	String id = parseHandle(nodes.item(0).getTextContent());
        	return Optional.of(id);
        }
        else return Optional.empty();
		
	}

	// TODO
	private String parseHandle(String in) {
		
		// <- https://library.oapen.org/bitstream/handle/20.500.12657/42289/9783653064650.pdf.jpg?sequence=3
		// -> 20.500.12657/42289 
		final String PREFIX = "handle";
		final String POSTFIX = "/";
		
		int start = in.indexOf(PREFIX);
		if (start > -1) in = in.substring(start + PREFIX.length() + 1);
		int end = in.lastIndexOf(POSTFIX);
		if (end > -1) in = in.substring(0,end);
		
		return in;
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
		return ExportType.ONIX;
	}

	@Override
	public boolean isValid() {
		
		return handle.isPresent();
	}

}









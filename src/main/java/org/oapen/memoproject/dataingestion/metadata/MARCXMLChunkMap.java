package org.oapen.memoproject.dataingestion.metadata;

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class MARCXMLChunkMap implements ExportChunkMap {
	
	private final String sourceData; 
	private final Map<String, String> map = new HashMap<>();
	private final XMLStringConverter converter = new XMLStringConverter();
	
	
	public MARCXMLChunkMap(String sourceData) {
		
		this.sourceData = sourceData;
		buildMap();
	}

	
	@Override
	public String getById(String id) {
		
		return map.get(id);
	}
	
	
	private void buildMap() {
		
		Document document = converter.stringToXMLDocument(sourceData);
		
		NodeList recordNodes = document.getElementsByTagName("marc:record");
		
		for (int i = 0; i < recordNodes.getLength(); i++) {
			
			Element record = (Element) recordNodes.item(i);
	        NodeList idNodes = record.getElementsByTagName("marc:controlfield");
	        
	        if (idNodes.getLength() > 0) {
	        	
	        	String id = parseId(idNodes.item(0).getTextContent());
	        	String chunk = converter.nodeToString(record);
	        	map.put(id, chunk);
	        }
		}
		
	}

	
	private String parseId(String in) {
		
		// <- https://library.oapen.org/handle/20.500.12657/42289 
		// -> 20.500.12657/42289 
		if (in.length() >= 18) return in.substring(in.length()-18);
		else return in;
	}
	

}









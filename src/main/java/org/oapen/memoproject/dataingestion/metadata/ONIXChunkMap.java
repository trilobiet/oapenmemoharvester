package org.oapen.memoproject.dataingestion.metadata;

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class ONIXChunkMap implements ExportChunkMap {
	
	private final String sourceData; 
	private final Map<String, String> map = new HashMap<>();
	private final XMLStringConverter converter = new XMLStringConverter();

	
	public ONIXChunkMap(String sourceData) {
		
		this.sourceData = sourceData;
		buildMap();
	}

	
	@Override
	public String getById(String id) {
		
		return map.get(id);
	}
	
	
	private void buildMap() {
		
		Document document = converter.stringToXMLDocument(sourceData);
		
		NodeList productNodes = document.getElementsByTagName("Product");
		
		for (int i = 0; i < productNodes.getLength(); i++) {
			
			Element record = (Element) productNodes.item(i);
	        NodeList idNodes = record.getElementsByTagName("RecordReference");
	        
	        if (idNodes.getLength() > 0) {
	        	
	        	String id = parseId(idNodes.item(0).getTextContent());
	        	String chunk = converter.nodeToString(record);
	        	map.put(id, chunk);
	        }
		}
		
	}
	
	private String parseId(String in) {
		
		// <- OAPEN-ID_5fd15d8e-628e-4e16-9dd8-f1b07b37efa7
		// -> 5fd15d8e-628e-4e16-9dd8-f1b07b37efa7 
		if (in.length() >= 36) return in.substring(in.length()-36);
		else return in;
	}
	
}









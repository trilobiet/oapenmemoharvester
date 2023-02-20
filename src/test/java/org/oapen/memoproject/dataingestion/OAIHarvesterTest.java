package org.oapen.memoproject.dataingestion;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class OAIHarvesterTest {
	
	public void test() throws ParserConfigurationException, SAXException, IOException {
		
		String s = "./src/test/resources/xoai-response-short.xml";
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document xml = db.parse(s);
		
	}

}

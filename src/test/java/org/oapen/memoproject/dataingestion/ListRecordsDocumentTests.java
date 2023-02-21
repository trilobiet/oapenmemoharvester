package org.oapen.memoproject.dataingestion;

import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class ListRecordsDocumentTests {
	
	private DocumentBuilder db;
	
	@BeforeEach
	public void setUp() throws ParserConfigurationException {

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		db = dbf.newDocumentBuilder();
	}
	
	@Test
	public void should_split_document_in_records() throws Exception {
		
		String s = "./src/test/resources/xoai-response-short.xml";
		Document xml = db.parse(s);
		ListRecordsDocument p = new ListRecordsDocumentImp(xml);
		
		assertTrue(p.getRecords().size()==3);
	}

	@Test
	public void should_return_resumptionToken_when_present() throws Exception {
		
		String s = "./src/test/resources/xoai-response-short-resumptionToken.xml";
		Document xml = db.parse(s);
		ListRecordsDocument p = new ListRecordsDocumentImp(xml);
		
		assertTrue(p.getResumptionToken().isPresent());
	}

	@Test
	public void should_not_return_resumptionToken_when_absent() throws Exception {
		
		String s = "./src/test/resources/xoai-response-short.xml";
		Document xml = db.parse(s);
		ListRecordsDocument p = new ListRecordsDocumentImp(xml);
		
		assertTrue(p.getResumptionToken().isEmpty());
	}
	

	@Test
	public void should_find_publisher_via_xpath_query() throws Exception {
		
		String s = "./src/test/resources/xoai-response-short.xml";
		Document xml = db.parse(s);
		ListRecordsDocument p = new ListRecordsDocumentImp(xml);
		
		XPath xpath = XPathFactory.newInstance().newXPath();
		
		Element record = p.getRecords().get(0);
		
		NodeList publishernodes = 
			(NodeList) xpath.evaluate("//element[@name='oapen.relation.isPublishedBy']/field[@name='handle']", record, XPathConstants.NODESET);
		
		assertTrue(publishernodes.item(0).getTextContent().equals("20.500.12657/22403"));
	}


}

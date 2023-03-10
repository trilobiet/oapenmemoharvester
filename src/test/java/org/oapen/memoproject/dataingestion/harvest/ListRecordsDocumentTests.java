package org.oapen.memoproject.dataingestion.harvest;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class ListRecordsDocumentTests {
	
	private final String xmldocument1 = TestConstants.xmldocument1;
	private final String xmldocumentResumptionToken = TestConstants.xmldocumentResumptionToken;
	
	private Document doc1;
	private Document docResumptionToken;

	private DocumentBuilder db;
	
	@BeforeEach
	public void setUp() throws ParserConfigurationException, SAXException, IOException {

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(false);
		db = dbf.newDocumentBuilder();
		
		doc1 = 
			db.parse(new InputSource( new StringReader( xmldocument1 ) ));
		docResumptionToken = 
			db.parse(new InputSource( new StringReader( xmldocumentResumptionToken ) ));
	}
	
	@Test
	public void should_split_document_in_records() throws Exception {
		
		ListRecordsDocument p = new ListRecordsDocumentImp(doc1);
		assertTrue(p.getRecords().size()>1);
	}

	@Test
	public void should_not_return_resumptionToken_when_absent() throws Exception {
		
		ListRecordsDocument p = new ListRecordsDocumentImp(doc1);
		assertTrue(p.getResumptionToken().isEmpty());
	}

	@Test
	public void should_return_resumptionToken_when_present() throws Exception {
		
		ListRecordsDocument p = new ListRecordsDocumentImp(docResumptionToken);
		assertTrue(p.getResumptionToken().isPresent());
	}

}

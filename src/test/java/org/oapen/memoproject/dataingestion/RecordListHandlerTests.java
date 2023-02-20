package org.oapen.memoproject.dataingestion;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class RecordListHandlerTests {
	
	@Test
	void testHandler() throws Exception {
		
		String s = "./src/test/resources/xoai-response-short.xml";
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document xml = db.parse(s);
		
		ListRecordsDocument lrdoc = new ListRecordsDocumentImp(xml);
		
		List<Element> elements = lrdoc.getRecords();
		
		RecordListHandler handler = new RecordListHandlerImp();
		handler.process(elements);
		
		// TODO test void method
		
		assertTrue(true);
	}

}

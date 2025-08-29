package org.oapen.memoproject.dataingestion.harvest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.oapen.memoproject.config.Domain;
import org.oapen.memoproject.dataingestion.harvest.oapen.OapenXOAIDocumentParser;
import org.oapen.memoproject.dataingestion.harvest.doabooks.DoabooksXOAIDocumentParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class RecordListHandlerTests {
	
	private final String xmldocument1 = TestConstants.xmldocument1;
	private DocumentBuilder db;
	private Document doc1;
	private Element el;
	
	@BeforeEach
	public void setUp() throws ParserConfigurationException, SAXException, IOException {

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(false);
		db = dbf.newDocumentBuilder();
		doc1 = db.parse(new InputSource( new StringReader( xmldocument1 ) ));
		el = doc1.getDocumentElement();
	}
	
	@Test
	void whenDomainIsOAPENPickDocumentParserForOAPEN() throws Exception {
		
		RecordListHandlerImp r = new RecordListHandlerImp(null, Domain.OAPEN);
		EntitiesSource m = r.getDocumentParser(el);
		//Assertions.assertThat(m.getClass().getName()).contains("oapen.OapenXOAIDocumentParser");//, CoreMatchers.containsString());
		assertEquals(m.getClass(), OapenXOAIDocumentParser.class);
	}

	@Test
	void whenDomainIsDOABOOKSPickDocumentParserForDOABOOKS() throws Exception {
		
		RecordListHandlerImp r = new RecordListHandlerImp(null, Domain.DOABOOKS);
		EntitiesSource m = r.getDocumentParser(el);
		//Assertions.assertThat(m.getClass().getName()).contains("doabooks.OapenXOAIDocumentParser");//, CoreMatchers.containsString());
		assertEquals(m.getClass(), DoabooksXOAIDocumentParser.class);
	}
	
	
}

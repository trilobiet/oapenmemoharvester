package org.oapen.memoproject.dataingestion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.oapen.memoproject.dataingestion.jpa.entities.Funder;
import org.oapen.memoproject.dataingestion.jpa.entities.Publisher;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ElementToEntitiesMapperTests {
	
	ElementToEntitiesMapper mapper;
	
	@BeforeEach
    void setUp() throws Exception {

		String s = "./src/test/resources/xoai-response-short.xml";
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document xml = db.parse(s);
		
		ListRecordsDocument lrdoc = new ListRecordsDocumentImp(xml);
		
		Element record = lrdoc.getRecords().get(1);
		
		this.mapper = new XpathElementToEntitiesMapper(record);
	}	
	

	@Test
	public void should_find_handler() throws MappingException {

		assertEquals("20.500.12657/60840", mapper.getHandle());
	}
	
	@Test
	public void should_find_sysId() throws MappingException {

		assertEquals("22222222-3aff-4e7b-9a1c-ce8c75fa9530", mapper.getSysId());
	}


	@Test
	public void should_find_publisher() throws MappingException {
		
		Publisher expectedPublisher = new Publisher("20.500.12657/22488","Springer Nature");
		Publisher foundPublisher = mapper.getPublisher();
		
		assertEquals(expectedPublisher, foundPublisher);
	}
	
	@Test
	public void should_find_funders() throws MappingException {
		
		Set<Funder> expectedFunders = new HashSet<>();
		expectedFunders.add(new Funder("20.500.12657/60839","Piet"));
		expectedFunders.add(new Funder("20.500.12657/61833","Klaas"));
		
		Set<Funder> foundFunders = mapper.getFunders();
		
		assertTrue(foundFunders.containsAll(expectedFunders));
	}
	
	
}

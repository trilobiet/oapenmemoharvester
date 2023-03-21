package org.oapen.memoproject.dataingestion.metadata;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class ExportChunkMapTests {
	
	@Test
	public void should_return_ONIX_record_string_by_id() {
		
		final String doc = TestConstants.onixdoc;
		
		ExportChunkMap map = new ONIXChunkMap(doc);
		String id = "5fd15d8e-628e-4e16-9dd8-f1b07b37efa7";
		String value = map.getById(id);
		
		// System.out.println(value);
		
		assertTrue(value.contains(id));
	}
	

	@Test
	public void should_return_MARCXML_record_string_by_id() {
		
		final String doc = TestConstants.marcxmldoc;
		
		ExportChunkMap map = new MARCXMLChunkMap(doc);
		
		String id = "20.500.12657/42289";
		String value = map.getById(id);
		
		// System.out.println(value);
		
		assertTrue(value.contains(id));
	}
	

	@Test
	public void should_return_RIS_record_string_by_id() {
		
		final String doc = TestConstants.risdoc;
		
		ExportChunkMap map = new RISChunkMap(doc);
		
		String id = "20.500.12657/37613";
		String value = map.getById(id);
		
		// System.out.println(value);
		
		assertTrue(value.contains(id));
	}
	
	
	@Test
	public void should_return_KBART_record_string_by_id() {
		
		final String doc = TestConstants.kbartdoc;
		
		ExportChunkMap map = new KBARTChunkMap(doc);
		
		String id = "20.500.12657/39385";
		String value = map.getById(id);
		
		// System.out.println(value);
		
		assertTrue(value.contains(id));
	}
	
}

package org.oapen.memoproject.dataingestion.metadata;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class ExportChunkableTests {
	
	@Test
	public void should_return_ONIX_record_id() {
		
		final String record = TestConstants.onixrecord;
		
		ExportChunkable chunk = new ONIXChunk(record);
		String expected = "20.500.12657/42289";
		String found = chunk.getHandle().get();
		
		// System.out.println(found);
		
		assertEquals(expected,found);
	}
	

	@Test
	public void should_return_MARCXML_record_handle() {
		
		final String record = TestConstants.marcxmlrecord;
		
		ExportChunkable chunk = new MARCXMLChunk(record);
		String expected = "20.500.12657/42289";
		String found = chunk.getHandle().get();
		
		// System.out.println(found);
		
		assertEquals(expected,found);
	}
	

	@Test
	public void should_return_RIS_record_handle() {
		
		final String record = TestConstants.risrecord;
		
		ExportChunkable chunk = new RISChunk(record);
		
		String expected = "20.500.12657/38065";
		String found = chunk.getHandle().get();
		
		// System.out.println(found);
		
		assertEquals(expected,found);
	}
	
	
	@Test
	public void should_return_KBART_record_handle() {
		
		final String record = TestConstants.kbartrecord;
		
		ExportChunkable chunk = new KBARTChunk(record);
		
		String expected = "20.500.12657/42289";
		String found = chunk.getHandle().get();
		
		// System.out.println(found);
		
		assertEquals(expected,found);
	}
	
}

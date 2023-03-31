package org.oapen.memoproject.dataingestion.metadata;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Stack;

import org.junit.jupiter.api.Test;

public class FileChunkifierTests {
	
	@Test
	public void test_chunk_counts_when_parsing_RIS_file() {
		
		FileChunkifier fp = new FileChunkifier("src/test/resources/test.ris", ExportType.RIS);
		Stack<String> st = new Stack<>();
		fp.chunkify(s -> st.add(s));
		
		int expectedLines = 6;
		int countedLines = st.size();

		assertEquals(expectedLines, countedLines);
	}

	@Test
	public void test_chunk_counts_when_parsing_MARCXML_file() {
		
		FileChunkifier fp = new FileChunkifier("src/test/resources/test.marcxml", ExportType.MARXXML);
		Stack<String> st = new Stack<>();
		fp.chunkify(s -> st.add(s));
		
		int expectedLines = 3;
		int countedLines = st.size();

		assertEquals(expectedLines, countedLines);
	}

	@Test
	public void test_chunk_counts_when_parsing_ONIX_file() {
		
		FileChunkifier fp = new FileChunkifier("src/test/resources/test.onix", ExportType.ONIXXML);
		Stack<String> st = new Stack<>();
		fp.chunkify(s -> st.add(s));
		
		int expectedLines = 7;
		int countedLines = st.size();

		assertEquals(expectedLines, countedLines);
	}
	
	@Test
	public void test_chunk_counts_when_parsing_KBART_TSV_file() {
		
		FileChunkifier fp = new FileChunkifier("src/test/resources/test.tsv", ExportType.KBART);
		Stack<String> st = new Stack<>();
		fp.chunkify(
			  s -> st.add(s)
			, 1  // skip header line
		);
		
		int expectedLines = 24; // first line skipped
		int countedLines = st.size();

		assertEquals(expectedLines, countedLines);
	}
	
	
}

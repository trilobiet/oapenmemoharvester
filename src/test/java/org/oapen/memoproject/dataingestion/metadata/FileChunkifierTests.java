package org.oapen.memoproject.dataingestion.metadata;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.util.Stack;

import org.junit.jupiter.api.Test;

public class FileChunkifierTests {
	
	@Test
	public void test_chunk_counts_when_parsing_RIS_file() {
		
		File file = new File("src/test/resources/test.ris");
		FileChunker fp = new FileChunker(file, ExportType.RIS);
		Stack<String> chunks = new Stack<>();
		fp.chunkify(chunk -> chunks.add(chunk));
		
		// chunks.forEach(l -> {System.out.println("============");System.out.println(l);});
		
		int expectedChunks = 6;
		int countedChunks = chunks.size();

		assertEquals(expectedChunks, countedChunks);
	}

	@Test
	public void test_chunk_counts_when_parsing_MARCXML_file() {
		
		File file = new File("src/test/resources/test.marcxml");
		FileChunker fp = new FileChunker(file, ExportType.MARCXML);
		Stack<String> chunks = new Stack<>();
		fp.chunkify(chunk -> chunks.add(chunk));
		
		// chunks.forEach(l -> {System.out.println("============");System.out.println(l);});
		
		int expectedChunks = 3;
		int countedChunks = chunks.size();

		assertEquals(expectedChunks, countedChunks);
	}

	@Test
	public void test_chunk_counts_when_parsing_ONIX_file() {
		
		File file = new File("src/test/resources/test.onix");
		FileChunker fp = new FileChunker(file, ExportType.ONIX);
		Stack<String> chunks = new Stack<>();
		fp.chunkify(chunk -> chunks.add(chunk));
		
		// chunks.forEach(l -> {System.out.println("============");System.out.println(l);});
		
		int expectedChunks = 7;
		int countedChunks = chunks.size();

		assertEquals(expectedChunks, countedChunks);
	}
	
	@Test
	public void test_chunk_counts_when_parsing_KBART_TSV_file() {
		
		File file = new File("src/test/resources/test.tsv");
		FileChunker fp = new FileChunker(file, ExportType.KBART);
		Stack<String> chunks = new Stack<>();
		fp.chunkify(
			  chunk -> chunks.add(chunk)
			, 1  // skip header line
		);
		
		// chunks.forEach(l -> {System.out.println("============");System.out.println(l);});
		
		int expectedChunks = 24; // first line skipped
		int countedChunks = chunks.size();

		assertEquals(expectedChunks, countedChunks);
	}
	
	
}

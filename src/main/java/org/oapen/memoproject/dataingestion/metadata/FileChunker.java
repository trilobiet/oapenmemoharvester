package org.oapen.memoproject.dataingestion.metadata;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Scanner;
import java.util.function.Consumer;

/**
 * A FileChunker takes a file and chunks its contents
 * according to the rules of the chosen ExportType (MARCXML, ONIX, RIS, KBART).
 * <br/>
 * Chunking is done streaming, no chunks are kept in memory, so there is no limit
 * to the size of the input file.
 * <br/>
 * Every chunk that is encountered is immediately handed over to a String Consumer
 * for further processing.  
 * 
 * @author acdhirr
 *
 */
public class FileChunker {
	
	private final File file;
	private final ExportType type;
	
	/**
	 * @param file File to be chunked
	 * @param type Expected chunk types to be returned 
	 */
	public FileChunker(File file, ExportType type) {

		this.file = file;
		this.type = type;
	}
	
	/**
	 * Chunkifies file as a stream and hands chunks to String processor. 
	 * 
	 * @param processor Consumer to handle each chunk as soon as it is available
	 * @throws FileNotFoundException 
	 */
	public void chunkify(Consumer<String> processor) throws IOException {
		
		chunkify(processor, 0);
	}
	
	
	public void chunkify(Consumer<String> processor, int skip) throws IOException {
		
		int line = -1;
		
		try (BufferedReader br = Files.newBufferedReader(file.toPath()) ) {
			
			String buffer;
			String chunk = "";
			   
			while ((buffer = br.readLine()) != null) {
				
				line ++;
				
				if (line < skip) continue;
			   
				if (buffer.trim().matches(type.chunkStart())) {
					chunk = "";
				}
				
				if (!buffer.trim().isBlank()) chunk += buffer + "\n";
				
				if (buffer.trim().matches(type.chunkEnd())) {
					if (chunk.trim() != "") processor.accept(chunk);
					chunk = "";
				}
			}
			   
		}
		
	}
	
	
	/**
	 * OLD uses Scanner
	 * 
	 * Chunkifies file as a stream and hands chunks to String processor. 
	 * 
	 * @param processor Consumer to handle each chunk as soon as it is available
	 * @param skip Number of file lines to skip (use for .csv type files) 
	 * @throws FileNotFoundException 
	 */
	public void _chunkify(Consumer<String> processor, int skip) throws FileNotFoundException {
		
		int line = -1;
		
		// try (Scanner scanner = new Scanner(file)) {
		try (Scanner scanner = new Scanner(file, "UTF-8")) {
			
			String chunk = "";

			while (scanner.hasNextLine()) {
				
				String buffer = scanner.nextLine();
				//System.out.println(buffer);
				
				line ++;
				
				if (line < skip) continue;
				
				if (buffer.trim().matches(type.chunkStart())) {
					chunk = "";
				}
				
				if (!buffer.trim().isBlank()) chunk += buffer + "\n";
				
				if (buffer.trim().matches(type.chunkEnd())) {
					if (chunk.trim() != "") processor.accept(chunk);
					chunk = "";
				}
			}
		} 
	}

	
	
}

package org.oapen.memoproject.dataingestion.metadata;

import java.io.File;
import java.io.FileNotFoundException;
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
	public void chunkify(Consumer<String> processor) throws FileNotFoundException {
	
		chunkify(processor, 0);
	}
	
	/**
	 * Chunkifies file as a stream and hands chunks to String processor. 
	 * 
	 * @param processor Consumer to handle each chunk as soon as it is available
	 * @param skip Number of file lines to skip (use for .csv type files) 
	 * @throws FileNotFoundException 
	 */
	public void chunkify(Consumer<String> processor, int skip) throws FileNotFoundException {
		
		int line = -1;
		
		try (Scanner scanner = new Scanner(file)) {
			
			String chunk = "";

			while (scanner.hasNextLine()) {
				
				String buffer = scanner.nextLine();
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

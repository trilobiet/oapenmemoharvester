package org.oapen.memoproject.dataingestion.metadata;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.function.Consumer;

public class FileChunkifier {
	
	private final String file;
	private final ExportType type;
	
	public FileChunkifier(String file, ExportType type) {

		this.file = file;
		this.type = type;
	}
	
	/**
	 * Chunkifies file as a stream and hands chunks to String processor. 
	 * 
	 * @param processor Consumer to handle each chunk as soon as it is available
	 */
	public void chunkify(Consumer<String> processor) {
	
		chunkify(processor, 0);

	}
	
	/**
	 * Chunkifies file as a stream and hands chunks to String processor. 
	 * 
	 * @param processor Consumer to handle each chunk as soon as it is available
	 * @param skip Number of file lines to skip (use for .csv type files) 
	 */
	public void chunkify(Consumer<String> processor, int skip) {
		
		int line = -1;
		
		try (Scanner scanner = new Scanner(new File(file))) {
			
			String chunk = "";

			while (scanner.hasNextLine()) {
				
				String s = scanner.nextLine();
				line ++;
				
				if (line < skip) continue;
				
				if (s.trim().matches(type.chunkStart())) {
					chunk = s;
				}
				else if (s.trim().matches(type.chunkEnd())) {
					String p = chunk + "\n" + s; //process(chunk + "\n"+s);
					if (p != "") processor.accept(p);
					chunk = "";
				}
				else chunk += "\n" + s;
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

}

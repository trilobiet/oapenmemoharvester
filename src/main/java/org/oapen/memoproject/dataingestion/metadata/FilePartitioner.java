package org.oapen.memoproject.dataingestion.metadata;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FilePartitioner {
	
	private final String file;
	private final String blockStartSign;
	private final String blockEndSign;
	
	public FilePartitioner(String file, String blockStartSign, String blockEndSign) {

		this.blockStartSign = blockStartSign;
		this.blockEndSign = blockEndSign;
		this.file = file;
	}
	
	public FilePartitioner(String file) {

		this.blockStartSign = "\n";
		this.blockEndSign = "\n";
		this.file = file;
	}
	
	
	
	/*
	 *     RIS: "", "ER.*"
	 *    ONIX: "<Product>", "</Product>"
	 * MARCXML: "<marc:record>", "</marc:record>"
	 * 
	 * 
	 */
	
	public void readFileLineByLine() {

		try (Scanner scanner = new Scanner(new File(file))) {
			
			String chunk = "";

			while (scanner.hasNextLine()) {
				
				String s = scanner.nextLine();
				
				if (s.trim().matches(blockStartSign)) {
					chunk = s;
				}
				else if (s.trim().matches(blockEndSign)) {
					process(chunk + "\n"+s);
					chunk = "";
				}
				else chunk += "\n"+s;
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	
	private void process(String s) {
		
		if (s != "") {
			System.out.println(s);
			System.out.println("====================================");
			System.out.println();
		}	
		
	}
	

}

package org.oapen.memoproject.dataingestion.metadata;

import org.junit.jupiter.api.Test;

public class FilePartitionerTests {
	
	@Test
	public void testSomething() {
		
		/*
		 *     RIS: "", "ER.*"
		 *    ONIX: "<Product>", "</Product>"
		 * MARCXML: "<marc:record>", "</marc:record>"
		 * 
		 * 
		 */
		
		FilePartitioner fp = new FilePartitioner("src/test/resources/test.ris","","ER.*");
		//FilePartitioner fp = new FilePartitioner("src/test/resources/test.marcxml","<marc:record>.*",".*</marc:record>");
		//FilePartitioner fp = new FilePartitioner("src/test/resources/test.onix","<Product>.*",".*</Product>");
		//FilePartitioner fp = new FilePartitioner("src/test/resources/test.tsv","",".*");
		fp.readFileLineByLine();
		
	}

}

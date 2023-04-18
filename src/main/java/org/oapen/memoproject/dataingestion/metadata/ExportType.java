package org.oapen.memoproject.dataingestion.metadata;

/**
 * Enum ExportType defines the available export types
 * and their chunk markers (start and end) as regular expressions.
 * 
 * @author acdhirr
 *
 */
public enum ExportType {
	
	MARCXML ( 
	   "<marc:record>.*" 
	  ,".*</marc:record>" 
	),
	ONIX ( 
	   "<Product>.*"    
	  ,".*</Product>"     
	),
	RIS ( 
	   ""      
	  ,"ER.*"             
	),
	KBART ( 
	   ""   
	  ,".*"
	  ,1 // skip first line containing column names
	)
	;
	
	private final String chunkStart;
	private final String chunkEnd;
	private final int skipLines;
	
	private ExportType(String cs, String ce) {
		
		this.chunkStart = cs;
		this.chunkEnd = ce;
		this.skipLines = 0;
	}
	
	private ExportType(String cs, String ce, int skipLines) {
		
		this.chunkStart = cs;
		this.chunkEnd = ce;
		this.skipLines = skipLines;
	}

	/**
	 * @return chunk start regex
	 */
	public String chunkStart() {
		return chunkStart;
	}
	
	/**
	 * @return chunk end regex
	 */
	public String chunkEnd() {
		return chunkEnd;
	}

	/**
	 * when parsing a text file into chunks an optional number of lines 
	 * can be skipped. Use this feature to skip the header line in .csv type files. 
	 * 
	 * @return number of lines to skip
	 */
	public int skipLines() {
		return skipLines;
	}
	
	/**
	 * @return lowercase name (MARCXML -> marcxml)
	 */
	public String lowerCaseName(){
        return name().toLowerCase();
    }
	
}

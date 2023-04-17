package org.oapen.memoproject.dataingestion.metadata;

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

	public String chunkStart() {
		return chunkStart;
	}
	
	public String chunkEnd() {
		return chunkEnd;
	}

	public int skipLines() {
		return skipLines;
	}
	
	public String lowerCaseName(){
        return name().toLowerCase();
    }
	
}

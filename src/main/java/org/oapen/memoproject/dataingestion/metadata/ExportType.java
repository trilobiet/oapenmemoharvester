package org.oapen.memoproject.dataingestion.metadata;

public enum ExportType {
	
	MARXXML ( 
	   "<marc:record>.*" 
	  ,".*</marc:record>" 
	),
	ONIXXML ( 
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
	)
	;
	
	private final String chunkStart;
	private final String chunkEnd;
	
	
	private ExportType(String cs, String ce) {
		
		this.chunkStart = cs;
		this.chunkEnd = ce;
	}
	
	public String chunkStart() {
		return chunkStart;
	}
	
	public String chunkEnd() {
		return chunkEnd;
	}
	
}

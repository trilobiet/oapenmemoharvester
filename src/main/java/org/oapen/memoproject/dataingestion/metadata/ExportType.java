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
	
	public String lowerCaseName(){
        return name().toLowerCase();
    }
	
}

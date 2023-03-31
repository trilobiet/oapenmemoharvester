package org.oapen.memoproject.dataingestion.metadata;

import java.util.Optional;

public class KBARTChunk implements ExportChunk {
	
	private final String chunk; 
	private final Optional<String> handle;
	private final Optional<String> id;
	
	public KBARTChunk(String chunk) {
		
		this.chunk = chunk;
		handle = initHandle();
		id = Optional.empty();
	}
	
	
	private Optional<String> initHandle() {
		
		String[] cols = chunk.split("\t");
		
		if (cols.length > 11) {
			return Optional.of(cols[11].trim());
		}
		else return Optional.empty();
	}


	@Override
	public Optional<String> getHandle() {
		return handle;
	}

	@Override
	public Optional<String> getId() {
		return id;
	}


}

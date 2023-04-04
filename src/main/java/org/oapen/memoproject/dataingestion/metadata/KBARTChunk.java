package org.oapen.memoproject.dataingestion.metadata;

import java.util.Optional;

public class KBARTChunk implements ExportChunkable {
	
	private final String content; 
	private final Optional<String> handle;
	
	public KBARTChunk(String content) {
		
		this.content = content;
		handle = initHandle();
	}
	
	
	private Optional<String> initHandle() {
		
		String[] cols = content.split("\t");
		
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
	public String getContent() {
		return content;
	}

	@Override
	public ExportType getType() {
		return ExportType.KBART;
	}


}

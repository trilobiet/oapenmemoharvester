package org.oapen.memoproject.dataingestion.metadata;

import java.util.Optional;

public interface ExportChunkable {
	
	String getContent();
	ExportType getType(); 
	Optional<String> getHandle();
	boolean isValid();
}

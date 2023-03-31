package org.oapen.memoproject.dataingestion.metadata;

import java.util.Optional;

public interface ExportChunk {
	
	Optional<String> getHandle();
	Optional<String> getId();
}

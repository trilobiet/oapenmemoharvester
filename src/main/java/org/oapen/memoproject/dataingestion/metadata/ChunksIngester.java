package org.oapen.memoproject.dataingestion.metadata;

import java.util.List;

public interface ChunksIngester {
	
	/**
	 * Ingest all available ExportChunks for a given type
	 * 
	 * @param type 
	 * @return list of inserted handles
	 */
	List<String> ingestAll(ExportType type);
	
	
	/**
	 * ingest ExportChunks for a given list of title handles
	 * 
	 * @param handles
	 * @return list of inserted handles
	 */
	List<String> ingestForHandles(List<String> handles, ExportType type);

}

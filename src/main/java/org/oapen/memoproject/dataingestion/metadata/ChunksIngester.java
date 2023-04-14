package org.oapen.memoproject.dataingestion.metadata;

import java.util.List;

public interface ChunksIngester {
	
	/**
	 * Ingest all available ExportChunks for all types
	 * 
	 * @return list of inserted handles
	 */
	List<String> ingestAll() throws IngestException;
	
	/**
	 * Ingest all available ExportChunks for a given type
	 * 
	 * @param type 
	 * @return list of inserted handles
	 */
	List<String> ingestAll(ExportType type) throws IngestException;
	
	/**
	 * ingest ExportChunks for a given list of title handles
	 * 
	 * @param handles
	 * @return list of inserted handles
	 */
	List<String> ingestForHandles(List<String> handles) throws IngestException;

	/**
	 * ingest ExportChunks for a given list of title handles and a give type
	 * 
	 * @param handles
	 * @param type
	 * @return list of inserted handles
	 */
	List<String> ingestForHandles(List<String> handles, ExportType type) throws IngestException;
}

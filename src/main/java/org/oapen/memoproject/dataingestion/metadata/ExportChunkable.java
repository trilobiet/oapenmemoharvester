package org.oapen.memoproject.dataingestion.metadata;

import java.util.Optional;

/**
 * ExportChunkables are String objects from which an ExportType can be extracted,
 * and a handle (id) String.  
 * <br/>
 * They represent Title records in any metadata format of an ExportType type (MARCXML, ONIX, KBART, RIS).
 *  
 * @author acdhirr
 *
 */
public interface ExportChunkable {
	
	/**
	 * Get the full String object (chunk)
	 * @return
	 */
	String getContent();
	
	/**
	 * Get the extracted export type (MARCXML, ONIX, KBART, RIS) 
	 * @return
	 */
	ExportType getType(); 
	
	/**
	 * Get the extracted handle String (20.500.12657/12345)
	 * @return
	 */
	Optional<String> getHandle();
	
	/**
	 * 
	 * @return true if the required data are present in the content
	 */
	boolean isValid();
}

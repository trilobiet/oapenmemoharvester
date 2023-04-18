package org.oapen.memoproject.dataingestion.metadata;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Set;

/**
 * Interface for a download service for export chunks
 * 
 * @author acdhirr
 *
 */
public interface ExportsDownloader {

	/**
	 * Download all export chunks for all types
	 * 
	 * @return List of resulting files, one for each export type (MARCXML, ONIX, KBART, RIS)
	 * @throws IOException if the download fails or could not be saved. 
	 */
	List<File> downloadAll() throws IOException;
	
	/**
	 * Download all exports chunks for a given type (MARCXML, ONIX, KBART, RIS) 
	 * 
	 * @param type
	 * @return File location of the downloaded file
	 * @throws IOException
	 */
	File download(ExportType type) throws IOException;
	
	/**
	 * Get the directory where downloaded export files are stored
	 * 
	 * @return Download directory
	 */
	File getDirectory();
	
	/**
	 * @return available export types
	 */
	Set<ExportType> getExportTypes();
	
	/**
	 * Get the response at url as a String. 
	 * Use this to directly request a single export chunk 
	 * from a URL, as a String
	 * 
	 * @return The chunk as a String
	 * @throws IOException
	 */
	String getAsString(URL url) throws IOException;

	

}

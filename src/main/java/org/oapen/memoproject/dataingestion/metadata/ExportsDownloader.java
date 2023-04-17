package org.oapen.memoproject.dataingestion.metadata;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Set;

public interface ExportsDownloader {

	/**
	 * Download all exports for all types
	 * 
	 * @throws IOException if the download fails or could not be saved. 
	 */
	List<File> downloadAll() throws IOException;
	
	/**
	 * 
	 * @param type
	 * @return
	 * @throws IOException
	 */
	File download(ExportType type) throws IOException;
	
	/**
	 * Get the download directory
	 * @return Download directory
	 */
	File getDirectory();
	
	/**
	 * @return available export types
	 */
	Set<ExportType> getExportTypes();
	
	/**
	 * Get the response at url as a String. 
	 * 
	 * @return
	 * @throws IOException
	 */
	String getAsString(URL url) throws IOException;

	

}

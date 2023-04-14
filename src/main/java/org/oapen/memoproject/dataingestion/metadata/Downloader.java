package org.oapen.memoproject.dataingestion.metadata;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public interface Downloader {

	/**
	 * Download the response at url to a local file.
	 * 
	 * @param url remote url
	 * @throws IOException if the remote url could not be reached or local file 
	 * could not be saved. 
	 */
	File download(URL url) throws IOException;
	
	
	// !!! ==========================================
	// TODO MAke exportsUrls a property of this downloader and NONE else!
	
	
	/**
	 * Get the download directory
	 * @return Download directory
	 */
	String getDirectory();
	
	/**
	 * Get the response at url as a String. 
	 * 
	 * @return
	 * @throws IOException
	 */
	String getAsString(URL url) throws IOException;

}

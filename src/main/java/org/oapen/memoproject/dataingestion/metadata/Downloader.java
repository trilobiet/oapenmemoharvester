package org.oapen.memoproject.dataingestion.metadata;

import java.io.IOException;
import java.net.URL;
import java.util.Set;

public interface Downloader {

	/**
	 * Download the response at url to a local file.
	 * 
	 * @param url remote url
	 * @throws IOException if the remote url could not be reached or local file 
	 * could not be saved. 
	 */
	void download(URL url) throws IOException;
	
	/**
	 * Download the responses at urls to local files.
	 * 
	 * @param url remote url
	 * @throws IOException if the remote urls could not be reached or local files 
	 * could not be saved. 
	 */
	void download(Set<URL> urls) throws IOException;

	
	String getDirectory();
	
	/**
	 * Get the response at url as a String. 
	 * 
	 * @return
	 * @throws IOException
	 */
	String getAsString(URL url) throws IOException;

}

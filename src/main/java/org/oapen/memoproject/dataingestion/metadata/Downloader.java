package org.oapen.memoproject.dataingestion.metadata;

import java.io.IOException;
import java.util.Set;

public interface Downloader {

	/**
	 * Download the response at url to a local file.
	 * 
	 * @param url remote url
	 * @throws IOException if the remote url could not be reached or local file 
	 * could not be saved. 
	 */
	void download(String url) throws IOException;
	
	/**
	 * Download the responses at urls to local files.
	 * 
	 * @param url remote url
	 * @throws IOException if the remote urls could not be reached or local files 
	 * could not be saved. 
	 */
	void download(Set<String> urls) throws IOException;

	
	String getDirectory();
	
	/**
	 * Get the response at url as a String. 
	 * 
	 * @return
	 * @throws IOException
	 */
	String getAsString(String url) throws IOException;

}

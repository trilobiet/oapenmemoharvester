package org.oapen.memoproject.dataingestion.metadata;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

public class ExportChunksLoader {
	
	private final URL url;
	
	/**
	 * @param url Url to be downloaded
	 * @throws MalformedURLException if url is not a valid url
	 */
	public ExportChunksLoader(URL url) throws MalformedURLException {
	
		this.url = url;
	}
	
	/**
	 * Save the contents of url to a local file.
	 * <br>
	 * Saves to a temporary path, then atomically renames, 
	 * to prevent partially downloaded files from being visible to the system.
	 * 
	 * @param filePath Local download destination
	 * @throws IOException if the remote url could not be reached or local file 
	 * could not be saved. 
	 */
	public void downloadTo(String filePath) throws IOException  {
		
		try (InputStream is = getInputStream(url)) {
			
			Path path = Path.of(filePath);
			Path parent = path.getParent();
			Files.createDirectories(parent);
			File tmp = File.createTempFile("tmp", ".tmp", parent.toFile()); 
			Files.copy(is, tmp.toPath(), StandardCopyOption.REPLACE_EXISTING);
			Files.move(tmp.toPath(), path, 
					StandardCopyOption.REPLACE_EXISTING,
					StandardCopyOption.ATOMIC_MOVE);
		}
	}

	
	public String getAsString() throws IOException {
		
		String text = null;
		
		try (InputStream is = getInputStream(url); Scanner scanner = new Scanner(is, StandardCharsets.UTF_8.name())) {
			text = scanner.useDelimiter("\\A").next();
		}
		
		return text;
	}
	
	
	public InputStream getInputStream(URL url) throws IOException {
		
		HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
		con.setConnectTimeout(60_000);
		int responseCode = con.getResponseCode();
		if (responseCode == 200) return con.getInputStream();
		else throw(new IOException("response code " + responseCode));
	}
		

}

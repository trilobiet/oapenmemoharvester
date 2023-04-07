package org.oapen.memoproject.dataingestion.metadata;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;

public class ExportsDownloader implements Downloader {
	
	// TODO: Make this a File path SO we can see by the type what it is supposed to be
	private final String directoryPath;
	
	public ExportsDownloader(String directoryPath) {
		this.directoryPath = directoryPath;
	}

	@Override
	public String getDirectory() {
		return directoryPath;
	}

	/*
	  Saves to a temporary file, then atomically renames, 
	  to prevent partially downloaded files from being visible to the system.
	*/
	@Override
	public void download(String cUrl) throws IOException  {
		
		URL url = new URL(cUrl);
		
		try (InputStream is = getInputStream(url)) {
			
			Path path = Path.of(directoryPath);
			Path parent = path.getParent();
			Files.createDirectories(parent);
			File tmp = File.createTempFile("tmp", ".tmp", parent.toFile()); 
			Files.copy(is, tmp.toPath(), StandardCopyOption.REPLACE_EXISTING);
			Files.move(tmp.toPath(), path, 
				StandardCopyOption.REPLACE_EXISTING,
				StandardCopyOption.ATOMIC_MOVE);
		}
	}


	@Override
	public void download(Set<String> urls) throws IOException {
		
		for (String url: urls) download(url);
	}
	
	
	@Override
	public String getAsString(String cUrl) throws IOException {
		
		String text = null;
		URL url = new URL(cUrl);
		
		try (InputStream is = getInputStream(url); Scanner scanner = new Scanner(is, StandardCharsets.UTF_8.name())) {
			text = scanner.useDelimiter("\\A").next();
		}
		
		return text;
	}
	
	
	private InputStream getInputStream(URL url) throws IOException {
		
		HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
		con.setConnectTimeout(60_000);
		int responseCode = con.getResponseCode();
		if (responseCode == 200) return con.getInputStream();
		else throw(new IOException("response code " + responseCode));
	}


}

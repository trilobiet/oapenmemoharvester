package org.oapen.memoproject.dataingestion.metadata;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ExportsDownloader imp0lementation
 * 
 * @author acdhirr
 *
 */
public class ExportsDownloaderImp implements ExportsDownloader {
	
	private final File destination;
	
	private Map<ExportType,URL> exportsUrls;
	
	private static final Logger logger = 
			LoggerFactory.getLogger(ExportsDownloaderImp.class);
	
	
	public ExportsDownloaderImp(Map<ExportType,URL> exportsUrls, File destination) {
		this.destination = destination;
		this.exportsUrls = exportsUrls;
	}

	@Override
	public File getDirectory() {
		return destination;
	}
	
	@Override
	public Set<ExportType> getExportTypes() {
		
		return exportsUrls.keySet();
	}
	
	@Override
	public List<File> downloadAll() throws IOException {
		
		List<File> files = new ArrayList<>();
		
		for(ExportType type: this.exportsUrls.keySet()) {
			
			files.add(download(type));
		}
		
		return files;
	}


	/*
	  Saves to a temporary file, then atomically renames, 
	  to prevent partially downloaded files from being visible to the system.
	*/
	@Override
	public File download(ExportType type) throws IOException  {
		
		URL url = exportsUrls.get(type);
		
		logger.info("Downloading from " + url.toString());
		
		String fileName = "exports." + type.lowerCaseName();
		
		try (InputStream is = getInputStream(url)) {
			
			Path path = Path.of(destination + "/" + fileName);
			Path parent = path.getParent();
			Files.createDirectories(parent);
			File tmp = File.createTempFile("tmp", ".tmp", parent.toFile()); 
			Files.copy(is, tmp.toPath(), StandardCopyOption.REPLACE_EXISTING);
			Files.move(tmp.toPath(), path, 
				StandardCopyOption.REPLACE_EXISTING,
				StandardCopyOption.ATOMIC_MOVE);
			return path.toFile();
		}
	}


	@Override
	public String getAsString(URL url) throws IOException {
		
		String text = null;
		
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

package org.oapen.memoproject.dataingestion.metadata;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.net.URL;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled
public class ExportChunksLoaderTests {
	
	@Test
	public void test_download_to_disk() throws IOException {
		
		ExportChunksLoader loader = new ExportChunksLoader(new URL("https://library.oapen.org/download-export?format=onix"));
		
		assertDoesNotThrow(() -> loader.downloadTo(System.getProperty("user.home") + "/test_download_to_disk.onix"));
	}

	@Test
	public void test_download_to_string() throws IOException {
		
		ExportChunksLoader loader = new ExportChunksLoader(new URL("https://library.oapen.org/download-export?format=onix"));
		
		String s = loader.getAsString();
		
		// System.out.println(s);
		
		assertNotNull(s);
	}
	
	
}

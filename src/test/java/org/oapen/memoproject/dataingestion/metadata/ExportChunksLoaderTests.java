package org.oapen.memoproject.dataingestion.metadata;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled
public class ExportChunksLoaderTests {
	
	// TODO
	@Test
	public void test_download_to_disk() throws IOException {
		
		//ExportsDownloader loader = new ExportsDownloader("https://library.oapen.org/download-export?format=onix");
		
		//assertDoesNotThrow(() -> loader.download(System.getProperty("user.home") + "/test_download_to_disk.onix"));
	}

	// TODO
	@Test
	public void test_download_to_string() throws IOException {
		
		//ExportsDownloader loader = new ExportsDownloader("https://library.oapen.org/download-export?format=onix");
		
		//String s = loader.getAsString();
		
		// System.out.println(s);
		
		//assertNotNull(s);
	}
	
	
}

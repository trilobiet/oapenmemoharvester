package org.oapen.memoproject.dataingestion.metadata;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.oapen.memoproject.dataingestion.jpa.PersistenceService;
import org.oapen.memoproject.dataingestion.jpa.entities.ExportChunk;

public class ChunksIngesterTests {
	
	@Test
	// TODO: not a very descriptive test
	public void test_ingestAll() throws IngestException {
		
		Set<ExportChunk> saveSet = new HashSet<>();
		saveSet.add(new ExportChunk("MARCXML","","20.500.12657/39385"));
		saveSet.add(new ExportChunk("MARCXML","","20.500.12657/42289"));
		saveSet.add(new ExportChunk("MARCXML","","20.500.12657/38065"));

		HashMap<ExportType,URL> exportsUrls = new HashMap<>();
		//exportsUrls.put("MARCXML","https://library.oapen.org/download-export?format=marcxml");
		
		PersistenceService perservice = mock(PersistenceService.class);
		File downloadDestination = new File("src/test/resources/downloads");
		ExportsDownloader downloader = new ExportsDownloaderImp(exportsUrls, downloadDestination);  
		
		// Mock da shit
		when(perservice.saveExportChunks(saveSet)).thenReturn(new ArrayList<ExportChunk>(saveSet));
		
		// or... irrespective of input saveSet:
		// when(perservice.saveExportChunks(any(Set.class))).thenReturn(new ArrayList<ExportChunk>(saveSet));

		ChunksIngesterService ciservice = new ChunksIngesterService(
			perservice,
			downloader  // here is some test data in exports.marcxml
		);
		
		ciservice.setDaysExpiration(Integer.MAX_VALUE);

		List<String> expectedList = new ArrayList<>();
		expectedList.add("20.500.12657/38065");
		expectedList.add("20.500.12657/39385");
		expectedList.add("20.500.12657/42289");
		
		List<String> returnedList = ciservice.ingestAll(ExportType.MARCXML);
		
		// System.out.println(returnedList);
		
		assertIterableEquals(expectedList, returnedList);
	}
	
	
	@SuppressWarnings("unchecked")
	@Test
	// TODO: not a very descriptive test
	public void test_ingestForHandles() throws IOException {
		
		Set<ExportChunk> returnedFromSaveSet = new HashSet<>();
		returnedFromSaveSet.add(new ExportChunk("MARCXML","","20.500.12657/42289"));
		
		PersistenceService perservice = mock(PersistenceService.class);
		ExportsDownloader downloader = mock(ExportsDownloader.class);
		
		HashMap<ExportType,URL> exportsUrls = new HashMap<>();
		URL mockUrl = new URL("https://mock.url");
		exportsUrls.put(ExportType.MARCXML, mockUrl);
		
		when(downloader.getAsString(mockUrl)).thenReturn(TestConstants.marcxmlrecord);
		when(perservice.saveExportChunks(any(Set.class))).thenReturn(new ArrayList<ExportChunk>(returnedFromSaveSet));
		
		ChunksIngesterService ciservice = new ChunksIngesterService(
			perservice,
			downloader  
		);
		
		// 20.500.12657/42289
		List<String> handles = new ArrayList<>();
		handles.add("20.500.12657/42289"); 
		
		List<String> q = ciservice.ingestForHandles(handles,ExportType.MARCXML);
		
		assertTrue(q.contains("20.500.12657/42289"));
		
	}
	
	
}		
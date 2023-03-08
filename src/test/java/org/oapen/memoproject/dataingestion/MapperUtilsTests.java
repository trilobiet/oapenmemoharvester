package org.oapen.memoproject.dataingestion;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.oapen.memoproject.dataingestion.jpa.entities.Classification;

public class MapperUtilsTests {
	
	@Test
	void testParseClassifications() {
		
		String line1 = "bic Book Industry Communication::H Humanities::HB History::HBJ Regional &amp; national history::HBJD European history::HBJD1 British &amp; Irish history";
		String line2 = "bic Book Industry Communication::H Humanities::HB History::HBT History: specific events &amp; topics::HBTB Social &amp; cultural history";
		String line3 = "bic Book Industry Communication::H Humanities::HB History::HBL History: earliest times to present day::HBLW 20th century history: c 1900 to c 2000";
		
		List<String> lst = Arrays.asList(line1, line2, line3);
		
		Set<Classification> s = MapperUtils.parseClassifications(lst);
		
		assertTrue(s.size()==9);
	}

	
	@Test
	void testIsUUID() {
		
		boolean isUuid = MapperUtils.isUUID("12345678-1234-1234-1234-1234567890AB");
		boolean isNotUuid = MapperUtils.isUUID("1234567");
		boolean isNull = MapperUtils.isUUID(null);
		
		assertTrue(isUuid);
		assertFalse(isNotUuid);
		assertFalse(isNull);
	}

	
	@Test
	void testParseDate() {
		
		Optional<LocalDate> d1 = MapperUtils.parseDate("2020-08-03");
		Optional<LocalDate> d2 = MapperUtils.parseDate("2020-08-03abcdefg");
		Optional<LocalDate> d3 = MapperUtils.parseDate("abcdefg");
		Optional<LocalDate> d4 = MapperUtils.parseDate("03/08/2020");
		
		assertTrue(d1.isPresent());
		assertTrue(d2.isPresent());
		assertTrue(d3.isEmpty());
		assertTrue(d4.isEmpty());
		
	}
	
	
	@Test 
	void testExportChunkType() {
		
		assertTrue(MapperUtils.exportChunkType("https://path.to/12345.ris").equals("RIS"));
		assertTrue(MapperUtils.exportChunkType("https://path.to/12345.tsv").equals("KBART"));
		assertTrue(MapperUtils.exportChunkType("https://path.to/12345.onix_3.0.xml").equals("ONIX"));
		assertTrue(MapperUtils.exportChunkType("https://path.to/12345.marc.xml").equals("MARCXML"));
		assertTrue(MapperUtils.exportChunkType("https://path.to/12345.abc").equals("UNKNOWN"));
	}
	
	
	@Test
	@Disabled
	// TODO
	void testStringifyNodeList() {
	}

	
	@Test
	@Disabled
	// TODO
	void testCombineNodeLists() {
	}
	
	
}

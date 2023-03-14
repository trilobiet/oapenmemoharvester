package org.oapen.memoproject.dataingestion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.assertj.core.util.Sets;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.oapen.memoproject.dataingestion.jpa.entities.Classification;

public class XOAIDocumentParserUtilsTests {
	
	@Test
	void testParseClassifications() {
		
		String line1 = "bic Book Industry Communication::H Humanities::HB History::HBJ Regional &amp; national history::HBJD European history::HBJD1 British &amp; Irish history";
		String line2 = "bic Book Industry Communication::H Humanities::HB History::HBT History: specific events &amp; topics::HBTB Social &amp; cultural history";
		String line3 = "bic Book Industry Communication::H Humanities::HB History::HBL History: earliest times to present day::HBLW 20th century history: c 1900 to c 2000";
		
		List<String> lst = Arrays.asList(line1, line2, line3);
		
		Set<Classification> s = XOAIDocumentParserUtils.parseClassifications(lst);
		
		assertTrue(s.size() == 9);
	}
	
	
	@Test
	void testParseSubjects() {
		
		String subject1 = "subject1";
		String subject2 = "subject2";
		String subject3 = "subject3, subject4, subject5";
		String subject4 = "subject6; subject7 /subject8";
		String subject5 = "subject3 / subject4 , subject9";
		String subject6 = "subject10;subject11 -- subject12 -- subject13";
		
		Set<String> subjects = Sets.newLinkedHashSet(subject1, subject2, subject3, subject4, subject5, subject6);

		Set<String> subjectsSplit = XOAIDocumentParserUtils.parseSubjects(subjects);
		
		assertTrue(subjectsSplit.size() == 13);
	}

	
	@Test
	void testIsUUID() {
		
		boolean isUuid = XOAIDocumentParserUtils.isUUID("12345678-1234-1234-1234-1234567890AB");
		boolean isNotUuid = XOAIDocumentParserUtils.isUUID("1234567");
		boolean isNull = XOAIDocumentParserUtils.isUUID(null);
		
		assertTrue(isUuid);
		assertFalse(isNotUuid);
		assertFalse(isNull);
	}

	
	@Test
	void testParseDate() {
		
		Optional<LocalDate> d1 = XOAIDocumentParserUtils.parseDate("2020-08-03");
		Optional<LocalDate> d2 = XOAIDocumentParserUtils.parseDate("2020-08-03abcdefg");
		Optional<LocalDate> d3 = XOAIDocumentParserUtils.parseDate("abcdefg");
		Optional<LocalDate> d4 = XOAIDocumentParserUtils.parseDate("03/08/2020");
		
		assertTrue(d1.isPresent());
		assertTrue(d2.isPresent());
		assertTrue(d3.isEmpty());
		assertTrue(d4.isEmpty());
		
	}
	
	
	@Test 
	void testExportChunkType() {
		
		assertTrue(XOAIDocumentParserUtils.exportChunkType("https://path.to/12345.ris").equals("RIS"));
		assertTrue(XOAIDocumentParserUtils.exportChunkType("https://path.to/12345.tsv").equals("KBART"));
		assertTrue(XOAIDocumentParserUtils.exportChunkType("https://path.to/12345.onix_3.0.xml").equals("ONIX"));
		assertTrue(XOAIDocumentParserUtils.exportChunkType("https://path.to/12345.marc.xml").equals("MARCXML"));
		assertTrue(XOAIDocumentParserUtils.exportChunkType("https://path.to/12345.abc").equals("UNKNOWN"));
	}
	
	
	@Test
	void testYearFromString() {
		
		assertTrue( XOAIDocumentParserUtils.yearFromString(null).isEmpty() );
		assertTrue( XOAIDocumentParserUtils.yearFromString("pipodeclown").isEmpty() );
		assertTrue( XOAIDocumentParserUtils.yearFromString("").isEmpty() );
		assertTrue( XOAIDocumentParserUtils.yearFromString("202").isEmpty() );
		assertTrue( XOAIDocumentParserUtils.yearFromString("x2023").isEmpty() );
		assertTrue( XOAIDocumentParserUtils.yearFromString("2023").equals( Optional.of(2023) ));
		assertTrue( XOAIDocumentParserUtils.yearFromString("202345pipodeclown").equals( Optional.of(2023) ));
		assertTrue( XOAIDocumentParserUtils.yearFromString("2023-01-20T13:59:11Z").equals( Optional.of(2023) ));
	}
	
	
	@Test 
	void testExtractHandleFromIdentifier() {
		
		assertTrue( XOAIDocumentParserUtils.extractHandleFromIdentifier(null).isEmpty());
		assertTrue( XOAIDocumentParserUtils.extractHandleFromIdentifier("").isEmpty());
		assertTrue( XOAIDocumentParserUtils.extractHandleFromIdentifier("  ").isEmpty());
		
		assertEquals( 
			XOAIDocumentParserUtils.extractHandleFromIdentifier("oai:library.oapen.org:20.500.12657/44456").get(),
			"20.500.12657/44456"
		);
		
		assertEquals( 
			XOAIDocumentParserUtils.extractHandleFromIdentifier("blahblahblah:20.500.12657/44456").get(),
			"20.500.12657/44456"
		);
			
		assertEquals( 
			XOAIDocumentParserUtils.extractHandleFromIdentifier("20.500.12657/44456").get(),
			"20.500.12657/44456"
		);

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

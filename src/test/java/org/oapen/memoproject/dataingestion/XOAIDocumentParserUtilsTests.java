package org.oapen.memoproject.dataingestion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
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
		
		assertEquals(s.size(), 9);
	}
	
	
	@Test
	void testParseSubjects() {
		
		String subject1 = "subject1";
		String subject2 = "subject2";
		String subject3 = "subject3, subject4, subject5";
		String subject4 = "subject6; subject7 /subject8 | subject9";
		String subject5 = "subject3 / subject4 , subject9";
		String subject6 = "subject10;subject11 -- subject12 -- subject13";
		
		Set<String> subjects = Sets.newLinkedHashSet(subject1, subject2, subject3, subject4, subject5, subject6);

		Set<String> subjectsSplit = XOAIDocumentParserUtils.parseSubjects(subjects);
		
		assertEquals(subjectsSplit.size(), 13);
	}
	
	
	@Test
	void testParseSubjects2() {
		
		// first character = C2 AO (non breaking space)
		String subject1 = " Industry & water ";  
		
		Set<String> subjects = Sets.newLinkedHashSet(subject1);
		Set<String> subjectsSplit = XOAIDocumentParserUtils.parseSubjects(subjects);
		
		assertEquals(subjectsSplit.size(), 1);
		assertTrue(new ArrayList<String>(subjectsSplit).get(0).length() == 16);
	}

	
	@Test
	void testParselanguages() {
		
		String l1 = "eng";
		String l2 = "eng,dut,fra/Undetermined[und]";
		String l3 = "spa::TooLong::Some weird Test Language";
		
		Set<String> languages = new HashSet<>();
		languages.add(l1);
		languages.add(l2);
		languages.add(l3);
		
		Set<String> pls = XOAIDocumentParserUtils.parseLanguages(languages);
		
		Set<String> expected = Sets.newLinkedHashSet("eng","fra","spa","dut");
		
		assertEquals(pls,expected);
	}
	

	@Test
	void testParseISBNOrISSN() {
		
		String l1 = "1000000000000;2000000000000";
		String l2 = "3000000000000 ; 4000000000000";
		String l3 = "5000000000000 | 6000000000000|";
		String l4 = "7000000000000";
		
		Set<String> numbers = new HashSet<>();
		numbers.add(l1);
		numbers.add(l2);
		numbers.add(l3);
		numbers.add(l4);
		
		Set<String> pnrs = XOAIDocumentParserUtils.parseISBNOrISSN(numbers);
		
		Set<String> expected = Sets.newLinkedHashSet(
			"1000000000000","2000000000000","3000000000000",
			"4000000000000","5000000000000","6000000000000",
			"7000000000000"
		);
		
		assertEquals(pnrs,expected);
	}
	
	
	@Test
	void testParseOCN() {
		
		String ocn1 = "OCN: 123456789";
		String ocn2 = "ocn:123456789";
		String ocn3 = "123456789";
		
		assertEquals("123456789", XOAIDocumentParserUtils.parseOCN(ocn1));
		assertEquals("123456789", XOAIDocumentParserUtils.parseOCN(ocn2));
		assertEquals("123456789", XOAIDocumentParserUtils.parseOCN(ocn3));
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

package org.oapen.memoproject.dataingestion;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

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
		
		s.forEach(System.out::println);
		
		
	}

}

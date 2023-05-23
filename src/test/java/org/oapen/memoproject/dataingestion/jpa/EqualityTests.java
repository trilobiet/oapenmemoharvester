package org.oapen.memoproject.dataingestion.jpa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.text.WordUtils;
import org.junit.jupiter.api.Test;
import org.oapen.memoproject.dataingestion.jpa.entities.Contribution;

public class EqualityTests {
	
	@Test
	void testEquality1() {
		
		/*
			Holzer-Kawałko, Anna - Holzer-Kawalko, Anna
	    	Kłosowska, Anna - klosowska, anna
	    	COMPLOI, Franz - Comploi, Franz
	    	van Vrekhem, Stijn - Van Vrekhem, Stijn
	    	Sesta, Michele - sesta, michele
		*/
				
		String s1a = "Äñëöŷ"; 
		String s1b = "Aneoy";
		String s1n = Normalizer.normalize(s1a, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
		
		assertNotEquals(s1a, s1b);
		assertEquals(s1b, s1n);
		
	}
	

	@Test
	void test_set_with_equal_contributions_should_be_size_one() {
		
		Contribution c1 = new Contribution("Sesta, Michele","role1");
		Contribution c2 = new Contribution("Sesta, Michele","role1");
		Set<Contribution> s = new HashSet<>();
		s.add(c1);
		s.add(c2);
		assertTrue(s.size()==1);
		
	}

	
	
	@Test
	void testCapitalize() {
		
		ArrayList<String> expected = new ArrayList<String>(Arrays.asList(
				"Jan Kees",
				"Barthélémy, Daniel",
				"Mcdonald, Old"
				
			)); 
		
		ArrayList<String> lst = new ArrayList<String>(Arrays.asList(
			"JAN KEES",
			"Barthélémy, Daniel",
			"mcDonald, Old"
			
		));
		
		ArrayList<String> resulting = new ArrayList<>();
		
		lst.forEach(s -> {
			// System.out.println(WordUtils.capitalizeFully(s));
			resulting.add(WordUtils.capitalizeFully(s));
		});
		
		assertIterableEquals(expected, resulting);
		
	}

	
	
}

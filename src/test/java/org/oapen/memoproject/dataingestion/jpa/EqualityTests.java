package org.oapen.memoproject.dataingestion.jpa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.text.Normalizer;

import org.junit.jupiter.api.Test;
import org.oapen.memoproject.dataingestion.jpa.entities.Contribution;
import org.oapen.memoproject.dataingestion.jpa.entities.Contributor;

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
	void test_contributors_should_be_equal_on_trimmed_lowercase_names() {
		
		Contributor c1 = new Contributor();
		c1.setName("Piet Paaltjens");
		
		Contributor c2 = new Contributor();
		c2.setName(" piet paaltjens  ");

		assertEquals(c1, c2);
		
	}

	
	@Test
	void test_contributions_should_be_equal_on_trimmed_lowercase_names() {
		
		Contribution c1 = new Contribution("Piet Paaltjens","role1");
		Contribution c2 = new Contribution("  piet paaltjens      ","role1");

		assertEquals(c1, c2);
		
	}
	
	

}

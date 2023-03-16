package org.oapen.memoproject.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class StringUtilsTests {
	
	@Test
	void testTrimAllSpace() {
		
		// first character = C2 AO (non breaking space)
		String source = " abc  de f g     \n";  
		
		String result = StringUtils.trimAllSpace(source);
		String expected = "abc de f g";
		
		// System.out.println("+" + result + "+");
		
		assertEquals(result,expected);
	}
	
	
	@Test
	void testCutOff() {
		
		String s0 = null;
		String s1 = "";
		String s2 = "abc";
		String s3 = "abcde";
		String s4 = "abcdefg";
		
		assertNull(StringUtils.cutOff(s0, 5));
		assertEquals(StringUtils.cutOff(s1, 5),"");
		assertEquals(StringUtils.cutOff(s2, 5),"abc");
		assertEquals(StringUtils.cutOff(s3, 5),"abcde");
		assertEquals(StringUtils.cutOff(s4, 5),"abcde");
	}


	
	@Test
	void testIsUUID() {
		
		boolean isUuid = StringUtils.isUUID("12345678-1234-1234-1234-1234567890AB");
		boolean isNotUuid = StringUtils.isUUID("1234567");
		boolean isNull = StringUtils.isUUID(null);
		
		assertTrue(isUuid);
		assertFalse(isNotUuid);
		assertFalse(isNull);
	}
	
	
}

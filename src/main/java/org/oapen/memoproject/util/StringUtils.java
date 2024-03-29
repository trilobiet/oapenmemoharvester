package org.oapen.memoproject.util;

/**
 * A library of static String functions on Strings.
 * 
 * @author acdhirr
 *
 */
public final class StringUtils {

	/**
	 * @param s
	 * @return s without all repeated whitespace (spaces, tabs, newlines etc.)
	 */
	public final static String trimAllSpace(String s) {
		
		return s
			// replace all whitespace-like sh*t with a single space				
			.replaceAll("[ \\h\\t\\n\\x0B\\f\\r]+", " ")
			// then trim
			.trim(); 
	}

	/**
	 * 
	 * @param s
	 * @param size
	 * @return String s, if longer than size, cut off at size/ 
	 */
	public final static String cutOff(String s, int size) {
		
		if (s != null && s.length() > size) {
			return s.substring(0, size);
		}
		else return s;
		
	}

	/**
	 * Test if a string is a UUID
	 * 
	 * @param s
	 * @return true if s is a UUID
	 */
	public final static boolean isUUID(String s) {
		
		final String UUID_STRING =
			    "[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}";
		
		return s != null && s.matches(UUID_STRING);
	}

	
	/**
	 * Remove quotes and double quotes from beginning and ending of string.
	 * 
	 * @param string
	 * @return String, without leading or trailing (double) quotes
	 */
	public final static String trimQuotes(String string) {
		
		return string.replaceAll("^[\"']|[\"']$", "");
	}

}

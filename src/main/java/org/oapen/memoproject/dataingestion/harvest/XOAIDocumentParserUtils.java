package org.oapen.memoproject.dataingestion.harvest;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.oapen.memoproject.dataingestion.jpa.entities.Classification;
import org.oapen.memoproject.util.StringUtils;

/**
 * Utility library for XOAIDocumentParser
 * 
 * @author acdhirr
 *
 */
public final class XOAIDocumentParserUtils {
		
	private final static String SPLIT_CHARS = "[,;:/|]|--";
	
	
	/**
	 * Parses Classification Entities from a List of Strings
	 * 
	 * @param lines unparsed input
	 * @return Set of Classification entities
	 */
	public final static Set<Classification> parseClassifications(List<String> lines) {
		
		Set<Classification> categories = lines
			.stream().map( line ->	
				Arrays.asList(line.split("::")).stream() // separator for categories = '::' 
				.skip(1)  // always skip first item 'bic Book Industry Communication'
				.map(cat -> Arrays.asList(cat.split(" ",2))) // split on first space 'HBL History: earliest times to present day'
				.filter(p -> p.size() == 2) // remove faulty categories
				.map(p -> new Classification( p.get(0), p.get(1) )) // 0 = 'HBL' 1 = 'History: earliest times to present day'
				.collect(Collectors.toList())
		)
		.flatMap(List::stream)
		.collect(Collectors.toSet());
		
		return categories;
	}
	
	
	/**
	 * Parses subjects or keywords from a Set of Strings
	 * 
	 * @param strings unparsed input
	 * @return Set of subject Strings
	 */
	public final static Set<String> parseSubjects(Set<String> strings) {
		
		Set<String> p = strings.stream()
			.map(s -> Arrays.asList(s.split(SPLIT_CHARS)))
			.flatMap(List::stream)
			.map(s -> StringUtils.trimAllSpace(s))
			.map(s -> StringUtils.trimQuotes(s))
			.filter(s -> s.length() > 1)
			.map(s -> StringUtils.cutOff(s,100))
			.collect(Collectors.toSet());
		
		return p;
	}
	
	
	/**
	 * Parses languages from a Set of Strings
	 * 
	 * @param strings unparsed input
	 * @return Set of subject Strings
	 */
	public final static Set<String> parseLanguages(Set<String> strings) {
		
		Set<String> p = strings.stream()
			.map(s -> Arrays.asList(s.split(SPLIT_CHARS)))
			.flatMap(List::stream)
			.map(s -> StringUtils.trimAllSpace(s))
			.map(String::toLowerCase)
			.filter(s -> s.length() == 3)
			.collect(Collectors.toSet());
		
		return p;
	}

	
	/**
	 * Parses ISBN/ISSN numbers from a Set of Strings
	 * 
	 * @param strings unparsed input
	 * @return Set of ISBN/ISSN Strings
	 */
	public final static Set<String> parseISBNOrISSN(Set<String> strings) {
		
		Set<String> p = strings.stream()
			.map(s -> Arrays.asList(s.split("[;|]")))
			.flatMap(List::stream)
			.map(s -> StringUtils.trimAllSpace(s))
			.map(String::toLowerCase)
			.collect(Collectors.toSet());
		
		return p;
	}	
	
	
	/**
	 * Parses OCN number from a string value
	 *  
	 * @param ocn unparsed input
	 * @return OCN String
	 */
	public final static String parseOCN(String ocn) {
		
		return ocn.replaceAll("(?i)^ocn:\\s*", "").trim();
	}
	
	
	/**
	 * Parses a LocalDate from a String
	 * 
	 * @param d unparsed input
	 * @return an Optional containing a date when successfully parsed, else empty.
	 */
	public final static Optional<LocalDate> parseDate(String d) {
    	
	     try {
	        return Optional.of(LocalDate.parse(d.substring(0,10)));
	     }
	     catch(Exception e){
	        return Optional.empty();
	     }
    }
    
    
	/**
	 * Parses the export chunks type from a Url String.
	 * 
	 * @param url unparsed input
	 * @return A String denoting the export chunk type: MARCXML, ONIX, RIS, KBART or UNKNOWN
	 */
    public final static String exportChunkType(String url) {
    	
    	if (url == null)
    		return "UNKNOWN";
    	else if (url.endsWith("marc.xml"))
    		return "MARCXML";
    	else if (url.endsWith(".xml") && url.contains("onix"))
    		return "ONIX";
    	else if (url.endsWith(".ris"))
    		return "RIS";
    	else if (url.endsWith(".tsv"))
    		return "KBART";
    	else 
    		return "UNKNOWN"; 
    	
    }

    
    /**
     * Parses a year from a String 
     * 
     * @param s unparsed input
     * @return an Optional Integer containing a year when successfully parsed, else empty.
     */
    public static Optional<Integer> yearFromString(String s) {
    	
    	Optional<Integer> r = Optional.empty();
    	
    	if (s != null && s.length() >= 4 && s.substring(0,4).matches("[1-2][0-9]{3}")) 
    		r =  Optional.of(Integer.parseInt(s.substring(0,4)));
    	
    	return r;
    }
    
    
    /**
     * Parses the handle part from a complete identifier ('handle').
     * <br><br>
     * oai:library.oapen.org:20.500.12657/62415 -> 20.500.12657/62415 
     * 
     * @param s unparsed input
     * @return an Optional String containing a handle when successfully parsed, else empty.
     */
    public static Optional<String> extractHandleFromIdentifier(String s) {
    	
    	Optional<String> handle = Optional.empty();
    	
    	if ( s!= null && !s.isBlank() ) {
    		String[] parts = s.split(":");
    		if(parts.length > 0) handle = Optional.of(parts[parts.length-1]);
    	}	
    	
    	return handle;
    }
    
}

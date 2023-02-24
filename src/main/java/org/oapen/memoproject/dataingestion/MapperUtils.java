package org.oapen.memoproject.dataingestion;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.oapen.memoproject.dataingestion.jpa.entities.Classification;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public final class MapperUtils {
	
	
	public static Set<Classification> parseClassifications(List<String> lines) {
		
		Set<Classification> categories = lines
			.stream().map( line ->	
				Arrays.asList(line.split("::")).stream() // separator for categories = '::' 
				.skip(1)  // always skip first item 'bic Book Industry Communication'
				.map(cat -> Arrays.asList(cat.split(" ",2))) // split on first space 'HBL History: earliest times to present day'
				.filter(p -> p.size() == 2) // remove faulty categories
				.map(p -> new Classification(p.get(0),p.get(1))) // 0 = 'HBL' 1 = 'History: earliest times to present day'
				.collect(Collectors.toList())
		)
		.flatMap(List::stream)
		.collect(Collectors.toSet());
		
		return categories;
	}

	
	/**
     * Provides a NodeList of multiple nodelists
     * http://www.java2s.com/example/java-utility-method/xml-nodelist/combine-final-nodelist...-nls-aaf92.html
     */
    public static NodeList combine(final NodeList... nls) {

        return new NodeList() {
        	
            public Node item(final int index) {
                int offset = 0;
                for (int i = 0; i < nls.length; i++) {
                    if (index - offset < nls[i].getLength()) {
                        return nls[i].item(index - offset);
                    } else {
                        offset += nls[i].getLength();
                    }
                }
                return null;
            }

            public int getLength() {
                int result = 0;
                for (int i = 0; i < nls.length; i++) {
                    result += nls[i].getLength();
                }
                return result;
            }
        };
    }	
    
    /**
     * Test if a string is a UUID
     * 
     * @param s
     * @return true if s is a UUID
     */
    public boolean isUUID(String s) {
    	
    	final String UUID_STRING =
    		    "[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}";
    	
    	return s.matches(UUID_STRING);
    }
	
}

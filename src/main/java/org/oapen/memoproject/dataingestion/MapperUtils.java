package org.oapen.memoproject.dataingestion;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.oapen.memoproject.dataingestion.jpa.entities.Classification;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public final class MapperUtils {
	
	
	public final static Set<Classification> parseClassifications(List<String> lines) {
		
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
    
    
    public final static Optional<LocalDate> parseDate(String d) {
    	
	     try {
	        return Optional.of(LocalDate.parse(d.substring(0,10)));
	     }
	     catch(Exception e){
	        return Optional.empty();
	     }
    }
    
    
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


    public final static String stringify(NodeList nodes) {
    	
    	if (nodes == null) return "(nodes is null!)";
    	
    	List<String> ns = new ArrayList<>();
    	
    	for (int i=0; i<nodes.getLength(); i++) {
    		ns.add(nodes.item(i).getTextContent());
    	}
    	
    	return ns.stream().collect(Collectors.joining(", "));
    }
    
    
    
    /**
     * Provides a NodeList of multiple nodelists
     * http://www.java2s.com/example/java-utility-method/xml-nodelist/combine-final-nodelist...-nls-aaf92.html
     */
    public final static NodeList combine(final NodeList... nls) {

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
    
    public static Optional<Integer> yearFromString(String s) {
    	
    	Optional<Integer> r = Optional.empty();
    	
    	if (s != null && s.length() >= 4 && s.substring(0,4).matches("[1-2][0-9]{3}")) 
    		r =  Optional.of(Integer.parseInt(s.substring(0,4)));
    	
    	return r;
    }
    
    
}

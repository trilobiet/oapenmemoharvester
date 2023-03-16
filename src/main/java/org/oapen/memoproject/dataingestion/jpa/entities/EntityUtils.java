package org.oapen.memoproject.dataingestion.jpa.entities;

import java.util.Set;
import java.util.stream.Collectors;

public final class EntityUtils {

    public final static String setToString(Set<String> set, String delimiter) {
    	
    	if (set != null)
    		return set.stream()
    			.map(String::trim)
    			.sorted()
				.collect(Collectors.joining(delimiter));
    	else 
    		return "";
    }
	
}

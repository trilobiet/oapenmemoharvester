package org.oapen.memoproject.dataingestion;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.oapen.memoproject.dataingestion.jpa.entities.Classification;

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

}

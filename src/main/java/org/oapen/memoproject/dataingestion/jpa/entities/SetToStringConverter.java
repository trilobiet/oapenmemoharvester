package org.oapen.memoproject.dataingestion.jpa.entities;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Joins all members of a Set of Strings into a single pipe-separated String, and v.v.
 * 
 * @author acdhirr
 *
 */
@Converter
public class SetToStringConverter implements AttributeConverter<Set<String>, String> {

	@Override
	public String convertToDatabaseColumn(Set<String> set) {

		if (set != null) 
			return set.stream()
    			.map(String::trim)
    			.sorted()
				.collect(Collectors.joining("||"));
		else 
			return "";

	}

	@Override
	public Set<String> convertToEntityAttribute(String dbData) {
		
		HashSet<String> r = new HashSet<>();
		
		if (dbData != null ) {
			String[] ar = dbData.split("\\|\\|");
			r.addAll(Arrays.asList(ar));
		}
		return r;
	}


}

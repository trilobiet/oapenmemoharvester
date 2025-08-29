package org.oapen.memoproject.dataingestion.jpa.entities;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class StringToBooleanConverter implements AttributeConverter<String, Boolean> {

	@Override
	public Boolean convertToDatabaseColumn(String attribute) {
		
		Set<String> trueValues = new HashSet<>(Arrays.asList("TRUE","T","YES","Y"));
		if (trueValues.contains(attribute.trim().toUpperCase())) return true;
		else return false;
	}

	@Override
	public String convertToEntityAttribute(Boolean dbData) {

		return dbData.toString();
	}
}

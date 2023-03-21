package org.oapen.memoproject.dataingestion.jpa.entities;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.oapen.memoproject.util.StringUtils;

/**
 * Cleans up Strings on persisting 
 * (remove leading, trailing and excessive white space characters). 
 * 
 * @author acdhirr
 *
 */
@Converter
public class StringConverter implements AttributeConverter<String, String> {

	@Override
	public String convertToDatabaseColumn(String attribute) {
		
		if (attribute != null) return StringUtils.trimAllSpace(attribute);
		else return attribute;
	}

	@Override
	public String convertToEntityAttribute(String dbData) {

		return dbData;
	}

}

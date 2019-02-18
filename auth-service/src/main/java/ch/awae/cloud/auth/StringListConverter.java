package ch.awae.cloud.auth;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.persistence.AttributeConverter;

public class StringListConverter implements AttributeConverter<List<String>, String> {

	@Override
	public String convertToDatabaseColumn(List<String> list) {
		return String.join(";", list);
	}

	@Override
	public List<String> convertToEntityAttribute(String entry) {
		if (entry == null)
			return Collections.emptyList();
		return Arrays.asList(entry.split(";"));
	}

}

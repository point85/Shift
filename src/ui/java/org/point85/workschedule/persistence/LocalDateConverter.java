package org.point85.workschedule.persistence;

import java.time.LocalDate;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class LocalDateConverter implements AttributeConverter<java.time.LocalDate, java.sql.Date> {
	@Override
	public java.sql.Date convertToDatabaseColumn(LocalDate entityValue) {
		return java.sql.Date.valueOf(entityValue);
	}

	@Override
	public LocalDate convertToEntityAttribute(java.sql.Date databaseValue) {
		return databaseValue.toLocalDate();
	}
}

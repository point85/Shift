package org.point85.workschedule.persistence;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class LocalDateTimeConverter implements AttributeConverter<LocalDateTime, Timestamp> {
	@Override
	public java.sql.Timestamp convertToDatabaseColumn(LocalDateTime entityValue) {
		return Timestamp.valueOf(entityValue);
	}

	@Override
	public LocalDateTime convertToEntityAttribute(java.sql.Timestamp databaseValue) {
		return databaseValue.toLocalDateTime();
	}
}

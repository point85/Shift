package org.point85.workschedule.persistence;

import java.time.LocalTime;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class LocalTimeConverter implements AttributeConverter<LocalTime, Integer> {
	@Override
	public Integer convertToDatabaseColumn(LocalTime entityValue) {
		return entityValue.toSecondOfDay();
	}

	@Override
	public LocalTime convertToEntityAttribute(Integer secondOfDay) {
		return LocalTime.ofSecondOfDay(secondOfDay);
	}
}

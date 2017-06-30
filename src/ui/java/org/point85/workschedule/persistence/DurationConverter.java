package org.point85.workschedule.persistence;

import java.time.Duration;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class DurationConverter implements AttributeConverter<Duration, Long> {
	@Override
	public Long convertToDatabaseColumn(Duration entityValue) {
		return entityValue.getSeconds();
	}

	@Override
	public Duration convertToEntityAttribute(Long seconds) {
		return Duration.ofSeconds(seconds);
	}
}


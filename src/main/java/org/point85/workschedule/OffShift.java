package org.point85.workschedule;

import java.time.Duration;
import java.time.LocalTime;

public class OffShift extends TimePeriod {
	OffShift(String name, String description, LocalTime start, Duration duration) {
		super(name, description, start, duration);
	}

	@Override
	public boolean isWorkingPeriod() {
		return false;
	}
}

package org.point85.workschedule;

import java.time.Duration;
import java.time.LocalTime;

// recurring non-working time
public class BreakPeriod extends TimePeriod {

	public BreakPeriod(String name, String description, LocalTime start, Duration duration) {
		super(name, description, start, duration);
	}

	@Override
	public boolean isWorkingPeriod() {
		return false;
	}
	
	@Override
	public String toString() {
		return super.toString();
	}
}

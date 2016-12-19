package org.point85.workschedule;

import java.time.Duration;
import java.time.LocalDateTime;

// non-working non-recurring periods, e.g. holidays
public class NonWorkingPeriod extends Named implements Comparable<NonWorkingPeriod> {
	// starting date and time of day
	private LocalDateTime startDateTime;

	// duration of period
	private Duration duration;

	NonWorkingPeriod(String name, String description, LocalDateTime startDateTime, Duration duration) {
		super(name, description);
		this.startDateTime = startDateTime;
		this.duration = duration;
	}

	public LocalDateTime getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(LocalDateTime startDateTime) {
		this.startDateTime = startDateTime;
	}

	public LocalDateTime getEndDateTime() throws Exception {
		if (startDateTime == null) {
			throw new Exception("Start date and time is not defined.");
		}

		if (duration == null) {
			throw new Exception("Duration is not defined.");
		}
		return startDateTime.plus(duration);
	}

	public Duration getDuration() {
		return duration;
	}

	public void setDuration(Duration duration) {
		this.duration = duration;
	}

	@Override
	public String toString() {
		String text = null;
		try {
			text = super.toString() + ", Start: " + startDateTime + " (" + duration + ")" + ", End: " + getEndDateTime();
		} catch (Exception e) {
			text = e.getMessage();
		}
		return text;
	}

	@Override
	public int compareTo(NonWorkingPeriod other) {
		return getStartDateTime().compareTo(other.getStartDateTime());
	}
}

package org.point85.workschedule;

import java.time.Duration;
import java.time.LocalDateTime;

// non-working non-recurring periods
public class NonWorkingPeriod extends Nameable {
	// starting date and time of day
	private LocalDateTime startDateTime;

	// duration of break
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

	public Duration getDuration() {
		return duration;
	}

	public void setDuration(Duration duration) {
		this.duration = duration;
	}

	@Override
	public String toString() {
		return startDateTime + "(" + duration + ")";
	}

}

package org.point85.workschedule;

import java.time.Duration;
import java.time.LocalTime;

// recurring non-working time
public class BreakPeriod extends Nameable {
	// starting time of day
	private LocalTime startTime;

	// duration of break
	private Duration duration;

	public BreakPeriod(String name, String description, LocalTime startTime, Duration duration) {
		super(name, description);
		this.startTime = startTime;
		this.duration = duration;
	}

	public LocalTime getStartime() {
		return startTime;
	}

	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}

	public Duration getDuration() {
		return duration;
	}

	public void setDuration(Duration duration) {
		this.duration = duration;
	}

	@Override
	public String toString() {
		return startTime + "(" + duration + ")";
	}
}

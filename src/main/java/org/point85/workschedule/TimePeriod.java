package org.point85.workschedule;

import java.time.Duration;
import java.time.LocalTime;

public abstract class TimePeriod extends Named {
	// starting time of day
	protected LocalTime startTime;

	// length of time period
	protected Duration duration;

	protected TimePeriod(String name, String description, LocalTime startTime, Duration duration) {
		super(name, description);
		this.startTime = startTime;
		this.duration = duration;
	}

	public Duration getDuration() {
		return duration;
	}

	public void setDuration(Duration duration) {
		this.duration = duration;
	}

	public LocalTime getStart() {
		return startTime;
	}

	public LocalTime getEnd() throws Exception {
		if (startTime == null) {
			throw new Exception("Start time is not defined.");
		}

		if (duration == null) {
			throw new Exception("Duration is not defined.");
		}
		return startTime.plus(duration);
	}

	public void setStart(LocalTime startTime) {
		this.startTime = startTime;
	}

	public abstract boolean isWorkingPeriod();

	@Override
	public String toString() {
		return super.toString() + ", Start: " + startTime + " (" + duration + ")";
	}
}

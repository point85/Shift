package org.point85.workschedule;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ShiftDefinition extends Nameable {
	// starting time of day
	private LocalTime start;

	// length of shift during the day
	private Duration duration;

	// shift rotation days
	private RotationDefinition rotationDefinition;

	// breaks
	private List<BreakPeriod> breaks = new ArrayList<>();

	ShiftDefinition(String name, String description, LocalTime start, Duration duration) {
		super(name, description);
		this.start = start;
		this.duration = duration;
	}

	public Duration getDuration() {
		return duration;
	}

	public void setDuration(Duration duration) {
		this.duration = duration;
	}

	public LocalTime getStart() {
		return start;
	}

	public void setStart(LocalTime startTime) {
		this.start = startTime;
	}

	public List<BreakPeriod> getBreaks() {
		return this.breaks;
	}

	public void setBreaks(List<BreakPeriod> breaks) {
		this.breaks = breaks;
	}

	public void addBreak(BreakPeriod breakPeriod) {

		if (!this.breaks.contains(breakPeriod)) {
			this.breaks.add(breakPeriod);
		}
	}

	public void removeBreak(NonWorkingPeriod breakDefinition) {
		if (this.breaks.contains(breakDefinition)) {
			this.breaks.remove(breakDefinition);
		}
	}

	public Duration calculateWorkingTime() {
		// add up breaks
		Duration breakDurations = Duration.ofSeconds(0);

		for (BreakPeriod breakDefinition : this.breaks) {
			breakDurations.plus(breakDefinition.getDuration());
		}

		// subtract from shift duration
		return this.duration.minus(breakDurations);
	}

	@Override
	public String toString() {
		String allBreaks = "";
		for (BreakPeriod breakDefinition : this.breaks) {
			if (allBreaks.length() > 0) {
				allBreaks += ",";
			}
			allBreaks += breakDefinition.toString();
		}
		return this.getName() + " (" + getDescription() + ") starts at " + getStart() + " for " + getDuration()
				+ " hours.  Breaks[" + allBreaks + "]";
	}

	public RotationDefinition getRotationDefinition() {
		return rotationDefinition;
	}

	public void setRotationDefinition(RotationDefinition rotationDefinition) {
		this.rotationDefinition = rotationDefinition;
	}

	public RotationDefinition createRotation(LocalDate rotationStart) {
		return new RotationDefinition(rotationStart);
	}

	public BreakPeriod createBreak(String name, String description, LocalTime startTime, Duration duration) {
		BreakPeriod period = new BreakPeriod(name, description, startTime, duration);
		addBreak(period);
		return period;
	}
}

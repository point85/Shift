package org.point85.workschedule;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Shift extends TimePeriod {

	// breaks
	private List<BreakPeriod> breaks = new ArrayList<>();

	Shift(String name, String description, LocalTime start, Duration duration) {
		super(name, description, start, duration);
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
		for (BreakPeriod period : this.breaks) {
			if (allBreaks.length() > 0) {
				allBreaks += ",";
			}
			allBreaks += period.toString();
		}
		return super.toString() + ", Breaks [" + allBreaks + "]";
	}


	public BreakPeriod createBreak(String name, String description, LocalTime startTime, Duration duration) {
		BreakPeriod period = new BreakPeriod(name, description, startTime, duration);
		addBreak(period);
		return period;
	}
	
	public OffShift createOffShift() {
		return new OffShift(name, description, startTime, duration);
	}

	@Override
	public boolean isWorkingPeriod() {
		return true;
	}
			
}

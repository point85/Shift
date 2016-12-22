package org.point85.workschedule;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Shift extends TimePeriod {

	// breaks
	private List<BreakPeriod> breaks = new ArrayList<>();
	
	// corresponding off-shift period
	private OffShift offShift;

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
		return getDuration().minus(breakDurations);
	}

	public BreakPeriod createBreak(String name, String description, LocalTime startTime, Duration duration) {
		BreakPeriod period = new BreakPeriod(name, description, startTime, duration);
		addBreak(period);
		return period;
	}

	public OffShift createOffShift() {
		return new OffShift(name, description, getStart(), getDuration());
	}

	@Override
	public boolean isWorkingPeriod() {
		return true;
	}

	public Duration getWorkingTimeTo(LocalTime time) throws Exception {
		Duration duration = null;

		if (time.isBefore(getStart()) || time.isAfter(getEnd())) {
			duration = Duration.ZERO;
		} else {
			duration = Duration.ofSeconds(time.toSecondOfDay() - getStart().toSecondOfDay());
		}

		return duration;
	}

	public Duration getWorkingTimeFrom(LocalTime time) throws Exception {
		Duration duration = null;

		if (time.isBefore(getStart()) || time.isAfter(getEnd())) {
			duration = Duration.ZERO;
		} else {
			duration = Duration.ofSeconds(getEnd().toSecondOfDay() - time.toSecondOfDay());
		}

		return duration;
	}

	@Override
	public String toString() {
		String text = super.toString();

		if (getBreaks().size() > 0) {
			text += "\n      " + getBreaks().size() + " Breaks: ";
		}

		for (BreakPeriod breakPeriod : getBreaks()) {
			text += "\n      " + breakPeriod.toString();
		}
		return text;
	}

	public OffShift getOffShift() {
		if (offShift == null) {
			offShift = createOffShift();
		}
		return offShift;
	}

	public void setOffShift(OffShift offShift) {
		this.offShift = offShift;
	}

}

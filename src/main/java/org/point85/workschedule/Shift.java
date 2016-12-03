package org.point85.workschedule;

import java.time.LocalDateTime;

public class Shift {
	// definition of the shift
	private ShiftDefinition shiftDefinition;

	// start date and time of day
	private LocalDateTime startTime;

	public Shift(ShiftDefinition shiftDefinition, LocalDateTime startTime) {
		this.setShiftDefinition(shiftDefinition);
		this.setStartTime(startTime);
	}

	public ShiftDefinition getShiftDefinition() {
		return shiftDefinition;
	}

	public void setShiftDefinition(ShiftDefinition shiftDefinition) {
		this.shiftDefinition = shiftDefinition;
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

	@Override
	public String toString() {
		String n = "Name: " + getShiftDefinition().getName();
		String s = ", Start: " + getStartTime();
		String d = ", Duration: "
				+ getShiftDefinition().getDuration().toString();

		return n + s + d;
	}
}

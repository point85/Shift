package org.point85.workschedule;

import java.time.Duration;
import java.time.LocalDate;

public class Team extends Named {

	// shift rotation days
	private ShiftRotation rotation;

	public Team(String name, String description) {
		super(name, description);
	}

	public ShiftRotation getShiftRotation() {
		return rotation;
	}

	public void setShiftRotation(ShiftRotation shiftRotation) {
		this.rotation = shiftRotation;
	}

	public ShiftRotation createRotation(LocalDate rotationStart) {
		ShiftRotation rotation = new ShiftRotation(rotationStart);
		this.rotation = rotation;
		return rotation;
	}

	public Duration getRotationDuration() throws Exception {
		if (rotation == null) {
			throw new Exception("The rotation for " + getName() + " is not defined.");
		}

		return Duration.ofDays(getShiftRotation().getDays());
	}

	public float getPercentageWorked() throws Exception {
		return ((float) getShiftRotation().getWorkingTime().getSeconds()) / ((float) getRotationDuration().getSeconds())
				* 100.0f;
	}

	public float getHoursWorkedPerWeek() {
		float days = (float) getShiftRotation().getDays();
		return ((float) getShiftRotation().getWorkingTime().getSeconds() / 3600.0f) * (7.0f / days);
	}

	public Duration getWorkingTime() {
		return getShiftRotation().getWorkingTime();
	}
}

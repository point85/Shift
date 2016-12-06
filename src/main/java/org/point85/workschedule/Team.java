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
		Duration sum = null;
		for (TimePeriod period : rotation.getRotation()) {
			if (sum == null) {
				sum = period.getDuration();
			} else {
				sum = sum.plus(period.getDuration());
			}
		}

		if (sum == null) {
			throw new Exception("The rotation for " + getName() + " is not defined.");
		}
		return sum;
	}

	public Duration getWorkingTime() {
		Duration sum = null;
		for (TimePeriod period : rotation.getRotation()) {
			if (period instanceof Shift) {
				if (sum == null) {
					sum = period.getDuration();
				} else {
					sum = sum.plus(period.getDuration());
				}
			}
		}
		return sum;
	}

	public float getPercentageWorked() throws Exception {
		return ((float) getWorkingTime().getSeconds()) / ((float) getRotationDuration().getSeconds()) * 100.0f;
	}

	public float getHoursWorkedPerWeek() {
		float days = (float) getShiftRotation().getDays();
		return ((float) getWorkingTime().getSeconds() / 3600.0f) * (7.0f / days);
	}
}

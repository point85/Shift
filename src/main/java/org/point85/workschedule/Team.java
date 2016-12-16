package org.point85.workschedule;

import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoField;

public class Team extends Named {

	// reference date for starting the rotations
	private LocalDate rotationStart;

	// shift rotation days
	private ShiftRotation rotation;

	public Team(String name, String description,  ShiftRotation rotation, LocalDate rotationStart) {
		super(name, description);
		this.rotation = rotation;
		this.rotationStart = rotationStart;
	}

	public LocalDate getRotationStart() {
		return rotationStart;
	}

	public void setRotationStart(LocalDate startDate) {
		this.rotationStart = startDate;
	}

	public ShiftRotation getShiftRotation() {
		return rotation;
	}

	public void setShiftRotation(ShiftRotation shiftRotation) {
		this.rotation = shiftRotation;
	}

	public ShiftRotation createRotation(LocalDate rotationStart) {
		ShiftRotation rotation = new ShiftRotation();
		this.rotation = rotation;
		this.rotationStart = rotationStart;
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

	public int getDayInRotation(LocalDate date) throws Exception {

		// calculate total number of days from start of rotation
		long dayFrom = rotationStart.getLong(ChronoField.EPOCH_DAY);
		long dayTo = date.getLong(ChronoField.EPOCH_DAY);
		long deltaDays = dayTo - dayFrom;

		if (deltaDays < 0) {
			throw new IllegalArgumentException("Start of rotation " + rotationStart + " must be earlier than " + date);
		}

		int dayInRotation = (int) (deltaDays % getShiftRotation().getDays());
		return dayInRotation;
	}

	public Duration getWorkingTimeTo(LocalDate date) throws Exception {
		Duration sum = null;

		int dayInRotation = getDayInRotation(date);

		for (int i = 0; i < dayInRotation; i++) {
			TimePeriod period = getShiftRotation().getPeriods().get(i);

			if (period.isWorkingPeriod()) {
				if (sum == null) {
					sum = period.getDuration();
				} else {
					sum = sum.plus(period.getDuration());
				}
			}
		}

		return sum;
	}

	public Duration getWorkingTimeFrom(LocalDate date) throws Exception {
		Duration sum = null;

		int dayInRotation = getDayInRotation(date);

		for (int i = dayInRotation; i < getShiftRotation().getDays(); i++) {
			TimePeriod period = getShiftRotation().getPeriods().get(i);

			if (period.isWorkingPeriod()) {
				if (sum == null) {
					sum = period.getDuration();
				} else {
					sum = sum.plus(period.getDuration());
				}
			}
		}

		return sum;
	}

	@Override
	public String toString() {
		return "Start: " + rotationStart;
	}
}

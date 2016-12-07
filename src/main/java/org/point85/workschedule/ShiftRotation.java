package org.point85.workschedule;

import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;

public class ShiftRotation {
	// reference date for starting the rotations
	private LocalDate startDate;

	// list of shifts and off-shift periods
	private List<TimePeriod> periods = new ArrayList<>();

	public ShiftRotation(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getRotationStart() {
		return startDate;
	}

	public void setRotationStart(LocalDate startDate) {
		this.startDate = startDate;
	}

	public List<TimePeriod> getPeriods() {
		return periods;
	}

	public void setPeriods(List<TimePeriod> periods) {
		this.periods = periods;
	}

	public void addOn(Shift shift, int count) {
		for (int i = 0; i < count; i++) {
			periods.add(shift);
		}
	}

	public void addOff(OffShift offShift, int count) {
		for (int i = 0; i < count; i++) {
			periods.add(offShift);
		}
	}

	public int getDays() {
		return periods.size();
	}

	public TimePeriod getOnOff(int index) {
		return periods.get(index);
	}

	public int getDayInRotation(LocalDate date) throws Exception {

		// calculate total number of days from start of rotation
		long dayFrom = startDate.getLong(ChronoField.EPOCH_DAY);
		long dayTo = date.getLong(ChronoField.EPOCH_DAY);
		long deltaDays = dayTo - dayFrom;

		if (deltaDays < 0) {
			throw new IllegalArgumentException("Start of rotation " + startDate + " must be earlier than " + date);
		}

		int dayInRotation = (int) (deltaDays % getDays());
		return dayInRotation;
	}

	public Duration getWorkingTime() {
		Duration sum = null;
		for (TimePeriod period : periods) {
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

	public Duration getWorkingTimeTo(LocalDate date) throws Exception {
		Duration sum = null;

		int dayInRotation = getDayInRotation(date);

		for (int i = 0; i < dayInRotation; i++) {
			TimePeriod period = periods.get(i);

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

		for (int i = dayInRotation; i < getDays(); i++) {
			TimePeriod period = periods.get(i);

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
		return "Start: " + startDate + " Rotation: " + periods;
	}
}

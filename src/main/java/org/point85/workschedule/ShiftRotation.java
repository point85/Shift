package org.point85.workschedule;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class ShiftRotation {
	// list of shifts and off-shift periods
	private List<TimePeriod> periods = new ArrayList<>();

	public ShiftRotation() {
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

	@Override
	public String toString() {
		return " Rotation: " + periods;
	}
}

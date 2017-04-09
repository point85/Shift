/*
MIT License

Copyright (c) 2016 Kent Randall

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
package org.point85.workschedule;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Class Rotation maintains a sequenced list of shift and off-shift time
 * periods.
 * 
 * @author Kent Randall
 *
 */
public class Rotation {

	// 24-hour day off period
	private static final DayOff DAY_OFF = initializeDayOff();

	// list of shift and off-shift periods
	private List<TimePeriod> periods = new ArrayList<>();

	/**
	 * Construct a shift rotation
	 * 
	 */
	public Rotation() {

	}

	private static DayOff initializeDayOff() {
		DayOff dayOff = null;
		try {
			dayOff = new DayOff("", "", LocalTime.MIDNIGHT, Duration.ofHours(24));
		} catch (Exception e) {
			// ignore
		}
		return dayOff;
	}

	/**
	 * Get the shifts and off-shifts in the rotation
	 * 
	 * @return List of periods
	 */
	public List<TimePeriod> getPeriods() {
		return periods;
	}

	/**
	 * Get the number of days in the rotation
	 * 
	 * @return Day count
	 */

	public int getDayCount() {
		return getPeriods().size();
	}

	/**
	 * Define a working shift period of time
	 * 
	 * @param count
	 *            Number of consecutive shifts
	 * @param shift
	 *            {@link Shift}
	 * @return This shift rotation
	 */
	public Rotation on(int count, Shift shift) {
		for (int i = 0; i < count; i++) {
			periods.add(shift);
		}
		return this;
	}

	/**
	 * Define an off-shift day
	 * 
	 * @param count
	 *            Number of consecutive off-shift days
	 * @return This shift rotation
	 * @throws Exception
	 *             exception
	 */
	public Rotation off(int count) throws Exception {
		for (int i = 0; i < count; i++) {
			periods.add(Rotation.DAY_OFF);
		}
		return this;
	}

	/**
	 * Get the duration of this rotation
	 * 
	 * @return Duration
	 */
	public Duration getDuration() {
		return Duration.ofDays(periods.size());
	}

	/**
	 * Get the shift rotation's total working time
	 * 
	 * @return Duration of working time
	 */
	public Duration getWorkingTime() {
		Duration sum = Duration.ZERO;

		for (TimePeriod period : periods) {
			if (period.isWorkingPeriod()) {
				sum = sum.plus(period.getDuration());
			}
		}
		return sum;
	}

	/**
	 * Build a string representation of this rotation
	 */
	@Override
	public String toString() {
		String rd = WorkSchedule.getMessage("rotation.duration");
		String rda = WorkSchedule.getMessage("rotation.days");
		String rw = WorkSchedule.getMessage("rotation.working");
		String rper = WorkSchedule.getMessage("rotation.periods");
		String on = WorkSchedule.getMessage("rotation.on");
		String off = WorkSchedule.getMessage("rotation.off");

		String periodsString = "";

		for (TimePeriod period : periods) {
			if (periodsString.length() > 0) {
				periodsString += ", ";
			}

			String onOff = period.isWorkingPeriod() ? on : off;
			periodsString += period.getName() + " (" + onOff + ")";
		}

		String text = rper + ": [" + periodsString + "], " + rd + ": " + getDuration() + ", " + rda + ": "
				+ getDuration().toDays() + ", " + rw + ": " + getWorkingTime();

		return text;
	}
}

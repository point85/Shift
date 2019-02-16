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
import java.util.Collections;
import java.util.List;

/**
 * Class Rotation maintains a sequenced list of shift and off-shift time
 * periods.
 * 
 * @author Kent Randall
 *
 */
public class Rotation extends Named implements Comparable<Rotation> {

	// working periods in the rotation
	private List<RotationSegment> rotationSegments = new ArrayList<>();

	// list of working and non-working days
	private transient List<TimePeriod> periods;

	// name of the day off time period
	private static final String DAY_OFF_NAME = "DAY_OFF";

	// 24-hour day off period
	private static final DayOff DAY_OFF = initializeDayOff();

	// owning work schedule
	private WorkSchedule workSchedule;

	/**
	 * Default constructor
	 */
	public Rotation() {
		super();
	}

	/**
	 * Constructor
	 * 
	 * @param name        Rotation name
	 * @param description Description
	 * @throws Exception Exception
	 */
	Rotation(String name, String description) throws Exception {
		super(name, description);
	}

	// create the day-off
	private static DayOff initializeDayOff() {
		DayOff dayOff = null;
		try {
			dayOff = new DayOff(DAY_OFF_NAME, "24 hour off period", LocalTime.MIDNIGHT, Duration.ofHours(24));
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
		if (periods == null) {
			periods = new ArrayList<>();

			// sort by sequence number
			Collections.sort(rotationSegments);

			for (RotationSegment segment : rotationSegments) {
				// add the on days
				if (segment.getStartingShift() != null) {
					for (int i = 0; i < segment.getDaysOn(); i++) {
						periods.add(segment.getStartingShift());
					}
				}

				// add the off days
				for (int i = 0; i < segment.getDaysOff(); i++) {
					periods.add(Rotation.DAY_OFF);
				}
			}
		}

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
	 * Get the duration of this rotation
	 * 
	 * @return Duration
	 */
	public Duration getDuration() {
		return Duration.ofDays(getPeriods().size());
	}

	/**
	 * Get the shift rotation's total working time
	 * 
	 * @return Duration of working time
	 */
	public Duration getWorkingTime() {
		Duration sum = Duration.ZERO;

		for (TimePeriod period : getPeriods()) {
			if (period.isWorkingPeriod()) {
				sum = sum.plus(period.getDuration());
			}
		}
		return sum;
	}

	/**
	 * Get the rotation's working periods
	 * 
	 * @return List of {@link RotationSegment}
	 */
	public List<RotationSegment> getRotationSegments() {
		return rotationSegments;
	}

	/**
	 * Add a working period to this rotation. A working period starts with a shift
	 * and specifies the number of days on and days off
	 * 
	 * @param startingShift {@link Shift} that starts the period
	 * @param daysOn        Number of days on shift
	 * @param daysOff       Number of days off shift
	 * @return {@link RotationSegment}
	 * @throws Exception Exception
	 */
	public RotationSegment addSegment(Shift startingShift, int daysOn, int daysOff) throws Exception {
		if (startingShift == null) {
			throw new Exception("The starting shift must be specified.");
		}
		RotationSegment segment = new RotationSegment(startingShift, daysOn, daysOff, this);
		rotationSegments.add(segment);
		segment.setSequence(rotationSegments.size());
		return segment;
	}

	/**
	 * Get the work schedule that owns this rotation
	 * 
	 * @return {@link WorkSchedule}
	 */
	public WorkSchedule getWorkSchedule() {
		return workSchedule;
	}

	void setWorkSchedule(WorkSchedule workSchedule) {
		this.workSchedule = workSchedule;
	}

	@Override
	public int compareTo(Rotation other) {
		return getName().compareTo(other.getName());
	}

	/**
	 * Build a string representation of this rotation
	 */
	@Override
	public String toString() {
		String named = super.toString();
		String rd = WorkSchedule.getMessage("rotation.duration");
		String rda = WorkSchedule.getMessage("rotation.days");
		String rw = WorkSchedule.getMessage("rotation.working");
		String rper = WorkSchedule.getMessage("rotation.periods");
		String on = WorkSchedule.getMessage("rotation.on");
		String off = WorkSchedule.getMessage("rotation.off");

		String periodsString = "";

		for (TimePeriod period : getPeriods()) {
			if (periodsString.length() > 0) {
				periodsString += ", ";
			}

			String onOff = period.isWorkingPeriod() ? on : off;
			periodsString += period.getName() + " (" + onOff + ")";
		}

		String text = named + "\n" + rper + ": [" + periodsString + "], " + rd + ": " + getDuration() + ", " + rda
				+ ": " + getDuration().toDays() + ", " + rw + ": " + getWorkingTime();

		return text;
	}
}

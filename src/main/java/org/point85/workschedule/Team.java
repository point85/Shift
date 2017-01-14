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

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoField;

/**
 * Class Team is a group of individuals who rotate through a shift schedule
 * 
 * @author Kent Randall
 *
 */
public class Team extends Named {

	// reference date for starting the rotations
	private LocalDate rotationStart;

	// shift rotation days
	private ShiftRotation rotation;

	Team(String name, String description, ShiftRotation rotation, LocalDate rotationStart) {
		super(name, description);
		this.rotation = rotation;
		this.rotationStart = rotationStart;
	}

	/**
	 * Get rotation start
	 * 
	 * @return Rotation start date
	 */
	public LocalDate getRotationStart() {
		return rotationStart;
	}

	/**
	 * Get the shift rotation for this team
	 * 
	 * @return {@link ShiftRotation}
	 */
	public ShiftRotation getShiftRotation() {
		return rotation;
	}

	/**
	 * Get the duration of the shift rotation
	 * 
	 * @return Duration
	 * @throws Exception
	 */
	public Duration getRotationDuration() throws Exception {
		if (rotation == null) {
			String msg = MessageFormat.format(WorkSchedule.getMessage("rotation.not.defined"), getName());
			throw new Exception(msg);
		}

		return getShiftRotation().getDuration();
	}

	/**
	 * Get the shift rotation's working time as a percentage of the rotation
	 * duration
	 * 
	 * @return Percentage
	 * @throws Exception
	 */
	public float getPercentageWorked() throws Exception {
		return ((float) getShiftRotation().getWorkingTime().getSeconds()) / ((float) getRotationDuration().getSeconds())
				* 100.0f;
	}

	/**
	 * Get the average number of hours worked each week by this team
	 * 
	 * @return
	 */
	/*
	public float getHoursWorkedPerWeek() {
		float days = (float) getShiftRotation().getDuration().toDays();
		return ((float) getShiftRotation().getWorkingTime().getSeconds() / 3600.0f) * (7.0f / days);
	}
	*/

	public Duration getHoursWorkedPerWeek() {
		float days = (float) getShiftRotation().getDuration().toDays();
		float secPerWeek = (float) getShiftRotation().getWorkingTime().getSeconds() * (7.0f / days);
		return Duration.ofSeconds((long) secPerWeek);
	}

	/**
	 * Get the day number in the rotation for this local date
	 * 
	 * @param date
	 *            LocalDate
	 * @return day number in the rotation, starting at 0
	 * @throws Exception
	 */
	public int getDayInRotation(LocalDate date) throws Exception {

		// calculate total number of days from start of rotation
		long dayFrom = rotationStart.getLong(ChronoField.EPOCH_DAY);
		long dayTo = date.getLong(ChronoField.EPOCH_DAY);
		long deltaDays = dayTo - dayFrom;

		if (deltaDays < 0) {
			String msg = MessageFormat.format(WorkSchedule.getMessage("end.earlier.than.start"), rotationStart, date);
			throw new Exception(msg);
		}

		int dayInRotation = (int) (deltaDays % getShiftRotation().getDuration().toDays());
		return dayInRotation;
	}

	/**
	 * Calculate the working time from the beginning of the rotation to the
	 * specified day in the rotation
	 * 
	 * @param date
	 *            Date in the rotation
	 * @return Duration
	 * @throws Exception
	 */
	public Duration calculateWorkingTimeTo(LocalDate date) throws Exception {
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

	/**
	 * Calculate the working time from the specified day in the rotation to the
	 * end of the rotation
	 * 
	 * @param date
	 *            Date in the rotation
	 * @return Duration
	 * @throws Exception
	 */
	public Duration calculateWorkingTimeFrom(LocalDate date) throws Exception {
		Duration sum = null;

		int dayInRotation = getDayInRotation(date);

		for (int i = dayInRotation; i < getShiftRotation().getDuration().toDays(); i++) {
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

	/**
	 * Build a string value for this team
	 */
	@Override
	public String toString() {
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(2);
		String rs = WorkSchedule.getMessage("rotation.start");
		String rd = WorkSchedule.getMessage("rotation.duration");
		String rda = WorkSchedule.getMessage("rotation.days");
		String rw = WorkSchedule.getMessage("rotation.working");
		String rp = WorkSchedule.getMessage("rotation.percentage");
		String avg = WorkSchedule.getMessage("team.hours");

		String text;
		try {
			text = super.toString() + ", " + rs + ": " + getRotationStart() + ", " + rd + ": " + getRotationDuration()
					+ ", " + rda + ": " + getShiftRotation().getDuration().toDays() + ", " + rw + ": "
					+ getShiftRotation().getWorkingTime() + ", " + rp + ": " + df.format(getPercentageWorked()) + "%"
					+ ", " + avg + ": " + getHoursWorkedPerWeek();

		} catch (Exception e) {
			text = e.getMessage();
		}

		return text;
	}
}

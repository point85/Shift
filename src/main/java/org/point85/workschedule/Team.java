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
 * @author Kent Randall
 *
 */
public class Team extends Named {

	// reference date for starting the rotations
	private LocalDate rotationStart;

	// shift rotation days
	private ShiftRotation rotation;

	/**
	 * Construct a team
	 * @param name Name of team
	 * @param description Team description
	 * @param rotation Shift rotation
	 * @param rotationStart Start of rotation
	 */
	public Team(String name, String description, ShiftRotation rotation, LocalDate rotationStart) {
		super(name, description);
		this.rotation = rotation;
		this.rotationStart = rotationStart;
	}

	/**
	 * Get rotation start
	 * @return Rotation start date
	 */
	public LocalDate getRotationStart() {
		return rotationStart;
	}

	/**
	 * Set the rotation start
	 * @param startDate Start date
	 */
	public void setRotationStart(LocalDate startDate) {
		this.rotationStart = startDate;
	}

	/**
	 * Get the shift rotation for this team
	 * @return {@link ShiftRotation}
	 */
	public ShiftRotation getShiftRotation() {
		return rotation;
	}

	/**
	 * Set the shift rotation
	 * @param shiftRotation {@link ShiftRotation}
	 */
	public void setShiftRotation(ShiftRotation shiftRotation) {
		this.rotation = shiftRotation;
	}

	/**
	 * Get the duration of the shift rotation
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
	 * Get the shift rotation's working time as a percentage of the rotation duration
	 * @return Percentage
	 * @throws Exception
	 */
	public float getPercentageWorked() throws Exception {
		return ((float) getShiftRotation().getWorkingTime().getSeconds()) / ((float) getRotationDuration().getSeconds())
				* 100.0f;
	}

	/**
	 * Get the average number of hours worked each week by this team
	 * @return 
	 */
	public float getHoursWorkedPerWeek() {
		float days = (float) getShiftRotation().getDuration().toDays();
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

		int dayInRotation = (int) (deltaDays % getShiftRotation().getDuration().toDays());
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

	@Override
	public String toString() {
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(2);

		String text;
		try {
			text = super.toString() + ", rotation start: " + getRotationStart() + ", rotation duration: "
					+ getRotationDuration() + ", days in rotation: " + getShiftRotation().getDuration().toDays()
					+ ", scheduled working time: " + getWorkingTime() + ", percentage worked: "
					+ df.format(getPercentageWorked()) + "%" + ", avg hours worked per week: "
					+ getHoursWorkedPerWeek();

		} catch (Exception e) {
			text = e.getMessage();
		}

		return text;
	}
}

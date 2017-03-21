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
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Class Team is a named group of individuals who rotate through a shift
 * schedule.
 * 
 * @author Kent Randall
 *
 */
public class Team extends Named {

	// reference date for starting the rotations
	private LocalDate rotationStart;

	// shift rotation days
	private Rotation rotation;

	Team(String name, String description, Rotation rotation, LocalDate rotationStart) throws Exception {
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
	 * Get rotation start
	 * 
	 * @param rotationStart
	 *            Starting date of rotation
	 */
	public void setRotationStart(LocalDate rotationStart) {
		this.rotationStart = rotationStart;
	}

	/**
	 * Get the shift rotation for this team
	 * 
	 * @return {@link Rotation}
	 */
	public Rotation getRotation() {
		return rotation;
	}

	/**
	 * Get the duration of the shift rotation
	 * 
	 * @return Duration
	 * @throws Exception
	 *             exception
	 */
	public Duration getRotationDuration() throws Exception {
		return getRotation().getDuration();
	}

	/**
	 * Get the shift rotation's working time as a percentage of the rotation
	 * duration
	 * 
	 * @return Percentage
	 * @throws Exception
	 *             exception
	 */
	public float getPercentageWorked() throws Exception {
		return ((float) getRotation().getWorkingTime().getSeconds()) / ((float) getRotationDuration().getSeconds())
				* 100.0f;
	}

	/**
	 * Get the average number of hours worked each week by this team
	 * 
	 * @return Duration of hours worked per week
	 */
	public Duration getHoursWorkedPerWeek() {
		float days = (float) getRotation().getDuration().toDays();
		float secPerWeek = (float) getRotation().getWorkingTime().getSeconds() * (7.0f / days);
		return Duration.ofSeconds((long) secPerWeek);
	}

	/**
	 * Get the day number in the rotation for this local date
	 * 
	 * @param date
	 *            LocalDate
	 * @return day number in the rotation, starting at 0
	 * @throws Exception
	 *             exception
	 */
	public int getDayInRotation(LocalDate date) throws Exception {

		// calculate total number of days from start of rotation
		long dayFrom = rotationStart.toEpochDay();
		long dayTo = date.toEpochDay();
		long deltaDays = dayTo - dayFrom;

		if (deltaDays < 0) {
			String msg = MessageFormat.format(WorkSchedule.getMessage("end.earlier.than.start"), rotationStart, date);
			throw new Exception(msg);
		}

		int dayInRotation = (int) (deltaDays % getRotation().getDuration().toDays());
		return dayInRotation;
	}

	// Calculate the working time from the specified day in the rotation to the
	// end of the rotation
	private Duration calculateWorkingTimeFromToEnd(LocalDate date) throws Exception {
		Duration sum = Duration.ZERO;

		int dayInRotation = getDayInRotation(date);

		for (int i = dayInRotation; i < getRotation().getDuration().toDays(); i++) {
			TimePeriod period = getRotation().getPeriods().get(i);

			if (period.isWorkingPeriod()) {
				sum = sum.plus(period.getDuration());
			}
		}

		return sum;
	}
	
	/**
	 * Get the {@link ShiftInstance} for the specified day
	 * @param day Day with a shift instance
	 * @return {@link ShiftInstance}
	 * @throws Exception exception
	 */

	public ShiftInstance getShiftInstanceForDay(LocalDate day) throws Exception {
		ShiftInstance instance = null;

		Rotation shiftRotation = getRotation();
		int dayInRotation = getDayInRotation(day);

		// shift or off shift
		TimePeriod period = shiftRotation.getPeriods().get(dayInRotation);

		if (period.isWorkingPeriod()) {
			LocalDateTime startDateTime = LocalDateTime.of(day, period.getStart());
			instance = new ShiftInstance((Shift) period, startDateTime, this);
		}

		return instance;
	}

	/**
	 * Calculate the schedule working time between the specified dates and times
	 * 
	 * @param from
	 *            Starting date and time of day
	 * @param to
	 *            Ending date and time of day
	 * @return Duration of working time
	 * @throws Exception
	 *             exception
	 */
	public Duration calculateWorkingTime(LocalDateTime from, LocalDateTime to) throws Exception {
		Duration sum = Duration.ZERO;

		if (from.isAfter(to)) {
			String msg = MessageFormat.format(WorkSchedule.getMessage("end.earlier.than.start"), to, from); 
			throw new Exception(msg);
		}

		// find number of complete rotations
		LocalDate fromDate = from.toLocalDate();
		LocalDate toDate = to.toLocalDate();

		long deltaDays = toDate.toEpochDay() - fromDate.toEpochDay();

		Rotation rotation = getRotation();
		long rotationDays = rotation.getDayCount();

		long rotationCount = deltaDays / rotationDays;
		Duration rotationTime = rotation.getWorkingTime();

		for (int i = 0; i < rotationCount; i++) {
			sum = sum.plus(rotationTime);
		}

		if (deltaDays % rotationDays != 0) {
			Duration begin = calculateWorkingTimeFromToEnd(fromDate);
			Duration end = calculateWorkingTimeFromToEnd(toDate);
			sum = sum.plus(begin.minus(end));
		}

		// remove day edge effects
		Shift shift = null;
		Duration edge = null;

		// from midnight to starting time of day in day 1
		ShiftInstance instance = getShiftInstanceForDay(fromDate);

		if (instance != null) {
			shift = instance.getShift();
			edge = shift.calculateWorkingTime(LocalTime.MIN, from.toLocalTime());
			sum = sum.minus(edge);
		}

		// from ending time of day to midnight in day 1
		instance = getShiftInstanceForDay(toDate);

		if (instance != null) {
			shift = instance.getShift();
			edge = shift.calculateWorkingTime(LocalTime.MIN, to.toLocalTime());
			sum = sum.plus(edge);
		}

		return sum;
	}

	/**
	 * Build a string value for this team
	 */
	@Override
	public String toString() {
		String rpct = WorkSchedule.getMessage("rotation.percentage");
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(2);

		String rs = WorkSchedule.getMessage("rotation.start");
		String avg = WorkSchedule.getMessage("team.hours");

		String text = "";
		try {
			text = super.toString() + ", " + rs + ": " + getRotationStart() + ", " + getRotation() + ", " + rpct + ": "
					+ df.format(getPercentageWorked()) + "%" + ", " + avg + ": " + getHoursWorkedPerWeek();

		} catch (Exception e) {
			// ignore
		}

		return text;
	}
}

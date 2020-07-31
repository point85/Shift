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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Class NonWorkingPeriod represents named non-working, non-recurring periods.
 * For example holidays and scheduled outages such as for preventive
 * maintenance.
 * 
 * @author Kent Randall
 *
 */
public class NonWorkingPeriod extends Named implements Comparable<NonWorkingPeriod> {
	// owning work schedule
	private WorkSchedule workSchedule;

	// starting date and time of day
	private LocalDateTime startDateTime;

	// duration of period
	private Duration duration;

	/**
	 * Default constructor
	 */
	public NonWorkingPeriod() {
		super();
	}

	NonWorkingPeriod(String name, String description, LocalDateTime startDateTime, Duration duration) throws Exception {
		super(name, description);
		setStartDateTime(startDateTime);
		setDuration(duration);
	}

	/**
	 * Get period start date and time
	 * 
	 * @return Start date and time
	 */
	public LocalDateTime getStartDateTime() {
		return startDateTime;
	}

	/**
	 * Set period start date and time
	 * 
	 * @param startDateTime Period start
	 * @throws Exception exception
	 */
	public void setStartDateTime(LocalDateTime startDateTime) throws Exception {
		if (startDateTime == null) {
			throw new Exception(WorkSchedule.getMessage("start.not.defined"));
		}

		this.startDateTime = startDateTime;
	}

	/**
	 * Get period end date and time
	 * 
	 * @return Period end
	 * @throws Exception exception
	 */
	public LocalDateTime getEndDateTime() throws Exception {
		return startDateTime.plus(duration);
	}

	/**
	 * Get period duration
	 * 
	 * @return Duration
	 */
	public Duration getDuration() {
		return duration;
	}

	/**
	 * Set duration
	 * 
	 * @param duration Duration
	 * @throws Exception exception
	 */
	public void setDuration(Duration duration) throws Exception {
		if (duration == null || duration.getSeconds() == 0) {
			throw new Exception(WorkSchedule.getMessage("duration.not.defined"));
		}

		this.duration = duration;
	}

	/**
	 * Build a string representation of this non-working period
	 */
	@Override
	public String toString() {
		String text = "";
		String start = WorkSchedule.getMessage("period.start");
		String end = WorkSchedule.getMessage("period.end");

		try {
			text = super.toString() + ", " + start + ": " + getStartDateTime() + " (" + getDuration() + ")" + ", " + end
					+ ": " + getEndDateTime();
		} catch (Exception e) {
			// ignore
		}
		return text;
	}

	/**
	 * Compare this non-working period to another such period by start date and time
	 * of day
	 * 
	 * @param other {@link NonWorkingPeriod}
	 * @return negative if less than, 0 if equal and positive if greater than
	 */
	@Override
	public int compareTo(NonWorkingPeriod other) {
		return getStartDateTime().compareTo(other.getStartDateTime());
	}

	/**
	 * Compare this NonWorkingPeriod to another NonWorkingPeriod
	 * 
	 * @return true if equal
	 */
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof NonWorkingPeriod)) {
			return false;
		}
		NonWorkingPeriod otherNonWorkingPeriod = (NonWorkingPeriod) other;

		// same work schedule
		if (getWorkSchedule() != null && otherNonWorkingPeriod.getWorkSchedule() != null) {
			if (!getWorkSchedule().equals(otherNonWorkingPeriod.getWorkSchedule())) {
				return false;
			}
		}

		return super.equals(other);
	}
	
	/**
	 * Get the hash code
	 * 
	 * @return hash code
	 */
	@Override
	public int hashCode() {
		return Objects.hash(getName(), getWorkSchedule());
	}

	/**
	 * Get the work schedule that owns this non-working period
	 * 
	 * @return {@link WorkSchedule}
	 */
	public WorkSchedule getWorkSchedule() {
		return workSchedule;
	}

	void setWorkSchedule(WorkSchedule workSchedule) {
		this.workSchedule = workSchedule;
	}

	/**
	 * Check to see if this day is contained in the non-working period
	 * 
	 * @param day Date to check
	 * @return True if in the non-working period
	 * @throws Exception Exception
	 */
	public boolean isInPeriod(LocalDate day) throws Exception {
		boolean isInPeriod = false;

		LocalDate periodStart = getStartDateTime().toLocalDate();
		LocalDate periodEnd = getEndDateTime().toLocalDate();

		if (day.compareTo(periodStart) >= 0 && day.compareTo(periodEnd) <= 0) {
			isInPeriod = true;
		}

		return isInPeriod;
	}
}

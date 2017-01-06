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
import java.time.LocalDateTime;

/**
 * Class NonWorkingPeriod represents non-working, non-recurring periods, e.g.
 * holidays and scheduled outages
 * 
 * @author Kent Randall
 *
 */
public class NonWorkingPeriod extends Named implements Comparable<NonWorkingPeriod> {
	// starting date and time of day
	private LocalDateTime startDateTime;

	// duration of period
	private Duration duration;

	NonWorkingPeriod(String name, String description, LocalDateTime startDateTime, Duration duration) {
		super(name, description);
		this.startDateTime = startDateTime;
		this.duration = duration;
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
	 * @param startDateTime
	 *            Period start
	 */
	public void setStartDateTime(LocalDateTime startDateTime) {
		this.startDateTime = startDateTime;
	}

	/**
	 * Get period end date and time
	 * 
	 * @return Period end
	 * @throws Exception
	 */
	public LocalDateTime getEndDateTime() throws Exception {
		if (startDateTime == null) {
			throw new Exception(WorkSchedule.getMessage("start.not.defined"));
		}

		if (duration == null) {
			throw new Exception(WorkSchedule.getMessage("duration.not.defined"));
		}
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
	 * @param duration
	 *            Duration
	 */
	public void setDuration(Duration duration) {
		this.duration = duration;
	}

	/**
	 * Get a string representation of this non-working period
	 */
	@Override
	public String toString() {
		String text = null;
		String start = WorkSchedule.getMessage("period.start");
		String end = WorkSchedule.getMessage("period.end");

		try {
			text = super.toString() + ", " + start + ": " + getStartDateTime() + " (" + getDuration() + ")" + ", " + end
					+ ": " + getEndDateTime();
		} catch (Exception e) {
			text = e.getMessage();
		}
		return text;
	}

	/**
	 * Compare this non-working period to another such period
	 * @return -1 if less than, 0 if equal and 1 if greater than
	 */
	@Override
	public int compareTo(NonWorkingPeriod other) {
		return getStartDateTime().compareTo(other.getStartDateTime());
	}
}

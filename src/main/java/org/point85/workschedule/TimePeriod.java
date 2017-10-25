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

/**
 * Class TimePeriod is a named period of time with a specified duration and
 * starting time of day.
 * 
 * @author Kent Randall
 *
 */
abstract class TimePeriod extends Named {
	private static final int SECONDS_PER_DAY = 24 * 60 * 60;
	
	// starting time of day
	private LocalTime startTime;

	// length of time period
	private Duration duration;

	protected TimePeriod() {
		super();
	}
	
	protected TimePeriod(String name, String description, LocalTime startTime, Duration duration) throws Exception {
		super(name, description);
		setStart(startTime);
		setDuration(duration);
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
	 *            Period duration
	 * @throws Exception exception
	 */
	public void setDuration(Duration duration) throws Exception {
		if (duration == null || duration.getSeconds() == 0) {
			throw new Exception(WorkSchedule.getMessage("duration.not.defined"));
		}
		
		if (duration.getSeconds() > SECONDS_PER_DAY) {
			throw new Exception(WorkSchedule.getMessage("duration.not.allowed"));
		}
		this.duration = duration;
	}

	/**
	 * Get period start time
	 * 
	 * @return Period start
	 */
	public LocalTime getStart() {
		return startTime;
	}
	
	/**
	 * Set period start time
	 * 
	 * @param startTime
	 *            Start time
	 * @throws Exception exception
	 */
	public void setStart(LocalTime startTime) throws Exception {
		if (startTime == null) {
			throw new Exception(WorkSchedule.getMessage("start.not.defined"));
		}
		this.startTime = startTime;
	}

	/**
	 * Get period end
	 * 
	 * @return End time
	 * @throws Exception exception
	 */
	public LocalTime getEnd() throws Exception {
		return startTime.plus(duration);
	}

	// breaks are considered to be in the shift's working period
	abstract boolean isWorkingPeriod();

	/**
	 * Build a string value for this period
	 */
	@Override
	public String toString() {
		String text = "";
		String start = WorkSchedule.getMessage("period.start");
		String end = WorkSchedule.getMessage("period.end");

		try {
			text = super.toString() + ", " + start + ": " + getStart() + " (" + getDuration() + ")" + ", " + end + ": "
					+ getEnd();
		} catch (Exception e) {
			// ignore
		}

		return text;
	}
}

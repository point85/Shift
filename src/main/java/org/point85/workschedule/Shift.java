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
 * Class Shift is a scheduled working time period
 * 
 * @author Kent Randall
 *
 */
public class Shift extends TimePeriod {

	// breaks
	private List<Break> breaks = new ArrayList<>();

	// corresponding off-shift period
	private OffShift offShift;

	Shift(String name, String description, LocalTime start, Duration duration) {
		super(name, description, start, duration);
	}

	/**
	 * Get the break periods for this shift
	 * 
	 * @return List {@link Break}
	 */
	public List<Break> getBreaks() {
		return this.breaks;
	}

	/**
	 * Add a break period to this shift
	 * 
	 * @param breakPeriod
	 *            {@link Break}
	 */
	public void addBreak(Break breakPeriod) {
		if (!this.breaks.contains(breakPeriod)) {
			this.breaks.add(breakPeriod);
		}
	}

	/**
	 * Remove a break from this shift
	 * 
	 * @param breakDefinition
	 */
	public void removeBreak(Break breakPeriod) {
		if (this.breaks.contains(breakPeriod)) {
			this.breaks.remove(breakPeriod);
		}
	}

	/**
	 * Calculate the working time as the scheduled time less breaks
	 * 
	 * @return
	 */
	public Duration calculateWorkingTime() {
		// add up breaks
		Duration breakDurations = Duration.ofSeconds(0);

		for (Break breakDefinition : this.breaks) {
			breakDurations.plus(breakDefinition.getDuration());
		}

		// subtract from shift duration
		return getDuration().minus(breakDurations);
	}

	/**
	 * Create a break for this shift
	 * 
	 * @param name
	 *            Name of break
	 * @param description
	 *            Description of break
	 * @param startTime
	 *            Start of break
	 * @param duration
	 *            Duration of break
	 * @return {@link Break}
	 */
	public Break createBreak(String name, String description, LocalTime startTime, Duration duration) {
		Break period = new Break(name, description, startTime, duration);
		addBreak(period);
		return period;
	}

	/**
	 * Create an off-shift time period
	 * 
	 * @return {@link OffShift}
	 */
	public OffShift createOffShift() {
		return new OffShift(getName(), getDescription(), getStart(), getDuration());
	}

	@Override
	boolean isWorkingPeriod() {
		return true;
	}

	/**
	 * Calculate working time from the beginning of the shift to the specified
	 * time
	 * 
	 * @param time
	 *            Ending time
	 * @return Duration
	 * @throws Exception
	 */
	public Duration getWorkingTimeTo(LocalTime time) throws Exception {
		Duration duration = null;

		if (time.isBefore(getStart()) || time.isAfter(getEnd())) {
			duration = Duration.ZERO;
		} else {
			duration = Duration.ofSeconds(time.toSecondOfDay() - getStart().toSecondOfDay());
		}

		return duration;
	}

	/**
	 * Calculate the working time from the specified time to the end of the
	 * shift
	 * 
	 * @param time
	 *            Beginning time
	 * @return Duration
	 * @throws Exception
	 */
	public Duration getWorkingTimeFrom(LocalTime time) throws Exception {
		Duration duration = null;

		if (time.isBefore(getStart()) || time.isAfter(getEnd())) {
			duration = Duration.ZERO;
		} else {
			duration = Duration.ofSeconds(getEnd().toSecondOfDay() - time.toSecondOfDay());
		}

		return duration;
	}

	OffShift getOffShift() {
		if (offShift == null) {
			offShift = createOffShift();
		}
		return offShift;
	}

	@Override
	public String toString() {
		String text = super.toString();

		if (getBreaks().size() > 0) {
			text += "\n      " + getBreaks().size() + " Breaks: ";
		}

		for (Break breakPeriod : getBreaks()) {
			text += "\n      " + breakPeriod.toString();
		}
		return text;
	}

}

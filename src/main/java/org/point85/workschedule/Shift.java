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

import java.text.MessageFormat;
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

	// corresponding off-shift break period
	private Break offShift;

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
	 * @return Duration
	 */
	public Duration calculateWorkingTime() {
		// add up breaks
		Duration breaksDuration = Duration.ofSeconds(0);

		for (Break breakDefinition : this.breaks) {
			breaksDuration = breaksDuration.plus(breakDefinition.getDuration());
		}

		// subtract from shift duration
		return getDuration().minus(breaksDuration);
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

	@Override
	boolean isWorkingPeriod() {
		return true;
	}

	/**
	 * Calculate working time from the start of the shift to the specified time
	 * 
	 * @param to
	 *            Ending time
	 * @return Duration
	 * @throws Exception
	 */
	public Duration getWorkingTimeBetween(LocalTime from, LocalTime to) throws Exception {
		Duration duration = null;

		LocalTime start = getStart();
		LocalTime end = getEnd();

		int toSecond = to.toSecondOfDay();
		int fromSecond = from.toSecondOfDay();

		// look for 24 hr shift
		if (toSecond == fromSecond) {
			if (this.getDuration().toHours() == 24) {
				duration = Duration.ofHours(24);
			} else {
				duration = Duration.ZERO;
			}
		} else {

			if (start.isBefore(end)) {
				// shift did not cross midnight
				duration = Duration.ofSeconds(toSecond - fromSecond);
			} else {
				// shift crossed midnight
				if (toSecond >= fromSecond) {
					// after midnight
					duration = Duration.ofSeconds(toSecond - fromSecond);
				} else {
					// before midnight
					Duration toMidnight = Duration.ofDays(1).minus(Duration.ofSeconds(fromSecond));
					Duration fromMidnight = Duration.ofSeconds(toSecond);
					duration = toMidnight.plus(fromMidnight);
				}
			}
		}

		if (duration.isNegative() || duration.getSeconds() > getDuration().getSeconds()) {
			String msg = MessageFormat.format(WorkSchedule.getMessage("period.not.in.shift"), from, to, start, end);
			throw new Exception(msg);
		}

		return duration;
	}

	public boolean isInShift(LocalTime time) throws Exception {
		boolean answer = false;

		LocalTime start = getStart();
		LocalTime end = getEnd();

		int onStart = time.compareTo(start);
		int onEnd = time.compareTo(end);

		int timeSecond = time.toSecondOfDay();

		if (start.isBefore(end)) {
			// shift did not cross midnight
			if (onStart >= 0 && onEnd <= 0) {
				answer = true;
			}
		} else {
			// shift crossed midnight, check before and after midnight
			if (timeSecond <= end.toSecondOfDay()) {
				// after midnight
				answer = true;
			} else {
				// before midnight
				if (timeSecond >= start.toSecondOfDay()) {
					answer = true;
				}
			}
		}
		return answer;
	}

	Break getOffShift() {
		if (offShift == null) {
			offShift = new Break(getName(), getDescription(), getStart(), getDuration());
		}
		return offShift;
	}

	/** 
	 * Calculate the total break time for the shift
	 * @return Duration of all breaks
	 */
	public Duration calculateBreakTime() {
		Duration sum = null;

		List<Break> breaks = this.getBreaks();

		for (Break b : breaks) {
			if (sum == null) {
				sum = b.getDuration();
			} else {
				sum = sum.plus(b.getDuration());
			}
		}

		return sum;
	}

	/**
	 * Build a string representation of this shift
	 * 
	 * @return String
	 */
	@Override
	public String toString() {
		String text = super.toString();

		if (getBreaks().size() > 0) {
			text += "\n      " + getBreaks().size() + " " + WorkSchedule.getMessage("breaks") + ":";
		}

		for (Break breakPeriod : getBreaks()) {
			text += "\n      " + breakPeriod.toString();
		}
		return text;
	}

}

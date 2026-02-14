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
import java.util.Objects;

/**
 * Class Shift is a scheduled working time period, and can include breaks.
 * 
 * @author Kent Randall
 *
 */
public class Shift extends TimePeriod implements Comparable<Shift> {
	// owning work schedule
	private WorkSchedule workSchedule;

	// breaks
	private List<Break> breaks = new ArrayList<>();

	/**
	 * Default constructor
	 */
	public Shift() {
		super();
	}

	Shift(String name, String description, LocalTime start, Duration duration) throws Exception {
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
	 * @param breakPeriod {@link Break}
	 */
	public void addBreak(Break breakPeriod) {
		if (!this.breaks.contains(breakPeriod)) {
			this.breaks.add(breakPeriod);
		}
	}

	/**
	 * Remove a break from this shift
	 * 
	 * @param breakPeriod {@link Break}
	 */
	public void removeBreak(Break breakPeriod) {
		if (this.breaks.contains(breakPeriod)) {
			this.breaks.remove(breakPeriod);
		}
	}

	/**
	 * Create a break for this shift
	 * 
	 * @param name        Name of break
	 * @param description Description of break
	 * @param startTime   Start of break
	 * @param duration    Duration of break
	 * @return {@link Break}
	 * @throws Exception exception
	 */
	public Break createBreak(String name, String description, LocalTime startTime, Duration duration) throws Exception {
		Break period = new Break(name, description, startTime, duration);
		addBreak(period);
		return period;
	}

	private int toRoundedSecond(LocalTime time) {
		int second = time.toSecondOfDay();

		if (time.getNano() > 500E+06) {
			second++;
		}

		return second;
	}

	/**
	 * Calculate the working time between the specified times of day. The shift must
	 * not span midnight.
	 * 
	 * @param from starting time
	 * @param to   Ending time
	 * @return Duration of working time
	 * @throws Exception exception
	 */
	public Duration calculateWorkingTime(LocalTime from, LocalTime to) throws Exception {

		if (spansMidnight()) {
			String msg = MessageFormat.format(WorkSchedule.getMessage("shift.spans.midnight"), getName(), from, to);
			throw new Exception(msg);
		}

		return this.calculateWorkingTime(from, to, true);
	}

	/**
	 * Check to see if this shift crosses midnight
	 * 
	 * @return True if the shift extends over midnight, otherwise false
	 * @throws Exception exception
	 */
	public boolean spansMidnight() throws Exception {
		int startSecond = toRoundedSecond(getStart());
		int endSecond = toRoundedSecond(getEnd());
		return endSecond <= startSecond;
	}

	/**
	 * Calculate the working time between the specified times of day
	 * 
	 * @param from           starting time
	 * @param to             Ending time
	 * @param beforeMidnight If true, and a shift spans midnight, calculate the time
	 *                       before midnight. Otherwise calculate the time after
	 *                       midnight.
	 * @return Duration of working time
	 * @throws Exception exception
	 */
	public Duration calculateWorkingTime(LocalTime from, LocalTime to, boolean beforeMidnight) throws Exception {
		Duration duration;

		int startSecond = toRoundedSecond(getStart());
		int endSecond = toRoundedSecond(getEnd());
		int fromSecond = toRoundedSecond(from);
		int toSecond = toRoundedSecond(to);

		int delta = toSecond - fromSecond;

		// check for 24 hour shift
		if (delta == 0 && fromSecond == startSecond && getDuration().toHours() == 24) {
			delta = 86400;
		}

		if (delta < 0) {
			delta = 86400 + toSecond - fromSecond;
		}

		if (spansMidnight()) {
			// adjust for shift crossing midnight
			if (fromSecond < startSecond && fromSecond < endSecond) {
				if (!beforeMidnight) {
					fromSecond = fromSecond + 86400;
				}
			}
			toSecond = fromSecond + delta;
			endSecond = endSecond + 86400;
		}

		// clip seconds on edge conditions
		if (fromSecond < startSecond) {
			fromSecond = startSecond;
		}

		if (toSecond < startSecond) {
			toSecond = startSecond;
		}

		if (fromSecond > endSecond) {
			fromSecond = endSecond;
		}

		if (toSecond > endSecond) {
			toSecond = endSecond;
		}

		duration = Duration.ofSeconds((long) toSecond - (long) fromSecond);

		return duration;
	}

	/**
	 * Test if the specified time falls within the shift
	 * 
	 * @param time {@link LocalTime}
	 * @return True if in the shift
	 * @throws Exception exception
	 */
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

	/**
	 * Calculate the total break time for the shift
	 * 
	 * @return Duration of all breaks
	 */
	public Duration calculateBreakTime() {
		Duration sum = Duration.ZERO;

		List<Break> breakList = getBreaks();

		for (Break b : breakList) {
			sum = sum.plus(b.getDuration());
		}

		return sum;
	}

	/**
	 * Get the work schedule that owns this shift
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
	 * Compare one shift to another one
	 */
	@Override
	public int compareTo(Shift other) {
		return this.getName().compareTo(other.getName());
	}

	/**
	 * Build a string representation of this shift
	 * 
	 * @return String
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(super.toString());

		if (!getBreaks().isEmpty()) {
			sb.append("\n      ").append(getBreaks().size()).append(" ").append(WorkSchedule.getMessage("breaks")).append(":");
		}

		for (Break breakPeriod : getBreaks()) {
			sb.append("\n      ").append(breakPeriod.toString());
		}
		return sb.toString();
	}

	@Override
	public boolean isWorkingPeriod() {
		return true;
	}

	/**
	 * Compare this Shift to another Shift
	 * 
	 * @return true if equal
	 */
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Shift)) {
			return false;
		}
		Shift otherShift = (Shift) other;

		// same work schedule
		if (getWorkSchedule() != null && otherShift.getWorkSchedule() != null) {
			if (!getWorkSchedule().equals(otherShift.getWorkSchedule())) {
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
}

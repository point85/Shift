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

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Class ShiftInstance is an instance of a {@link Shift}. A shift instance is
 * worked by a {@link Team}.
 * 
 * @author Kent Randall
 *
 */
public class ShiftInstance implements Comparable<ShiftInstance> {
	// definition of the shift
	private Shift shift;

	// team working it
	private Team team;

	// start date and time of day
	private LocalDateTime startDateTime;

	ShiftInstance(Shift shift, LocalDateTime startDateTime, Team team) {
		this.shift = shift;
		this.startDateTime = startDateTime;
		this.team = team;
	}

	/**
	 * Get the shift for this instance
	 * 
	 * @return {@link Shift}
	 */
	public Shift getShift() {
		return shift;
	}

	/**
	 * Get the starting date and time of day
	 * 
	 * @return LocalDateTime
	 */
	public LocalDateTime getStartTime() {
		return startDateTime;
	}

	/**
	 * Get the end date and time of day
	 * 
	 * @return LocalDateTime
	 */
	public LocalDateTime getEndTime() {
		return startDateTime.plus(shift.getDuration());
	}

	/**
	 * Get the team
	 * 
	 * @return {@link Team}
	 */
	public Team getTeam() {
		return team;
	}

	/**
	 * Determine if this time falls within the shift instance period
	 * 
	 * @param dateTime Date and time to check
	 * @return True if the specified time is in this shift instance
	 */
	public boolean isInShiftInstance(LocalDateTime dateTime) {
		return (dateTime.compareTo(startDateTime) >= 0 && dateTime.compareTo(getEndTime()) <= 0);
	}

	/**
	 * Compare this shift instance to another such instance by start time of day
	 * 
	 * @return -1 if less than, 0 if equal and 1 if greater than
	 */
	@Override
	public int compareTo(ShiftInstance other) {
		return getStartTime().compareTo(other.getStartTime());
	}

	/**
	 * Compare this shift instance to another shift instance
	 * 
	 * @return true if equal
	 */
	@Override
	public boolean equals(Object other) {

		if (!(other instanceof ShiftInstance)) {
			return false;
		}

		boolean teamMatch = getTeam().getName().equals(((ShiftInstance) other).getTeam().getName());
		boolean shiftMatch = getShift().getName().equals(((ShiftInstance) other).getShift().getName());
		boolean startMatch = getStartTime().equals(((ShiftInstance) other).getStartTime());

		return teamMatch && shiftMatch && startMatch;

	}

	/**
	 * Get the hash code
	 * 
	 * @return hash code
	 */
	@Override
	public int hashCode() {
		return Objects.hash(getShift(), getTeam(), getStartTime());
	}

	/**
	 * Build a string representation of a shift instance
	 */
	@Override
	public String toString() {
		String t = WorkSchedule.getMessage("team");
		String s = WorkSchedule.getMessage("shift");
		String ps = WorkSchedule.getMessage("period.start");
		String pe = WorkSchedule.getMessage("period.end");
		String members = WorkSchedule.getMessage("team.members");

		String text = " " + t + ": " + getTeam().getName() + " (" + team.getDescription() + ")" + ", " + s + ": " + getShift().getName() + ", " + ps + ": "
				+ getStartTime() + ", " + pe + ": " + getEndTime() + "\n" + members;

		for (TeamMember member : getTeam().getMembers(getStartTime())) {
			text += "\n\t" + member;
		}

		return text;
	}

}

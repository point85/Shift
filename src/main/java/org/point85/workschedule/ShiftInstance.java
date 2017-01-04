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

public class ShiftInstance implements Comparable<ShiftInstance> {
	// definition of the shift
	private Shift shift;

	// team working it
	private Team team;

	// start date and time of day
	private LocalDateTime startDateTime;

	public ShiftInstance(Shift shiftDefinition, LocalDateTime startDateTime, Team team) {
		this.setShiftDefinition(shiftDefinition);
		this.setStartTime(startDateTime);
		this.setTeam(team);
	}

	public Shift getShift() {
		return shift;
	}

	public void setShiftDefinition(Shift shiftDefinition) {
		this.shift = shiftDefinition;
	}

	public LocalDateTime getStartTime() {
		return startDateTime;
	}

	public void setStartTime(LocalDateTime startTime) {
		this.startDateTime = startTime;
	}

	public LocalDateTime getEndTime() {
		return startDateTime.plus(shift.getDuration());
	}

	@Override
	public String toString() {
		String text = " Team: " + getTeam().getName() + ", shift: " + getShift().getName() + ", start: "
				+ getStartTime() + ", end: " + getEndTime();
		return text;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	@Override
	public int compareTo(ShiftInstance other) {
		return getStartTime().compareTo(other.getStartTime());
	}
}

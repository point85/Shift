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

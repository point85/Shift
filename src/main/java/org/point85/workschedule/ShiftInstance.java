package org.point85.workschedule;

import java.time.LocalDateTime;

public class ShiftInstance {
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
		return shift.toString() + ", Starts on " + startDateTime.toString() + ", Ends on " + getEndTime();
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}
}

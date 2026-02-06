package org.point85.workschedule.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;

public class WorkScheduleDto {
	@NotBlank
	private String name;
	private String description;
	private List<ShiftDto> shifts = new ArrayList<>();
	private List<TeamDto> teams = new ArrayList<>();
	private List<RotationDto> rotations = new ArrayList<>();
	private List<NonWorkingPeriodDto> nonWorkingPeriods = new ArrayList<>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<ShiftDto> getShifts() {
		return shifts;
	}

	public void setShifts(List<ShiftDto> shifts) {
		this.shifts = shifts;
	}

	public List<TeamDto> getTeams() {
		return teams;
	}

	public void setTeams(List<TeamDto> teams) {
		this.teams = teams;
	}

	public List<RotationDto> getRotations() {
		return rotations;
	}

	public void setRotations(List<RotationDto> rotations) {
		this.rotations = rotations;
	}

	public List<NonWorkingPeriodDto> getNonWorkingPeriods() {
		return nonWorkingPeriods;
	}

	public void setNonWorkingPeriods(List<NonWorkingPeriodDto> nonWorkingPeriods) {
		this.nonWorkingPeriods = nonWorkingPeriods;
	}
}

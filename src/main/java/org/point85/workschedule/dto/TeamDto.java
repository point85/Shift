package org.point85.workschedule.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class TeamDto {
	@NotBlank
	private String name;
	private String description;
	@NotBlank
	private String rotationName;
	@NotNull
	private LocalDate rotationStart;
	private List<TeamMemberDto> members = new ArrayList<>();

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

	public String getRotationName() {
		return rotationName;
	}

	public void setRotationName(String rotationName) {
		this.rotationName = rotationName;
	}

	public LocalDate getRotationStart() {
		return rotationStart;
	}

	public void setRotationStart(LocalDate rotationStart) {
		this.rotationStart = rotationStart;
	}

	public List<TeamMemberDto> getMembers() {
		return members;
	}

	public void setMembers(List<TeamMemberDto> members) {
		this.members = members;
	}
}

package org.point85.workschedule.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;

public class RotationDto {
	@NotBlank
	private String name;
	private String description;
	private List<RotationSegmentDto> segments = new ArrayList<>();

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

	public List<RotationSegmentDto> getSegments() {
		return segments;
	}

	public void setSegments(List<RotationSegmentDto> segments) {
		this.segments = segments;
	}
}

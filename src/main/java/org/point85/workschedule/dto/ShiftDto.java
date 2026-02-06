package org.point85.workschedule.dto;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ShiftDto {
	@NotBlank
	private String name;
	private String description;
	@NotNull
	private LocalTime start;
	@NotBlank
	private String duration; // ISO-8601, e.g. "PT8H"
	private List<BreakDto> breaks = new ArrayList<>();

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

	public LocalTime getStart() {
		return start;
	}

	public void setStart(LocalTime start) {
		this.start = start;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public List<BreakDto> getBreaks() {
		return breaks;
	}

	public void setBreaks(List<BreakDto> breaks) {
		this.breaks = breaks;
	}
}

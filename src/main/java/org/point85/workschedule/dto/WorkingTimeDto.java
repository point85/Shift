package org.point85.workschedule.dto;

import java.time.LocalDateTime;

public class WorkingTimeDto {
	private LocalDateTime from;
	private LocalDateTime to;
	private String workingTime;    // ISO-8601 duration
	private String nonWorkingTime; // ISO-8601 duration

	public LocalDateTime getFrom() {
		return from;
	}

	public void setFrom(LocalDateTime from) {
		this.from = from;
	}

	public LocalDateTime getTo() {
		return to;
	}

	public void setTo(LocalDateTime to) {
		this.to = to;
	}

	public String getWorkingTime() {
		return workingTime;
	}

	public void setWorkingTime(String workingTime) {
		this.workingTime = workingTime;
	}

	public String getNonWorkingTime() {
		return nonWorkingTime;
	}

	public void setNonWorkingTime(String nonWorkingTime) {
		this.nonWorkingTime = nonWorkingTime;
	}
}

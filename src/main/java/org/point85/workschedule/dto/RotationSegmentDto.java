package org.point85.workschedule.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

public class RotationSegmentDto {
	@NotBlank
	private String shiftName;
	@Min(0)
	private int daysOn;
	@Min(0)
	private int daysOff;

	public String getShiftName() {
		return shiftName;
	}

	public void setShiftName(String shiftName) {
		this.shiftName = shiftName;
	}

	public int getDaysOn() {
		return daysOn;
	}

	public void setDaysOn(int daysOn) {
		this.daysOn = daysOn;
	}

	public int getDaysOff() {
		return daysOff;
	}

	public void setDaysOff(int daysOff) {
		this.daysOff = daysOff;
	}
}

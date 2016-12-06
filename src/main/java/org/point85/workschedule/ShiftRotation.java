package org.point85.workschedule;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ShiftRotation {
	// reference date for starting the rotations
	private LocalDate startDate;

	// list of shifts and off-shift periods
	private List<TimePeriod> rotation = new ArrayList<>();
	
	public ShiftRotation(LocalDate startDate) {
		this.startDate = startDate;
	}
	
	public LocalDate getRotationStart() {
		return startDate;
	}

	public void setRotationStart(LocalDate startDate) {
		this.startDate = startDate;
	}

	public List<TimePeriod> getRotation() {
		return rotation;
	}

	public void setRotation(List<TimePeriod> rotation) {
		this.rotation = rotation;
	}
	
	public void addOn(Shift shift, int count)
	{
		for (int i = 0; i < count;i++) {
			rotation.add(shift);
		}
	}
	
	public void addOff(OffShift offShift, int count)
	{
		for (int i = 0; i < count;i++) {
			rotation.add(offShift);
		}
	}
	
	public int getDays() {
		return rotation.size();
	}
	
	public TimePeriod getOnOff(int index) {
		return rotation.get(index);
	}
	
	@Override
	public String toString() {
		return "Start: " + startDate + " Rotation: " + rotation;
	}
}

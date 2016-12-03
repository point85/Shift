package org.point85.workschedule;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RotationDefinition {
	// reference date for starting the rotations
	private LocalDate rotationStart;

	// rotation day flags
	private List<Boolean> rotation = new ArrayList<>();
	
	public RotationDefinition(LocalDate rotationStart) {
		this.rotationStart = rotationStart;
	}
	
	public LocalDate getRotationStart() {
		return rotationStart;
	}

	public void setRotationStart(LocalDate rotationStart) {
		this.rotationStart = rotationStart;
	}

	public List<Boolean> getRotation() {
		return rotation;
	}

	public void setRotation(List<Boolean> rotation) {
		this.rotation = rotation;
	}
	
	public void addOn(int count)
	{
		for (int i = 0; i < count;i++) {
			rotation.add(new Boolean(true));
		}
	}
	
	public void addOff(int count)
	{
		for (int i = 0; i < count;i++) {
			rotation.add(new Boolean(false));
		}
	}
	
	public int days() {
		return rotation.size();
	}
	
	public Boolean getOnOff(int dayIndex) {
		return rotation.get(dayIndex);
	}
}

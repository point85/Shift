package org.point85.workschedule;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorkSchedule extends Nameable {
	// list of shift definitions
	private List<ShiftDefinition> shiftDefinitions = new ArrayList<>();

	// holidays
	private List<NonWorkingPeriod> nonWorkingPeriods = new ArrayList<>();

	WorkSchedule(String name, String description) {
		super(name, description);
	}

	public void addShiftDefinition(ShiftDefinition shiftDefinition) {
		if (!this.shiftDefinitions.contains(shiftDefinition)) {
			this.shiftDefinitions.add(shiftDefinition);
		}
	}

	public void removeShiftDefinition(ShiftDefinition shiftDefinition) {
		if (this.shiftDefinitions.contains(shiftDefinition)) {
			this.shiftDefinitions.remove(shiftDefinition);
		}
	}

	public void addNonWorkingPeriod(NonWorkingPeriod period) {
		if (!this.nonWorkingPeriods.contains(period)) {
			this.nonWorkingPeriods.add(period);
		}
	}

	public void removeNonWorkingPeriod(NonWorkingPeriod period) {
		if (this.nonWorkingPeriods.contains(period)) {
			this.nonWorkingPeriods.remove(period);
		}
	}

	public List<Shift> getShiftsForDay(LocalDate day) {
		List<Shift> workingShifts = new ArrayList<>();

		if (this.nonWorkingPeriods.contains(day)) {
			return workingShifts;
		}

		for (ShiftDefinition shiftDefinition : shiftDefinitions) {
			RotationDefinition rotationDefinition = shiftDefinition.getRotationDefinition();
			LocalDate rotationStart = rotationDefinition.getRotationStart();

			long dayFrom = rotationStart.getLong(ChronoField.EPOCH_DAY);
			long dayTo = day.getLong(ChronoField.EPOCH_DAY);
			long deltaDays = dayTo - dayFrom;

			if (deltaDays < 0) {
				throw new IllegalArgumentException(
						"Start of rotation " + rotationStart + " must be earlier than " + day);
			}

			long dayInRotation = deltaDays % rotationDefinition.days();

			Boolean onOff = rotationDefinition.getOnOff((int) dayInRotation);

			if (onOff) {
				// working day
				LocalDateTime startTime = LocalDateTime.of(day, shiftDefinition.getStart());
				Shift shift = new Shift(shiftDefinition, startTime);
				workingShifts.add(shift);
			}
		}

		return workingShifts;
	}

	@Override
	public String toString() {
		return "Schedule " + getName() + " (" + getDescription() + ").";
	}

	public Map<ShiftDefinition, Duration> calculateWorkingTimeByShift(LocalDate from, LocalDate to) {

		Map<ShiftDefinition, Duration> workingTime = new HashMap<ShiftDefinition, Duration>();

		LocalDate currentDate = from;

		// iterate over each day
		while (currentDate.isBefore(to)) {
			List<Shift> shifts = getShiftsForDay(currentDate);

			for (Shift shift : shifts) {
				Duration duration = shift.getShiftDefinition().calculateWorkingTime();

				Duration sum = workingTime.get(shift.getShiftDefinition());

				if (sum == null) {
					sum = duration;
				} else {
					sum = sum.plus(duration);
				}
				workingTime.put(shift.getShiftDefinition(), sum);
			}

			currentDate = currentDate.plusDays(1);
		}

		return workingTime;
	}

	public boolean isHoliday(LocalDate date) {
		return this.nonWorkingPeriods.contains(date);
	}

	public void showShifts(LocalDate from, LocalDate to) {

		LocalDate currentDate = from;

		// iterate over each day
		while (currentDate.isBefore(to)) {
			List<Shift> shifts = getShiftsForDay(currentDate);

			if (shifts.size() == 0) {
				String flag = isHoliday(currentDate) ? " (holiday)" : " (off)";

				System.out.println("   no shifts for " + currentDate.toString() + flag);
			} else {

				System.out.println(toString() + "  Shifts for " + currentDate.toString());

				for (Shift shift : shifts) {
					System.out.println("   " + shift.toString());
				}
			}

			currentDate = currentDate.plusDays(1);
		}
	}

	public ShiftDefinition createShiftDefinition(String name, String description, LocalTime start, Duration duration) {
		ShiftDefinition shiftDefinition = new ShiftDefinition(name, description, start, duration);
		this.addShiftDefinition(shiftDefinition);
		return shiftDefinition;
	}

	public static WorkSchedule instance(String name, String description) {
		return new WorkSchedule(name, description);
	}
	
	public NonWorkingPeriod createNonWorkingPeriod(String name, String description, LocalDateTime startDateTime, Duration duration) {
		NonWorkingPeriod period = new NonWorkingPeriod(name, description, startDateTime, duration);
		this.addNonWorkingPeriod(period);
		return period;
	}
}

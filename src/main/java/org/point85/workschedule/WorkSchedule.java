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

public class WorkSchedule extends Named {
	// list of treams
	private List<Team> teams = new ArrayList<>();

	// holidays and planned downtime
	private List<NonWorkingPeriod> nonWorkingPeriods = new ArrayList<>();

	WorkSchedule(String name, String description) {
		super(name, description);
	}

	public void addTeam(Team team) {
		if (!this.teams.contains(team)) {
			this.teams.add(team);
		}
	}

	public void removeTeam(Team team) {
		if (this.teams.contains(team)) {
			this.teams.remove(team);
		}
	}
	
	public List<Team> getTeams() {
		return this.teams;
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

	public List<ShiftInstance> getShiftInstancesForDay(LocalDate day) {
		List<ShiftInstance> workingShifts = new ArrayList<>();

		if (this.nonWorkingPeriods.contains(day)) {
			return workingShifts;
		}
		
		long dayTo = day.getLong(ChronoField.EPOCH_DAY);

		// for each team see if there is a working shift
		for (Team team : teams) {
			ShiftRotation rotation = team.getShiftRotation();
			LocalDate rotationStart = rotation.getRotationStart();

			// calculate total number of days from start of rotation
			long dayFrom = rotationStart.getLong(ChronoField.EPOCH_DAY);
			long deltaDays = dayTo - dayFrom;

			if (deltaDays < 0) {
				throw new IllegalArgumentException(
						"Start of rotation " + rotationStart + " must be earlier than " + day);
			}

			int dayInRotation = (int) (deltaDays % rotation.getDays());

			// shift or off shift
			TimePeriod period = rotation.getRotation().get(dayInRotation);

			if (period.isWorkingPeriod()) {
				LocalDateTime startDateTime = LocalDateTime.of(day, period.getStart());
				ShiftInstance instance = new ShiftInstance((Shift)period, startDateTime, team);
				workingShifts.add(instance);
			}
		}

		return workingShifts;
	}

	@Override
	public String toString() {
		return super.toString() + " Teams: " + teams + ", Non-working: " + nonWorkingPeriods;
	}

	/*
	public Map<Team, Duration> calculateTeamWorkingTime(LocalDate from, LocalDate to) {

		Map<Team, Duration> workingTime = new HashMap<Team, Duration>();

		LocalDate currentDate = from;

		// iterate over each day
		while (currentDate.isBefore(to)) {
			List<TeamInstance> shifts = getTeamsForDay(currentDate);

			for (TeamInstance shift : shifts) {
				Duration duration = shift.getTeam().calculateWorkingTime();

				Duration sum = workingTime.get(shift.getTeam());

				if (sum == null) {
					sum = duration;
				} else {
					sum = sum.plus(duration);
				}
				workingTime.put(shift.getTeam(), sum);
			}

			currentDate = currentDate.plusDays(1);
		}

		return workingTime;
	}
*/
	public boolean isHoliday(LocalDate date) {
		return this.nonWorkingPeriods.contains(date);
	}

	/*
	public void printTeams(LocalDate from, LocalDate to) {
		System.out.println(toString());
			
		LocalDate currentDate = from;

		// iterate over each day
		while (currentDate.isBefore(to)) {
			List<TeamInstance> shifts = getTeamsForDay(currentDate);

			if (shifts.size() == 0) {
				String flag = isHoliday(currentDate) ? " (holiday)" : " (off)";

				System.out.println("   no shifts for " + currentDate.toString() + flag);
			} else {

				System.out.println("   Teams for " + currentDate.toString());

				for (TeamInstance shift : shifts) {
					System.out.println("      " + shift.toString());
				}
			}

			currentDate = currentDate.plusDays(1);
		}
	}
*/
	public Team createTeam(String name, String description) {
		Team team = new Team(name, description);
		this.addTeam(team);
		return team;
	}
	
	public Shift createShift(String name, String description, LocalTime start, Duration duration) {
		return new Shift(name, description, start, duration);
	}
	
	public OffShift createOffShift(String name, String description, LocalTime start, Duration duration) {
		return new OffShift(name, description, start, duration);
	}

	public static WorkSchedule instance(String name, String description) {
		return new WorkSchedule(name, description);
	}
	
	public NonWorkingPeriod createNonWorkingPeriod(String name, String description, LocalDateTime startDateTime, Duration duration) {
		NonWorkingPeriod period = new NonWorkingPeriod(name, description, startDateTime, duration);
		this.addNonWorkingPeriod(period);
		return period;
	}
	
	public Duration getRotationDuration() throws Exception {
		Duration sum = null;
		for (Team team : teams) {
			if (sum == null) {
				sum = team.getRotationDuration();
			} else {
				sum = sum.plus(team.getRotationDuration());
			}
		}
		return sum;
	}
	
	public Duration getWorkingTime() {
		Duration sum = null;
		for (Team team : teams) {
			if (sum == null) {
				sum = team.getWorkingTime();
			} else {
				sum = sum.plus(team.getWorkingTime());
			}
		}
		return sum;
	}
}

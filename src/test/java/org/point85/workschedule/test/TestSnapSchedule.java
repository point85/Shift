package org.point85.workschedule.test;

import java.text.DecimalFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.junit.Test;
import org.point85.workschedule.OffShift;
import org.point85.workschedule.Shift;
import org.point85.workschedule.ShiftInstance;
import org.point85.workschedule.ShiftRotation;
import org.point85.workschedule.Team;
import org.point85.workschedule.WorkSchedule;

public class TestSnapSchedule {

	// reference date for start of shift rotations
	private LocalDate referenceDate = LocalDate.of(2016, 10, 31);

	private void printTeams(List<Team> teams) throws Exception {
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(2);

		for (Team team : teams) {
			System.out.println("Team: " + team.getName() + ", rotation duration: " + team.getRotationDuration()
					+ ", scheduled working time: " + team.getWorkingTime() + ", percentage worked: "
					+ df.format(team.getPercentageWorked()) + ", days in rotation: " + team.getShiftRotation().getDays()
					+ ", avg hours worked per week: " + team.getHoursWorkedPerWeek());
		}
	}

	private void printWorkSchedule(WorkSchedule workSchedule) throws Exception {
		System.out.println("Work schedule rotation duration: " + workSchedule.getRotationDuration()
				+ ", scheduled working time " + workSchedule.getWorkingTime());
	}

	private void printShiftInstances(WorkSchedule workSchedule, LocalDate start) throws Exception {
		LocalDate day = start;
		int days = workSchedule.getTeams().get(0).getShiftRotation().getDays();

		for (int i = 0; i < days; i++) {
			System.out.println("[" + (i + 1) + "] Shifts for day: " + day);

			List<ShiftInstance> instances = workSchedule.getShiftInstancesForDay(day);

			if (instances.size() == 0) {
				System.out.println("   No working shifts");
			} else {

				for (ShiftInstance instance : instances) {
					System.out.println("   Team: " + instance.getTeam().getName() + ", shift: "
							+ instance.getShift().getName() + ", start: " + instance.getShift().getStart() + ", end: "
							+ instance.getShift().getEnd());
				}
			}
			day = day.plusDays(1);
		}
	}

	@Test
	public void test9to5() throws Exception {
		String description = "This is the basic 9 to 5 schedule plan for office employees. Every employee works 8 hrs a day from Monday to Friday.";

		WorkSchedule workSchedule = WorkSchedule.instance("9 To 5 Plan", description);

		// Shift starts at 09:00 for 8 hours
		Shift dayShift = workSchedule.createShift("Day", "Day shift", LocalTime.of(9, 0, 0), Duration.ofHours(8));

		/// off shift
		OffShift dayOff = dayShift.createOffShift();

		// Team1 rotation (5 days)
		ShiftRotation rotation = new ShiftRotation();
		rotation.addOn(dayShift, 5);
		rotation.addOff(dayOff, 2);

		// 1 team, 1 shift
		Team team = workSchedule.createTeam("Team", "One team", rotation, referenceDate);

		printWorkSchedule(workSchedule);
		printTeams(workSchedule.getTeams());
		printShiftInstances(workSchedule, referenceDate);
	}

	@Test
	public void test8Plus12() throws Exception {
		String description = "This is a fast rotation plan that uses 4 teams and a combination of three 8-hr shifts on weekdays "
				+ "and two 12-hr shifts on weekends to provide 24/7 coverage.";

		// work schedule
		WorkSchedule workSchedule = WorkSchedule.instance("8 Plus 12 Plan", description);

		// Day shift #1, starts at 07:00 for 12 hours
		Shift dayShift1 = workSchedule.createShift("Day1", "Day shift #1", LocalTime.of(7, 0, 0), Duration.ofHours(12));

		// Day shift #2, starts at 07:00 for 8 hours
		Shift dayShift2 = workSchedule.createShift("Day2", "Day shift #2", LocalTime.of(7, 0, 0), Duration.ofHours(8));

		// Swing shift, starts at 15:00 for 8 hours
		Shift swingShift = workSchedule.createShift("Swing", "Swing shift", LocalTime.of(15, 0, 0),
				Duration.ofHours(8));

		// Night shift #1, starts at 19:00 for 12 hours
		Shift nightShift1 = workSchedule.createShift("Night1", "Night shift #1", LocalTime.of(19, 0, 0),
				Duration.ofHours(12));

		// Night shift #2, starts at 23:00 for 8 hours
		Shift nightShift2 = workSchedule.createShift("Night2", "Night shift #2", LocalTime.of(23, 0, 0),
				Duration.ofHours(8));

		/// off shifts
		OffShift day1Off = dayShift1.createOffShift();

		// shift rotation (28 days)
		ShiftRotation rotation = new ShiftRotation();
		rotation.addOn(dayShift2, 5);
		rotation.addOn(dayShift1, 2);
		rotation.addOff(day1Off, 3);
		rotation.addOn(nightShift2, 2);
		rotation.addOn(nightShift1, 2);
		rotation.addOn(nightShift2, 3);
		rotation.addOff(day1Off, 4);
		rotation.addOn(swingShift, 5);
		rotation.addOff(day1Off, 2);

		// 4 teams, rotating through 5 shifts
		Team team1 = workSchedule.createTeam("Team 1", "First team", rotation, referenceDate);
		Team team2 = workSchedule.createTeam("Team 2", "Second team", rotation, referenceDate.minusDays(7));
		Team team3 = workSchedule.createTeam("Team 3", "Third team", rotation, referenceDate.minusDays(14));
		Team team4 = workSchedule.createTeam("Team 4", "Fourth team", rotation, referenceDate.minusDays(21));

		printWorkSchedule(workSchedule);
		printTeams(workSchedule.getTeams());
		printShiftInstances(workSchedule, referenceDate);
	}

	@Test
	public void testNurseICU() throws Exception {
		String description = "This plan supports a combination of 14-hr day shift , 15.5-hr cross-cover shift , and a 14-hr night shift for medical interns. "
				+ "The day shift and the cross-cover shift have the same start time (7:00AM). "
				+ "The night shift starts at around 10:00PM and ends at 12:00PM on the next day.";

		WorkSchedule workSchedule = WorkSchedule.instance("ICU Interns Plan", description);

		// Day shift #1, starts at 07:00 for 15.5 hours
		Shift dayShiftCrossover = workSchedule.createShift("Crossover", "Day shift #1 cross-over",
				LocalTime.of(7, 0, 0), Duration.ofHours(15).plusMinutes(30));

		// Day shift #2, starts at 07:00 for 14 hours
		Shift dayShift = workSchedule.createShift("Day", "Day shift #2", LocalTime.of(7, 0, 0), Duration.ofHours(14));

		// Night shift, starts at 22:00 for 14 hours
		Shift nightShift = workSchedule.createShift("Night", "Night shift", LocalTime.of(22, 0, 0),
				Duration.ofHours(14));

		/// off shifts
		OffShift dayOff = dayShift.createOffShift();

		// Team1 rotation
		ShiftRotation rotation = new ShiftRotation();
		rotation.addOn(dayShift, 1);
		rotation.addOn(dayShiftCrossover, 1);
		rotation.addOn(nightShift, 1);
		rotation.addOff(dayOff, 1);

		Team team1 = workSchedule.createTeam("Team 1", "First team", rotation, referenceDate);
		Team team2 = workSchedule.createTeam("Team 2", "Second team", rotation, referenceDate.minusDays(3));
		Team team3 = workSchedule.createTeam("Team 3", "Third team", rotation, referenceDate.minusDays(2));
		Team team4 = workSchedule.createTeam("Team 4", "Forth team", rotation, referenceDate.minusDays(1));

		printWorkSchedule(workSchedule);
		printTeams(workSchedule.getTeams());
		printShiftInstances(workSchedule, referenceDate);
	}

	@Test
	public void testDupont() throws Exception {
		String description = "The DuPont 12-hour rotating shift schedule uses 4 teams (crews) and 2 twelve-hour shifts to provide 24/7 coverage. "
				+ "It consists of a 4-week cycle where each team works 4 consecutive night shifts, "
				+ "followed by 3 days off duty, works 3 consecutive day shifts, followed by 1 day off duty, works 3 consecutive night shifts, "
				+ "followed by 3 days off duty, work 4 consecutive day shift, then have 7 consecutive days off duty. "
				+ "Personnel works an average 42 hours per week.";

		WorkSchedule workSchedule = WorkSchedule.instance("DuPont Shift Schedule", description);

		// Day shift, starts at 07:00 for 12 hours
		Shift dayShift = workSchedule.createShift("Day", "Day shift", LocalTime.of(7, 0, 0), Duration.ofHours(12));

		// Night shift, starts at 19:00 for 12 hours
		Shift nightShift = workSchedule.createShift("Night", "Night shift", LocalTime.of(19, 0, 0),
				Duration.ofHours(12));

		/// off shifts
		OffShift dayOff = dayShift.createOffShift();
		OffShift nightOff = nightShift.createOffShift();

		// Team1 rotation
		ShiftRotation rotation = new ShiftRotation();
		rotation.addOn(nightShift, 4);
		rotation.addOff(nightOff, 3);
		rotation.addOn(dayShift, 3);
		rotation.addOff(dayOff, 1);
		rotation.addOn(nightShift, 3);
		rotation.addOff(nightOff, 3);
		rotation.addOn(dayShift, 4);
		rotation.addOff(dayOff, 7);

		Team team1 = workSchedule.createTeam("Team 1", "First team", rotation, referenceDate);
		Team team2 = workSchedule.createTeam("Team 2", "Second team", rotation, referenceDate.minusDays(7));
		Team team3 = workSchedule.createTeam("Team 3", "Third team", rotation, referenceDate.minusDays(14));
		Team team4 = workSchedule.createTeam("Team 4", "Forth team", rotation, referenceDate.minusDays(21));

		printWorkSchedule(workSchedule);
		printTeams(workSchedule.getTeams());
		printShiftInstances(workSchedule, referenceDate);
	}

	@Test
	public void testDNO() throws Exception {
		String description = "This is a fast rotation plan that uses 3 teams and two 12-hr shifts to provide 24/7 coverage. "
				+ "Each team rotates through the following sequence every three days: 1 day shift, 1 night shift, and 1 day off.";

		WorkSchedule workSchedule = WorkSchedule.instance("DNO Plan", description);

		// Day shift, starts at 07:00 for 12 hours
		Shift dayShift = workSchedule.createShift("Day", "Day shift", LocalTime.of(7, 0, 0), Duration.ofHours(12));

		// Night shift, starts at 19:00 for 12 hours
		Shift nightShift = workSchedule.createShift("Night", "Night shift", LocalTime.of(19, 0, 0),
				Duration.ofHours(12));

		/// off shift
		OffShift nightOff = nightShift.createOffShift();

		// rotation
		ShiftRotation rotation = new ShiftRotation();
		rotation.addOn(dayShift, 1);
		rotation.addOn(nightShift, 1);
		rotation.addOff(nightOff, 1);

		Team team1 = workSchedule.createTeam("Team 1", "First team", rotation, referenceDate);
		Team team2 = workSchedule.createTeam("Team 2", "Second team", rotation, referenceDate.minusDays(1));
		Team team3 = workSchedule.createTeam("Team 3", "Third team", rotation, referenceDate.minusDays(2));

		printWorkSchedule(workSchedule);
		printTeams(workSchedule.getTeams());
		printShiftInstances(workSchedule, referenceDate);
	}

	@Test
	public void testTwoTeam() throws Exception {
		String description = "This is a fixed (no rotation) plan that uses 2 teams and two 12-hr shifts to provide 24/7 coverage. "
				+ "One team will be permanently on the day shift and the other will be on the night shift.";

		WorkSchedule workSchedule = WorkSchedule.instance("2 Team Fixed 12 Plan", description);

		// Day shift, starts at 07:00 for 12 hours
		Shift dayShift = workSchedule.createShift("Day", "Day shift", LocalTime.of(7, 0, 0), Duration.ofHours(12));

		// Night shift, starts at 19:00 for 12 hours
		Shift nightShift = workSchedule.createShift("Night", "Night shift", LocalTime.of(19, 0, 0),
				Duration.ofHours(12));

		// Team1 rotation
		ShiftRotation team1Rotation = new ShiftRotation();
		// 1 day on (and repeat)
		team1Rotation.addOn(dayShift, 1);

		// Team1 rotation
		ShiftRotation team2Rotation = new ShiftRotation();
		// 1 night on (and repeat)
		team2Rotation.addOn(nightShift, 1);
		
		Team team1 = workSchedule.createTeam("Team 1", "First team", team1Rotation, referenceDate);
		Team team2 = workSchedule.createTeam("Team 2", "Second team", team2Rotation, referenceDate);

		printWorkSchedule(workSchedule);
		printTeams(workSchedule.getTeams());
		printShiftInstances(workSchedule, referenceDate);
	}

	@Test
	public void testPanama() throws Exception {
		String description = "This is a slow rotation plan that uses 4 teams and two 12-hr shifts to provide 24/7 coverage. "
				+ "The working and non-working days follow this pattern: 2 days on, 2 days off, 3 days on, 2 days off, 2 days on, 3 days off. "
				+ "Each team works the same shift (day or night) for 28 days then switches over to the other shift for the next 28 days. "
				+ "After 56 days, the same sequence starts over.";

		WorkSchedule workSchedule = WorkSchedule.instance("Panama", description);

		// Day shift, starts at 07:00 for 12 hours
		Shift dayShift = workSchedule.createShift("Day", "Day shift", LocalTime.of(7, 0, 0), Duration.ofHours(12));
		OffShift dayOff = dayShift.createOffShift();

		// Night shift, starts at 19:00 for 12 hours
		Shift nightShift = workSchedule.createShift("Night", "Night shift", LocalTime.of(19, 0, 0),
				Duration.ofHours(12));
		OffShift nightOff = nightShift.createOffShift();

		// Team1 rotation
		ShiftRotation rotation = new ShiftRotation();
		// 2 days on, 2 off, 3 on, 2 off, 2 on, 3 off (and repeat)
		rotation.addOn(dayShift, 2);
		rotation.addOff(dayOff, 2);
		rotation.addOn(dayShift, 3);
		rotation.addOff(dayOff, 2);
		rotation.addOn(dayShift, 2);
		rotation.addOff(dayOff, 3);
		rotation.addOn(dayShift, 2);
		rotation.addOff(dayOff, 2);
		rotation.addOn(dayShift, 3);
		rotation.addOff(dayOff, 2);
		rotation.addOn(dayShift, 2);
		rotation.addOff(dayOff, 3);
		// 2 nights on, 2 off, 3 on, 2 off, 2 on, 3 off (and repeat)
		rotation.addOn(nightShift, 2);
		rotation.addOff(nightOff, 2);
		rotation.addOn(nightShift, 3);
		rotation.addOff(nightOff, 2);
		rotation.addOn(nightShift, 2);
		rotation.addOff(nightOff, 3);
		rotation.addOn(nightShift, 2);
		rotation.addOff(nightOff, 2);
		rotation.addOn(nightShift, 3);
		rotation.addOff(nightOff, 2);
		rotation.addOn(nightShift, 2);
		rotation.addOff(nightOff, 3);
	
		Team team1 = workSchedule.createTeam("Team 1", "First team", rotation, referenceDate);
		Team team2 = workSchedule.createTeam("Team 2", "Second team", rotation, referenceDate.minusDays(28));
		Team team3 = workSchedule.createTeam("Team 3", "Third team", rotation, referenceDate.minusDays(7));
		Team team4 = workSchedule.createTeam("Team 4", "Fourth team", rotation, referenceDate.minusDays(35));

		printWorkSchedule(workSchedule);
		printTeams(workSchedule.getTeams());
		printShiftInstances(workSchedule, referenceDate);
	}

	public static void main(String[] args) {
		TestSnapSchedule test = new TestSnapSchedule();

		try {
			test.testPanama();
			 //test.testTwoTeam();
			//test.testDNO();
			// test.testDupont();
			// test.testNurseICU();
			// test.test8Plus12();
			// test.test9to5();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}

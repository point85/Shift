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
			System.out.println("[" + (i+1) + "] Shifts for day: " + day);

			List<ShiftInstance> instances = workSchedule.getShiftInstancesForDay(day);

			for (ShiftInstance instance : instances) {
				System.out.println(
						"   Shift: " + instance.getShift().getName() + ", start: " + instance.getShift().getStart()
								+ ", end: " + instance.getShift().getEnd() + ", team: " + instance.getTeam().getName());
			}
			day = day.plusDays(1);
		}
	}

	@Test
	public void test8Plus12() throws Exception {
		String description = "This is a fast rotation plan that uses 4 teams and a combination of three 8-hr shifts on weekdays "
				+ "and two 12-hr shifts on weekends to provide 24/7 coverage.";

		WorkSchedule workSchedule = WorkSchedule.instance("8 Plus 12 Plan", description);

		// 4 teams, rotating through 5 shifts
		Team team1 = workSchedule.createTeam("Team 1", "First team");
		Team team2 = workSchedule.createTeam("Team 2", "Second team");
		Team team3 = workSchedule.createTeam("Team 3", "Third team");
		Team team4 = workSchedule.createTeam("Team 4", "Forth team");

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

		// Team1 rotation (28 days)
		ShiftRotation team1Rotation = team1.createRotation(LocalDate.of(2016, 10, 31));
		team1Rotation.addOn(dayShift2, 5);
		team1Rotation.addOn(dayShift1, 2);
		team1Rotation.addOff(day1Off, 3);
		team1Rotation.addOn(nightShift2, 2);
		team1Rotation.addOn(nightShift1, 2);
		team1Rotation.addOn(nightShift2, 3);
		team1Rotation.addOff(day1Off, 4);
		team1Rotation.addOn(swingShift, 5);
		team1Rotation.addOff(day1Off, 2);
		
		// Team2 rotation (28 days)
		ShiftRotation team2Rotation = team2.createRotation(LocalDate.of(2016, 10, 31));
		team2Rotation.addOff(day1Off, 3);
		team2Rotation.addOn(nightShift2, 2);
		team2Rotation.addOn(nightShift1, 2);
		team2Rotation.addOn(nightShift2, 3);
		team2Rotation.addOff(day1Off, 4);
		team2Rotation.addOn(swingShift, 5);
		team2Rotation.addOff(day1Off, 2);
		team2Rotation.addOn(dayShift2, 5);
		team2Rotation.addOn(dayShift1, 2);
		
		// Team3 rotation (28 days)
		ShiftRotation team3Rotation = team3.createRotation(LocalDate.of(2016, 10, 31));
		team3Rotation.addOn(nightShift2, 3);
		team3Rotation.addOff(day1Off, 4);
		team3Rotation.addOn(swingShift, 5);
		team3Rotation.addOff(day1Off, 2);
		team3Rotation.addOn(dayShift2, 5);
		team3Rotation.addOn(dayShift1, 2);
		team3Rotation.addOff(day1Off, 3);
		team3Rotation.addOn(nightShift2, 2);
		team3Rotation.addOn(nightShift1, 2);
		
		// Team4 rotation (28 days)
		ShiftRotation team4Rotation = team4.createRotation(LocalDate.of(2016, 10, 31));
		team4Rotation.addOn(swingShift, 5);
		team4Rotation.addOff(day1Off, 2);
		team4Rotation.addOn(dayShift2, 5);
		team4Rotation.addOn(dayShift1, 2);
		team4Rotation.addOff(day1Off, 3);
		team4Rotation.addOn(nightShift2, 2);
		team4Rotation.addOn(nightShift1, 2);
		team4Rotation.addOn(nightShift2, 3);
		team4Rotation.addOff(day1Off, 4);

		printWorkSchedule(workSchedule);
		printTeams(workSchedule.getTeams());
		printShiftInstances(workSchedule, LocalDate.of(2016, 10, 31));
	}

	@Test
	public void testNurseICU() throws Exception {
		String description = "This plan supports a combination of 14-hr day shift , 15.5-hr cross-cover shift , and a 14-hr night shift for medical interns. "
				+ "The day shift and the cross-cover shift have the same start time (7:00AM). "
				+ "The night shift starts at around 10:00PM and ends at 12:00PM on the next day.";

		WorkSchedule workSchedule = WorkSchedule.instance("ICU Interns Plan", description);

		Team team1 = workSchedule.createTeam("Team 1", "First team");
		Team team2 = workSchedule.createTeam("Team 2", "Second team");
		Team team3 = workSchedule.createTeam("Team 3", "Third team");
		Team team4 = workSchedule.createTeam("Team 4", "Forth team");

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
		ShiftRotation team1Rotation = team1.createRotation(LocalDate.of(2016, 10, 31));
		team1Rotation.addOn(dayShift, 1);
		team1Rotation.addOn(dayShiftCrossover, 1);
		team1Rotation.addOn(nightShift, 1);
		team1Rotation.addOff(dayOff, 1);

		// Team2 rotation
		ShiftRotation team2Rotation = team2.createRotation(LocalDate.of(2016, 10, 31));
		team2Rotation.addOff(dayOff, 1);
		team2Rotation.addOn(dayShift, 1);
		team2Rotation.addOn(dayShiftCrossover, 1);
		team2Rotation.addOn(nightShift, 1);

		// Team3 rotation
		ShiftRotation team3Rotation = team3.createRotation(LocalDate.of(2016, 10, 31));
		team3Rotation.addOn(nightShift, 1);
		team3Rotation.addOff(dayOff, 1);
		team3Rotation.addOn(dayShift, 1);
		team3Rotation.addOn(dayShiftCrossover, 1);

		// Team4 rotation
		ShiftRotation team4Rotation = team4.createRotation(LocalDate.of(2016, 10, 31));
		team4Rotation.addOn(dayShiftCrossover, 1);
		team4Rotation.addOn(nightShift, 1);
		team4Rotation.addOff(dayOff, 1);
		team4Rotation.addOn(dayShift, 1);

		printWorkSchedule(workSchedule);
		printTeams(workSchedule.getTeams());
		printShiftInstances(workSchedule, LocalDate.of(2016, 10, 31));
	}

	@Test
	public void testDupont() throws Exception {
		String description = "The DuPont 12-hour rotating shift schedule uses 4 teams (crews) and 2 twelve-hour shifts to provide 24/7 coverage. "
				+ "It consists of a 4-week cycle where each team works 4 consecutive night shifts, "
				+ "followed by 3 days off duty, works 3 consecutive day shifts, followed by 1 day off duty, works 3 consecutive night shifts, "
				+ "followed by 3 days off duty, work 4 consecutive day shift, then have 7 consecutive days off duty. "
				+ "Personnel works an average 42 hours per week.";

		WorkSchedule workSchedule = WorkSchedule.instance("DuPont Shift Schedule", description);

		Team team1 = workSchedule.createTeam("Team 1", "First team");
		Team team2 = workSchedule.createTeam("Team 2", "Second team");
		Team team3 = workSchedule.createTeam("Team 3", "Third team");
		Team team4 = workSchedule.createTeam("Team 4", "Forth team");

		// Day shift, starts at 07:00 for 12 hours
		Shift dayShift = workSchedule.createShift("Day", "Day shift", LocalTime.of(7, 0, 0), Duration.ofHours(12));

		// Night shift, starts at 19:00 for 12 hours
		Shift nightShift = workSchedule.createShift("Night", "Night shift", LocalTime.of(19, 0, 0),
				Duration.ofHours(12));

		/// off shifts
		OffShift dayOff = dayShift.createOffShift();
		OffShift nightOff = nightShift.createOffShift();

		// Team1 rotation
		ShiftRotation team1Rotation = team1.createRotation(LocalDate.of(2016, 10, 31));
		team1Rotation.addOn(nightShift, 4);
		team1Rotation.addOff(nightOff, 3);
		team1Rotation.addOn(dayShift, 3);
		team1Rotation.addOff(dayOff, 1);
		team1Rotation.addOn(nightShift, 3);
		team1Rotation.addOff(nightOff, 3);
		team1Rotation.addOn(dayShift, 4);
		team1Rotation.addOff(dayOff, 7);

		// Team2 rotation
		ShiftRotation team2Rotation = team2.createRotation(LocalDate.of(2016, 10, 31));
		team2Rotation.addOn(dayShift, 3);
		team2Rotation.addOff(dayOff, 1);
		team2Rotation.addOn(nightShift, 3);
		team2Rotation.addOff(nightOff, 3);
		team2Rotation.addOn(dayShift, 4);
		team2Rotation.addOff(dayOff, 7);
		team2Rotation.addOn(nightShift, 4);
		team2Rotation.addOff(nightOff, 3);

		// Team3 rotation
		ShiftRotation team3Rotation = team3.createRotation(LocalDate.of(2016, 10, 31));
		team3Rotation.addOff(nightOff, 3);
		team3Rotation.addOn(dayShift, 4);
		team3Rotation.addOff(dayOff, 7);
		team3Rotation.addOn(nightShift, 4);
		team3Rotation.addOff(nightOff, 3);
		team3Rotation.addOn(dayShift, 3);
		team3Rotation.addOff(dayOff, 1);
		team3Rotation.addOn(nightShift, 3);

		// Team4 rotation
		ShiftRotation team4Rotation = team4.createRotation(LocalDate.of(2016, 10, 31));
		team4Rotation.addOff(dayOff, 7);
		team4Rotation.addOn(nightShift, 4);
		team4Rotation.addOff(nightOff, 3);
		team4Rotation.addOn(dayShift, 3);
		team4Rotation.addOff(dayOff, 1);
		team4Rotation.addOn(nightShift, 3);
		team4Rotation.addOff(nightOff, 3);
		team4Rotation.addOn(dayShift, 4);

		printWorkSchedule(workSchedule);
		printTeams(workSchedule.getTeams());
		printShiftInstances(workSchedule, LocalDate.of(2016, 10, 31));
	}

	@Test
	public void testDNO() throws Exception {
		String description = "This is a fast rotation plan that uses 3 teams and two 12-hr shifts to provide 24/7 coverage. "
				+ "Each team rotates through the following sequence every three days: 1 day shift, 1 night shift, and 1 day off.";

		WorkSchedule workSchedule = WorkSchedule.instance("DNO Plan", description);

		Team team1 = workSchedule.createTeam("Team 1", "First team");
		Team team2 = workSchedule.createTeam("Team 2", "Second team");
		Team team3 = workSchedule.createTeam("Team 3", "Third team");

		// Day shift, starts at 07:00 for 12 hours
		Shift dayShift = workSchedule.createShift("Day", "Day shift", LocalTime.of(7, 0, 0), Duration.ofHours(12));

		// Night shift, starts at 19:00 for 12 hours
		Shift nightShift = workSchedule.createShift("Night", "Night shift", LocalTime.of(19, 0, 0),
				Duration.ofHours(12));

		/// off shift
		OffShift nightOff = nightShift.createOffShift();

		// Team1 rotation
		ShiftRotation team1Rotation = team1.createRotation(LocalDate.of(2016, 10, 31));
		team1Rotation.addOn(dayShift, 1);
		team1Rotation.addOn(nightShift, 1);
		team1Rotation.addOff(nightOff, 1);

		// Team2 rotation
		ShiftRotation team2Rotation = team2.createRotation(LocalDate.of(2016, 10, 31));
		team2Rotation.addOn(nightShift, 1);
		team2Rotation.addOff(nightOff, 1);
		team2Rotation.addOn(dayShift, 1);

		// Team3 rotation
		ShiftRotation team3Rotation = team3.createRotation(LocalDate.of(2016, 10, 31));
		team3Rotation.addOff(nightOff, 1);
		team3Rotation.addOn(dayShift, 1);
		team3Rotation.addOn(nightShift, 1);

		printWorkSchedule(workSchedule);
		printTeams(workSchedule.getTeams());
		printShiftInstances(workSchedule, LocalDate.of(2016, 10, 31));
	}

	@Test
	public void testTwoTeam() throws Exception {
		String description = "This is a fixed (no rotation) plan that uses 2 teams and two 12-hr shifts to provide 24/7 coverage. "
				+ "One team will be permanently on the day shift and the other will be on the night shift.";

		WorkSchedule workSchedule = WorkSchedule.instance("2 Team Fixed 12 Plan", description);

		Team team1 = workSchedule.createTeam("Team 1", "First team");
		Team team2 = workSchedule.createTeam("Team 2", "Second team");

		// Day shift, starts at 07:00 for 12 hours
		Shift dayShift = workSchedule.createShift("Day", "Day shift", LocalTime.of(7, 0, 0), Duration.ofHours(12));

		// Night shift, starts at 19:00 for 12 hours
		Shift nightShift = workSchedule.createShift("Night", "Night shift", LocalTime.of(19, 0, 0),
				Duration.ofHours(12));

		// Team1 rotation
		ShiftRotation team1Rotation = team1.createRotation(LocalDate.of(2016, 10, 31));
		// 1 day on (and repeat)
		team1Rotation.addOn(dayShift, 1);

		// Team1 rotation
		ShiftRotation team2Rotation = team2.createRotation(LocalDate.of(2016, 10, 31));
		// 1 night on (and repeat)
		team2Rotation.addOn(nightShift, 1);

		printWorkSchedule(workSchedule);
		printTeams(workSchedule.getTeams());
		printShiftInstances(workSchedule, LocalDate.of(2016, 10, 31));
	}

	@Test
	public void testPanama() throws Exception {
		String description = "This is a slow rotation plan that uses 4 teams and two 12-hr shifts to provide 24/7 coverage. "
				+ "The working and non-working days follow this pattern: 2 days on, 2 days off, 3 days on, 2 days off, 2 days on, 3 days off. "
				+ "Each team works the same shift (day or night) for 28 days then switches over to the other shift for the next 28 days. "
				+ "After 56 days, the same sequence starts over.";

		WorkSchedule workSchedule = WorkSchedule.instance("Panama", description);

		Team team1 = workSchedule.createTeam("Team 1", "First team");
		Team team2 = workSchedule.createTeam("Team 2", "Second team");
		Team team3 = workSchedule.createTeam("Team 3", "Third team");
		Team team4 = workSchedule.createTeam("Team 4", "Fourth team");

		// Day shift, starts at 07:00 for 12 hours
		Shift dayShift = workSchedule.createShift("Day", "Day shift", LocalTime.of(7, 0, 0), Duration.ofHours(12));
		OffShift dayOff = dayShift.createOffShift();

		// Night shift, starts at 19:00 for 12 hours
		Shift nightShift = workSchedule.createShift("Night", "Night shift", LocalTime.of(19, 0, 0),
				Duration.ofHours(12));
		OffShift nightOff = nightShift.createOffShift();

		// Team1 rotation
		ShiftRotation team1Rotation = team1.createRotation(LocalDate.of(2016, 10, 31));
		// 2 days on, 2 off, 3 on, 2 off, 2 on, 3 off (and repeat)
		team1Rotation.addOn(dayShift, 2);
		team1Rotation.addOff(dayOff, 2);
		team1Rotation.addOn(dayShift, 3);
		team1Rotation.addOff(dayOff, 2);
		team1Rotation.addOn(dayShift, 2);
		team1Rotation.addOff(dayOff, 3);
		team1Rotation.addOn(dayShift, 2);
		team1Rotation.addOff(dayOff, 2);
		team1Rotation.addOn(dayShift, 3);
		team1Rotation.addOff(dayOff, 2);
		team1Rotation.addOn(dayShift, 2);
		team1Rotation.addOff(dayOff, 3);
		// 2 nights on, 2 off, 3 on, 2 off, 2 on, 3 off (and repeat)
		team1Rotation.addOn(nightShift, 2);
		team1Rotation.addOff(nightOff, 2);
		team1Rotation.addOn(nightShift, 3);
		team1Rotation.addOff(nightOff, 2);
		team1Rotation.addOn(nightShift, 2);
		team1Rotation.addOff(nightOff, 3);
		team1Rotation.addOn(nightShift, 2);
		team1Rotation.addOff(nightOff, 2);
		team1Rotation.addOn(nightShift, 3);
		team1Rotation.addOff(nightOff, 2);
		team1Rotation.addOn(nightShift, 2);
		team1Rotation.addOff(nightOff, 3);

		System.out.println("Team1, rotation duration: " + team1.getRotationDuration() + ", scheduled working time: "
				+ team1.getWorkingTime() + ", percentage worked: " + team1.getPercentageWorked()
				+ ", days in rotation: " + team1.getShiftRotation().getDays() + ", avg hours worked per week: "
				+ team1.getHoursWorkedPerWeek());

		// Team2 rotation
		ShiftRotation team2Rotation = team2.createRotation(LocalDate.of(2016, 10, 31));
		// 2 nights on, 2 off, 3 on, 2 off, 2 on, 3 off (and repeat)
		team2Rotation.addOn(nightShift, 2);
		team2Rotation.addOff(nightOff, 2);
		team2Rotation.addOn(nightShift, 3);
		team2Rotation.addOff(nightOff, 2);
		team2Rotation.addOn(nightShift, 2);
		team2Rotation.addOff(nightOff, 3);
		team2Rotation.addOn(nightShift, 2);
		team2Rotation.addOff(nightOff, 2);
		team2Rotation.addOn(nightShift, 3);
		team2Rotation.addOff(nightOff, 2);
		team2Rotation.addOn(nightShift, 2);
		team2Rotation.addOff(nightOff, 3);
		// 2 days on, 2 off, 3 on, 2 off, 2 on, 3 off (and repeat)
		team2Rotation.addOn(dayShift, 2);
		team2Rotation.addOff(dayOff, 2);
		team2Rotation.addOn(dayShift, 3);
		team2Rotation.addOff(dayOff, 2);
		team2Rotation.addOn(dayShift, 2);
		team2Rotation.addOff(dayOff, 3);
		team2Rotation.addOn(dayShift, 2);
		team2Rotation.addOff(dayOff, 2);
		team2Rotation.addOn(dayShift, 3);
		team2Rotation.addOff(dayOff, 2);
		team2Rotation.addOn(dayShift, 2);
		team2Rotation.addOff(dayOff, 3);

		System.out.println("Team2 rotation duration: " + team2.getRotationDuration() + ", scheduled working time "
				+ team2.getWorkingTime());

		// Team3 rotation, same as Team1 with a 7-day offset
		ShiftRotation team3Rotation = team3.createRotation(LocalDate.of(2016, 10, 31));
		// 2 days on, 2 off, 3 on, 2 off, 2 on, 3 off (and repeat)
		team3Rotation.addOff(dayOff, 2);
		team3Rotation.addOn(dayShift, 2);
		team3Rotation.addOff(dayOff, 3);
		team3Rotation.addOn(dayShift, 2);
		team3Rotation.addOff(dayOff, 2);
		team3Rotation.addOn(dayShift, 3);
		team3Rotation.addOff(dayOff, 2);
		team3Rotation.addOn(dayShift, 2);
		team3Rotation.addOff(dayOff, 3);
		team3Rotation.addOn(dayShift, 2);
		team3Rotation.addOff(dayOff, 2);
		team3Rotation.addOn(dayShift, 3);
		// 2 nights on, 2 off, 3 on, 2 off, 2 on, 3 off (and repeat)
		team3Rotation.addOff(nightOff, 2);
		team3Rotation.addOn(nightShift, 2);
		team3Rotation.addOff(nightOff, 3);
		team3Rotation.addOn(nightShift, 2);
		team3Rotation.addOff(nightOff, 2);
		team3Rotation.addOn(nightShift, 3);
		team3Rotation.addOff(nightOff, 2);
		team3Rotation.addOn(nightShift, 2);
		team3Rotation.addOff(nightOff, 3);
		team3Rotation.addOn(nightShift, 2);
		team3Rotation.addOff(nightOff, 2);
		team3Rotation.addOn(nightShift, 3);

		System.out.println("Team3 rotation duration: " + team3.getRotationDuration() + ", scheduled working time "
				+ team3.getWorkingTime());

		// Team4 rotation, same as Team2 with a 7-day offset
		ShiftRotation team4Rotation = team4.createRotation(LocalDate.of(2016, 10, 31));
		// 2 nights on, 2 off, 3 on, 2 off, 2 on, 3 off (and repeat)
		team4Rotation.addOff(nightOff, 2);
		team4Rotation.addOn(nightShift, 2);
		team4Rotation.addOff(nightOff, 3);
		team4Rotation.addOn(nightShift, 2);
		team4Rotation.addOff(nightOff, 2);
		team4Rotation.addOn(nightShift, 3);
		team4Rotation.addOff(nightOff, 2);
		team4Rotation.addOn(nightShift, 2);
		team4Rotation.addOff(nightOff, 3);
		team4Rotation.addOn(nightShift, 2);
		team4Rotation.addOff(nightOff, 2);
		team4Rotation.addOn(nightShift, 3);
		// 2 days on, 2 off, 3 on, 2 off, 2 on, 3 off (and repeat)
		team4Rotation.addOff(dayOff, 2);
		team4Rotation.addOn(dayShift, 2);
		team4Rotation.addOff(dayOff, 3);
		team4Rotation.addOn(dayShift, 2);
		team4Rotation.addOff(dayOff, 2);
		team4Rotation.addOn(dayShift, 3);
		team4Rotation.addOff(dayOff, 2);
		team4Rotation.addOn(dayShift, 2);
		team4Rotation.addOff(dayOff, 3);
		team4Rotation.addOn(dayShift, 2);
		team4Rotation.addOff(dayOff, 2);
		team4Rotation.addOn(dayShift, 3);

		printWorkSchedule(workSchedule);
		printTeams(workSchedule.getTeams());
		printShiftInstances(workSchedule, LocalDate.of(2016, 10, 31));
	}

	public static void main(String[] args) {
		TestSnapSchedule test = new TestSnapSchedule();

		try {
			// test.testPanama();
			// test.testTwoTeam();
			// test.testDNO();
			// test.testDupont();
			//test.testNurseICU();
			test.test8Plus12();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}

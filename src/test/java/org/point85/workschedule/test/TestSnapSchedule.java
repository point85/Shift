package org.point85.workschedule.test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.Test;
import org.point85.workschedule.OffShift;
import org.point85.workschedule.Shift;
import org.point85.workschedule.ShiftRotation;
import org.point85.workschedule.Team;
import org.point85.workschedule.WorkSchedule;

public class TestSnapSchedule extends BaseTest {

	// reference date for start of shift rotations
	private LocalDate referenceDate = LocalDate.of(2016, 10, 31);

	@Test
	public void test9to5() throws Exception {
		String description = "This is the basic 9 to 5 schedule plan for office employees. Every employee works 8 hrs a day from Monday to Friday.";

		WorkSchedule workSchedule = new WorkSchedule("9 To 5 Plan", description);

		// Shift starts at 09:00 for 8 hours
		Shift dayShift = workSchedule.createShift("Day", "Day shift", LocalTime.of(9, 0, 0), Duration.ofHours(8));

		/// off shift
		OffShift dayOff = dayShift.createOffShift();

		// Team1 rotation (5 days)
		ShiftRotation rotation = new ShiftRotation();
		rotation.on(dayShift, 5);
		rotation.off(dayOff, 2);

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
		WorkSchedule workSchedule = new WorkSchedule("8 Plus 12 Plan", description);

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
		rotation.on(dayShift2, 5);
		rotation.on(dayShift1, 2);
		rotation.off(day1Off, 3);
		rotation.on(nightShift2, 2);
		rotation.on(nightShift1, 2);
		rotation.on(nightShift2, 3);
		rotation.off(day1Off, 4);
		rotation.on(swingShift, 5);
		rotation.off(day1Off, 2);

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

		WorkSchedule workSchedule = new WorkSchedule("ICU Interns Plan", description);

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
		rotation.on(dayShift, 1);
		rotation.on(dayShiftCrossover, 1);
		rotation.on(nightShift, 1);
		rotation.off(dayOff, 1);

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

		WorkSchedule workSchedule = new WorkSchedule("DuPont Shift Schedule", description);

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
		rotation.on(nightShift, 4);
		rotation.off(nightOff, 3);
		rotation.on(dayShift, 3);
		rotation.off(dayOff, 1);
		rotation.on(nightShift, 3);
		rotation.off(nightOff, 3);
		rotation.on(dayShift, 4);
		rotation.off(dayOff, 7);

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

		WorkSchedule workSchedule = new WorkSchedule("DNO Plan", description);

		// Day shift, starts at 07:00 for 12 hours
		Shift dayShift = workSchedule.createShift("Day", "Day shift", LocalTime.of(7, 0, 0), Duration.ofHours(12));

		// Night shift, starts at 19:00 for 12 hours
		Shift nightShift = workSchedule.createShift("Night", "Night shift", LocalTime.of(19, 0, 0),
				Duration.ofHours(12));

		// off shift
		OffShift nightOff = nightShift.createOffShift();

		// rotation
		ShiftRotation rotation = new ShiftRotation();
		rotation.on(dayShift, 1);
		rotation.on(nightShift, 1);
		rotation.off(nightOff, 1);

		Team team1 = workSchedule.createTeam("Team 1", "First team", rotation, referenceDate);
		Team team2 = workSchedule.createTeam("Team 2", "Second team", rotation, referenceDate.minusDays(1));
		Team team3 = workSchedule.createTeam("Team 3", "Third team", rotation, referenceDate.minusDays(2));

		printWorkSchedule(workSchedule);
		printTeams(workSchedule.getTeams());
		printShiftInstances(workSchedule, referenceDate);
	}

	@Test
	public void test21TeamFixed() throws Exception {
		String description = "This plan is a fixed (no rotation) plan that uses 21 teams and three 8-hr shifts to provide 24/7 coverage. " +
				"It maximizes the number of consecutive days off while still averaging 40 hours per week. " +
				"Over a 7 week cycle, each employee has two 3 consecutive days off and is required to work 6 consecutive days on 5 of the 7 weeks. " +
				"On any given day, 15 teams will be scheduled to work and 6 teams will be off. " +
				"Each shift will be staffed by 5 teams so the minimum number of employees per shift is five. ";

		WorkSchedule workSchedule = new WorkSchedule("21 Team Fixed 8 6D Plan", description);

		// Day shift, starts at 07:00 for 8 hours
		Shift day = workSchedule.createShift("Day", "Day shift", LocalTime.of(7, 0, 0), Duration.ofHours(8));
		OffShift dayOff = day.createOffShift();
		
		// Swing shift, starts at 15:00 for 8 hours
		Shift swing = workSchedule.createShift("Swing", "Swing shift", LocalTime.of(15, 0, 0), Duration.ofHours(8));
		OffShift swingOff = swing.createOffShift();

		// Night shift, starts at 15:00 for 8 hours
		Shift night = workSchedule.createShift("Night", "Night shift", LocalTime.of(23, 0, 0), Duration.ofHours(8));
		OffShift nightOff = night.createOffShift();
		
		// day rotation
		ShiftRotation dayRotation = new ShiftRotation();
		dayRotation.on(day, 6).off(dayOff, 3).on(day, 5).off(dayOff, 3).on(day, 6).off(dayOff, 2).on(day, 6).off(dayOff, 2).on(day, 6).off(dayOff, 2).on(day, 6).off(dayOff, 2);
		
		// swing rotation
		ShiftRotation swingRotation = new ShiftRotation();
		swingRotation.on(swing, 6).off(swingOff, 3).on(swing, 5).off(swingOff, 3).on(swing, 6).off(swingOff, 2).on(swing, 6).off(swingOff, 2).on(swing, 6).off(swingOff, 2).on(swing, 6).off(swingOff, 2);
		
		// night rotation
		ShiftRotation nightRotation = new ShiftRotation();
		nightRotation.on(night, 6).off(nightOff, 3).on(night, 5).off(nightOff, 3).on(night, 6).off(nightOff, 2).on(night, 6).off(nightOff, 2).on(night, 6).off(nightOff, 2).on(night, 6).off(nightOff, 2);
		
		// day teams
		Team team1 = workSchedule.createTeam("Team 1", "1st day team", dayRotation, referenceDate);
		Team team2 = workSchedule.createTeam("Team 2", "2nd day team", dayRotation, referenceDate.plusDays(7));
		Team team3 = workSchedule.createTeam("Team 3", "3rd day team", dayRotation, referenceDate.plusDays(14));
		Team team4 = workSchedule.createTeam("Team 4", "4th day team", dayRotation, referenceDate.plusDays(21));
		Team team5 = workSchedule.createTeam("Team 5", "5th day team", dayRotation, referenceDate.plusDays(28));
		Team team6 = workSchedule.createTeam("Team 6", "6th day team", dayRotation, referenceDate.plusDays(35));
		Team team7 = workSchedule.createTeam("Team 7", "7th day team", dayRotation, referenceDate.plusDays(42));
		
		// swing teams
		Team team8 = workSchedule.createTeam("Team 8", "1st swing team", dayRotation, referenceDate);
		Team team9 = workSchedule.createTeam("Team 9", "2nd swing team", dayRotation, referenceDate.plusDays(7));
		Team team10 = workSchedule.createTeam("Team 10", "3rd swing team", dayRotation, referenceDate.plusDays(14));
		Team team11 = workSchedule.createTeam("Team 11", "4th swing team", dayRotation, referenceDate.plusDays(21));
		Team team12 = workSchedule.createTeam("Team 12", "5th swing team", dayRotation, referenceDate.plusDays(28));
		Team team13 = workSchedule.createTeam("Team 13", "6th swing team", dayRotation, referenceDate.plusDays(35));
		Team team14 = workSchedule.createTeam("Team 14", "7th swing team", dayRotation, referenceDate.plusDays(42));
		
		// night teams
		Team team15 = workSchedule.createTeam("Team 15", "1st night team", dayRotation, referenceDate);
		Team team16 = workSchedule.createTeam("Team 16", "2nd night team", dayRotation, referenceDate.plusDays(7));
		Team team17 = workSchedule.createTeam("Team 17", "3rd night team", dayRotation, referenceDate.plusDays(14));
		Team team18 = workSchedule.createTeam("Team 18", "4th night team", dayRotation, referenceDate.plusDays(21));
		Team team19 = workSchedule.createTeam("Team 19", "5th night team", dayRotation, referenceDate.plusDays(28));
		Team team20 = workSchedule.createTeam("Team 20", "6th night team", dayRotation, referenceDate.plusDays(35));
		Team team21 = workSchedule.createTeam("Team 21", "7th night team", dayRotation, referenceDate.plusDays(42));
		


		printWorkSchedule(workSchedule);
		printTeams(workSchedule.getTeams());
		printShiftInstances(workSchedule, referenceDate.plusDays(42));
		
	}

	@Test
	public void testTwoTeam() throws Exception {
		String description = "This is a fixed (no rotation) plan that uses 2 teams and two 12-hr shifts to provide 24/7 coverage. "
				+ "One team will be permanently on the day shift and the other will be on the night shift.";

		WorkSchedule workSchedule = new WorkSchedule("2 Team Fixed 12 Plan", description);

		// Day shift, starts at 07:00 for 12 hours
		Shift dayShift = workSchedule.createShift("Day", "Day shift", LocalTime.of(7, 0, 0), Duration.ofHours(12));

		// Night shift, starts at 19:00 for 12 hours
		Shift nightShift = workSchedule.createShift("Night", "Night shift", LocalTime.of(19, 0, 0),
				Duration.ofHours(12));

		// Team1 rotation
		ShiftRotation team1Rotation = new ShiftRotation();
		// 1 day on (and repeat)
		team1Rotation.on(dayShift, 1);

		// Team1 rotation
		ShiftRotation team2Rotation = new ShiftRotation();
		// 1 night on (and repeat)
		team2Rotation.on(nightShift, 1);

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

		WorkSchedule workSchedule = new WorkSchedule("Panama", description);

		// Day shift, starts at 07:00 for 12 hours
		Shift dayShift = workSchedule.createShift("Day", "Day shift", LocalTime.of(7, 0, 0), Duration.ofHours(12));
		OffShift dayOff = dayShift.createOffShift();

		// Night shift, starts at 19:00 for 12 hours
		Shift nightShift = workSchedule.createShift("Night", "Night shift", LocalTime.of(19, 0, 0),
				Duration.ofHours(12));
		OffShift nightOff = nightShift.createOffShift();

		// rotation
		ShiftRotation rotation = new ShiftRotation();
		// 2 days on, 2 off, 3 on, 2 off, 2 on, 3 off (and repeat)
		rotation.on(dayShift, 2);
		rotation.off(dayOff, 2);
		rotation.on(dayShift, 3);
		rotation.off(dayOff, 2);
		rotation.on(dayShift, 2);
		rotation.off(dayOff, 3);
		rotation.on(dayShift, 2);
		rotation.off(dayOff, 2);
		rotation.on(dayShift, 3);
		rotation.off(dayOff, 2);
		rotation.on(dayShift, 2);
		rotation.off(dayOff, 3);
		// 2 nights on, 2 off, 3 on, 2 off, 2 on, 3 off (and repeat)
		rotation.on(nightShift, 2);
		rotation.off(nightOff, 2);
		rotation.on(nightShift, 3);
		rotation.off(nightOff, 2);
		rotation.on(nightShift, 2);
		rotation.off(nightOff, 3);
		rotation.on(nightShift, 2);
		rotation.off(nightOff, 2);
		rotation.on(nightShift, 3);
		rotation.off(nightOff, 2);
		rotation.on(nightShift, 2);
		rotation.off(nightOff, 3);

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
			//test.testPanama();
			// test.testTwoTeam();
			// test.testDNO();
			// test.testDupont();
			// test.testNurseICU();
			// test.test8Plus12();
			// test.test9to5();
			test.test21TeamFixed();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}

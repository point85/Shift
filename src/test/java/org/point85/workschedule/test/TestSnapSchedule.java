package org.point85.workschedule.test;

import static org.hamcrest.number.BigDecimalCloseTo.closeTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.Test;
import org.point85.workschedule.Shift;
import org.point85.workschedule.ShiftRotation;
import org.point85.workschedule.Team;
import org.point85.workschedule.WorkSchedule;

public class TestSnapSchedule extends BaseTest {

	// reference date for start of shift rotations
	private LocalDate referenceDate = LocalDate.of(2016, 10, 31);
	
	@Test
	public void testLowNight() throws Exception {
		String description = "Low night demand";

		WorkSchedule schedule = new WorkSchedule("Low Night Demand Plan", description);

		// 3 shifts
		Shift day = schedule.createShift("Day", "Day shift", LocalTime.of(7, 0, 0), Duration.ofHours(8));
		Shift swing = schedule.createShift("Swing", "Swing shift", LocalTime.of(15, 0, 0), Duration.ofHours(8));
		Shift night = schedule.createShift("Night", "Night shift", LocalTime.of(23, 0, 0), Duration.ofHours(8));

		// Team rotation
		ShiftRotation rotation = new ShiftRotation();
		rotation.on(3, day).on(4, swing).off(3, day).on(4, day).on(3, swing).off(4, day).on(3, day).on(4, night).off(3, day).on(4, day).on(3, night).off(4, day);

		// 6 teams
		Team team1 = schedule.createTeam("Team1", "First team", rotation, referenceDate);
		Team team2 = schedule.createTeam("Team2", "Second team", rotation, referenceDate.minusDays(21));
		Team team3 = schedule.createTeam("Team3", "Third team", rotation, referenceDate.minusDays(7));
		Team team4 = schedule.createTeam("Team4", "Fourth team", rotation, referenceDate.minusDays(28));
		Team team5 = schedule.createTeam("Team5", "Fifth team", rotation, referenceDate.minusDays(14));
		Team team6 = schedule.createTeam("Team6", "Sixth team", rotation, referenceDate.minusDays(35));
		
		System.out.println(schedule.toString());
		schedule.printShiftInstances(referenceDate, referenceDate.plusDays(rotation.getDays()));
		
		BigDecimal hrs = new BigDecimal(team1.getHoursWorkedPerWeek());
		assertThat(hrs, closeTo(new BigDecimal("37.333", MATH_CONTEXT), DELTA3));
		assertTrue(team1.getRotationDuration().toDays() == 42);
	}
	
	@Test
	public void test3TeamFixed24() throws Exception {
		String description = "Fire departments";

		WorkSchedule schedule = new WorkSchedule("3 Team Fixed 24 Plan", description);

		// Shift starts at 00:00 for 24 hours
		Shift shift = schedule.createShift("24 Hour", "24 hour shift", LocalTime.of(0, 0, 0), Duration.ofHours(24));

		// Team rotation
		ShiftRotation rotation = new ShiftRotation();
		rotation.on(1, shift).off(1, shift).on(1, shift).off(1, shift).on(1, shift).off(4, shift);

		// 3 teams
		Team team1 = schedule.createTeam("Team1", "First team", rotation, referenceDate);
		Team team2 = schedule.createTeam("Team2", "Second team", rotation, referenceDate.minusDays(3));
		Team team3 = schedule.createTeam("Team3", "Third team", rotation, referenceDate.minusDays(6));
		
		System.out.println(schedule.toString());
		schedule.printShiftInstances(referenceDate, referenceDate.plusDays(rotation.getDays()));
		
		assertTrue(team1.getHoursWorkedPerWeek() == 56.0f);
		assertTrue(team2.getHoursWorkedPerWeek() == 56.0f);
		assertTrue(team3.getHoursWorkedPerWeek() == 56.0f);
		
		assertTrue(team1.getRotationDuration().toDays() == 9);
		assertTrue(team2.getRotationDuration().toDays() == 9);
		assertTrue(team3.getRotationDuration().toDays() == 9);
	}
	
	@Test
	public void test549() throws Exception {
		String description = "Compressed work schedule.";

		WorkSchedule schedule = new WorkSchedule("5/4/9 Plan", description);

		// Shift 1 starts at 07:00 for 9 hours
		Shift day1 = schedule.createShift("Day1", "Day shift #1", LocalTime.of(7, 0, 0), Duration.ofHours(9));
		
		// Shift 2 starts at 07:00 for 8 hours
		Shift day2 = schedule.createShift("Day2", "Day shift #2", LocalTime.of(7, 0, 0), Duration.ofHours(8));

		// Team rotation (28 days)
		ShiftRotation rotation = new ShiftRotation();
		rotation.on(4, day1).on(1, day2).off(3, day1).on(4, day1).off(3, day1).on(4, day1).off(2, day2).on(4, day1).on(1, day2).off(2, day1);

		// 2 teams
		Team team1 = schedule.createTeam("Team1", "First team", rotation, referenceDate);
		Team team2 = schedule.createTeam("Team2", "Second team", rotation, referenceDate.minusDays(14));
		
		System.out.println(schedule.toString());
		schedule.printShiftInstances(referenceDate, referenceDate.plusDays(rotation.getDays()));
		
		assertTrue(team1.getHoursWorkedPerWeek() == 40.0f);
		assertTrue(team2.getHoursWorkedPerWeek() == 40.0f);
		
		assertTrue(team1.getRotationDuration().toDays() == 28);
		assertTrue(team2.getRotationDuration().toDays() == 28);
	}

	@Test
	public void test9to5() throws Exception {
		String description = "This is the basic 9 to 5 schedule plan for office employees. Every employee works 8 hrs a day from Monday to Friday.";

		WorkSchedule schedule = new WorkSchedule("9 To 5 Plan", description);

		// Shift starts at 09:00 for 8 hours
		Shift day = schedule.createShift("Day", "Day shift", LocalTime.of(9, 0, 0), Duration.ofHours(8));

		// Team1 rotation (5 days)
		ShiftRotation rotation = new ShiftRotation();
		rotation.on(5, day).off(2, day);

		// 1 team, 1 shift
		Team team = schedule.createTeam("Team", "One team", rotation, referenceDate);

		System.out.println(schedule.toString());
		schedule.printShiftInstances(referenceDate, referenceDate.plusDays(rotation.getDays()));
	}

	@Test
	public void test8Plus12() throws Exception {
		String description = "This is a fast rotation plan that uses 4 teams and a combination of three 8-hr shifts on weekdays "
				+ "and two 12-hr shifts on weekends to provide 24/7 coverage.";

		// work schedule
		WorkSchedule schedule = new WorkSchedule("8 Plus 12 Plan", description);

		// Day shift #1, starts at 07:00 for 12 hours
		Shift day1 = schedule.createShift("Day1", "Day shift #1", LocalTime.of(7, 0, 0), Duration.ofHours(12));

		// Day shift #2, starts at 07:00 for 8 hours
		Shift day2 = schedule.createShift("Day2", "Day shift #2", LocalTime.of(7, 0, 0), Duration.ofHours(8));

		// Swing shift, starts at 15:00 for 8 hours
		Shift swing = schedule.createShift("Swing", "Swing shift", LocalTime.of(15, 0, 0), Duration.ofHours(8));

		// Night shift #1, starts at 19:00 for 12 hours
		Shift night1 = schedule.createShift("Night1", "Night shift #1", LocalTime.of(19, 0, 0), Duration.ofHours(12));

		// Night shift #2, starts at 23:00 for 8 hours
		Shift night2 = schedule.createShift("Night2", "Night shift #2", LocalTime.of(23, 0, 0), Duration.ofHours(8));

		// shift rotation (28 days)
		ShiftRotation rotation = new ShiftRotation();
		rotation.on(5, day2).on(2, day1).off(3, day1).on(2, night2).on(2, night1).on(3, night2).off(4, day1)
				.on(5, swing).off(2, day1);

		// 4 teams, rotating through 5 shifts
		Team team1 = schedule.createTeam("Team 1", "First team", rotation, referenceDate);
		Team team2 = schedule.createTeam("Team 2", "Second team", rotation, referenceDate.minusDays(7));
		Team team3 = schedule.createTeam("Team 3", "Third team", rotation, referenceDate.minusDays(14));
		Team team4 = schedule.createTeam("Team 4", "Fourth team", rotation, referenceDate.minusDays(21));

		System.out.println(schedule.toString());
		schedule.printShiftInstances(referenceDate, referenceDate.plusDays(rotation.getDays()));
	}

	@Test
	public void testNurseICU() throws Exception {
		String description = "This plan supports a combination of 14-hr day shift , 15.5-hr cross-cover shift , and a 14-hr night shift for medical interns. "
				+ "The day shift and the cross-cover shift have the same start time (7:00AM). "
				+ "The night shift starts at around 10:00PM and ends at 12:00PM on the next day.";

		WorkSchedule schedule = new WorkSchedule("ICU Interns Plan", description);

		// Day shift #1, starts at 07:00 for 15.5 hours
		Shift crossover = schedule.createShift("Crossover", "Day shift #1 cross-over", LocalTime.of(7, 0, 0),
				Duration.ofHours(15).plusMinutes(30));

		// Day shift #2, starts at 07:00 for 14 hours
		Shift day = schedule.createShift("Day", "Day shift #2", LocalTime.of(7, 0, 0), Duration.ofHours(14));

		// Night shift, starts at 22:00 for 14 hours
		Shift night = schedule.createShift("Night", "Night shift", LocalTime.of(22, 0, 0), Duration.ofHours(14));

		// Team1 rotation
		ShiftRotation rotation = new ShiftRotation();
		rotation.on(1, day).on(1, crossover).on(1, night).off(1, day);

		Team team1 = schedule.createTeam("Team 1", "First team", rotation, referenceDate);
		Team team2 = schedule.createTeam("Team 2", "Second team", rotation, referenceDate.minusDays(3));
		Team team3 = schedule.createTeam("Team 3", "Third team", rotation, referenceDate.minusDays(2));
		Team team4 = schedule.createTeam("Team 4", "Forth team", rotation, referenceDate.minusDays(1));

		System.out.println(schedule.toString());
		schedule.printShiftInstances(referenceDate, referenceDate.plusDays(rotation.getDays()));
	}

	@Test
	public void testDupont() throws Exception {
		String description = "The DuPont 12-hour rotating shift schedule uses 4 teams (crews) and 2 twelve-hour shifts to provide 24/7 coverage. "
				+ "It consists of a 4-week cycle where each team works 4 consecutive night shifts, "
				+ "followed by 3 days off duty, works 3 consecutive day shifts, followed by 1 day off duty, works 3 consecutive night shifts, "
				+ "followed by 3 days off duty, work 4 consecutive day shift, then have 7 consecutive days off duty. "
				+ "Personnel works an average 42 hours per week.";

		WorkSchedule schedule = new WorkSchedule("DuPont Shift Schedule", description);

		// Day shift, starts at 07:00 for 12 hours
		Shift day = schedule.createShift("Day", "Day shift", LocalTime.of(7, 0, 0), Duration.ofHours(12));

		// Night shift, starts at 19:00 for 12 hours
		Shift night = schedule.createShift("Night", "Night shift", LocalTime.of(19, 0, 0), Duration.ofHours(12));

		// Team1 rotation
		ShiftRotation rotation = new ShiftRotation();
		rotation.on(4, night).off(3, night).on(3, day).off(1, day).on(3, night).off(3, night).on(4, day).off(7, day);

		Team team1 = schedule.createTeam("Team 1", "First team", rotation, referenceDate);
		Team team2 = schedule.createTeam("Team 2", "Second team", rotation, referenceDate.minusDays(7));
		Team team3 = schedule.createTeam("Team 3", "Third team", rotation, referenceDate.minusDays(14));
		Team team4 = schedule.createTeam("Team 4", "Forth team", rotation, referenceDate.minusDays(21));

		System.out.println(schedule.toString());
		schedule.printShiftInstances(referenceDate, referenceDate.plusDays(rotation.getDays()));
	}

	@Test
	public void testDNO() throws Exception {
		String description = "This is a fast rotation plan that uses 3 teams and two 12-hr shifts to provide 24/7 coverage. "
				+ "Each team rotates through the following sequence every three days: 1 day shift, 1 night shift, and 1 day off.";

		WorkSchedule schedule = new WorkSchedule("DNO Plan", description);

		// Day shift, starts at 07:00 for 12 hours
		Shift day = schedule.createShift("Day", "Day shift", LocalTime.of(7, 0, 0), Duration.ofHours(12));

		// Night shift, starts at 19:00 for 12 hours
		Shift night = schedule.createShift("Night", "Night shift", LocalTime.of(19, 0, 0), Duration.ofHours(12));

		// rotation
		ShiftRotation rotation = new ShiftRotation();
		rotation.on(1, day).on(1, night).off(1, night);

		Team team1 = schedule.createTeam("Team 1", "First team", rotation, referenceDate);
		Team team2 = schedule.createTeam("Team 2", "Second team", rotation, referenceDate.minusDays(1));
		Team team3 = schedule.createTeam("Team 3", "Third team", rotation, referenceDate.minusDays(2));

		System.out.println(schedule.toString());
		schedule.printShiftInstances(referenceDate, referenceDate.plusDays(rotation.getDays()));
	}

	@Test
	public void test21TeamFixed() throws Exception {
		String description = "This plan is a fixed (no rotation) plan that uses 21 teams and three 8-hr shifts to provide 24/7 coverage. "
				+ "It maximizes the number of consecutive days off while still averaging 40 hours per week. "
				+ "Over a 7 week cycle, each employee has two 3 consecutive days off and is required to work 6 consecutive days on 5 of the 7 weeks. "
				+ "On any given day, 15 teams will be scheduled to work and 6 teams will be off. "
				+ "Each shift will be staffed by 5 teams so the minimum number of employees per shift is five. ";

		WorkSchedule schedule = new WorkSchedule("21 Team Fixed 8 6D Plan", description);

		// Day shift, starts at 07:00 for 8 hours
		Shift day = schedule.createShift("Day", "Day shift", LocalTime.of(7, 0, 0), Duration.ofHours(8));

		// Swing shift, starts at 15:00 for 8 hours
		Shift swing = schedule.createShift("Swing", "Swing shift", LocalTime.of(15, 0, 0), Duration.ofHours(8));

		// Night shift, starts at 15:00 for 8 hours
		Shift night = schedule.createShift("Night", "Night shift", LocalTime.of(23, 0, 0), Duration.ofHours(8));

		// day rotation
		ShiftRotation dayRotation = new ShiftRotation();
		dayRotation.on(6, day).off(3, day).on(5, day).off(3, day).on(6, day).off(2, day).on(6, day).off(2, day)
				.on(6, day).off(2, day).on(6, day).off(2, day);

		// swing rotation
		ShiftRotation swingRotation = new ShiftRotation();
		swingRotation.on(6, swing).off(3, swing).on(5, swing).off(3, swing).on(6, swing).off(2, swing).on(6, swing)
				.off(2, swing).on(6, swing).off(2, swing).on(6, swing).off(2, swing);

		// night rotation
		ShiftRotation nightRotation = new ShiftRotation();
		nightRotation.on(6, night).off(3, night).on(5, night).off(3, night).on(6, night).off(2, night).on(6, night)
				.off(2, night).on(6, night).off(2, night).on(6, night).off(2, night);

		// day teams
		Team team1 = schedule.createTeam("Team 1", "1st day team", dayRotation, referenceDate);
		Team team2 = schedule.createTeam("Team 2", "2nd day team", dayRotation, referenceDate.plusDays(7));
		Team team3 = schedule.createTeam("Team 3", "3rd day team", dayRotation, referenceDate.plusDays(14));
		Team team4 = schedule.createTeam("Team 4", "4th day team", dayRotation, referenceDate.plusDays(21));
		Team team5 = schedule.createTeam("Team 5", "5th day team", dayRotation, referenceDate.plusDays(28));
		Team team6 = schedule.createTeam("Team 6", "6th day team", dayRotation, referenceDate.plusDays(35));
		Team team7 = schedule.createTeam("Team 7", "7th day team", dayRotation, referenceDate.plusDays(42));

		// swing teams
		Team team8 = schedule.createTeam("Team 8", "1st swing team", swingRotation, referenceDate);
		Team team9 = schedule.createTeam("Team 9", "2nd swing team", swingRotation, referenceDate.plusDays(7));
		Team team10 = schedule.createTeam("Team 10", "3rd swing team", swingRotation, referenceDate.plusDays(14));
		Team team11 = schedule.createTeam("Team 11", "4th swing team", swingRotation, referenceDate.plusDays(21));
		Team team12 = schedule.createTeam("Team 12", "5th swing team", swingRotation, referenceDate.plusDays(28));
		Team team13 = schedule.createTeam("Team 13", "6th swing team", swingRotation, referenceDate.plusDays(35));
		Team team14 = schedule.createTeam("Team 14", "7th swing team", swingRotation, referenceDate.plusDays(42));

		// night teams
		Team team15 = schedule.createTeam("Team 15", "1st night team", nightRotation, referenceDate);
		Team team16 = schedule.createTeam("Team 16", "2nd night team", nightRotation, referenceDate.plusDays(7));
		Team team17 = schedule.createTeam("Team 17", "3rd night team", nightRotation, referenceDate.plusDays(14));
		Team team18 = schedule.createTeam("Team 18", "4th night team", nightRotation, referenceDate.plusDays(21));
		Team team19 = schedule.createTeam("Team 19", "5th night team", nightRotation, referenceDate.plusDays(28));
		Team team20 = schedule.createTeam("Team 20", "6th night team", nightRotation, referenceDate.plusDays(35));
		Team team21 = schedule.createTeam("Team 21", "7th night team", nightRotation, referenceDate.plusDays(42));

		System.out.println(schedule.toString());
		schedule.printShiftInstances(referenceDate.plusDays(49), referenceDate.plusDays(88));

	}

	@Test
	public void testTwoTeam() throws Exception {
		String description = "This is a fixed (no rotation) plan that uses 2 teams and two 12-hr shifts to provide 24/7 coverage. "
				+ "One team will be permanently on the day shift and the other will be on the night shift.";

		WorkSchedule schedule = new WorkSchedule("2 Team Fixed 12 Plan", description);

		// Day shift, starts at 07:00 for 12 hours
		Shift day = schedule.createShift("Day", "Day shift", LocalTime.of(7, 0, 0), Duration.ofHours(12));

		// Night shift, starts at 19:00 for 12 hours
		Shift night = schedule.createShift("Night", "Night shift", LocalTime.of(19, 0, 0), Duration.ofHours(12));

		// Team1 rotation
		ShiftRotation team1Rotation = new ShiftRotation();
		// 1 day on (and repeat)
		team1Rotation.on(1, day);

		// Team1 rotation
		ShiftRotation team2Rotation = new ShiftRotation();
		// 1 night on (and repeat)
		team2Rotation.on(1, night);

		Team team1 = schedule.createTeam("Team 1", "First team", team1Rotation, referenceDate);
		Team team2 = schedule.createTeam("Team 2", "Second team", team2Rotation, referenceDate);

		System.out.println(schedule.toString());
		schedule.printShiftInstances(referenceDate, referenceDate.plusDays(7));
	}

	@Test
	public void testPanama() throws Exception {
		String description = "This is a slow rotation plan that uses 4 teams and two 12-hr shifts to provide 24/7 coverage. "
				+ "The working and non-working days follow this pattern: 2 days on, 2 days off, 3 days on, 2 days off, 2 days on, 3 days off. "
				+ "Each team works the same shift (day or night) for 28 days then switches over to the other shift for the next 28 days. "
				+ "After 56 days, the same sequence starts over.";

		WorkSchedule schedule = new WorkSchedule("Panama", description);

		// Day shift, starts at 07:00 for 12 hours
		Shift day = schedule.createShift("Day", "Day shift", LocalTime.of(7, 0, 0), Duration.ofHours(12));

		// Night shift, starts at 19:00 for 12 hours
		Shift night = schedule.createShift("Night", "Night shift", LocalTime.of(19, 0, 0), Duration.ofHours(12));

		// rotation
		ShiftRotation rotation = new ShiftRotation();
		// 2 days on, 2 off, 3 on, 2 off, 2 on, 3 off (and repeat)
		rotation.on(2, day).off(2, day).on(3, day).off(2, day).on(2, day).off(3, day).on(2, day).off(2, day).on(3, day)
				.off(2, day).on(2, day).off(3, day);

		// 2 nights on, 2 off, 3 on, 2 off, 2 on, 3 off (and repeat)
		rotation.on(2, night).off(2, night).on(3, night).off(2, night).on(2, night).off(3, night).on(2, night)
				.off(2, night).on(3, night).off(2, night).on(2, night).off(3, night);

		Team team1 = schedule.createTeam("Team 1", "First team", rotation, referenceDate);
		Team team2 = schedule.createTeam("Team 2", "Second team", rotation, referenceDate.minusDays(28));
		Team team3 = schedule.createTeam("Team 3", "Third team", rotation, referenceDate.minusDays(7));
		Team team4 = schedule.createTeam("Team 4", "Fourth team", rotation, referenceDate.minusDays(35));

		System.out.println(schedule.toString());
		schedule.printShiftInstances(referenceDate, referenceDate.plusDays(rotation.getDays()));
	}

	public static void main(String[] args) {
		TestSnapSchedule test = new TestSnapSchedule();

		try {
			/*
			test.testPanama();
			test.testTwoTeam();
			test.testDNO();
			test.testDupont();
			test.testNurseICU();
			test.test8Plus12();
			test.test9to5();
			test.test21TeamFixed();
			*/
			//test.test549();
			//test.test3TeamFixed24();
			test.testLowNight();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}

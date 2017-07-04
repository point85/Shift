/*
MIT License

Copyright (c) 2016 Kent Randall

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

package org.point85.workschedule.test;

import static org.junit.Assert.assertTrue;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.Test;
import org.point85.workschedule.Rotation;
import org.point85.workschedule.Shift;
import org.point85.workschedule.WorkSchedule;

public class TestSnapSchedule extends BaseTest {

	@Test
	public void testLowNight() throws Exception {
		String description = "Low night demand";

		schedule = new WorkSchedule("Low Night Demand Plan", description);

		// 3 shifts
		Shift day = schedule.createShift("Day", "Day shift", LocalTime.of(7, 0, 0), Duration.ofHours(8));
		Shift swing = schedule.createShift("Swing", "Swing shift", LocalTime.of(15, 0, 0), Duration.ofHours(8));
		Shift night = schedule.createShift("Night", "Night shift", LocalTime.of(23, 0, 0), Duration.ofHours(8));

		// Team rotation
		Rotation rotation = new Rotation("Low night demand", "Low night demand");
		rotation.addSegment(day, 3, 0);
		rotation.addSegment(swing, 4, 3);
		rotation.addSegment(day, 4, 0);
		rotation.addSegment(swing, 3, 4);
		rotation.addSegment(day, 3, 0);
		rotation.addSegment(night, 4, 3);
		rotation.addSegment(day, 4, 0);
		rotation.addSegment(night, 3, 4);

		// 6 teams
		schedule.createTeam("Team1", "First team", rotation, referenceDate);
		schedule.createTeam("Team2", "Second team", rotation, referenceDate.minusDays(21));
		schedule.createTeam("Team3", "Third team", rotation, referenceDate.minusDays(7));
		schedule.createTeam("Team4", "Fourth team", rotation, referenceDate.minusDays(28));
		schedule.createTeam("Team5", "Fifth team", rotation, referenceDate.minusDays(14));
		schedule.createTeam("Team6", "Sixth team", rotation, referenceDate.minusDays(35));

		runBaseTest(schedule, Duration.ofHours(224), Duration.ofDays(42), referenceDate);
	}

	@Test
	public void test3TeamFixed24() throws Exception {
		String description = "Fire departments";

		schedule = new WorkSchedule("3 Team Fixed 24 Plan", description);

		// Shift starts at 00:00 for 24 hours
		Shift shift = schedule.createShift("24 Hour", "24 hour shift", LocalTime.of(0, 0, 0), Duration.ofHours(24));

		// Team rotation
		Rotation rotation = new Rotation("3 Team Fixed 24 Plan", "3 Team Fixed 24 Plan");
		rotation.addSegment(shift, 1, 1);
		rotation.addSegment(shift, 1, 1);
		rotation.addSegment(shift, 1, 4);

		// 3 teams
		schedule.createTeam("Team1", "First team", rotation, referenceDate);
		schedule.createTeam("Team2", "Second team", rotation, referenceDate.minusDays(3));
		schedule.createTeam("Team3", "Third team", rotation, referenceDate.minusDays(6));

		runBaseTest(schedule, Duration.ofHours(72), Duration.ofDays(9), referenceDate);
	}

	@Test
	public void test549() throws Exception {
		String description = "Compressed work schedule.";

		schedule = new WorkSchedule("5/4/9 Plan", description);

		// Shift 1 starts at 07:00 for 9 hours
		Shift day1 = schedule.createShift("Day1", "Day shift #1", LocalTime.of(7, 0, 0), Duration.ofHours(9));

		// Shift 2 starts at 07:00 for 8 hours
		Shift day2 = schedule.createShift("Day2", "Day shift #2", LocalTime.of(7, 0, 0), Duration.ofHours(8));

		// Team rotation (28 days)
		Rotation rotation = new Rotation("5/4/9 ", "5/4/9 ");
		rotation.addSegment(day1, 4, 0);
		rotation.addSegment(day2, 1, 3);
		rotation.addSegment(day1, 4, 3);
		rotation.addSegment(day1, 4, 2);
		rotation.addSegment(day1, 4, 0);
		rotation.addSegment(day2, 1, 2);

		// 2 teams
		schedule.createTeam("Team1", "First team", rotation, referenceDate);
		schedule.createTeam("Team2", "Second team", rotation, referenceDate.minusDays(14));

		runBaseTest(schedule, Duration.ofHours(160), Duration.ofDays(28), referenceDate);
	}

	@Test
	public void test9to5() throws Exception {
		String description = "This is the basic 9 to 5 schedule plan for office employees. Every employee works 8 hrs a day from Monday to Friday.";

		schedule = new WorkSchedule("9 To 5 Plan", description);

		// Shift starts at 09:00 for 8 hours
		Shift day = schedule.createShift("Day", "Day shift", LocalTime.of(9, 0, 0), Duration.ofHours(8));

		// Team1 rotation (5 days)
		Rotation rotation = new Rotation("9 To 5 ", "9 To 5 ");
		rotation.addSegment(day, 5, 2);

		// 1 team, 1 shift
		schedule.createTeam("Team", "One team", rotation, referenceDate);

		runBaseTest(schedule, Duration.ofHours(40), Duration.ofDays(7), referenceDate);
	}

	@Test
	public void test8Plus12() throws Exception {
		String description = "This is a fast rotation plan that uses 4 teams and a combination of three 8-hr shifts on weekdays "
				+ "and two 12-hr shifts on weekends to provide 24/7 coverage.";

		// work schedule
		schedule = new WorkSchedule("8 Plus 12 Plan", description);

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
		Rotation rotation = new Rotation("8 Plus 12", "8 Plus 12");
		rotation.addSegment(day2, 5, 0);
		rotation.addSegment(day1, 2, 3);
		rotation.addSegment(night2, 2, 0);
		rotation.addSegment(night1, 2, 0);
		rotation.addSegment(night2, 3, 4);
		rotation.addSegment(swing, 5, 2);

		// 4 teams, rotating through 5 shifts
		schedule.createTeam("Team 1", "First team", rotation, referenceDate);
		schedule.createTeam("Team 2", "Second team", rotation, referenceDate.minusDays(7));
		schedule.createTeam("Team 3", "Third team", rotation, referenceDate.minusDays(14));
		schedule.createTeam("Team 4", "Fourth team", rotation, referenceDate.minusDays(21));

		runBaseTest(schedule, Duration.ofHours(168), Duration.ofDays(28), referenceDate);
	}

	@Test
	public void testICUInterns() throws Exception {
		String description = "This plan supports a combination of 14-hr day shift , 15.5-hr cross-cover shift , and a 14-hr night shift for medical interns. "
				+ "The day shift and the cross-cover shift have the same start time (7:00AM). "
				+ "The night shift starts at around 10:00PM and ends at 12:00PM on the next day.";

		schedule = new WorkSchedule("ICU Interns Plan", description);

		// Day shift #1, starts at 07:00 for 15.5 hours
		Shift crossover = schedule.createShift("Crossover", "Day shift #1 cross-over", LocalTime.of(7, 0, 0),
				Duration.ofHours(15).plusMinutes(30));

		// Day shift #2, starts at 07:00 for 14 hours
		Shift day = schedule.createShift("Day", "Day shift #2", LocalTime.of(7, 0, 0), Duration.ofHours(14));

		// Night shift, starts at 22:00 for 14 hours
		Shift night = schedule.createShift("Night", "Night shift", LocalTime.of(22, 0, 0), Duration.ofHours(14));

		// Team1 rotation
		Rotation rotation = new Rotation("ICU", "ICU");
		rotation.addSegment(day, 1, 0);
		rotation.addSegment(crossover, 1, 0);
		rotation.addSegment(night, 1, 1);

		schedule.createTeam("Team 1", "First team", rotation, referenceDate);
		schedule.createTeam("Team 2", "Second team", rotation, referenceDate.minusDays(3));
		schedule.createTeam("Team 3", "Third team", rotation, referenceDate.minusDays(2));
		schedule.createTeam("Team 4", "Forth team", rotation, referenceDate.minusDays(1));

		runBaseTest(schedule, Duration.ofMinutes(2610), Duration.ofDays(4), referenceDate);
	}

	@Test
	public void testDupont() throws Exception {
		String description = "The DuPont 12-hour rotating shift schedule uses 4 teams (crews) and 2 twelve-hour shifts to provide 24/7 coverage. "
				+ "It consists of a 4-week cycle where each team works 4 consecutive night shifts, "
				+ "followed by 3 days off duty, works 3 consecutive day shifts, followed by 1 day off duty, works 3 consecutive night shifts, "
				+ "followed by 3 days off duty, work 4 consecutive day shift, then have 7 consecutive days off duty. "
				+ "Personnel works an average 42 hours per week.";

		schedule = new WorkSchedule("DuPont Shift Schedule", description);

		// Day shift, starts at 07:00 for 12 hours
		Shift day = schedule.createShift("Day", "Day shift", LocalTime.of(7, 0, 0), Duration.ofHours(12));

		// Night shift, starts at 19:00 for 12 hours
		Shift night = schedule.createShift("Night", "Night shift", LocalTime.of(19, 0, 0), Duration.ofHours(12));

		// Team1 rotation
		Rotation rotation = new Rotation("DuPont", "DuPont");
		rotation.addSegment(night, 4, 3);
		rotation.addSegment(day, 3, 1);
		rotation.addSegment(night, 3, 3);
		rotation.addSegment(day, 4, 7);

		schedule.createTeam("Team 1", "First team", rotation, referenceDate);
		schedule.createTeam("Team 2", "Second team", rotation, referenceDate.minusDays(7));
		schedule.createTeam("Team 3", "Third team", rotation, referenceDate.minusDays(14));
		schedule.createTeam("Team 4", "Forth team", rotation, referenceDate.minusDays(21));

		runBaseTest(schedule, Duration.ofHours(168), Duration.ofDays(28), referenceDate);
	}

	@Test
	public void testDNO() throws Exception {
		String description = "This is a fast rotation plan that uses 3 teams and two 12-hr shifts to provide 24/7 coverage. "
				+ "Each team rotates through the following sequence every three days: 1 day shift, 1 night shift, and 1 day off.";

		schedule = new WorkSchedule("DNO Plan", description);

		// Day shift, starts at 07:00 for 12 hours
		Shift day = schedule.createShift("Day", "Day shift", LocalTime.of(7, 0, 0), Duration.ofHours(12));

		// Night shift, starts at 19:00 for 12 hours
		Shift night = schedule.createShift("Night", "Night shift", LocalTime.of(19, 0, 0), Duration.ofHours(12));

		// rotation
		Rotation rotation = new Rotation("DNO", "DNO");
		rotation.addSegment(day, 1, 0);
		rotation.addSegment(night, 1, 1);

		schedule.createTeam("Team 1", "First team", rotation, referenceDate);
		schedule.createTeam("Team 2", "Second team", rotation, referenceDate.minusDays(1));
		schedule.createTeam("Team 3", "Third team", rotation, referenceDate.minusDays(2));

		// rotation working time
		LocalDateTime from = LocalDateTime.of(referenceDate.plusDays(rotation.getDayCount()), LocalTime.of(7, 0, 0));
		Duration duration = schedule.calculateWorkingTime(from, from.plusDays(3));
		assertTrue(duration.equals(Duration.ofHours(72)));

		runBaseTest(schedule, Duration.ofHours(24), Duration.ofDays(3), referenceDate);
	}

	@Test
	public void test21TeamFixed() throws Exception {
		String description = "This plan is a fixed (no rotation) plan that uses 21 teams and three 8-hr shifts to provide 24/7 coverage. "
				+ "It maximizes the number of consecutive days off while still averaging 40 hours per week. "
				+ "Over a 7 week cycle, each employee has two 3 consecutive days off and is required to work 6 consecutive days on 5 of the 7 weeks. "
				+ "On any given day, 15 teams will be scheduled to work and 6 teams will be off. "
				+ "Each shift will be staffed by 5 teams so the minimum number of employees per shift is five. ";

		schedule = new WorkSchedule("21 Team Fixed 8 6D Plan", description);

		// Day shift, starts at 07:00 for 8 hours
		Shift day = schedule.createShift("Day", "Day shift", LocalTime.of(7, 0, 0), Duration.ofHours(8));

		// Swing shift, starts at 15:00 for 8 hours
		Shift swing = schedule.createShift("Swing", "Swing shift", LocalTime.of(15, 0, 0), Duration.ofHours(8));

		// Night shift, starts at 15:00 for 8 hours
		Shift night = schedule.createShift("Night", "Night shift", LocalTime.of(23, 0, 0), Duration.ofHours(8));

		// day rotation
		Rotation dayRotation = new Rotation("Day", "Day");
		dayRotation.addSegment(day, 6, 3);
		dayRotation.addSegment(day, 5, 3);
		dayRotation.addSegment(day, 6, 2);
		dayRotation.addSegment(day, 6, 2);
		dayRotation.addSegment(day, 6, 2);
		dayRotation.addSegment(day, 6, 2);

		// swing rotation
		Rotation swingRotation = new Rotation("Swing", "Swing");
		swingRotation.addSegment(swing, 6, 3);
		swingRotation.addSegment(swing, 5, 3);
		swingRotation.addSegment(swing, 6, 2);
		swingRotation.addSegment(swing, 6, 2);
		swingRotation.addSegment(swing, 6, 2);
		swingRotation.addSegment(swing, 6, 2);

		// night rotation
		Rotation nightRotation = new Rotation("Night", "Night");
		nightRotation.addSegment(night, 6, 3);
		nightRotation.addSegment(night, 5, 3);
		nightRotation.addSegment(night, 6, 2);
		nightRotation.addSegment(night, 6, 2);
		nightRotation.addSegment(night, 6, 2);
		nightRotation.addSegment(night, 6, 2);

		// day teams
		schedule.createTeam("Team 1", "1st day team", dayRotation, referenceDate);
		schedule.createTeam("Team 2", "2nd day team", dayRotation, referenceDate.plusDays(7));
		schedule.createTeam("Team 3", "3rd day team", dayRotation, referenceDate.plusDays(14));
		schedule.createTeam("Team 4", "4th day team", dayRotation, referenceDate.plusDays(21));
		schedule.createTeam("Team 5", "5th day team", dayRotation, referenceDate.plusDays(28));
		schedule.createTeam("Team 6", "6th day team", dayRotation, referenceDate.plusDays(35));
		schedule.createTeam("Team 7", "7th day team", dayRotation, referenceDate.plusDays(42));

		// swing teams
		schedule.createTeam("Team 8", "1st swing team", swingRotation, referenceDate);
		schedule.createTeam("Team 9", "2nd swing team", swingRotation, referenceDate.plusDays(7));
		schedule.createTeam("Team 10", "3rd swing team", swingRotation, referenceDate.plusDays(14));
		schedule.createTeam("Team 11", "4th swing team", swingRotation, referenceDate.plusDays(21));
		schedule.createTeam("Team 12", "5th swing team", swingRotation, referenceDate.plusDays(28));
		schedule.createTeam("Team 13", "6th swing team", swingRotation, referenceDate.plusDays(35));
		schedule.createTeam("Team 14", "7th swing team", swingRotation, referenceDate.plusDays(42));

		// night teams
		schedule.createTeam("Team 15", "1st night team", nightRotation, referenceDate);
		schedule.createTeam("Team 16", "2nd night team", nightRotation, referenceDate.plusDays(7));
		schedule.createTeam("Team 17", "3rd night team", nightRotation, referenceDate.plusDays(14));
		schedule.createTeam("Team 18", "4th night team", nightRotation, referenceDate.plusDays(21));
		schedule.createTeam("Team 19", "5th night team", nightRotation, referenceDate.plusDays(28));
		schedule.createTeam("Team 20", "6th night team", nightRotation, referenceDate.plusDays(35));
		schedule.createTeam("Team 21", "7th night team", nightRotation, referenceDate.plusDays(42));

		runBaseTest(schedule, Duration.ofHours(280), Duration.ofDays(49), referenceDate.plusDays(49));

	}

	@Test
	public void testTwoTeam() throws Exception {
		String description = "This is a fixed (no rotation) plan that uses 2 teams and two 12-hr shifts to provide 24/7 coverage. "
				+ "One team will be permanently on the day shift and the other will be on the night shift.";

		schedule = new WorkSchedule("2 Team Fixed 12 Plan", description);

		// Day shift, starts at 07:00 for 12 hours
		Shift day = schedule.createShift("Day", "Day shift", LocalTime.of(7, 0, 0), Duration.ofHours(12));

		// Night shift, starts at 19:00 for 12 hours
		Shift night = schedule.createShift("Night", "Night shift", LocalTime.of(19, 0, 0), Duration.ofHours(12));

		// Team1 rotation
		Rotation team1Rotation = new Rotation("Team1", "Team1");
		team1Rotation.addSegment(day, 1, 0);

		// Team1 rotation
		Rotation team2Rotation = new Rotation("Team2", "Team2");
		team2Rotation.addSegment(night, 1, 0);

		schedule.createTeam("Team 1", "First team", team1Rotation, referenceDate);
		schedule.createTeam("Team 2", "Second team", team2Rotation, referenceDate);

		runBaseTest(schedule, Duration.ofHours(12), Duration.ofDays(1), referenceDate);
	}

	@Test
	public void testPanama() throws Exception {
		String description = "This is a slow rotation plan that uses 4 teams and two 12-hr shifts to provide 24/7 coverage. "
				+ "The working and non-working days follow this pattern: 2 days on, 2 days off, 3 days on, 2 days off, 2 days on, 3 days off. "
				+ "Each team works the same shift (day or night) for 28 days then switches over to the other shift for the next 28 days. "
				+ "After 56 days, the same sequence starts over.";

		String scheduleName = "Panama";
		schedule = new WorkSchedule(scheduleName, description);

		// Day shift, starts at 07:00 for 12 hours
		Shift day = schedule.createShift("Day", "Day shift", LocalTime.of(7, 0, 0), Duration.ofHours(12));

		// Night shift, starts at 19:00 for 12 hours
		Shift night = schedule.createShift("Night", "Night shift", LocalTime.of(19, 0, 0), Duration.ofHours(12));

		// rotation
		Rotation rotation = new Rotation("Panama",
				"2 days on, 2 days off, 3 days on, 2 days off, 2 days on, 3 days off");
		// 2 days on, 2 off, 3 on, 2 off, 2 on, 3 off (and repeat)
		rotation.addSegment(day, 2, 2);
		rotation.addSegment(day, 3, 2);
		rotation.addSegment(day, 2, 3);
		rotation.addSegment(day, 2, 2);
		rotation.addSegment(day, 3, 2);
		rotation.addSegment(day, 2, 3);

		// 2 nights on, 2 off, 3 on, 2 off, 2 on, 3 off (and repeat)
		rotation.addSegment(night, 2, 2);
		rotation.addSegment(night, 3, 2);
		rotation.addSegment(night, 2, 3);
		rotation.addSegment(night, 2, 2);
		rotation.addSegment(night, 3, 2);
		rotation.addSegment(night, 2, 3);

		// reference date for start of shift rotations
		LocalDate referenceDate = LocalDate.of(2016, 10, 31);

		schedule.createTeam("Team 1", "First team", rotation, referenceDate);
		schedule.createTeam("Team 2", "Second team", rotation, referenceDate.minusDays(28));
		schedule.createTeam("Team 3", "Third team", rotation, referenceDate.minusDays(7));
		schedule.createTeam("Team 4", "Fourth team", rotation, referenceDate.minusDays(35));

		runBaseTest(schedule, Duration.ofHours(336), Duration.ofDays(56), referenceDate);
	}
}

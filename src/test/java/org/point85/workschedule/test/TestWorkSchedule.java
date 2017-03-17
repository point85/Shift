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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.point85.workschedule.Break;
import org.point85.workschedule.NonWorkingPeriod;
import org.point85.workschedule.Rotation;
import org.point85.workschedule.Shift;
import org.point85.workschedule.ShiftInstance;
import org.point85.workschedule.Team;
import org.point85.workschedule.WorkSchedule;

public class TestWorkSchedule extends BaseTest {

	@Test
	public void testNursingICUShifts() throws Exception {
		// ER nursing schedule
		WorkSchedule schedule = new WorkSchedule("Nursing ICU",
				"Two 12 hr back-to-back shifts, rotating every 14 days");

		// day shift, starts at 06:00 for 12 hours
		Shift day = schedule.createShift("Day", "Day shift", LocalTime.of(6, 0, 0), Duration.ofHours(12));

		// night shift, starts at 18:00 for 12 hours
		Shift night = schedule.createShift("Night", "Night shift", LocalTime.of(18, 0, 0), Duration.ofHours(12));

		// day rotation
		Rotation dayRotation = new Rotation();
		dayRotation.on(3, day).off(4, day).on(4, day).off(3, day);

		// inverse day rotation
		Rotation inverseDayRotation = new Rotation();
		inverseDayRotation.off(3, day).on(4, day).off(4, day).on(3, day);

		// night rotation
		Rotation nightRotation = new Rotation();
		nightRotation.on(4, night).off(3, night).on(3, night).off(4, night);

		// inverse night rotation
		Rotation inverseNightRotation = new Rotation();
		inverseNightRotation.off(4, night).on(3, night).off(3, night).on(4, night);

		LocalDate rotationStart = LocalDate.of(2014, 1, 6);

		schedule.createTeam("A", "Day shift", dayRotation, rotationStart);
		schedule.createTeam("B", "Day inverse shift", inverseDayRotation, rotationStart);
		schedule.createTeam("C", "Night shift", nightRotation, rotationStart);
		schedule.createTeam("D", "Night inverse shift", inverseNightRotation, rotationStart);

		runBaseTest(schedule, Duration.ofHours(84), Duration.ofDays(14), rotationStart);
	}

	@Test
	public void testPostalServiceShifts() throws Exception {
		// United States Postal Service
		WorkSchedule schedule = new WorkSchedule("USPS", "Six 9 hr shifts, rotating every 42 days");

		// shift, start at 08:00 for 9 hours
		Shift day = schedule.createShift("Day", "day shift", LocalTime.of(8, 0, 0), Duration.ofHours(9));

		Rotation rotation = new Rotation();
		rotation.on(3, day).off(7, day).on(1, day).off(7, day).on(1, day).off(7, day).on(1, day).off(7, day).on(1, day)
				.off(7, day);

		LocalDate rotationStart = LocalDate.of(2017, 1, 27);

		// day teams
		schedule.createTeam("Team A", "A team", rotation, rotationStart);
		schedule.createTeam("Team B", "B team", rotation, rotationStart.minusDays(7));
		schedule.createTeam("Team C", "C team", rotation, rotationStart.minusDays(14));
		schedule.createTeam("Team D", "D team", rotation, rotationStart.minusDays(21));
		schedule.createTeam("Team E", "E team", rotation, rotationStart.minusDays(28));
		schedule.createTeam("Team F", "F team", rotation, rotationStart.minusDays(35));

		runBaseTest(schedule, Duration.ofHours(63), Duration.ofDays(42), rotationStart);
	}

	@Test
	public void testFirefighterShifts2() throws Exception {
		// Seattle, WA fire shifts
		WorkSchedule schedule = new WorkSchedule("Seattle", "Four 24 hour alternating shifts");

		// shift, start at 07:00 for 24 hours
		Shift shift = schedule.createShift("24 Hours", "24 hour shift", LocalTime.of(7, 0, 0), Duration.ofHours(24));

		// 1 day ON, 4 OFF, 1 ON, 2 OFF
		Rotation rotation = new Rotation();
		rotation.on(1, shift).off(4, shift).on(1, shift).off(2, shift);

		schedule.createTeam("A", "Platoon1", rotation, LocalDate.of(2014, 2, 2));
		schedule.createTeam("B", "Platoon2", rotation, LocalDate.of(2014, 2, 4));
		schedule.createTeam("C", "Platoon3", rotation, LocalDate.of(2014, 1, 31));
		schedule.createTeam("D", "Platoon4", rotation, LocalDate.of(2014, 1, 29));

		runBaseTest(schedule, Duration.ofHours(48), Duration.ofDays(8), LocalDate.of(2014, 2, 4));
	}

	@Test
	public void testFirefighterShifts1() throws Exception {
		// Kern Co, CA
		WorkSchedule schedule = new WorkSchedule("Kern Co.", "Three 24 hour alternating shifts");

		// shift, start 07:00 for 24 hours
		Shift shift = schedule.createShift("24 Hour", "24 hour shift", LocalTime.of(7, 0, 0), Duration.ofHours(24));

		// 2 days ON, 2 OFF, 2 ON, 2 OFF, 2 ON, 8 OFF
		Rotation rotation = new Rotation();
		rotation.on(2, shift).off(2, shift).on(2, shift).off(2, shift).on(2, shift).off(8, shift);

		Team platoon1 = schedule.createTeam("Red", "A Shift", rotation, LocalDate.of(2017, 1, 8));
		Team platoon2 = schedule.createTeam("Black", "B Shift", rotation, LocalDate.of(2017, 2, 1));
		Team platoon3 = schedule.createTeam("Green", "C Shift", rotation, LocalDate.of(2017, 1, 2));

		List<ShiftInstance> instances = schedule.getShiftInstancesForDay(LocalDate.of(2017, 3, 1));
		assertTrue(instances.size() == 1);
		assertTrue(instances.get(0).getTeam().equals(platoon3));

		instances = schedule.getShiftInstancesForDay(LocalDate.of(2017, 3, 3));
		assertTrue(instances.size() == 1);
		assertTrue(instances.get(0).getTeam().equals(platoon1));

		instances = schedule.getShiftInstancesForDay(LocalDate.of(2017, 3, 9));
		assertTrue(instances.size() == 1);
		assertTrue(instances.get(0).getTeam().equals(platoon2));

		runBaseTest(schedule, Duration.ofHours(144), Duration.ofDays(18), LocalDate.of(2017, 2, 1));

	}

	@Test
	public void testManufacturingShifts() throws Exception {
		// manufacturing company
		WorkSchedule schedule = new WorkSchedule("Manufacturing Company - four twelves",
				"Four 12 hour alternating day/night shifts");

		// day shift, start at 07:00 for 12 hours
		Shift day = schedule.createShift("Day", "Day shift", LocalTime.of(7, 0, 0), Duration.ofHours(12));

		// night shift, start at 19:00 for 12 hours
		Shift night = schedule.createShift("Night", "Night shift", LocalTime.of(19, 0, 0), Duration.ofHours(12));

		// 7 days ON, 7 OFF
		Rotation dayRotation = new Rotation();
		dayRotation.on(7, day).off(7, day);

		// 7 nights ON, 7 OFF
		Rotation nightRotation = new Rotation();
		nightRotation.on(7, night).off(7, night);

		schedule.createTeam("A", "A day shift", dayRotation, LocalDate.of(2014, 1, 2));
		schedule.createTeam("B", "B night shift", nightRotation, LocalDate.of(2014, 1, 2));
		schedule.createTeam("C", "C day shift", dayRotation, LocalDate.of(2014, 1, 9));
		schedule.createTeam("D", "D night shift", nightRotation, LocalDate.of(2014, 1, 9));

		runBaseTest(schedule, Duration.ofHours(84), Duration.ofDays(14), LocalDate.of(2014, 1, 9));

	}

	@Test
	public void testGenericShift() throws Exception {
		// regular work week with holidays and breaks
		WorkSchedule schedule = new WorkSchedule("Regular 40 hour work week", "9 to 5");
		
		try {
			schedule.setName(null);
			fail();
		} catch (Exception e) {
		}

		// holidays
		schedule.createNonWorkingPeriod("NEW YEARS", "New Years day", LocalDateTime.of(2016, 1, 1, 0, 0, 0),
				Duration.ofHours(24));
		schedule.createNonWorkingPeriod("MEMORIAL DAY", "Memorial day", LocalDateTime.of(2016, 5, 30, 0, 0, 0),
				Duration.ofHours(24));
		schedule.createNonWorkingPeriod("INDEPENDENCE DAY", "Independence day", LocalDateTime.of(2016, 7, 4, 0, 0, 0),
				Duration.ofHours(24));
		schedule.createNonWorkingPeriod("LABOR DAY", "Labor day", LocalDateTime.of(2016, 9, 5, 0, 0, 0),
				Duration.ofHours(24));
		schedule.createNonWorkingPeriod("THANKSGIVING", "Thanksgiving day and day after",
				LocalDateTime.of(2016, 11, 24, 0, 0, 0), Duration.ofHours(48));
		schedule.createNonWorkingPeriod("CHRISTMAS SHUTDOWN", "Christmas week scheduled maintenance",
				LocalDateTime.of(2016, 12, 25, 0, 30, 0), Duration.ofHours(168));

		// each shift duration
		Duration shiftDuration = Duration.ofHours(8);
		LocalTime shift1Start = LocalTime.of(9, 0, 0);
		LocalTime shift2Start = LocalTime.of(17, 0, 0);

		// shift 1
		Shift shift1 = schedule.createShift("Shift1", "Shift #1", shift1Start, shiftDuration);

		// breaks
		shift1.createBreak("10AM", "10 am break", LocalTime.of(10, 0, 0), Duration.ofMinutes(15));
		shift1.createBreak("LUNCH", "lunch", LocalTime.of(12, 0, 0), Duration.ofHours(1));
		shift1.createBreak("2PM", "2 pm break", LocalTime.of(14, 0, 0), Duration.ofMinutes(15));

		// shift 2
		Shift shift2 = schedule.createShift("Shift2", "Shift #2", shift2Start, shiftDuration);

		// shift 1, 5 days ON, 2 OFF
		Rotation rotation1 = new Rotation();
		rotation1.on(5, shift1).off(2, shift1);

		// shift 2, 5 days ON, 2 OFF
		Rotation rotation2 = new Rotation();
		rotation2.on(5, shift2).off(2, shift2);

		LocalDate startRotation = LocalDate.of(2016, 1, 1);
		Team team1 = schedule.createTeam("Team1", "Team #1", rotation1, startRotation);
		Team team2 = schedule.createTeam("Team2", "Team #2", rotation2, startRotation);

		// same day
		LocalDateTime from = LocalDateTime.of(startRotation, shift1Start);
		LocalDateTime to = null;

		Duration totalWorking = null;

		// 21 days, team1
		Duration d = Duration.ZERO;

		for (int i = 0; i < 21; i++) {
			to = from.plusDays(i);
			totalWorking = team1.calculateWorkingTime(from, to);
			int dir = team1.getDayInRotation(to.toLocalDate());

			assertTrue(totalWorking.equals(d));

			if (rotation1.getPeriods().get(dir) instanceof Shift) {
				d = d.plus(shiftDuration);
			}
		}
		Duration totalSchedule = totalWorking;

		// 21 days, team2
		from = LocalDateTime.of(startRotation, shift2Start);
		d = Duration.ZERO;

		for (int i = 0; i < 21; i++) {
			to = from.plusDays(i);
			totalWorking = team2.calculateWorkingTime(from, to);
			int dir = team2.getDayInRotation(to.toLocalDate());

			assertTrue(totalWorking.equals(d));

			if (rotation1.getPeriods().get(dir) instanceof Shift) {
				d = d.plus(shiftDuration);
			}
		}
		totalSchedule = totalSchedule.plus(totalWorking);

		Duration scheduleDuration = schedule.calculateWorkingTime(from, from.plusDays(20));
		assertTrue(scheduleDuration.equals(totalSchedule));

		// breaks
		Duration allBreaks = Duration.ofMinutes(90);
		assertTrue(shift1.calculateBreakTime().equals(allBreaks));

		runBaseTest(schedule, Duration.ofHours(40), Duration.ofDays(7), LocalDate.of(2016, 1, 1));

	}

	@Test
	public void testExceptions() throws Exception {
		WorkSchedule schedule = new WorkSchedule("Exceptions", "Test exceptions");
		Duration shiftDuration = Duration.ofHours(24);
		LocalTime shiftStart = LocalTime.of(7, 0, 0);

		NonWorkingPeriod period = schedule.createNonWorkingPeriod("Non-working", "Non-working period",
				LocalDateTime.of(2017, 1, 1, 0, 0, 0), Duration.ofHours(24));

		try {
			period.setDuration(null);
			fail();
		} catch (Exception e) {
		}
		
		try {
			period.setDuration(Duration.ofSeconds(0));
			fail();
		} catch (Exception e) {
		}
		
		try {
			period.setStartDateTime(null);
			fail();
		} catch (Exception e) {
		}

		try {
			// same period
			schedule.createNonWorkingPeriod("Non-working", "Non-working period", LocalDateTime.of(2017, 1, 1, 0, 0, 0),
					Duration.ofHours(24));
			fail();
		} catch (Exception e) {
		}

		// shift
		Shift shift = schedule.createShift("Test", "Test shift", shiftStart, shiftDuration);
		shift.getWorkingTimeBetween(shiftStart.minusHours(1), shift.getEnd().plusHours(1));
		
		try {
			shift.setDuration(null);
			fail();
		} catch (Exception e) {
		}
		
		try {
			shift.setDuration(Duration.ofSeconds(0));
			fail();
		} catch (Exception e) {
		}

		try {
			// same shift
			shift = schedule.createShift("Test", "Test shift", shiftStart, shiftDuration);
			fail();
		} catch (Exception e) {
		}

		Rotation rotation = new Rotation();
		rotation.on(5, shift).off(2, shift);

		LocalDate startRotation = LocalDate.of(2016, 12, 31);
		Team team = schedule.createTeam("Team", "Team", rotation, startRotation);
		team.setRotationStart(startRotation.plusDays(1));

		// ok
		schedule.calculateWorkingTime(LocalDateTime.of(2017, 1, 1, 7, 0, 0), LocalDateTime.of(2017, 2, 1, 0, 0, 0));

		try {
			// end before start
			schedule.calculateWorkingTime(LocalDateTime.of(2017, 1, 2, 0, 0, 0), LocalDateTime.of(2017, 1, 1, 0, 0, 0));
			fail();
		} catch (Exception e) {
		}

		try {
			// same team
			team = schedule.createTeam("Team", "Team", rotation, startRotation);
			fail();
		} catch (Exception e) {
		}

		try {
			// date before start
			team.getDayInRotation(LocalDate.of(2016, 1, 1));
			fail();
		} catch (Exception e) {
		}

		try {
			// end before start
			schedule.printShiftInstances(LocalDate.of(2017, 1, 2), LocalDate.of(2017, 1, 1));
			fail();
		} catch (Exception e) {
		}

		try {
			// delete in-use shift
			schedule.deleteShift(shift); 
			fail();
		} catch (Exception e) {
		}

		// breaks
		Break lunch = shift.createBreak("Lunch", "Lunch", LocalTime.of(12, 0, 0), Duration.ofMinutes(60));
		lunch.setDuration(Duration.ofMinutes(30));
		lunch.setStart(LocalTime.of(11, 30, 0));
		shift.removeBreak(lunch);
		shift.removeBreak(lunch);

		Shift shift2 = schedule.createShift("Test2", "Test shift2", shiftStart, shiftDuration);
		assertFalse(shift.equals(shift2));
		
		Break lunch2 = shift2.createBreak("Lunch2", "Lunch", LocalTime.of(12, 0, 0), Duration.ofMinutes(60));
		shift.removeBreak(lunch2);

		// ok to delete
		WorkSchedule schedule2 = new WorkSchedule("Exceptions2", "Test exceptions2");
		schedule2.setName("Schedule 2");
		schedule2.setDescription("a description");

		schedule2.deleteShift(shift);
		schedule2.deleteTeam(team);
		schedule2.deleteNonWorkingPeriod(period);

		// nulls
		try {
			schedule.createShift("1", "1", null, Duration.ofMinutes(60));
			fail();
		} catch (Exception e) {
		}

		try {
			schedule.createShift("1", "1", shiftStart, null);
			fail();
		} catch (Exception e) {
		}

		try {
			schedule.createShift(null, "1", shiftStart, Duration.ofMinutes(60));
			fail();
		} catch (Exception e) {
		}

		assertFalse(shift.equals(rotation));

		// hashcode()
		team.hashCode();
		String name = team.getName();
		Map<String, Team> teams = new HashMap<>();
		teams.put(name, team);
		teams.get(name);

	}
	
	@Test
	public void testShiftWorkingTime() throws Exception {
		WorkSchedule schedule = new WorkSchedule("Working Time1", "Test working time");
		
		// shift does not cross midnight
		Duration shiftDuration = Duration.ofHours(8);
		LocalTime shiftStart = LocalTime.of(7, 0, 0);
		
		Shift shift = schedule.createShift("Work Shift1", "Working time shift", shiftStart, shiftDuration);
		LocalTime shiftEnd = shift.getEnd();
		
		// case #1
		Duration time = shift.getWorkingTimeBetween(shiftStart.minusHours(3), shiftStart.minusHours(2));
		assertTrue(time.getSeconds() == 0);
		time = shift.getWorkingTimeBetween(shiftStart.minusHours(3), shiftStart.minusHours(3));
		assertTrue(time.getSeconds() == 0);
		
		// case #2
		time = shift.getWorkingTimeBetween(shiftStart.minusHours(1), shiftStart.plusHours(1));
		assertTrue(time.getSeconds() == 3600);
		
		// case #3
		time = shift.getWorkingTimeBetween(shiftStart.plusHours(1), shiftStart.plusHours(2));
		assertTrue(time.getSeconds() == 3600);
		
		// case #4
		time = shift.getWorkingTimeBetween(shiftEnd.minusHours(1), shiftEnd.plusHours(1));
		assertTrue(time.getSeconds() == 3600);
		
		// case #5
		time = shift.getWorkingTimeBetween(shiftEnd.plusHours(1), shiftEnd.plusHours(2));
		assertTrue(time.getSeconds() == 0);
		time = shift.getWorkingTimeBetween(shiftEnd.plusHours(1), shiftEnd.plusHours(1));
		assertTrue(time.getSeconds() == 0);
		
		// case #6
		time = shift.getWorkingTimeBetween(shiftStart.minusHours(1), shiftEnd.plusHours(1));
		assertTrue(time.getSeconds() == shiftDuration.getSeconds());
						
		// case #7
		time = shift.getWorkingTimeBetween(shiftStart.plusHours(1), shiftStart.plusHours(1));
		assertTrue(time.getSeconds() == 0);
		
		// case #8
		time = shift.getWorkingTimeBetween(shiftStart, shiftEnd);
		assertTrue(time.getSeconds() == shiftDuration.getSeconds());
		
		// case #9
		time = shift.getWorkingTimeBetween(shiftStart, shiftStart);
		assertTrue(time.getSeconds() == 0);
		
		// case #10
		time = shift.getWorkingTimeBetween(shiftEnd, shiftEnd);
		assertTrue(time.getSeconds() == 0);
		
		// 8 hr shift crossing midnight
		shiftStart = LocalTime.of(20, 0, 0);
		
		shift = schedule.createShift("Work Shift2", "Working time shift", shiftStart, shiftDuration);
		shiftEnd = shift.getEnd();

		// case #1
		time = shift.getWorkingTimeBetween(shiftStart.minusHours(3), shiftStart.minusHours(2));
		assertTrue(time.getSeconds() == 0);
		time = shift.getWorkingTimeBetween(shiftStart.minusHours(3), shiftStart.minusHours(3));
		assertTrue(time.getSeconds() == 0);
		
		// case #2
		time = shift.getWorkingTimeBetween(shiftStart.minusHours(1), shiftStart.plusHours(1));
		assertTrue(time.getSeconds() == 3600);
		
		// case #3
		time = shift.getWorkingTimeBetween(shiftStart.plusHours(1), shiftStart.plusHours(2));
		assertTrue(time.getSeconds() == 3600);
		
		// case #4
		time = shift.getWorkingTimeBetween(shiftEnd.minusHours(1), shiftEnd.plusHours(1));
		assertTrue(time.getSeconds() == 3600);
		
		// case #5
		time = shift.getWorkingTimeBetween(shiftEnd.plusHours(1), shiftEnd.plusHours(2));
		assertTrue(time.getSeconds() == 0);
		time = shift.getWorkingTimeBetween(shiftEnd.plusHours(1), shiftEnd.plusHours(1));
		assertTrue(time.getSeconds() == 0);
		
		// case #6
		time = shift.getWorkingTimeBetween(shiftStart.minusHours(1), shiftEnd.plusHours(1));
		assertTrue(time.getSeconds() == shiftDuration.getSeconds());
						
		// case #7
		time = shift.getWorkingTimeBetween(shiftStart.plusHours(1), shiftStart.plusHours(1));
		assertTrue(time.getSeconds() == 0);
		
		// case #8
		time = shift.getWorkingTimeBetween(shiftStart, shiftEnd);
		assertTrue(time.getSeconds() == shiftDuration.getSeconds());
		
		// case #9
		time = shift.getWorkingTimeBetween(shiftStart, shiftStart);
		assertTrue(time.getSeconds() == 0);
		
		// case #10
		time = shift.getWorkingTimeBetween(shiftEnd, shiftEnd);
		assertTrue(time.getSeconds() == 0);
		
		// 24 hr shift crossing midnight
		shiftDuration = Duration.ofHours(24);
		shiftStart = LocalTime.of(7, 0, 0);
		
		shift = schedule.createShift("Work Shift3", "Working time shift", shiftStart, shiftDuration);
		shiftEnd = shift.getEnd();

		// case #1
		time = shift.getWorkingTimeBetween(shiftStart.minusHours(3), shiftStart.minusHours(2));
		assertTrue(time.getSeconds() == 3600);
		time = shift.getWorkingTimeBetween(shiftStart.minusHours(3), shiftStart.minusHours(3));
		assertTrue(time.getSeconds() == 0);
		
		// case #2
		time = shift.getWorkingTimeBetween(shiftStart.minusHours(1), shiftStart.plusHours(1));
		assertTrue(time.getSeconds() == 3600);
		
		// case #3
		time = shift.getWorkingTimeBetween(shiftStart.plusHours(1), shiftStart.plusHours(2));
		assertTrue(time.getSeconds() == 3600);
		
		// case #4
		time = shift.getWorkingTimeBetween(shiftEnd.minusHours(1), shiftEnd.plusHours(1));
		assertTrue(time.getSeconds() == 3600);
		
		// case #5
		time = shift.getWorkingTimeBetween(shiftEnd.plusHours(1), shiftEnd.plusHours(2));
		assertTrue(time.getSeconds() == 3600);
		time = shift.getWorkingTimeBetween(shiftEnd.plusHours(1), shiftEnd.plusHours(1));
		assertTrue(time.getSeconds() == 0);
		
		// case #6
		time = shift.getWorkingTimeBetween(shiftStart.minusHours(1), shiftEnd.plusHours(1));
		assertTrue(time.getSeconds() == 3600);
						
		// case #7
		time = shift.getWorkingTimeBetween(shiftStart.plusHours(1), shiftStart.plusHours(1));
		assertTrue(time.getSeconds() == 0);
		
		// case #8
		time = shift.getWorkingTimeBetween(shiftStart, shiftEnd);
		assertTrue(time.getSeconds() == shiftDuration.getSeconds());
		
		// case #9
		time = shift.getWorkingTimeBetween(shiftStart, shiftStart);
		assertTrue(time.getSeconds() == shiftDuration.getSeconds());
		
		// case #10
		time = shift.getWorkingTimeBetween(shiftEnd, shiftEnd);
		assertTrue(time.getSeconds() == shiftDuration.getSeconds());
	}
}

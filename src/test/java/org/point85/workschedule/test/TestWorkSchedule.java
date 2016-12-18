package org.point85.workschedule.test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.Test;
import org.point85.workschedule.OffShift;
import org.point85.workschedule.Shift;
import org.point85.workschedule.ShiftRotation;
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

		OffShift dayOff = day.createOffShift();

		// night shift, starts at 18:00 for 12 hours
		Shift night = schedule.createShift("Night", "Night shift", LocalTime.of(18, 0, 0), Duration.ofHours(12));

		OffShift nightOff = night.createOffShift();

		// day rotation
		ShiftRotation dayRotation = new ShiftRotation();
		dayRotation.on(3, day).off(4, dayOff).on(4, day).off(3, dayOff);

		// inverse day rotation
		ShiftRotation inverseDayRotation = new ShiftRotation();
		inverseDayRotation.off(3, dayOff).on(4, day).off(4, dayOff).on(3, day);

		// night rotation
		ShiftRotation nightRotation = new ShiftRotation();
		nightRotation.on(4, night).off(3, nightOff).on(3, night).off(4, nightOff);

		// inverse night rotation
		ShiftRotation inverseNightRotation = new ShiftRotation();
		inverseNightRotation.off(4, nightOff).on(3, night).off(3, nightOff).on(4, night);

		Team A = schedule.createTeam("A", "Day shift", dayRotation, LocalDate.of(2014, 1, 6));
		Team B = schedule.createTeam("B", "Day inverse shift", inverseDayRotation, LocalDate.of(2014, 1, 6));
		Team C = schedule.createTeam("C", "Night shift", nightRotation, LocalDate.of(2014, 1, 6));
		Team D = schedule.createTeam("D", "Night inverse shift", inverseNightRotation, LocalDate.of(2014, 1, 6));

		printWorkSchedule(schedule);
		printTeams(schedule.getTeams());
		printShiftInstances(schedule, LocalDate.of(2014, 2, 1));
	}

	@Test
	public void testPostalServiceShifts() throws Exception {
		// United States Postal Service
		WorkSchedule schedule = new WorkSchedule("USPS", "Six 9 hr shifts, rotating every 42 days");

		// shift, start at 08:00 for 9 hours
		Shift day = schedule.createShift("Day", "day shift", LocalTime.of(8, 0, 0), Duration.ofHours(9));
		OffShift dayOff = day.createOffShift();

		ShiftRotation rotation = new ShiftRotation();
		rotation.on(3, day).off(7, dayOff).on(1, day).off(7, dayOff).on(1, day).off(7, dayOff).on(1, day).off(7, dayOff)
				.on(1, day).off(7, dayOff);

		// day teams
		LocalDate referenceDate = LocalDate.of(2017, 1, 27);
		Team teamA = schedule.createTeam("Team A", "A team", rotation, referenceDate);
		Team teamB = schedule.createTeam("Team B", "B team", rotation, referenceDate.minusDays(7));
		Team teamC = schedule.createTeam("Team C", "C team", rotation, referenceDate.minusDays(14));
		Team teamD = schedule.createTeam("Team D", "D team", rotation, referenceDate.minusDays(21));
		Team teamE = schedule.createTeam("Team E", "E team", rotation, referenceDate.minusDays(28));
		Team teamF = schedule.createTeam("Team F", "F team", rotation, referenceDate.minusDays(35));

		printWorkSchedule(schedule);
		printTeams(schedule.getTeams());
		printShiftInstances(schedule, referenceDate);

	}

	@Test
	public void testFirefighterShifts2() throws Exception {
		// Seattle, WA fire shifts
		WorkSchedule schedule = new WorkSchedule("Seattle", "Four 24 hour alternating shifts");

		// shift, start at 07:00 for 24 hours
		Shift shift = schedule.createShift("24 Hours", "24 hour shift", LocalTime.of(7, 0, 0), Duration.ofHours(24));

		// off shift
		OffShift offShift = shift.createOffShift();

		// 1 day ON, 4 OFF, 1 ON, 2 OFF
		ShiftRotation rotation = new ShiftRotation();
		rotation.on(1, shift).off(4, offShift).on(1, shift).off(2, offShift);

		Team platoon1 = schedule.createTeam("A", "Platoon1", rotation, LocalDate.of(2014, 2, 2));
		Team platoon2 = schedule.createTeam("B", "Platoon2", rotation, LocalDate.of(2014, 2, 4));
		Team platoon3 = schedule.createTeam("C", "Platoon3", rotation, LocalDate.of(2014, 1, 31));
		Team platoon4 = schedule.createTeam("D", "Platoon4", rotation, LocalDate.of(2014, 1, 29));

		printWorkSchedule(schedule);
		printTeams(schedule.getTeams());
		printShiftInstances(schedule, LocalDate.of(2016, 7, 1));
	}

	@Test
	public void testFirefighterShifts3() throws Exception {
		// Lansing, MI fire shifts
		WorkSchedule schedule = new WorkSchedule("Lansing MI R-1", "Three 24 hour shifts");

		// shift, start at 07:00 for 24 hours
		Shift shift = schedule.createShift("24 Hours", "24 hour shift", LocalTime.of(7, 0, 0), Duration.ofHours(24));

		// off shift
		OffShift offShift = shift.createOffShift();

		// 1 day ON, 1 OFF, 1 ON, 1 OFF, 1 ON, 1 OFF,
		ShiftRotation blackRotation = new ShiftRotation();
		blackRotation.on(1, shift).off(1, offShift).on(1, shift).off(1, offShift).on(1, shift).off(1, offShift);

		// 1 day ON, 1 OFF, 1 ON, 3 OFF
		ShiftRotation redRotation = new ShiftRotation();
		redRotation.on(1, shift).off(1, offShift).on(1, shift).off(3, offShift);

		// 1 ON, 5 OFF,
		ShiftRotation fushiaRotation = new ShiftRotation();
		fushiaRotation.on(1, shift).off(5, offShift);

		Team platoon1 = schedule.createTeam("Red", "Platoon1", redRotation, LocalDate.of(2017, 1, 6));
		Team platoon2 = schedule.createTeam("Black", "Platoon2", blackRotation, LocalDate.of(2017, 1, 3));
		Team platoon3 = schedule.createTeam("Fushia", "Platoon3", fushiaRotation, LocalDate.of(2017, 1, 4));

		printWorkSchedule(schedule);
		printTeams(schedule.getTeams());
		printShiftInstances(schedule, LocalDate.of(2017, 2, 1));
	}

	@Test
	public void testFirefighterShifts1() throws Exception {
		// Kern Co, CA
		WorkSchedule schedule = new WorkSchedule("Kern Co.", "Three 24 hour alternating shifts");

		// shift, start 07:00 for 24 hours
		Shift shift = schedule.createShift("24 Hour", "24 hour shift", LocalTime.of(7, 0, 0), Duration.ofHours(24));

		// off shift
		OffShift offShift = shift.createOffShift();

		// 2 days ON, 2 OFF, 2 ON, 2 OFF, 2 ON, 8 OFF
		ShiftRotation rotation = new ShiftRotation();
		rotation.on(2, shift).off(2, offShift).on(2, shift).off(2, offShift).on(2, shift).off(8, offShift);

		Team platoon1 = schedule.createTeam("Red", "Platoon1", rotation, LocalDate.of(2017, 1, 8));
		Team platoon2 = schedule.createTeam("Black", "Platoon2", rotation, LocalDate.of(2017, 2, 1));
		Team platoon3 = schedule.createTeam("Green", "Platoon3", rotation, LocalDate.of(2017, 1, 2));

		printWorkSchedule(schedule);
		printTeams(schedule.getTeams());
		printShiftInstances(schedule, LocalDate.of(2017, 2, 1));
	}

	@Test
	public void testManufacturingShifts() throws Exception {
		// manufacturing company
		WorkSchedule schedule = new WorkSchedule("Manufacturing Company - four twelves",
				"Four 12 hour alternating day/night shifts");

		// day shift, start at 07:00 for 12 hours
		Shift day = schedule.createShift("Day", "Day shift", LocalTime.of(7, 0, 0), Duration.ofHours(12));

		// day off shift
		OffShift dayOff = day.createOffShift();

		// night shift, start at 19:00 for 12 hours
		Shift night = schedule.createShift("Night", "Night shift", LocalTime.of(19, 0, 0), Duration.ofHours(12));

		// night off shift
		OffShift nightOff = day.createOffShift();

		// 7 days ON, 7 OFF
		ShiftRotation dayRotation = new ShiftRotation();
		dayRotation.on(7, day).off(7, dayOff);

		// 7 nights ON, 7 OFF
		ShiftRotation nightRotation = new ShiftRotation();
		nightRotation.on(7, night).off(7, nightOff);

		Team A = schedule.createTeam("A", "A day shift", dayRotation, LocalDate.of(2014, 1, 2));
		Team B = schedule.createTeam("B", "B night shift", nightRotation, LocalDate.of(2014, 1, 2));
		Team C = schedule.createTeam("C", "C day shift", dayRotation, LocalDate.of(2014, 1, 9));
		Team D = schedule.createTeam("D", "D night shift", nightRotation, LocalDate.of(2014, 1, 9));

		printWorkSchedule(schedule);
		printTeams(schedule.getTeams());
		printShiftInstances(schedule, LocalDate.of(2014, 2, 1));
	}

	@Test
	public void testGenericShift() throws Exception {
		// regular work week with holidays and breaks
		WorkSchedule schedule = new WorkSchedule("Regular 40 hour work week", "8 to 5");

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
				LocalDateTime.of(2016, 12, 25, 0, 0, 0), Duration.ofHours(168));

		// shift
		Shift weekDay = schedule.createShift("regular", "A regular shift", LocalTime.of(8, 0, 0), Duration.ofHours(9));

		// breaks
		weekDay.createBreak("10AM", "10 am break", LocalTime.of(10, 0, 0), Duration.ofMinutes(15));
		weekDay.createBreak("LUNCH", "lunch", LocalTime.of(12, 0, 0), Duration.ofHours(1));
		weekDay.createBreak("2PM", "2 pm break", LocalTime.of(14, 0, 0), Duration.ofMinutes(15));

		OffShift weekEnd = weekDay.createOffShift();

		// 5 days ON, 2 OFF
		ShiftRotation rotation = new ShiftRotation();
		rotation.on(5, weekDay).off(2, weekEnd);

		LocalDate referenceDate = LocalDate.of(2016, 1, 1);
		Team company = schedule.createTeam("Company", "Scheduled working days", rotation, referenceDate);

		printWorkSchedule(schedule);
		printTeams(schedule.getTeams());
		printShiftInstances(schedule, referenceDate);
	}

	public static void main(String[] args) throws Exception {
		TestWorkSchedule test = new TestWorkSchedule();
		test.testFirefighterShifts1();
		test.testFirefighterShifts2();
		test.testFirefighterShifts3();
		test.testManufacturingShifts();
		test.testNursingICUShifts();
		test.testPostalServiceShifts();
		test.testGenericShift();

	}
}

package org.point85.workschedule.test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoField;
import java.util.Map;
import java.util.Map.Entry;

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
		WorkSchedule workSchedule = new WorkSchedule("Nursing ICU",
				"Two 12 hr back-to-back shifts, rotating every 14 days");

		// day shift, starts at 06:00 for 12 hours
		Shift day = workSchedule.createShift("Day", "Day shift", LocalTime.of(6, 0, 0),
				Duration.ofHours(12));
		
		OffShift dayOff = day.createOffShift();
		
		// night shift, starts at 18:00 for 12 hours
		Shift night = workSchedule.createShift("Night", "Night shift", LocalTime.of(18, 0, 0),
				Duration.ofHours(12));
		
		OffShift nightOff = night.createOffShift();
		
		// day rotation
		ShiftRotation dayRotation = new ShiftRotation();
		dayRotation.on(day, 3);
		dayRotation.off(dayOff, 4);
		dayRotation.on(day, 4);
		dayRotation.off(dayOff, 3);
		
		// inverse day rotation
		ShiftRotation inverseDayRotation = new ShiftRotation();
		inverseDayRotation.off(dayOff, 3);
		inverseDayRotation.on(day, 4);
		inverseDayRotation.off(dayOff, 4);
		inverseDayRotation.on(day, 3);
		
		// night rotation
		ShiftRotation nightRotation = new ShiftRotation();
		nightRotation.on(night, 4);
		nightRotation.off(nightOff, 3);
		nightRotation.on(night, 3);
		nightRotation.off(nightOff, 4);
		
		// inverse night rotation
		ShiftRotation inverseNightRotation = new ShiftRotation();
		inverseNightRotation.off(nightOff, 4);
		inverseNightRotation.on(night, 3);
		inverseNightRotation.off(nightOff, 3);
		inverseNightRotation.on(night, 4);
		
		Team A = workSchedule.createTeam("A", "Day shift", dayRotation, LocalDate.of(2014, 1, 6));
		Team B = workSchedule.createTeam("B", "Day inverse shift", inverseDayRotation, LocalDate.of(2014, 1, 6));
		Team C = workSchedule.createTeam("C", "Night shift", nightRotation, LocalDate.of(2014, 1, 6));
		Team D = workSchedule.createTeam("D", "Night inverse shift", inverseNightRotation, LocalDate.of(2014, 1, 6));
		
		printWorkSchedule(workSchedule);
		printTeams(workSchedule.getTeams());
		printShiftInstances(workSchedule, LocalDate.of(2014, 2, 1)); 
		

		/*
		// 3 days ON, 4 OFF, 4 ON, 3 OFF
		TimePeriod offShiftA = workSchedule.createOffShift("A off", "Off A Shift", LocalTime.of(18, 0, 0), Duration.ofHours(12));
		ShiftRotation rotationA = teamA.createRotation(LocalDate.of(2014, 1, 6));
		rotationA.addOn(3);
		rotationA.addOff(4);
		rotationA.addOn(4);
		rotationA.addOff(3);



		// 4 days ON, 3 OFF, 3 ON, 4 OFF
		ShiftRotation rotationB = night.createRotation(LocalDate.of(2014, 1, 6));
		rotationB.addOn(4);
		rotationB.addOff(3);
		rotationB.addOn(3);
		rotationB.addOff(4);

		int Y = 2014;
		int M = 2;
		int D = 1;
		LocalDate from = LocalDate.of(Y, M, D);
		LocalDate to = from.plusDays(14);
		workSchedule.printShifts(from, to);
		showWorkingTime(workSchedule, from, to);
		*/
	}

	@Test
	public void testPostalServiceShifts() {
		// United States Postal Service
		WorkSchedule workSchedule = new WorkSchedule("USPS", "Six 9 hr shifts, rotating every 42 days");

		// A shift, start at 08:00 for 9 hours
		Shift shiftA = workSchedule.createShift("A", "A shift", LocalTime.of(8, 0, 0),
				Duration.ofHours(9));

		// 3 days ON, 7 OFF, 1 ON, 7 OFF, 1 ON, 7 OFF, 1 ON, 7 OFF, 1 ON, 7 OFF
		ShiftRotation rotationA = shiftA.createRotation(LocalDate.of(2014, 1, 31));
		rotationA.addOn(3);
		rotationA.addOff(7);
		rotationA.addOn(1);
		rotationA.addOff(7);
		rotationA.addOn(1);
		rotationA.addOff(7);
		rotationA.addOn(1);
		rotationA.addOff(7);
		rotationA.addOn(1);
		rotationA.addOff(7);

		// B shift, start at 08:00 for 9 hours
		Shift shiftB = workSchedule.createShift("B", "B shift", LocalTime.of(8, 0, 0),
				Duration.ofHours(9));

		// 3 days ON, 7 OFF, 1 ON, 7 OFF, 1 ON, 7 OFF, 1 ON, 7 OFF, 1 ON, 7 OFF
		ShiftRotation rotationB = shiftB.createRotation(LocalDate.of(2014, 1, 24));
		rotationB.addOn(3);
		rotationB.addOff(7);
		rotationB.addOn(1);
		rotationB.addOff(7);
		rotationB.addOn(1);
		rotationB.addOff(7);
		rotationB.addOn(1);
		rotationB.addOff(7);
		rotationB.addOn(1);
		rotationB.addOff(7);

		// C shift, start at 08:00 for 9 hours
		Shift shiftC = workSchedule.createShift("C", "C shift", LocalTime.of(8, 0, 0),
				Duration.ofHours(9));

		// 3 days ON, 7 OFF, 1 ON, 7 OFF, 1 ON, 7 OFF, 1 ON, 7 OFF, 1 ON, 7 OFF
		ShiftRotation rotationC = shiftC.createRotation(LocalDate.of(2014, 1, 17));
		rotationC.addOn(3);
		rotationC.addOff(7);
		rotationC.addOn(1);
		rotationC.addOff(7);
		rotationC.addOn(1);
		rotationC.addOff(7);
		rotationC.addOn(1);
		rotationC.addOff(7);
		rotationC.addOn(1);
		rotationC.addOff(7);

		// D shift, start at 08:00 for 9 hours
		Shift shiftD = workSchedule.createShift("D", "D shift", LocalTime.of(8, 0, 0),
				Duration.ofHours(9));

		// 3 days ON, 7 OFF, 1 ON, 7 OFF, 1 ON, 7 OFF, 1 ON, 7 OFF, 1 ON, 7 OFF
		ShiftRotation rotationD = shiftD.createRotation(LocalDate.of(2014, 1, 10));
		rotationD.addOn(3);
		rotationD.addOff(7);
		rotationD.addOn(1);
		rotationD.addOff(7);
		rotationD.addOn(1);
		rotationD.addOff(7);
		rotationD.addOn(1);
		rotationD.addOff(7);
		rotationD.addOn(1);
		rotationD.addOff(7);

		// E shift, start at 08:00 for 9 hours
		Shift shiftE = workSchedule.createShift("E", "E shift", LocalTime.of(8, 0, 0),
				Duration.ofHours(9));

		// 3 days ON, 7 OFF, 1 ON, 7 OFF, 1 ON, 7 OFF, 1 ON, 7 OFF, 1 ON, 7 OFF
		ShiftRotation rotationE = shiftE.createRotation(LocalDate.of(2014, 1, 3));
		rotationE.addOn(3);
		rotationE.addOff(7);
		rotationE.addOn(1);
		rotationE.addOff(7);
		rotationE.addOn(1);
		rotationE.addOff(7);
		rotationE.addOn(1);
		rotationE.addOff(7);
		rotationE.addOn(1);
		rotationE.addOff(7);

		// F shift, start at 08:00 for 9 hours
		Shift shiftF = workSchedule.createShift("F", "F shift", LocalTime.of(8, 0, 0),
				Duration.ofHours(9));

		// 3 days ON, 7 OFF, 1 ON, 7 OFF, 1 ON, 7 OFF, 1 ON, 7 OFF, 1 ON, 7 OFF
		ShiftRotation rotationF = shiftF.createRotation(LocalDate.of(2014, 2, 7));
		rotationF.addOn(3);
		rotationF.addOff(7);
		rotationF.addOn(1);
		rotationF.addOff(7);
		rotationF.addOn(1);
		rotationF.addOff(7);
		rotationF.addOn(1);
		rotationF.addOff(7);
		rotationF.addOn(1);
		rotationF.addOff(7);

		int Y = 2014;
		int M = 3;
		int D = 1;
		LocalDate from = LocalDate.of(Y, M, D);
		LocalDate to = from.plusDays(42);
		workSchedule.printShifts(from, to);
		showWorkingTime(workSchedule, from, to);
	}

	@Test
	public void testFirefighterShifts2() throws Exception {
		// Seattle, WA fire shifts
		WorkSchedule workSchedule = new WorkSchedule("Seattle", "Four 24 hour alternating shifts");

		// shift, start at 07:00 for 24 hours
		Shift shift = workSchedule.createShift("24 Hours", "24 hour shift", LocalTime.of(7, 0, 0),
				Duration.ofHours(24));
		
		// off shift
		OffShift offShift = shift.createOffShift();

		// 1 day ON, 4 OFF, 1 ON, 2 OFF
		ShiftRotation rotation = new ShiftRotation();
		rotation.on(shift, 1);
		rotation.off(offShift, 4);
		rotation.on(shift, 1);
		rotation.off(offShift, 2);
		
		Team teamA = workSchedule.createTeam("A", "A Shift", rotation, LocalDate.of(2014, 2, 2));
		Team teamB = workSchedule.createTeam("B", "B Shift", rotation, LocalDate.of(2014, 2, 4));
		Team teamC = workSchedule.createTeam("C", "C Shift", rotation, LocalDate.of(2014, 1, 31));
		Team teamD = workSchedule.createTeam("D", "D Shift", rotation, LocalDate.of(2014, 1, 29));

		printWorkSchedule(workSchedule);
		printTeams(workSchedule.getTeams());
		printShiftInstances(workSchedule, LocalDate.of(2016, 7, 1));
/*
		// B shift, start at 07:00 for 24 hours
		Shift shiftB = workSchedule.createShift("B", "B shift", LocalTime.of(7, 0, 0),
				Duration.ofHours(24));

		// 1 day ON, 4 OFF, 1 ON, 2 OFF
		ShiftRotation rotationB = shiftB.createRotation(LocalDate.of(2014, 2, 4));
		rotationB.addOn(1);
		rotationB.addOff(4);
		rotationB.addOn(1);
		rotationB.addOff(2);

		// C shift, start at 07:00 for 24 hours
		Shift shiftC = workSchedule.createShift("C", "C shift", LocalTime.of(7, 0, 0),
				Duration.ofHours(24));

		// 1 day ON, 4 OFF, 1 ON, 2 OFF
		ShiftRotation rotationC = shiftC.createRotation(LocalDate.of(2014, 1, 31));
		rotationC.addOn(1);
		rotationC.addOff(4);
		rotationC.addOn(1);
		rotationC.addOff(2);

		// D shift, start at 07:00 for 24 hours
		Shift shiftD = workSchedule.createShift("D", "D shift", LocalTime.of(7, 0, 0),
				Duration.ofHours(24));

		// 1 day ON, 4 OFF, 1 ON, 2 OFF
		ShiftRotation rotationD = shiftD.createRotation(LocalDate.of(2014, 1, 29));
		rotationD.addOn(1);
		rotationD.addOff(4);
		rotationD.addOn(1);
		rotationD.addOff(2);

		int Y = 2014;
		int M = 3;
		int D = 1;
		LocalDate from = LocalDate.of(Y, M, D);
		LocalDate to = from.plusDays(16);
		workSchedule.printShifts(from, to);
		showWorkingTime(workSchedule, from, to);
		*/
	}

	@Test
	public void testFirefighterShifts3() throws Exception {
		// Lansing, MI fire shifts
		WorkSchedule workSchedule = new WorkSchedule("Lansing MI R-1", "Three 24 hour shifts");

		// shift, start at 07:00 for 24 hours
		Shift shift = workSchedule.createShift("24 Hours", "24 hour shift", LocalTime.of(7, 0, 0),
				Duration.ofHours(24));
		
		// off shift
		OffShift offShift = shift.createOffShift();

		// 1 day ON, 1 OFF, 1 ON, 1 OFF, 1 ON, 1 OFF,
		ShiftRotation blackRotation = new ShiftRotation();
		blackRotation.on(shift, 1);
		blackRotation.off(offShift, 1);
		blackRotation.on(shift, 1);
		blackRotation.off(offShift, 1);
		blackRotation.on(shift, 1);
		blackRotation.off(offShift, 1);

		// 1 day ON, 1 OFF, 1 ON, 3 OFF
		ShiftRotation redRotation = new ShiftRotation();
		redRotation.on(shift, 1);
		redRotation.off(offShift, 1);
		redRotation.on(shift, 1);
		redRotation.off(offShift, 3);

		// 1 ON, 5 OFF,
		ShiftRotation fushiaRotation = new ShiftRotation();
		fushiaRotation.on(shift, 1);
		fushiaRotation.off(offShift, 5);

		Team team1 = workSchedule.createTeam("Red", "Red Shift", redRotation, LocalDate.of(2017, 1, 6));
		Team team2 = workSchedule.createTeam("Black", "Black Shift", blackRotation, LocalDate.of(2017, 1, 3));
		Team team3 = workSchedule.createTeam("Fushia", "Fushia Shift", fushiaRotation, LocalDate.of(2017, 1, 4));

		printWorkSchedule(workSchedule);
		printTeams(workSchedule.getTeams());
		printShiftInstances(workSchedule, LocalDate.of(2017, 2, 1)); 
	}

	@Test
	public void testFirefighterShifts1() throws Exception {
		// Kern Co, CA
		WorkSchedule workSchedule = new WorkSchedule("Kern Co.", "Three 24 hour alternating shifts");

		// shift, start 07:00 for 24 hours
		Shift shift = workSchedule.createShift("24 Hour", "24 hour shift", LocalTime.of(7, 0, 0),
				Duration.ofHours(24));
		
		// off shift
		OffShift offShift = shift.createOffShift();

		// 2 days ON, 2 OFF, 2 ON, 2 OFF, 2 ON, 8 OFF
		ShiftRotation rotation = new ShiftRotation();
		rotation.on(shift, 2);
		rotation.off(offShift, 2);
		rotation.on(shift, 2);
		rotation.off(offShift, 2);
		rotation.on(shift, 2);
		rotation.off(offShift, 8);
		
		Team team1 = workSchedule.createTeam("Red", "A Shift", rotation, LocalDate.of(2017, 1, 8));
		Team team2 = workSchedule.createTeam("Black", "B Shift", rotation, LocalDate.of(2017, 2, 1));
		Team team3 = workSchedule.createTeam("Green", "C Shift", rotation, LocalDate.of(2017, 1, 2));

		printWorkSchedule(workSchedule);
		printTeams(workSchedule.getTeams());
		printShiftInstances(workSchedule, LocalDate.of(2017, 2, 1));
	}

	@Test
	public void testManufacturingShifts() throws Exception {
		// manufacturing company
		WorkSchedule workSchedule = new WorkSchedule("Manufacturing Company - four twelves",
				"Four 12 hour alternating day/night shifts");
		
		// day shift, start at 07:00 for 12 hours
		Shift day = workSchedule.createShift("Day", "Day shift", LocalTime.of(7, 0, 0),
				Duration.ofHours(12));
		
		// day off shift
		OffShift dayOff = day.createOffShift();
		
		// night shift, start at 19:00 for 12 hours
		Shift night = workSchedule.createShift("Night", "Night shift", LocalTime.of(19, 0, 0),
				Duration.ofHours(12));
		
		// night off shift
		OffShift nightOff = day.createOffShift();
		
		// 7 days ON, 7 OFF
		ShiftRotation dayRotation = new ShiftRotation();
		dayRotation.on(day, 7);
		dayRotation.off(dayOff, 7);
		
		// 7 nights ON, 7 OFF
		ShiftRotation nightRotation = new ShiftRotation();
		nightRotation.on(night, 7);
		nightRotation.off(nightOff, 7);
		
		Team A = workSchedule.createTeam("A", "A day shift", dayRotation, LocalDate.of(2014, 1, 2));
		Team B = workSchedule.createTeam("B", "B night shift", nightRotation, LocalDate.of(2014, 1, 2));
		Team C = workSchedule.createTeam("C", "C day shift", dayRotation, LocalDate.of(2014, 1, 9));
		Team D = workSchedule.createTeam("D", "D night shift", nightRotation, LocalDate.of(2014, 1, 9));

		printWorkSchedule(workSchedule);
		printTeams(workSchedule.getTeams());
		printShiftInstances(workSchedule, LocalDate.of(2014, 2, 1)); 
	}

	@Test
	public void testRegularShift() {
		// regular work week
		WorkSchedule workSchedule = new WorkSchedule("Regular 40 hour work week", "8 to 5");
		workSchedule.createNonWorkingPeriod("NEW YEARS", "New Years day", LocalDateTime.of(2016, 1, 1, 0, 0, 0),
				Duration.ofHours(24));
		workSchedule.createNonWorkingPeriod("MEMORIAL DAY", "Memorial day", LocalDateTime.of(2016, 5, 30, 0, 0, 0),
				Duration.ofHours(24));
		workSchedule.createNonWorkingPeriod("INDEPENDENCE DAY", "Independence day",
				LocalDateTime.of(2016, 7, 4, 0, 0, 0), Duration.ofHours(24));
		workSchedule.createNonWorkingPeriod("LABOR DAY", "Labor day", LocalDateTime.of(2016, 9, 5, 0, 0, 0),
				Duration.ofHours(24));
		workSchedule.createNonWorkingPeriod("THANKSGIVING", "Thanksgiving day and day after",
				LocalDateTime.of(2016, 11, 24, 0, 0, 0), Duration.ofHours(48));
		workSchedule.createNonWorkingPeriod("CHRISTMAS", "Christmas and day after",
				LocalDateTime.of(2016, 12, 26, 0, 0, 0), Duration.ofHours(48));

		Shift regular = workSchedule.createShift("regular", "A regular shift",
				LocalTime.of(8, 0, 0), Duration.ofHours(9));
		regular.createBreak("LUNCH", "lunch", LocalTime.of(12, 0, 0), Duration.ofHours(1));

		// 5 days ON, 2 OFF
		ShiftRotation rotation = regular.createRotation(LocalDate.of(2016, 1, 1));
		rotation.addOn(5);
		rotation.addOff(2);

		int Y = 2015;
		int M = 1;
		int D = 1;

		LocalDate from = LocalDate.of(Y, M, D);
		LocalDate to = from.plusDays(7);
		workSchedule.printShifts(from, to);

		showWorkingTime(workSchedule, from, to);
	}	

	public static void main(String[] args) throws Exception {
		TestWorkSchedule test = new TestWorkSchedule();
		//test.testFirefighterShifts1();
		//test.testFirefighterShifts2();
		//test.testFirefighterShifts3();
		//test.testManufacturingShifts();
		test.testNursingICUShifts();
		//test.testPostalServiceShifts();
		//test.testRegularShift();
		
	}
}

package org.point85.workschedule.test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoField;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;
import org.point85.workschedule.RotationDefinition;
import org.point85.workschedule.ShiftDefinition;
import org.point85.workschedule.WorkSchedule;

public class TestWorkSchedule {

	@Test
	public void testNursingShifts() {
		// ER nursing schedule
		WorkSchedule workSchedule = WorkSchedule.instance("Nursing ICU",
				"Two 12 hr back-to-back shifts, rotating every 14 days");

		// A shift, starts at 06:00 for 12 hours
		ShiftDefinition shiftA = workSchedule.createShiftDefinition("A", "A shift", LocalTime.of(6, 0, 0),
				Duration.ofHours(12));

		// 3 days ON, 4 OFF, 4 ON, 3 OFF
		RotationDefinition rotationA = shiftA.createRotation(LocalDate.of(2014, 1, 6));
		rotationA.addOn(3);
		rotationA.addOff(4);
		rotationA.addOn(4);
		rotationA.addOff(3);

		// B shift, starts at 18:00 for 12 hours
		ShiftDefinition shiftB = workSchedule.createShiftDefinition("B", "B shift", LocalTime.of(18, 0, 0),
				Duration.ofHours(12));

		// 4 days ON, 3 OFF, 3 ON, 4 OFF
		RotationDefinition rotationB = shiftB.createRotation(LocalDate.of(2014, 1, 6));
		rotationB.addOn(4);
		rotationB.addOff(3);
		rotationB.addOn(3);
		rotationB.addOff(4);

		int Y = 2014;
		int M = 2;
		int D = 1;
		LocalDate from = LocalDate.of(Y, M, D);
		LocalDate to = from.plusDays(14);
		workSchedule.showShifts(from, to);
		showWorkingTime(workSchedule, from, to);
	}

	@Test
	public void testPostalServiceShifts() {
		// United States Postal Service
		WorkSchedule workSchedule = WorkSchedule.instance("USPS", "Six 9 hr shifts, rotating every 42 days");

		// A shift, start at 08:00 for 9 hours
		ShiftDefinition shiftA = workSchedule.createShiftDefinition("A", "A shift", LocalTime.of(8, 0, 0),
				Duration.ofHours(9));

		// 3 days ON, 7 OFF, 1 ON, 7 OFF, 1 ON, 7 OFF, 1 ON, 7 OFF, 1 ON, 7 OFF
		RotationDefinition rotationA = shiftA.createRotation(LocalDate.of(2014, 1, 31));
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
		ShiftDefinition shiftB = workSchedule.createShiftDefinition("B", "B shift", LocalTime.of(8, 0, 0),
				Duration.ofHours(9));

		// 3 days ON, 7 OFF, 1 ON, 7 OFF, 1 ON, 7 OFF, 1 ON, 7 OFF, 1 ON, 7 OFF
		RotationDefinition rotationB = shiftB.createRotation(LocalDate.of(2014, 1, 24));
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
		ShiftDefinition shiftC = workSchedule.createShiftDefinition("C", "C shift", LocalTime.of(8, 0, 0),
				Duration.ofHours(9));

		// 3 days ON, 7 OFF, 1 ON, 7 OFF, 1 ON, 7 OFF, 1 ON, 7 OFF, 1 ON, 7 OFF
		RotationDefinition rotationC = shiftC.createRotation(LocalDate.of(2014, 1, 17));
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
		ShiftDefinition shiftD = workSchedule.createShiftDefinition("D", "D shift", LocalTime.of(8, 0, 0),
				Duration.ofHours(9));

		// 3 days ON, 7 OFF, 1 ON, 7 OFF, 1 ON, 7 OFF, 1 ON, 7 OFF, 1 ON, 7 OFF
		RotationDefinition rotationD = shiftD.createRotation(LocalDate.of(2014, 1, 10));
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
		ShiftDefinition shiftE = workSchedule.createShiftDefinition("E", "E shift", LocalTime.of(8, 0, 0),
				Duration.ofHours(9));

		// 3 days ON, 7 OFF, 1 ON, 7 OFF, 1 ON, 7 OFF, 1 ON, 7 OFF, 1 ON, 7 OFF
		RotationDefinition rotationE = shiftE.createRotation(LocalDate.of(2014, 1, 3));
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
		ShiftDefinition shiftF = workSchedule.createShiftDefinition("F", "F shift", LocalTime.of(8, 0, 0),
				Duration.ofHours(9));

		// 3 days ON, 7 OFF, 1 ON, 7 OFF, 1 ON, 7 OFF, 1 ON, 7 OFF, 1 ON, 7 OFF
		RotationDefinition rotationF = shiftF.createRotation(LocalDate.of(2014, 2, 7));
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
		workSchedule.showShifts(from, to);
		showWorkingTime(workSchedule, from, to);
	}

	@Test
	public void testFirefighterShifts2() {
		// Seattle, WA fire shifts
		WorkSchedule workSchedule = WorkSchedule.instance("Seattle", "Four 24 hour alternating shifts");

		// A shift, start at 07:00 for 24 hours
		ShiftDefinition shiftA = workSchedule.createShiftDefinition("A", "A shift", LocalTime.of(7, 0, 0),
				Duration.ofHours(24));

		// 1 day ON, 4 OFF, 1 ON, 2 OFF
		RotationDefinition rotationA = shiftA.createRotation(LocalDate.of(2014, 2, 2));
		rotationA.addOn(1);
		rotationA.addOff(4);
		rotationA.addOn(1);
		rotationA.addOff(2);

		// B shift, start at 07:00 for 24 hours
		ShiftDefinition shiftB = workSchedule.createShiftDefinition("B", "B shift", LocalTime.of(7, 0, 0),
				Duration.ofHours(24));

		// 1 day ON, 4 OFF, 1 ON, 2 OFF
		RotationDefinition rotationB = shiftB.createRotation(LocalDate.of(2014, 2, 4));
		rotationB.addOn(1);
		rotationB.addOff(4);
		rotationB.addOn(1);
		rotationB.addOff(2);

		// C shift, start at 07:00 for 24 hours
		ShiftDefinition shiftC = workSchedule.createShiftDefinition("C", "C shift", LocalTime.of(7, 0, 0),
				Duration.ofHours(24));

		// 1 day ON, 4 OFF, 1 ON, 2 OFF
		RotationDefinition rotationC = shiftC.createRotation(LocalDate.of(2014, 1, 31));
		rotationC.addOn(1);
		rotationC.addOff(4);
		rotationC.addOn(1);
		rotationC.addOff(2);

		// D shift, start at 07:00 for 24 hours
		ShiftDefinition shiftD = workSchedule.createShiftDefinition("D", "D shift", LocalTime.of(7, 0, 0),
				Duration.ofHours(24));

		// 1 day ON, 4 OFF, 1 ON, 2 OFF
		RotationDefinition rotationD = shiftD.createRotation(LocalDate.of(2014, 1, 29));
		rotationD.addOn(1);
		rotationD.addOff(4);
		rotationD.addOn(1);
		rotationD.addOff(2);

		int Y = 2014;
		int M = 3;
		int D = 1;
		LocalDate from = LocalDate.of(Y, M, D);
		LocalDate to = from.plusDays(16);
		workSchedule.showShifts(from, to);
		showWorkingTime(workSchedule, from, to);
	}

	@Test
	public void testFirefighterShifts3() {
		// Lansing, MI fire shifts
		WorkSchedule workSchedule = WorkSchedule.instance("Lansing MI", "Three 24 hour shifts");

		// Red shift, start at 07:00 for 24 hours
		ShiftDefinition shiftRed = workSchedule.createShiftDefinition("RED", "Red shift", LocalTime.of(7, 0, 0),
				Duration.ofHours(24));

		// 1 day ON, 1 OFF, 1 ON, 1 OFF, 1 ON, 1 OFF,
		RotationDefinition redRotation = shiftRed.createRotation(LocalDate.of(2013, 1, 1));
		redRotation.addOn(1);
		redRotation.addOff(1);
		redRotation.addOn(1);
		redRotation.addOff(1);
		redRotation.addOn(1);
		redRotation.addOff(1);

		// Black shift, start at 07:00 for 24 hours
		ShiftDefinition shiftBlack = workSchedule.createShiftDefinition("BLACK", "Black shift", LocalTime.of(7, 0, 0),
				Duration.ofHours(24));

		// 1 day ON, 1 OFF, 1 ON, 3 OFF
		RotationDefinition blackRotation = shiftBlack.createRotation(LocalDate.of(2013, 1, 8));
		blackRotation.addOn(1);
		blackRotation.addOff(1);
		blackRotation.addOn(1);
		blackRotation.addOff(3);

		// Green shift, start at 07:00 for 24 hours
		ShiftDefinition shiftGreen = workSchedule.createShiftDefinition("GREEN", "Green shift", LocalTime.of(7, 0, 0),
				Duration.ofHours(24));

		// 1 ON, 5 OFF,
		RotationDefinition greenRotation = shiftGreen.createRotation(LocalDate.of(2013, 1, 3));
		greenRotation.addOn(1);
		greenRotation.addOff(5);

		int Y = 2013;
		int M = 2;
		int D = 1;
		LocalDate from = LocalDate.of(Y, M, D);
		LocalDate to = from.plusDays(28);
		workSchedule.showShifts(from, to);
		showWorkingTime(workSchedule, from, to);
	}

	@Test
	public void testFirefighterShifts1() {
		// Kern Co, CA
		WorkSchedule workSchedule = WorkSchedule.instance("Kern Co.", "Three 24 hour alternating shifts");

		/*
		 * workSchedule.setDescription(); List<Boolean> rotation = new
		 * ArrayList<Boolean>(18);
		 * 
		 * // 2 ON, 2 OFF, 2 ON, 2 OFF, 2 ON, 8 OFF rotation.add(0, new
		 * Boolean(true)); rotation.add(1, new Boolean(true)); rotation.add(2,
		 * new Boolean(false)); rotation.add(3, new Boolean(false));
		 * rotation.add(4, new Boolean(true)); rotation.add(5, new
		 * Boolean(true)); rotation.add(6, new Boolean(false)); rotation.add(7,
		 * new Boolean(false)); rotation.add(8, new Boolean(true));
		 * rotation.add(9, new Boolean(true)); rotation.add(10, new
		 * Boolean(false)); rotation.add(11, new Boolean(false));
		 * rotation.add(12, new Boolean(false)); rotation.add(13, new
		 * Boolean(false)); rotation.add(14, new Boolean(false));
		 * rotation.add(15, new Boolean(false)); rotation.add(16, new
		 * Boolean(false)); rotation.add(17, new Boolean(false));
		 * 
		 * LocalDate rotationStartRed = ; LocalDate rotationStartBlack = ;
		 * LocalDate rotationStartGreen = ;
		 * 
		 * LocalTime am7 = ; Duration hours24 = Duration.ofHours(24);
		 */

		// Red shift, start 07:00 for 24 hours
		ShiftDefinition shiftRed = workSchedule.createShiftDefinition("RED", "Red shift", LocalTime.of(7, 0, 0),
				Duration.ofHours(24));

		// 2 days ON, 2 OFF, 2 ON, 2 OFF, 2 ON, 8 OFF
		RotationDefinition redRotation = shiftRed.createRotation(LocalDate.of(2014, 1, 6));
		redRotation.addOn(2);
		redRotation.addOff(2);
		redRotation.addOn(2);
		redRotation.addOff(2);
		redRotation.addOn(2);
		redRotation.addOff(8);

		// Black shift, start 07:00 for 24 hours
		ShiftDefinition shiftBlack = workSchedule.createShiftDefinition("BLACK", "Black shift", LocalTime.of(7, 0, 0),
				Duration.ofHours(24));

		// 2 days ON, 2 OFF, 2 ON, 2 OFF, 2 ON, 8 OFF
		RotationDefinition blackRotation = shiftBlack.createRotation(LocalDate.of(2014, 1, 12));
		blackRotation.addOn(2);
		blackRotation.addOff(2);
		blackRotation.addOn(2);
		blackRotation.addOff(2);
		blackRotation.addOn(2);
		blackRotation.addOff(8);

		// Green shift, start 07:00 for 24 hours
		ShiftDefinition shiftGreen = workSchedule.createShiftDefinition("GREEN", "Green shift", LocalTime.of(7, 0, 0),
				Duration.ofHours(24));

		// 2 days ON, 2 OFF, 2 ON, 2 OFF, 2 ON, 8 OFF
		RotationDefinition greenRotation = shiftGreen.createRotation(LocalDate.of(2014, 1, 18));
		greenRotation.addOn(2);
		greenRotation.addOff(2);
		greenRotation.addOn(2);
		greenRotation.addOff(2);
		greenRotation.addOn(2);
		greenRotation.addOff(8);

		int Y = 2014;
		int M = 2;
		int D = 1;

		LocalDate from = LocalDate.of(Y, M, D);
		LocalDate to = from.plusDays(18);
		workSchedule.showShifts(from, to);
		showWorkingTime(workSchedule, from, to);
	}

	@Test
	public void testManufacturingShifts() {
		// manufacturing company
		WorkSchedule workSchedule = WorkSchedule.instance("Manufacturer - four twelves",
				"Four 12 hour alternating day/night shifts");

		// first day shift, start at 07:00 for 12 hours
		ShiftDefinition shiftA = workSchedule.createShiftDefinition("A", "A day shift", LocalTime.of(7, 0, 0),
				Duration.ofHours(12));

		// 7 days ON, 7 OFF
		RotationDefinition rotationA = shiftA.createRotation(LocalDate.of(2014, 1, 2));
		rotationA.addOn(7);
		rotationA.addOff(7);

		// second day shift, start at 07:00 for 12 hours
		ShiftDefinition shiftC = workSchedule.createShiftDefinition("C", "C day shift", LocalTime.of(7, 0, 0),
				Duration.ofHours(12));

		// 7 days ON, 7 OFF
		RotationDefinition rotationC = shiftC.createRotation(LocalDate.of(2014, 1, 9));
		rotationC.addOn(7);
		rotationC.addOff(7);

		// first night shift, start at 19:00 for 12 hours
		ShiftDefinition shiftB = workSchedule.createShiftDefinition("B", "B night shift", LocalTime.of(19, 0, 0),
				Duration.ofHours(12));

		// 7 days ON, 7 OFF
		RotationDefinition rotationB = shiftB.createRotation(LocalDate.of(2014, 1, 2));
		rotationB.addOn(7);
		rotationB.addOff(7);

		// second night shift, start at 19:00 for 12 hours
		ShiftDefinition shiftD = workSchedule.createShiftDefinition("D", "D night shift", LocalTime.of(19, 0, 0),
				Duration.ofHours(12));

		// 7 days ON, 7 OFF
		RotationDefinition rotationD = shiftD.createRotation(LocalDate.of(2014, 1, 9));
		rotationD.addOn(7);
		rotationD.addOff(7);

		int Y = 2014;
		int M = 2;
		int D = 1;

		LocalDate from = LocalDate.of(Y, M, D);
		LocalDate to = from.plusDays(14);

		workSchedule.showShifts(from, to);
		showWorkingTime(workSchedule, from, to);
	}

	@Test
	public void testRegularShift() {
		// regular work week
		WorkSchedule workSchedule = WorkSchedule.instance("Regular 40 hour work week", "8 to 5");
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

		ShiftDefinition regular = workSchedule.createShiftDefinition("regular", "A regular shift",
				LocalTime.of(8, 0, 0), Duration.ofHours(9));
		regular.createBreak("LUNCH", "lunch", LocalTime.of(12, 0, 0), Duration.ofHours(1));

		// 5 days ON, 2 OFF
		RotationDefinition rotation = regular.createRotation(LocalDate.of(2016, 1, 1));
		rotation.addOn(5);
		rotation.addOff(2);

		int Y = 2015;
		int M = 1;
		int D = 1;

		LocalDate from = LocalDate.of(Y, M, D);
		LocalDate to = from.plusDays(7);
		workSchedule.showShifts(from, to);

		showWorkingTime(workSchedule, from, to);
	}

	private void showWorkingTime(WorkSchedule workSchedule, LocalDate from, LocalDate to) {
		Map<ShiftDefinition, Duration> workingTime = workSchedule.calculateWorkingTimeByShift(from, to);

		System.out.println("Working time from: " + from + " to: " + to);

		Duration t = Duration.ofSeconds(0);

		long dayFrom = from.getLong(ChronoField.EPOCH_DAY);
		long dayTo = to.getLong(ChronoField.EPOCH_DAY);
		long totalDays = dayTo - dayFrom;

		for (Entry<ShiftDefinition, Duration> entry : workingTime.entrySet()) {
			t = t.plus(entry.getValue());

			long totalHours = entry.getValue().toHours();
			float avg = (float) totalHours / ((float) totalDays / 7.0f);

			System.out.println("   Shift: " + entry.getKey().getName() + ", Time: " + entry.getValue().toString()
					+ ", Avg Hrs/Week: " + avg);
		}

		System.out.println("Total working hours: " + t.toHours() + " out of days available: " + totalDays);
	}
}

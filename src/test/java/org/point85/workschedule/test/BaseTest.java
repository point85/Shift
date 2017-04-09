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

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.junit.BeforeClass;
import org.point85.workschedule.NonWorkingPeriod;
import org.point85.workschedule.Rotation;
import org.point85.workschedule.Shift;
import org.point85.workschedule.ShiftInstance;
import org.point85.workschedule.Team;
import org.point85.workschedule.WorkSchedule;

/**
 * Base class for testing shift plans from
 * //community.bmscentral.com/learnss/Tutorials/SchedulePlans/
 * 
 * @author Kent
 *
 */
public abstract class BaseTest {
	public static final MathContext MATH_CONTEXT = MathContext.DECIMAL64;

	protected static final BigDecimal DELTA3 = new BigDecimal("0.001", MATH_CONTEXT);

	// reference date for start of shift rotations
	protected LocalDate referenceDate = LocalDate.of(2016, 10, 31);

	private static boolean testToString = false;

	@BeforeClass
	public static void setFlags() {
		testToString = true;
	}

	private void testShifts(WorkSchedule ws) throws Exception {
		assertTrue(ws.getShifts().size() > 0);

		for (Shift shift : ws.getShifts()) {
			Duration total = shift.getDuration();
			LocalTime start = shift.getStart();
			LocalTime end = shift.getEnd();

			assertTrue(shift.getName().length() > 0);
			assertTrue(shift.getDescription().length() > 0);

			assertTrue(total.toMinutes() > 0);
			assertTrue(shift.getBreaks() != null);
			assertTrue(start != null);
			assertTrue(end != null);

			Duration worked = null;
			boolean spansMidnight = shift.spansMidnight();
			if (spansMidnight) {
				// get the interval before midnight
				worked = shift.calculateWorkingTime(start, end, true);
			} else {
				worked = shift.calculateWorkingTime(start, end);
			}
			assertTrue(worked.equals(total));

			if (spansMidnight) {
				worked = shift.calculateWorkingTime(start, start, true);
			} else {
				worked = shift.calculateWorkingTime(start, start);
			}

			// 24 hour shift on midnight is a special case
			if (total.equals(Duration.ofHours(24))) {
				assertTrue(worked.toHours() == 24);
			} else {
				assertTrue(worked.toHours() == 0);
			}

			if (spansMidnight) {
				worked = shift.calculateWorkingTime(end, end, true);
			} else {
				worked = shift.calculateWorkingTime(end, end);
			}

			if (total.equals(Duration.ofHours(24))) {
				assertTrue(worked.toHours() == 24);
			} else {
				assertTrue(worked.toHours() == 0);
			}

			try {
				LocalTime t = start.minusMinutes(1);
				worked = shift.calculateWorkingTime(t, end);

				if (!total.equals(shift.getDuration())) {
					fail("Bad working time");
				}
			} catch (Exception e) {
			}

			try {
				LocalTime t = end.plusMinutes(1);
				worked = shift.calculateWorkingTime(start, t);
				if (!total.equals(shift.getDuration())) {
					fail("Bad working time");
				}
			} catch (Exception e) {
			}
		}
	}

	private void testTeams(WorkSchedule ws, Duration hoursPerRotation, Duration rotationDays) throws Exception {
		assertTrue(ws.getTeams().size() > 0);

		for (Team team : ws.getTeams()) {
			assertTrue(team.getName().length() > 0);
			assertTrue(team.getDescription().length() > 0);
			assertTrue(team.getDayInRotation(team.getRotationStart()) == 1);
			Duration hours = team.getRotation().getWorkingTime();
			assertTrue(hours.equals(hoursPerRotation));
			assertTrue(team.getPercentageWorked() > 0.0f);
			assertTrue(team.getRotationDuration().equals(rotationDays));
			assertTrue(team.getRotationStart() != null);

			Rotation rotation = team.getRotation();
			assertTrue(rotation.getDuration().equals(rotationDays));
			assertTrue(rotation.getPeriods().size() > 0);
			assertTrue(rotation.getWorkingTime().getSeconds() <= rotation.getDuration().getSeconds());
		}

		assertTrue(ws.getNonWorkingPeriods() != null);
	}

	private void testShiftInstances(WorkSchedule ws, LocalDate instanceReference) throws Exception {
		Rotation rotation = ws.getTeams().get(0).getRotation();

		// shift instances
		LocalDate startDate = instanceReference;
		LocalDate endDate = instanceReference.plusDays(rotation.getDuration().toDays());

		long days = endDate.toEpochDay() - instanceReference.toEpochDay() + 1;
		LocalDate day = startDate;

		for (long i = 0; i < days; i++) {
			List<ShiftInstance> instances = ws.getShiftInstancesForDay(day);

			for (ShiftInstance instance : instances) {
				assertTrue(instance.getStartTime().isBefore(instance.getEndTime()));
				assertTrue(instance.getShift() != null);
				assertTrue(instance.getTeam() != null);

				Shift shift = instance.getShift();
				LocalTime startTime = shift.getStart();
				LocalTime endTime = shift.getEnd();

				assertTrue(shift.isInShift(startTime));
				assertTrue(shift.isInShift(startTime.plusSeconds(1)));

				Duration shiftDuration = instance.getShift().getDuration();

				// midnight is special case
				if (!shiftDuration.equals(Duration.ofHours(24))) {
					assertFalse(shift.isInShift(startTime.minusSeconds(1)));
				}

				assertTrue(shift.isInShift(endTime));
				assertTrue(shift.isInShift(endTime.minusSeconds(1)));

				if (!shiftDuration.equals(Duration.ofHours(24))) {
					assertFalse(shift.isInShift(endTime.plusSeconds(1)));
				}

				LocalDateTime ldt = LocalDateTime.of(day, startTime);
				assertTrue(ws.getShiftInstancesForTime(ldt).size() > 0);

				ldt = LocalDateTime.of(day, startTime.plusSeconds(1));
				assertTrue(ws.getShiftInstancesForTime(ldt).size() > 0);

				ldt = LocalDateTime.of(day, startTime.minusSeconds(1));

				for (ShiftInstance si : ws.getShiftInstancesForTime(ldt)) {
					if (!shiftDuration.equals(Duration.ofHours(24))) {
						assertFalse(shift.getName().equals(si.getShift().getName()));
					}
				}

				ldt = LocalDateTime.of(day, endTime);
				assertTrue(ws.getShiftInstancesForTime(ldt).size() > 0);

				ldt = LocalDateTime.of(day, endTime.minusSeconds(1));
				assertTrue(ws.getShiftInstancesForTime(ldt).size() > 0);

				ldt = LocalDateTime.of(day, endTime.plusSeconds(1));

				for (ShiftInstance si : ws.getShiftInstancesForTime(ldt)) {
					if (!shiftDuration.equals(Duration.ofHours(24))) {
						assertFalse(shift.getName().equals(si.getShift().getName()));
					}
				}
			}

			day = day.plusDays(1);
		}

	}

	protected void runBaseTest(WorkSchedule ws, Duration hoursPerRotation, Duration rotationDays,
			LocalDate instanceReference) throws Exception {

		// toString
		if (testToString) {
			System.out.println(ws.toString());
			ws.printShiftInstances(instanceReference, instanceReference.plusDays(rotationDays.toDays()));
		}

		assertTrue(ws.getName().length() > 0);
		assertTrue(ws.getDescription().length() > 0);
		assertTrue(ws.getNonWorkingPeriods() != null);

		// shifts
		testShifts(ws);

		// teams
		testTeams(ws, hoursPerRotation, rotationDays);

		// shift instances
		testShiftInstances(ws, instanceReference);

		// team deletions
		Team[] teams = new Team[ws.getTeams().size()];
		ws.getTeams().toArray(teams);

		for (Team team : teams) {
			ws.deleteTeam(team);
		}
		assertTrue(ws.getTeams().size() == 0);

		// shift deletions
		Shift[] shifts = new Shift[ws.getShifts().size()];
		ws.getShifts().toArray(shifts);

		for (Shift shift : shifts) {
			ws.deleteShift(shift);
		}
		assertTrue(ws.getShifts().size() == 0);

		// non-working period deletions
		NonWorkingPeriod[] periods = new NonWorkingPeriod[ws.getNonWorkingPeriods().size()];
		ws.getNonWorkingPeriods().toArray(periods);

		for (NonWorkingPeriod period : periods) {
			ws.deleteNonWorkingPeriod(period);
		}
		assertTrue(ws.getNonWorkingPeriods().size() == 0);

	}

}

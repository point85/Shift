package org.point85.workschedule.test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.Duration;
import java.time.LocalTime;

import org.point85.workschedule.Shift;
import org.point85.workschedule.ShiftRotation;
import org.point85.workschedule.Team;
import org.point85.workschedule.WorkSchedule;

abstract class BaseTest {
	public static final MathContext MATH_CONTEXT = MathContext.DECIMAL64;
	
	protected static final BigDecimal DELTA3 = new BigDecimal("0.001", MATH_CONTEXT);
	
	protected void runBaseTest(WorkSchedule ws, Duration avgHours, Duration rotationDays) throws Exception {
				
		// shifts
		assertTrue(ws.getShifts().size() > 0);
		for (Shift shift : ws.getShifts()) {
			Duration total = shift.getDuration();
			LocalTime start = shift.getStart();
			LocalTime end = shift.getEnd();
			
			assertTrue(shift.getName().length() > 0);
			assertTrue(shift.getDescription().length() > 0);

			assertTrue(total.toMinutes() > 0);
			assertTrue(shift.getBreaks().size() >= 0);
			assertTrue(start!= null);
			assertTrue(end != null);
			
			Duration worked = shift.getWorkingTimeBetween(start, end);
			assertTrue(worked.equals(total));
			
			worked = shift.getWorkingTimeBetween(start, start);
			assertTrue(worked.toMinutes() == 0);
			
			worked = shift.getWorkingTimeBetween(end, end);
			assertTrue(worked.toMinutes() == 0);
			
			try {
				LocalTime t = start.minusMinutes(1);
				worked = shift.getWorkingTimeBetween(t, end);
				fail("Bad time");
			} catch (Exception e) {
			}
			
			try {
				LocalTime t = end.plusMinutes(1);
				worked = shift.getWorkingTimeBetween(start, t);
				fail("Bad time");
			} catch (Exception e) {
			}
		}
		
		// teams
		assertTrue(ws.getTeams().size() > 0);
		
		for (Team team : ws.getTeams()) {
			assertTrue(team.getName().length() > 0);
			assertTrue(team.getDescription().length() > 0);
			assertTrue(team.getDayInRotation(team.getRotationStart()) == 0);
			Duration hours = team.getHoursWorkedPerWeek();
			assertTrue(hours.equals(avgHours));
			assertTrue(team.getPercentageWorked() > 0.0f);
			assertTrue(team.getRotationDuration().equals(rotationDays));
			assertTrue(team.getRotationStart() != null);
			
			ShiftRotation rotation = team.getShiftRotation();
			assertTrue(rotation.getDuration().equals(rotationDays));
			assertTrue(rotation.getPeriods().size() > 0);
			assertTrue(rotation.getWorkingTime().getSeconds() <= rotation.getDuration().getSeconds());
		}
		
	}
	
}

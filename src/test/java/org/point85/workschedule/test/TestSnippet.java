package org.point85.workschedule.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.time.Duration;
import java.time.LocalTime;

import org.junit.Test;
import org.point85.workschedule.Shift;
import org.point85.workschedule.WorkSchedule;

public class TestSnippet {
	
	@Test
	public void testWorkingTime() throws Exception {
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
		
		
	}
}

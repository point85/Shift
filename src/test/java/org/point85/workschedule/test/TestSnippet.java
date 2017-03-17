package org.point85.workschedule.test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.Test;
import org.point85.workschedule.Rotation;
import org.point85.workschedule.Shift;
import org.point85.workschedule.Team;
import org.point85.workschedule.WorkSchedule;

public class TestSnippet {
	@Test
	public void testTeamWorkingTime() throws Exception {
		WorkSchedule schedule = new WorkSchedule("Team Working Time", "Test team working time");
		Duration shiftDuration = Duration.ofHours(12);
		LocalTime shiftStart = LocalTime.of(7, 0, 0);
		
		Shift shift = schedule.createShift("Team Shift", "Team shift", shiftStart, shiftDuration);

		Rotation rotation = new Rotation();
		rotation.on(1, shift).off(1, shift);

		LocalDate startRotation = LocalDate.of(2017, 1, 1);
		Team team = schedule.createTeam("Team", "Team", rotation, startRotation);
		team.setRotationStart(startRotation);
		
		// case #1
		LocalDateTime from = LocalDateTime.of(startRotation, shiftStart);
		LocalDateTime to  = from.plusDays(1);
		Duration time = team.calculateWorkingTime(from, to);
		assertTrue(time.equals(shiftDuration));
		
		// case #2
		to  = from.plusDays(2);
		time = team.calculateWorkingTime(from, to);
		assertTrue(time.equals(shiftDuration));
		
		// case #3
		to  = from.plusDays(3);
		time = team.calculateWorkingTime(from, to);
		assertTrue(time.equals(shiftDuration.plus(shiftDuration)));
		
		// case #4
		to  = from.plusDays(4);
		time = team.calculateWorkingTime(from, to);
		assertTrue(time.equals(shiftDuration.plus(shiftDuration)));
		
		// case #5
		from = LocalDateTime.of(startRotation, shiftStart.plusHours(6));
		to  = from.plusDays(1);
		time = team.calculateWorkingTime(from, to);
		Duration halfShift = Duration.ofHours(6);
		assertTrue(time.equals(halfShift));
		
		// case #6
		to  = from.plusDays(2);
		time = team.calculateWorkingTime(from, to);
		assertTrue(time.equals(halfShift));
		
		// case #7
		to  = from.plusDays(3);
		time = team.calculateWorkingTime(from, to);
		assertTrue(time.equals(shiftDuration));
		
		// case #8
		to  = from.plusDays(4);
		time = team.calculateWorkingTime(from, to);
		assertTrue(time.equals(shiftDuration));
	}

}

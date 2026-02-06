package org.point85.workschedule.test.library;

import static org.junit.Assert.assertTrue;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Locale;

import org.junit.After;
import org.junit.Test;
import org.point85.workschedule.Rotation;
import org.point85.workschedule.Shift;
import org.point85.workschedule.WorkSchedule;

public class TestSwedishLocale {

	@After
	public void cleanup() {
		WorkSchedule.clearLocale();
	}

	@Test
	public void testSwedishMessages() throws Exception {
		WorkSchedule.setLocale(new Locale("sv", "SE"));

		WorkSchedule ws = new WorkSchedule("TestSchema", "Testbeskrivning");
		Shift shift = ws.createShift("Dag", "Dagskift", LocalTime.of(7, 0), Duration.ofHours(8));
		Rotation rotation = ws.createRotation("Rotation1", "Testrotation");
		rotation.addSegment(shift, 5, 2);
		ws.createTeam("LagA", "F\u00f6rsta laget", rotation, LocalDate.of(2026, 1, 1));

		String output = ws.toString();

		// Verify Swedish labels appear in the output
		assertTrue("Should contain 'Schema'", output.contains("Schema"));
		assertTrue("Should contain 'Skift'", output.contains("Skift"));
		assertTrue("Should contain 'Lag'", output.contains("Lag"));
		assertTrue("Should contain 'Schemalagd arbetstid'", output.contains("Schemalagd arbetstid"));
		assertTrue("Should contain 'Rotationsstart'", output.contains("Rotationsstart"));
	}

	@Test
	public void testEnglishFallback() throws Exception {
		// No locale set - should use default (English)
		WorkSchedule.clearLocale();

		WorkSchedule ws = new WorkSchedule("TestSchedule", "Test description");
		Shift shift = ws.createShift("Day", "Day shift", LocalTime.of(7, 0), Duration.ofHours(8));
		Rotation rotation = ws.createRotation("Rotation1", "Test rotation");
		rotation.addSegment(shift, 5, 2);
		ws.createTeam("TeamA", "First team", rotation, LocalDate.of(2026, 1, 1));

		String output = ws.toString();

		// Verify English labels appear in the output
		assertTrue("Should contain 'Schedule'", output.contains("Schedule"));
		assertTrue("Should contain 'Shifts'", output.contains("Shift"));
		assertTrue("Should contain 'Teams'", output.contains("Team"));
	}

	@Test
	public void testLocaleSwitch() throws Exception {
		WorkSchedule ws = new WorkSchedule("TestSchedule", "Description");
		Shift shift = ws.createShift("Day", "Day shift", LocalTime.of(7, 0), Duration.ofHours(8));
		Rotation rotation = ws.createRotation("Rotation1", "Rotation");
		rotation.addSegment(shift, 5, 2);
		ws.createTeam("TeamA", "Team A", rotation, LocalDate.of(2026, 1, 1));

		// Start with Swedish
		WorkSchedule.setLocale(new Locale("sv", "SE"));
		String swedishOutput = ws.toString();
		assertTrue("Swedish output should contain 'Schema'", swedishOutput.contains("Schema"));

		// Switch to English
		WorkSchedule.clearLocale();
		String englishOutput = ws.toString();
		assertTrue("English output should contain 'Schedule'", englishOutput.contains("Schedule"));
	}
}

package org.point85.workschedule.test.library;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import org.junit.Test;
import org.point85.workschedule.dto.NonWorkingPeriodDto;
import org.point85.workschedule.service.SwedishHolidayService;

public class TestSwedishHolidays {

	private final SwedishHolidayService service = new SwedishHolidayService();

	@Test
	public void testEasterComputation() {
		// Known Easter dates
		assertEquals(LocalDate.of(2024, 3, 31), service.calculateEaster(2024));
		assertEquals(LocalDate.of(2025, 4, 20), service.calculateEaster(2025));
		assertEquals(LocalDate.of(2026, 4, 5), service.calculateEaster(2026));
		assertEquals(LocalDate.of(2027, 3, 28), service.calculateEaster(2027));
	}

	@Test
	public void testHolidayCount() {
		List<NonWorkingPeriodDto> holidays = service.getHolidays(2026);
		assertEquals("Should have 16 Swedish holidays", 16, holidays.size());
	}

	@Test
	public void testFixedHolidays2026() {
		List<NonWorkingPeriodDto> holidays = service.getHolidays(2026);

		assertContainsHoliday(holidays, "Ny\u00e5rsdagen", 2026, 1, 1);
		assertContainsHoliday(holidays, "Trettondedag jul", 2026, 1, 6);
		assertContainsHoliday(holidays, "F\u00f6rsta maj", 2026, 5, 1);
		assertContainsHoliday(holidays, "Sveriges nationaldag", 2026, 6, 6);
		assertContainsHoliday(holidays, "Julafton", 2026, 12, 24);
		assertContainsHoliday(holidays, "Juldagen", 2026, 12, 25);
		assertContainsHoliday(holidays, "Annandag jul", 2026, 12, 26);
		assertContainsHoliday(holidays, "Ny\u00e5rsafton", 2026, 12, 31);
	}

	@Test
	public void testEasterHolidays2026() {
		// Easter 2026 is April 5
		List<NonWorkingPeriodDto> holidays = service.getHolidays(2026);

		assertContainsHoliday(holidays, "L\u00e5ngfredagen", 2026, 4, 3);     // Good Friday
		assertContainsHoliday(holidays, "P\u00e5skafton", 2026, 4, 4);         // Easter Saturday
		assertContainsHoliday(holidays, "P\u00e5skdagen", 2026, 4, 5);         // Easter Sunday
		assertContainsHoliday(holidays, "Annandag p\u00e5sk", 2026, 4, 6);     // Easter Monday
		assertContainsHoliday(holidays, "Kristi himmelsf\u00e4rdsdag", 2026, 5, 14); // Ascension Day
	}

	@Test
	public void testMidsummerEveIsFriday() {
		for (int year = 2024; year <= 2030; year++) {
			List<NonWorkingPeriodDto> holidays = service.getHolidays(year);
			NonWorkingPeriodDto midsummer = holidays.stream()
					.filter(h -> h.getName().equals("Midsommarafton"))
					.findFirst()
					.orElseThrow(() -> new AssertionError("Midsommarafton not found for year " + year));

			LocalDate date = midsummer.getStartDateTime().toLocalDate();
			assertEquals("Midsommarafton should be a Friday in " + year,
					DayOfWeek.FRIDAY, date.getDayOfWeek());
			assertTrue("Midsommarafton should be June 19-25 in " + year,
					date.getDayOfMonth() >= 19 && date.getDayOfMonth() <= 25);
		}
	}

	@Test
	public void testAllSaintsDayIsSaturday() {
		for (int year = 2024; year <= 2030; year++) {
			List<NonWorkingPeriodDto> holidays = service.getHolidays(year);
			NonWorkingPeriodDto allSaints = holidays.stream()
					.filter(h -> h.getName().equals("Alla helgons dag"))
					.findFirst()
					.orElseThrow(() -> new AssertionError("Alla helgons dag not found for year " + year));

			LocalDate date = allSaints.getStartDateTime().toLocalDate();
			assertEquals("Alla helgons dag should be a Saturday in " + year,
					DayOfWeek.SATURDAY, date.getDayOfWeek());
		}
	}

	private void assertContainsHoliday(List<NonWorkingPeriodDto> holidays, String name, int year, int month,
			int day) {
		LocalDate expected = LocalDate.of(year, month, day);
		boolean found = holidays.stream()
				.anyMatch(h -> h.getName().equals(name) && h.getStartDateTime().toLocalDate().equals(expected));
		assertTrue("Holiday '" + name + "' should be on " + expected, found);
	}

	// Inner class to work around lambda requiring effectively final error messages
	private static class AssertionError extends RuntimeException {
		private static final long serialVersionUID = 1L;

		AssertionError(String message) {
			super(message);
		}
	}
}

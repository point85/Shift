package org.point85.workschedule.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

import org.point85.workschedule.dto.NonWorkingPeriodDto;
import org.springframework.stereotype.Service;

@Service
public class SwedishHolidayService {

	private static final String DURATION_24H = "PT24H";

	public List<NonWorkingPeriodDto> getHolidays(int year) {
		List<NonWorkingPeriodDto> holidays = new ArrayList<>();

		// Fixed holidays
		holidays.add(holiday("Ny\u00e5rsdagen", "New Year's Day", year, 1, 1));
		holidays.add(holiday("Trettondedag jul", "Epiphany", year, 1, 6));
		holidays.add(holiday("F\u00f6rsta maj", "May Day", year, 5, 1));
		holidays.add(holiday("Sveriges nationaldag", "National Day of Sweden", year, 6, 6));
		holidays.add(holiday("Julafton", "Christmas Eve", year, 12, 24));
		holidays.add(holiday("Juldagen", "Christmas Day", year, 12, 25));
		holidays.add(holiday("Annandag jul", "Boxing Day", year, 12, 26));
		holidays.add(holiday("Ny\u00e5rsafton", "New Year's Eve", year, 12, 31));

		// Easter-based movable holidays
		LocalDate easter = calculateEaster(year);
		holidays.add(holiday("L\u00e5ngfredagen", "Good Friday", easter.minusDays(2)));
		holidays.add(holiday("P\u00e5skafton", "Easter Saturday", easter.minusDays(1)));
		holidays.add(holiday("P\u00e5skdagen", "Easter Sunday", easter));
		holidays.add(holiday("Annandag p\u00e5sk", "Easter Monday", easter.plusDays(1)));
		holidays.add(holiday("Kristi himmelsf\u00e4rdsdag", "Ascension Day", easter.plusDays(39)));

		// Midsummer Eve: Friday between June 19-25
		LocalDate midsummerEve = LocalDate.of(year, 6, 19)
				.with(TemporalAdjusters.nextOrSame(DayOfWeek.FRIDAY));
		holidays.add(holiday("Midsommarafton", "Midsummer Eve", midsummerEve));
		holidays.add(holiday("Midsommardagen", "Midsummer Day", midsummerEve.plusDays(1)));

		// All Saints' Day: Saturday between Oct 31 - Nov 6
		LocalDate allSaints = LocalDate.of(year, 10, 31)
				.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY));
		holidays.add(holiday("Alla helgons dag", "All Saints' Day", allSaints));

		return holidays;
	}

	/**
	 * Calculate Easter Sunday using the Anonymous Gregorian algorithm (Computus).
	 */
	LocalDate calculateEaster(int year) {
		int a = year % 19;
		int b = year / 100;
		int c = year % 100;
		int d = b / 4;
		int e = b % 4;
		int f = (b + 8) / 25;
		int g = (b - f + 1) / 3;
		int h = (19 * a + b - d - g + 15) % 30;
		int i = c / 4;
		int k = c % 4;
		int l = (32 + 2 * e + 2 * i - h - k) % 7;
		int m = (a + 11 * h + 22 * l) / 451;
		int month = (h + l - 7 * m + 114) / 31;
		int day = ((h + l - 7 * m + 114) % 31) + 1;
		return LocalDate.of(year, month, day);
	}

	private NonWorkingPeriodDto holiday(String name, String description, int year, int month, int day) {
		return holiday(name, description, LocalDate.of(year, month, day));
	}

	private NonWorkingPeriodDto holiday(String name, String description, LocalDate date) {
		NonWorkingPeriodDto dto = new NonWorkingPeriodDto();
		dto.setName(name);
		dto.setDescription(description);
		dto.setStartDateTime(LocalDateTime.of(date, LocalTime.MIDNIGHT));
		dto.setDuration(DURATION_24H);
		return dto;
	}
}

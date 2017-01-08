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
package org.point85.workschedule;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Class WorkSchedule represents a groyup of teams who collectively work one or
 * more shifts.
 * 
 * @author Kent Randall
 *
 */
public class WorkSchedule extends Named {
	// name of resource bundle with translatable strings for exception messages
	private static final String MESSAGES_BUNDLE_NAME = "Message";

	// resource bundle for exception messages
	private static ResourceBundle messages = ResourceBundle.getBundle(MESSAGES_BUNDLE_NAME, Locale.getDefault());

	// list of teams
	private List<Team> teams = new ArrayList<>();

	// list of shifts
	private List<Shift> shifts = new ArrayList<>();

	// holidays and planned downtime
	private List<NonWorkingPeriod> nonWorkingPeriods = new ArrayList<>();

	/**
	 * Construct a work schedule
	 * 
	 * @param name
	 *            Schedule name
	 * @param description
	 *            Schedule description
	 */
	public WorkSchedule(String name, String description) {
		super(name, description);
	}

	// get a particular message by its key
	static String getMessage(String key) {
		return messages.getString(key);
	}

	private void addTeam(Team team) {
		if (!this.teams.contains(team)) {
			this.teams.add(team);
		}
	}

	/**
	 * Remove this team from the schedule
	 * 
	 * @param team
	 *            {@link Team}
	 */
	public void removeTeam(Team team) {
		if (this.teams.contains(team)) {
			this.teams.remove(team);
		}
	}

	/**
	 * Get all teams
	 * 
	 * @return List of {@link Team}
	 */
	public List<Team> getTeams() {
		return this.teams;
	}

	private void addNonWorkingPeriod(NonWorkingPeriod period) {
		if (!this.nonWorkingPeriods.contains(period)) {
			this.nonWorkingPeriods.add(period);
		}
	}

	/**
	 * Remove a non-working period from the schedule
	 * 
	 * @param period
	 *            {@link NonWorkingPeriod}
	 */
	public void removeNonWorkingPeriod(NonWorkingPeriod period) {
		if (this.nonWorkingPeriods.contains(period)) {
			this.nonWorkingPeriods.remove(period);
		}
	}

	/**
	 * Get all non-working periods in the schedule
	 * 
	 * @return List of {@link NonWorkingPeriod}
	 */
	public List<NonWorkingPeriod> getNonWorkingPeriods() {
		return this.nonWorkingPeriods;
	}

	/**
	 * Get the list of shift instances for the specified date
	 * 
	 * @param day
	 *            LocalDate
	 * @return List of {@link ShiftInstance}
	 * @throws Exception
	 */
	public List<ShiftInstance> getShiftInstancesForDay(LocalDate day) throws Exception {
		List<ShiftInstance> workingShifts = new ArrayList<>();

		// for each team see if there is a working shift
		for (Team team : teams) {
			ShiftRotation shiftRotation = team.getShiftRotation();
			int dayInRotation = team.getDayInRotation(day);

			// shift or off shift
			TimePeriod period = shiftRotation.getPeriods().get(dayInRotation);

			if (period.isWorkingPeriod()) {
				LocalDateTime startDateTime = LocalDateTime.of(day, period.getStart());
				ShiftInstance instance = new ShiftInstance((Shift) period, startDateTime, team);
				workingShifts.add(instance);
			}
		}

		Collections.sort(workingShifts);

		return workingShifts;
	}

	/**
	 * Create a team
	 * 
	 * @param name
	 *            Name of team
	 * @param description
	 *            Team description
	 * @param rotation
	 *            Shift rotation
	 * @param rotationStart
	 *            Start of rotation
	 * @return {@link Team}
	 */
	public Team createTeam(String name, String description, ShiftRotation rotation, LocalDate rotationStart) {
		Team team = new Team(name, description, rotation, rotationStart);
		this.addTeam(team);
		return team;
	}

	/**
	 * Create a shift
	 * 
	 * @param name
	 *            Name of shift
	 * @param description
	 *            Description of shift
	 * @param start
	 *            Shift start time of day
	 * @param duration
	 *            Shift duration
	 * @return {@link Shift}
	 */
	public Shift createShift(String name, String description, LocalTime start, Duration duration) {
		Shift shift = new Shift(name, description, start, duration);
		shifts.add(shift);
		return shift;
	}

	/**
	 * Create a non-working period of time
	 * 
	 * @param name
	 *            Name of period
	 * @param description
	 *            Description of period
	 * @param startDateTime
	 *            Starting date and time of day
	 * @param duration
	 *            Duration of period
	 * @return {@link NonWorkingPeriod}
	 */
	public NonWorkingPeriod createNonWorkingPeriod(String name, String description, LocalDateTime startDateTime,
			Duration duration) {
		NonWorkingPeriod period = new NonWorkingPeriod(name, description, startDateTime, duration);
		this.addNonWorkingPeriod(period);
		return period;
	}

	private Duration getRotationDuration() throws Exception {
		Duration sum = null;
		for (Team team : teams) {
			if (sum == null) {
				sum = team.getRotationDuration();
			} else {
				sum = sum.plus(team.getRotationDuration());
			}
		}
		return sum;
	}

	private Duration getWorkingTime() {
		Duration sum = null;
		for (Team team : teams) {
			if (sum == null) {
				sum = team.getShiftRotation().getWorkingTime();
			} else {
				sum = sum.plus(team.getShiftRotation().getWorkingTime());
			}
		}
		return sum;
	}

	/**
	 * Get the list of shifts in this schedule
	 * 
	 * @return List of {@link Shift}
	 */
	public List<Shift> getShifts() {
		return shifts;
	}

	/**
	 * Send shift instance output to a print stream
	 * 
	 * @param start
	 *            Starting date
	 * @param end
	 *            Ending date
	 * @param stream
	 *            Output stream
	 * @throws Exception
	 */
	public void printShiftInstances(LocalDate start, LocalDate end) throws Exception {
		if (start.isAfter(end)) {
			String msg = MessageFormat.format(WorkSchedule.getMessage("end.earlier.than.start"), start, end);
			throw new Exception(msg);
		}

		long days = end.toEpochDay() - start.toEpochDay() + 1;

		LocalDate day = start;

		System.out.println(getMessage("shifts.working"));
		for (long i = 0; i < days; i++) {
			System.out.println("[" + (i + 1) + "] " + getMessage("shifts.day") + ": " + day);

			List<ShiftInstance> instances = getShiftInstancesForDay(day);

			if (instances.size() == 0) {
				System.out.println("   " + getMessage("shifts.non.working"));
			} else {
				int count = 1;
				for (ShiftInstance instance : instances) {
					System.out.println("   (" + count + ")" + instance);
					count++;
				}
			}
			day = day.plusDays(1);
		}
	}

	/**
	 * Build a string value for the work schedule
	 * 
	 * @return String
	 */
	@Override
	public String toString() {
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(2);
		String sch = getMessage("schedule");
		String rd = getMessage("rotation.duration");
		String sw = getMessage("schedule.working");
		String sf = getMessage("schedule.shifts");
		String st = getMessage("schedule.teams");
		String sc = getMessage("schedule.coverage");
		String sn = getMessage("schedule.non");
		String stn = getMessage("schedule.total");

		String text = sch + ": " + super.toString();

		try {
			text += "\n" + rd + ": " + getRotationDuration() + ", " + sw + ": " + getWorkingTime();

			// shifts
			text += "\n" + sf + ": ";
			int count = 1;
			for (Shift shift : getShifts()) {
				text += "\n   (" + count + ") " + shift;
				count++;
			}

			// teams
			text += "\n" + st + ": ";
			count = 1;
			float teamPercent = 0.0f;
			for (Team team : this.getTeams()) {
				text += "\n   (" + count + ") " + team;
				teamPercent += team.getPercentageWorked();
				count++;
			}
			text += "\n" + sc + ": " + df.format(teamPercent) + "%";

			// non-working periods
			if (getNonWorkingPeriods().size() > 0) {
				text += "\n" + sn + ":";

				Duration totalMinutes = Duration.ofMinutes(0);

				count = 1;
				for (NonWorkingPeriod period : getNonWorkingPeriods()) {
					totalMinutes = totalMinutes.plusMinutes(period.getDuration().toMinutes());
					text += "\n   (" + count + ") " + period;
					count++;
				}
				text += "\n" + stn + ": " + totalMinutes;
			}

		} catch (Exception e) {
			text = e.getMessage();
		}
		return text;
	}
}

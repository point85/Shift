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
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Class WorkSchedule represents a named group of teams who collectively work
 * one or more shifts with off-shift periods. A work schedule can have periods
 * of non-working time.
 * 
 * @author Kent Randall
 *
 */
public class WorkSchedule extends Named implements Comparable<WorkSchedule> {
	// name of resource bundle with translatable strings for exception messages
	private static final String MESSAGES_BUNDLE_NAME = "WorkScheduleMessage";

	// resource bundle for exception messages
	private static final ResourceBundle messages = ResourceBundle.getBundle(MESSAGES_BUNDLE_NAME, Locale.getDefault());

	// cached UTC time zone for working time calculations
	private static final ZoneId ZONE_ID = ZoneId.of("Z");

	// list of teams
	private List<Team> teams = new ArrayList<>();

	// list of shifts
	private List<Shift> shifts = new ArrayList<>();

	// list of rotations
	private final List<Rotation> rotations = new ArrayList<>();

	// holidays and planned downtime
	private List<NonWorkingPeriod> nonWorkingPeriods = new ArrayList<>();

	// optimistic locking version
	private Integer version;

	/**
	 * Default constructor
	 */
	public WorkSchedule() {
		super();
	}

	/**
	 * Construct a work schedule
	 * 
	 * @param name        Schedule name
	 * @param description Schedule description
	 * @throws Exception exception
	 */
	public WorkSchedule(String name, String description) throws Exception {
		super(name, description);
	}

	// get a particular message by its key
	static String getMessage(String key) {
		return messages.getString(key);
	}

	/**
	 * Remove this team from the schedule
	 * 
	 * @param team {@link Team}
	 */
	public void deleteTeam(Team team) {
		if (teams.contains(team)) {
			teams.remove(team);
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

	/**
	 * Remove a non-working period from the schedule
	 * 
	 * @param period {@link NonWorkingPeriod}
	 */
	public void deleteNonWorkingPeriod(NonWorkingPeriod period) {
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
	 * Get the list of shift instances for the specified date that start in that
	 * date
	 * 
	 * @param day LocalDate
	 * @return List of {@link ShiftInstance}
	 * @throws Exception exception
	 */
	public List<ShiftInstance> getShiftInstancesForDay(LocalDate day) throws Exception {
		List<ShiftInstance> workingShifts = new ArrayList<>();

		// for each team see if there is a working shift
		for (Team team : teams) {
			ShiftInstance instance = team.getShiftInstanceForDay(day);

			if (instance == null) {
				continue;
			}

			// check to see if this is a non-working day
			boolean addShift = true;

			LocalDate startDate = instance.getStartTime().toLocalDate();

			for (NonWorkingPeriod nonWorkingPeriod : nonWorkingPeriods) {
				if (nonWorkingPeriod.isInPeriod(startDate)) {
					addShift = false;
					break;
				}
			}

			if (addShift) {
				workingShifts.add(instance);
			}
		}

		Collections.sort(workingShifts);

		return workingShifts;
	}

	/**
	 * Get the list of shift instances for the specified date and time of day
	 * 
	 * @param dateTime Date and time of day
	 * @return List of {@link ShiftInstance}
	 * @throws Exception exception
	 */
	public List<ShiftInstance> getShiftInstancesForTime(LocalDateTime dateTime) throws Exception {
		List<ShiftInstance> workingShifts = new ArrayList<>();

		// day
		List<ShiftInstance> candidateShifts = getShiftInstancesForDay(dateTime.toLocalDate());

		// check time now
		for (ShiftInstance instance : candidateShifts) {
			if (instance.getShift().isInShift(dateTime.toLocalTime())) {
				workingShifts.add(instance);
			}
		}

		return workingShifts;
	}

	/**
	 * Create a team
	 * 
	 * @param name          Name of team
	 * @param description   Team description
	 * @param rotation      Shift rotation
	 * @param rotationStart Start of rotation
	 * @return {@link Team}
	 * @throws Exception exception
	 */
	public Team createTeam(String name, String description, Rotation rotation, LocalDate rotationStart)
			throws Exception {
		Team team = new Team(name, description, rotation, rotationStart);

		if (teams.contains(team)) {
			String msg = MessageFormat.format(WorkSchedule.getMessage("team.already.exists"), name);
			throw new Exception(msg);
		}

		teams.add(team);
		team.setWorkSchedule(this);
		return team;
	}

	/**
	 * Create a shift
	 * 
	 * @param name        Name of shift
	 * @param description Description of shift
	 * @param start       Shift start time of day
	 * @param duration    Shift duration
	 * @return {@link Shift}
	 * @throws Exception exception
	 */
	public Shift createShift(String name, String description, LocalTime start, Duration duration) throws Exception {
		Shift shift = new Shift(name, description, start, duration);

		if (shifts.contains(shift)) {
			String msg = MessageFormat.format(WorkSchedule.getMessage("shift.already.exists"), name);
			throw new Exception(msg);
		}
		shifts.add(shift);
		shift.setWorkSchedule(this);
		return shift;
	}

	/**
	 * Create a rotation
	 * 
	 * @param name        Name of rotation
	 * @param description Description of rotation
	 * @return {@link Rotation}
	 * @throws Exception exception
	 */
	public Rotation createRotation(String name, String description) throws Exception {
		Rotation rotation = new Rotation(name, description);

		if (rotations.contains(rotation)) {
			String msg = MessageFormat.format(WorkSchedule.getMessage("rotation.already.exists"), name);
			throw new Exception(msg);
		}

		rotations.add(rotation);
		rotation.setWorkSchedule(this);
		return rotation;
	}

	/**
	 * Delete this shift
	 * 
	 * @param shift {@link Shift} to delete
	 * @throws Exception exception
	 */
	public void deleteShift(Shift shift) throws Exception {
		if (!shifts.contains(shift)) {
			return;
		}

		// can't be in use
		for (Shift inUseShift : shifts) {
			for (Team team : teams) {
				Rotation rotation = team.getRotation();

				for (TimePeriod period : rotation.getPeriods()) {
					if (period.equals(inUseShift)) {
						String msg = MessageFormat.format(WorkSchedule.getMessage("shift.in.use"), shift.getName());
						throw new Exception(msg);
					}
				}
			}
		}

		shifts.remove(shift);
	}

	/**
	 * Create a non-working period of time
	 * 
	 * @param name          Name of period
	 * @param description   Description of period
	 * @param startDateTime Starting date and time of day
	 * @param duration      Duration of period
	 * @return {@link NonWorkingPeriod}
	 * @throws Exception exception
	 */
	public NonWorkingPeriod createNonWorkingPeriod(String name, String description, LocalDateTime startDateTime,
			Duration duration) throws Exception {
		NonWorkingPeriod period = new NonWorkingPeriod(name, description, startDateTime, duration);

		if (nonWorkingPeriods.contains(period)) {
			String msg = MessageFormat.format(WorkSchedule.getMessage("nonworking.period.already.exists"), name);
			throw new Exception(msg);
		}
		period.setWorkSchedule(this);
		nonWorkingPeriods.add(period);

		Collections.sort(nonWorkingPeriods);

		return period;
	}

	/**
	 * Get total duration of rotation across all teams.
	 * 
	 * @return Duration of rotation
	 * @throws Exception Exception
	 */
	public Duration getRotationDuration() throws Exception {
		Duration sum = Duration.ZERO;

		for (Team team : teams) {
			sum = sum.plus(team.getRotationDuration());
		}
		return sum;
	}

	/**
	 * Get the total working time for all team rotations
	 * 
	 * @return Team rotation working time
	 */
	public Duration getRotationWorkingTime() {
		Duration sum = Duration.ZERO;

		for (Team team : teams) {
			sum = sum.plus(team.getRotation().getWorkingTime());
		}
		return sum;
	}

	/**
	 * Calculate the scheduled working time between the specified dates and times of
	 * day. Non-working periods are removed.
	 * 
	 * @param from Starting date and time
	 * @param to   Ending date and time
	 * @return Working time duration
	 * @throws Exception exception
	 */
	public Duration calculateWorkingTime(LocalDateTime from, LocalDateTime to) throws Exception {
		Duration sum = Duration.ZERO;

		// now add up scheduled time by team
		for (Team team : getTeams()) {
			sum = sum.plus(team.calculateWorkingTime(from, to));
		}

		// remove the non-working time
		Duration nonWorking = calculateNonWorkingTime(from, to);
		sum = sum.minus(nonWorking);

		// clip if negative
		if (sum.isNegative()) {
			sum = Duration.ZERO;
		}

		return sum;
	}

	/**
	 * Calculate the non-working time between the specified dates and times of day.
	 * 
	 * @param from Starting date and time
	 * @param to   Ending date and time
	 * @return Non-working time duration
	 * @throws Exception exception
	 */
	public Duration calculateNonWorkingTime(LocalDateTime from, LocalDateTime to) throws Exception {
		Duration sum = Duration.ZERO;

		long fromSeconds = from.atZone(ZONE_ID).toEpochSecond();
		long toSeconds = to.atZone(ZONE_ID).toEpochSecond();

		for (NonWorkingPeriod period : getNonWorkingPeriods()) {
			LocalDateTime start = period.getStartDateTime();
			long startSeconds = start.atZone(ZONE_ID).toEpochSecond();

			LocalDateTime end = period.getEndDateTime();
			long endSeconds = end.atZone(ZONE_ID).toEpochSecond();

			if (fromSeconds >= endSeconds) {
				// look at next period
				continue;
			}

			if (toSeconds <= startSeconds) {
				// done with periods
				break;
			}

			if (fromSeconds <= endSeconds) {
				// found a period, check edge conditions
				if (fromSeconds > startSeconds) {
					startSeconds = fromSeconds;
				}

				if (toSeconds < endSeconds) {
					endSeconds = toSeconds;
				}

				sum = sum.plusSeconds(endSeconds - startSeconds);
			}

			if (toSeconds <= endSeconds) {
				break;
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
	 * Get the list of rotations in this schedule
	 * 
	 * @return List of {@link Rotation}
	 */
	public List<Rotation> getRotations() {
		return rotations;
	}

	/**
	 * Print shift instances
	 * 
	 * @param start Starting date
	 * @param end   Ending date
	 * @throws Exception exception
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
			text += "\n" + rd + ": " + getRotationDuration() + ", " + sw + ": " + getRotationWorkingTime();

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
			List<NonWorkingPeriod> periods = getNonWorkingPeriods();

			if (periods.size() > 0) {
				text += "\n" + sn + ":";

				Duration totalMinutes = Duration.ZERO;

				count = 1;
				for (NonWorkingPeriod period : periods) {
					totalMinutes = totalMinutes.plusMinutes(period.getDuration().toMinutes());
					text += "\n   (" + count + ") " + period;
					count++;
				}
				text += "\n" + stn + ": " + totalMinutes;
			}
		} catch (Exception e) {
			// ignore
		}

		return text;
	}

	/**
	 * Get the optimistic locking version
	 * 
	 * @return version
	 */
	public Integer getVersion() {
		return version;
	}

	/**
	 * Set the optimistic locking version
	 * 
	 * @param version Version
	 */
	public void setVersion(Integer version) {
		this.version = version;
	}

	/**
	 * Compare one work schedule to another
	 */
	@Override
	public int compareTo(WorkSchedule other) {
		return getName().compareTo(other.getName());
	}
}

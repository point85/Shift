package org.point85.workschedule.persistency;

import org.point85.workschedule.persistence.PersistentWorkSchedule;
import org.point85.workschedule.test.TestWorkSchedule;

public class TestWorkSchedulePersistency extends TestWorkSchedule {

	public TestWorkSchedulePersistency() {
		testDeletions = false;
	}

	@Override
	public void testFirefighterShifts1() throws Exception {
		super.testFirefighterShifts1();
		PersistentWorkSchedule.getInstance().saveWorkSchedule(schedule);
	}

	@Override
	public void testFirefighterShifts2() throws Exception {
		super.testFirefighterShifts2();
		PersistentWorkSchedule.getInstance().saveWorkSchedule(schedule);
	}

	@Override
	public void testGenericShift() throws Exception {
		super.testGenericShift();
		PersistentWorkSchedule.getInstance().saveWorkSchedule(schedule);
	}

	@Override
	public void testManufacturingShifts() throws Exception {
		super.testManufacturingShifts();
		PersistentWorkSchedule.getInstance().saveWorkSchedule(schedule);
	}

	@Override
	public void testNonWorkingTime() throws Exception {
		super.testNonWorkingTime();
		PersistentWorkSchedule.getInstance().saveWorkSchedule(schedule);
	}

	@Override
	public void testNursingICUShifts() throws Exception {
		super.testNursingICUShifts();
		PersistentWorkSchedule.getInstance().saveWorkSchedule(schedule);
	}

	@Override
	public void testPostalServiceShifts() throws Exception {
		super.testPostalServiceShifts();
		PersistentWorkSchedule.getInstance().saveWorkSchedule(schedule);
	}

	@Override
	public void testShiftWorkingTime() throws Exception {
		super.testShiftWorkingTime();
		PersistentWorkSchedule.getInstance().saveWorkSchedule(schedule);
	}

	@Override
	public void testTeamWorkingTime() throws Exception {
		super.testTeamWorkingTime();
		PersistentWorkSchedule.getInstance().saveWorkSchedule(schedule);
	}

	@Override
	public void testTeamWorkingTime2() throws Exception {
		super.testTeamWorkingTime2();
		PersistentWorkSchedule.getInstance().saveWorkSchedule(schedule);
	}

	public static void main(String[] args) {
		TestWorkSchedulePersistency tester = new TestWorkSchedulePersistency();

		try {
			tester.testFirefighterShifts1();
			tester.testFirefighterShifts2();
			tester.testGenericShift();
			tester.testManufacturingShifts();
			tester.testNonWorkingTime();
			tester.testNursingICUShifts();
			tester.testPostalServiceShifts();
			tester.testShiftWorkingTime();
			tester.testTeamWorkingTime();
			tester.testTeamWorkingTime2();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

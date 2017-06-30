package org.point85.workschedule.persistency;

import org.point85.workschedule.persistence.PersistentWorkSchedule;
import org.point85.workschedule.test.TestWorkSchedule;

public class TestWorkSchedulePersistency extends TestWorkSchedule {
	private PersistentWorkSchedule pws = new PersistentWorkSchedule();

	public TestWorkSchedulePersistency() {
		testDeletions = false;
	}

	@Override
	public void testFirefighterShifts1() throws Exception {
		super.testFirefighterShifts1();
		pws.saveWorkSchedule(schedule);
	}

	@Override
	public void testFirefighterShifts2() throws Exception {
		super.testFirefighterShifts2();
		pws.saveWorkSchedule(schedule);
	}

	@Override
	public void testGenericShift() throws Exception {
		super.testGenericShift();
		pws.saveWorkSchedule(schedule);
	}

	@Override
	public void testManufacturingShifts() throws Exception {
		super.testManufacturingShifts();
		pws.saveWorkSchedule(schedule);
	}

	@Override
	public void testNonWorkingTime() throws Exception {
		super.testNonWorkingTime();
		pws.saveWorkSchedule(schedule);
	}

	@Override
	public void testNursingICUShifts() throws Exception {
		super.testNursingICUShifts();
		pws.saveWorkSchedule(schedule);
	}

	@Override
	public void testPostalServiceShifts() throws Exception {
		super.testPostalServiceShifts();
		pws.saveWorkSchedule(schedule);
	}

	@Override
	public void testShiftWorkingTime() throws Exception {
		super.testShiftWorkingTime();
		pws.saveWorkSchedule(schedule);
	}

	@Override
	public void testTeamWorkingTime() throws Exception {
		super.testTeamWorkingTime();
		pws.saveWorkSchedule(schedule);
	}

	@Override
	public void testTeamWorkingTime2() throws Exception {
		super.testTeamWorkingTime2();
		pws.saveWorkSchedule(schedule);
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

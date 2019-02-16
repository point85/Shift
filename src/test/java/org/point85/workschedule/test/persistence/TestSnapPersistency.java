package org.point85.workschedule.test.persistence;

import org.point85.workschedule.test.app.PersistentWorkSchedule;
import org.point85.workschedule.test.library.TestSnapSchedule;

public class TestSnapPersistency extends TestSnapSchedule {

	public TestSnapPersistency() {
		testDeletions = true;
	}

	@Override
	public void testPanama() throws Exception {
		super.testPanama();
		PersistentWorkSchedule.getInstance().saveWorkSchedule(schedule);
	}

	@Override
	public void testLowNight() throws Exception {
		super.testLowNight();
		PersistentWorkSchedule.getInstance().saveWorkSchedule(schedule);
	}

	@Override
	public void test3TeamFixed24() throws Exception {
		super.test3TeamFixed24();
		PersistentWorkSchedule.getInstance().saveWorkSchedule(schedule);
	}

	@Override
	public void test21TeamFixed() throws Exception {
		super.test21TeamFixed();
		PersistentWorkSchedule.getInstance().saveWorkSchedule(schedule);
	}

	@Override
	public void test549() throws Exception {
		super.test549();
		PersistentWorkSchedule.getInstance().saveWorkSchedule(schedule);
	}

	@Override
	public void test8Plus12() throws Exception {
		super.test8Plus12();
		PersistentWorkSchedule.getInstance().saveWorkSchedule(schedule);
	}

	@Override
	public void test9to5() throws Exception {
		super.test9to5();
		PersistentWorkSchedule.getInstance().saveWorkSchedule(schedule);
	}

	@Override
	public void testDNO() throws Exception {
		super.testDNO();
		PersistentWorkSchedule.getInstance().saveWorkSchedule(schedule);
	}

	@Override
	public void testDupont() throws Exception {
		super.testDupont();
		PersistentWorkSchedule.getInstance().saveWorkSchedule(schedule);
	}

	@Override
	public void testICUInterns() throws Exception {
		super.testICUInterns();
		PersistentWorkSchedule.getInstance().saveWorkSchedule(schedule);
	}

	@Override
	public void testTwoTeam() throws Exception {
		super.testTwoTeam();
		PersistentWorkSchedule.getInstance().saveWorkSchedule(schedule);
	}

	public static void main(String[] args) {
		TestSnapPersistency tester = new TestSnapPersistency();

		try {
			tester.test21TeamFixed();
			tester.test3TeamFixed24();
			tester.test549();
			tester.test8Plus12();
			tester.test9to5();
			tester.testDNO();
			tester.testDupont();
			tester.testICUInterns();
			tester.testLowNight();
			tester.testPanama();
			tester.testTwoTeam();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

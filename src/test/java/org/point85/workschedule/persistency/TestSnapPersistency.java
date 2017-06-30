package org.point85.workschedule.persistency;

import org.point85.workschedule.persistence.PersistentWorkSchedule;
import org.point85.workschedule.test.TestSnapSchedule;

public class TestSnapPersistency extends TestSnapSchedule {
	private PersistentWorkSchedule pws = new PersistentWorkSchedule();

	public TestSnapPersistency() {
		testDeletions = false;
	}

	@Override
	public void testPanama() throws Exception {
		super.testPanama();
		pws.saveWorkSchedule(schedule);
	}

	@Override
	public void testLowNight() throws Exception {
		super.testLowNight();
		pws.saveWorkSchedule(schedule);
	}

	@Override
	public void test3TeamFixed24() throws Exception {
		super.test3TeamFixed24();
		pws.saveWorkSchedule(schedule);
	}

	@Override
	public void test21TeamFixed() throws Exception {
		super.test21TeamFixed();
		pws.saveWorkSchedule(schedule);
	}

	@Override
	public void test549() throws Exception {
		super.test549();
		pws.saveWorkSchedule(schedule);
	}

	@Override
	public void test8Plus12() throws Exception {
		super.test8Plus12();
		pws.saveWorkSchedule(schedule);
	}

	@Override
	public void test9to5() throws Exception {
		super.test9to5();
		pws.saveWorkSchedule(schedule);
	}

	@Override
	public void testDNO() throws Exception {
		super.testDNO();
		pws.saveWorkSchedule(schedule);
	}

	@Override
	public void testDupont() throws Exception {
		super.testDupont();
		pws.saveWorkSchedule(schedule);
	}

	@Override
	public void testICUInterns() throws Exception {
		super.testICUInterns();
		pws.saveWorkSchedule(schedule);
	}

	@Override
	public void testTwoTeam() throws Exception {
		super.testTwoTeam();
		pws.saveWorkSchedule(schedule);
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

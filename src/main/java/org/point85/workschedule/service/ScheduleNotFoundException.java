package org.point85.workschedule.service;

public class ScheduleNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ScheduleNotFoundException(String message) {
		super(message);
	}
}

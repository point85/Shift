package org.point85.workschedule;

import java.time.LocalDateTime;

/**
 * This class provides information for adding or removing a team member for a
 * team working an instance of a shift. The shift instance is identified by its
 * starting date and time.
 */
public class TeamMemberException {
	// start date and time of day of the shift
	private LocalDateTime dateTime;

	// reason for the change
	private String reason;

	// team member to add
	private TeamMember addition;

	// team member to remove
	private TeamMember removal;

	/*
	 * Construct an exception for the shift instance at this starting date and time
	 */
	public TeamMemberException(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public TeamMember getAddition() {
		return addition;
	}

	public void setAddition(TeamMember addition) {
		this.addition = addition;
	}

	public TeamMember getRemoval() {
		return removal;
	}

	public void setRemoval(TeamMember removal) {
		this.removal = removal;
	}
}

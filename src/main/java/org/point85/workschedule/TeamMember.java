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

import java.util.Objects;

/**
 * Class TeamMember represents a person assigned to a Team
 * 
 * @author Kent Randall
 *
 */
public class TeamMember extends Named implements Comparable<TeamMember> {
	// ID, e.g. employee ID
	private String memberID;

	/**
	 * Default constructor
	 */
	public TeamMember() {
		super();
	}

	/**
	 * Constructor with id
	 * @param name Member name
	 * @param description Member description, e.g. title
	 * @param id Member ID, e.g. employee ID
	 * @throws Exception Exception
	 */
	public TeamMember(String name, String description, String id) throws Exception {
		super(name, description);
		this.setMemberID(id);
	}

	/**
	 * Compare one team member to another
	 */
	@Override
	public int compareTo(TeamMember other) {
		return this.getMemberID().compareTo(other.getMemberID());
	}

	/**
	 * Compare this member to another member
	 * 
	 * @return true if equal
	 */
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof TeamMember)) {
			return false;
		}
		TeamMember otherMember = (TeamMember) other;

		// same ID
		if (getMemberID() != null && otherMember.getMemberID() != null) {
			if (!getMemberID().equals(otherMember.getMemberID())) {
				return false;
			}
		}
		return super.equals(other);
	}

	/**
	 * Get the hash code
	 * 
	 * @return hash code
	 */
	@Override
	public int hashCode() {
		return Objects.hash(getName(), getMemberID());
	}

	public String getMemberID() {
		return memberID;
	}

	public void setMemberID(String memberID) {
		this.memberID = memberID;
	}

	/**
	 * Build a string value for this team member
	 */
	@Override
	public String toString() {
		String id = WorkSchedule.getMessage("member.id");

		String text = "";
		try {
			text = super.toString() + ", " + id + ": " + getMemberID();

		} catch (Exception e) {
			// ignore
		}
		return text;
	}
}

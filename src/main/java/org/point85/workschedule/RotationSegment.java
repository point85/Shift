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
 * This class represents part of an entire rotation. The segment starts with a
 * shift and includes a count of the number of days on followed by the number of
 * days off.
 * 
 * @author Kent Randall
 *
 */
public class RotationSegment implements Comparable<RotationSegment> {

	// parent rotation
	private Rotation rotation;

	// strict ordering
	private int sequence = 0;

	// shift that starts this segment
	private Shift startingShift;

	// number of days on
	private int daysOn = 0;

	// number of days off
	private int daysOff = 0;

	// primary key
	private Integer primaryKey;

	/**
	 * Constructor
	 */
	public RotationSegment() {

	}

	RotationSegment(Shift startingShift, int daysOn, int daysOff, Rotation rotation) {
		this.startingShift = startingShift;
		this.daysOn = daysOn;
		this.daysOff = daysOff;
		this.rotation = rotation;
	}

	/**
	 * Get the starting shift
	 * 
	 * @return {@link Shift}
	 */
	public Shift getStartingShift() {
		return startingShift;
	}

	/**
	 * Set the starting shift
	 * 
	 * @param startingShift {@link Shift}
	 */
	public void setStartingShift(Shift startingShift) {
		this.startingShift = startingShift;
	}

	/**
	 * Get the number of days on shift
	 * 
	 * @return Day count
	 */
	public int getDaysOn() {
		return daysOn;
	}

	/**
	 * Set the number of days on shift
	 * 
	 * @param daysOn Day count
	 */
	public void setDaysOn(int daysOn) {
		this.daysOn = daysOn;
	}

	/**
	 * Get the number of days off shift
	 * 
	 * @return Day count
	 */
	public int getDaysOff() {
		return daysOff;
	}

	/**
	 * Set the number of days off shift
	 * 
	 * @param daysOff Day count
	 */
	public void setDaysOff(int daysOff) {
		this.daysOff = daysOff;
	}

	/**
	 * Get the database record's primary key
	 * 
	 * @return Key
	 */
	public Integer getKey() {
		return primaryKey;
	}

	/**
	 * Set the database record's primary key
	 * 
	 * @param primaryKey Key
	 */
	public void setKey(Integer primaryKey) {
		this.primaryKey = primaryKey;
	}

	/**
	 * Get the rotation for this segment
	 * 
	 * @return {@link Rotation}
	 */
	public Rotation getRotation() {
		return rotation;
	}

	/**
	 * Get the sequence in the rotation
	 * 
	 * @return Sequence
	 */
	public int getSequence() {
		return sequence;
	}

	/**
	 * Get the sequence in the rotation
	 * 
	 * @param sequence Sequence
	 */
	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	/**
	 * Compare this rotation segment to another one.
	 * 
	 * @param other rotation segment
	 * @return -1 if less than, 0 if equal and 1 if greater than
	 */
	@Override
	public int compareTo(RotationSegment other) {
		int value = 0;
		if (this.getSequence() < other.getSequence()) {
			value = -1;
		} else if (this.getSequence() > other.getSequence()) {
			value = 1;
		}
		return value;
	}

	/**
	 * Compare this rotation segment to another rotation segment
	 * 
	 * @return true if equal
	 */
	@Override
	public boolean equals(Object other) {

		if (!(other instanceof RotationSegment)) {
			return false;
		}

		boolean rotationMatch = getRotation().getName().equals(((RotationSegment) other).getRotation().getName());
		boolean shiftMatch = getStartingShift().getName()
				.equals(((RotationSegment) other).getStartingShift().getName());
		boolean sequenceMatch = (getSequence() == ((RotationSegment) other).getSequence());

		return rotationMatch && shiftMatch && sequenceMatch;

	}

	/**
	 * Get the hash code
	 * 
	 * @return hash code
	 */
	@Override
	public int hashCode() {
		return Objects.hash(getRotation(), getStartingShift(), getSequence());
	}
}

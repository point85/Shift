package org.point85.workschedule;

abstract class Nameable {
	// name
	private String name;

	// description
	private String description;

	protected Nameable(String name, String description) {
		this.name = name;
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}

package org.point85.workschedule;

abstract class Named {
	// name
	protected String name;

	// description
	protected String description;

	protected Named(String name, String description) {
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
	
	@Override
	public String toString() {
		return name + " (" + description + ")";
	}
}

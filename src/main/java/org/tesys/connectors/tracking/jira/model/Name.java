package org.tesys.connectors.tracking.jira.model;

public class Name {
    @Override
    public String toString() {
	return "Assignee [name=" + name + "]";
    }

    private String name;

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }
}

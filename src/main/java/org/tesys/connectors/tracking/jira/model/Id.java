package org.tesys.connectors.tracking.jira.model;

public class Id {
    private String id;

    public String getId() {
	return id;
    }

    public void setId(String id) {
	this.id = id;
    }

    @Override
    public String toString() {
	return "Issuetype [id=" + id + "]";
    }
}

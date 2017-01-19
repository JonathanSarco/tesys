package org.tesys.connectors.tracking.jira.model;

public class JiraIssue {
    private String key;

    private Fields fields;

    public String getKey() {
	return key;
    }

    public void setKey(String key) {
	this.key = key;
    }

    public Fields getFields() {
	return fields;
    }

    public void setFields(Fields fields) {
	this.fields = fields;
    }

    @Override
    public String toString() {
	return "JiraIssue [key=" + key + ", fields=" + fields + "]";
    }
}

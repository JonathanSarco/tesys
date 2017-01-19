package org.tesys.connectors.tracking.jira.model;

import java.util.Arrays;

import org.tesys.core.project.tracking.Progress;

public class Fields {
    private Progress progress;

    private Key[] subtasks;

    private Id issuetype;

    private Id status;

    private String workratio;

    private Name assignee;

    private String resolutiondate;

    private String aggregatetimeestimate;

    private Name reporter;

    private String aggregatetimeoriginalestimate;

    private Key project;

    private String updated;

    private String created;

    private Id priority;
    
    private String[] labels ;

    public Progress getProgress() {
	return progress;
    }

    public void setProgress(Progress progress) {
	this.progress = progress;
    }

    public Key[] getSubtasks() {
	return subtasks;
    }

    public void setSubtasks(Key[] subtasks) {
	this.subtasks = subtasks;
    }

    public Id getIssuetype() {
	return issuetype;
    }

    public void setIssuetype(Id issuetype) {
	this.issuetype = issuetype;
    }

    public Id getStatus() {
	return status;
    }

    public void setStatus(Id status) {
	this.status = status;
    }

    public String getWorkratio() {
	return workratio;
    }

    public void setWorkratio(String workratio) {
	this.workratio = workratio;
    }

    public Name getAssignee() {
	return assignee;
    }

    public void setAssignee(Name assignee) {
	this.assignee = assignee;
    }

    public String getResolutiondate() {
	return resolutiondate;
    }

    public void setResolutiondate(String resolutiondate) {
	this.resolutiondate = resolutiondate;
    }

    public String getAggregatetimeestimate() {
	return aggregatetimeestimate;
    }

    public void setAggregatetimeestimate(String aggregatetimeestimate) {
	this.aggregatetimeestimate = aggregatetimeestimate;
    }

    public Name getReporter() {
	return reporter;
    }

    public void setReporter(Name reporter) {
	this.reporter = reporter;
    }

    public String getAggregatetimeoriginalestimate() {
	return aggregatetimeoriginalestimate;
    }

    public void setAggregatetimeoriginalestimate(
	    String aggregatetimeoriginalestimate) {
	this.aggregatetimeoriginalestimate = aggregatetimeoriginalestimate;
    }

    public Key getProject() {
	return project;
    }

    public void setProject(Key project) {
	this.project = project;
    }

    public String getUpdated() {
	return updated;
    }

    public void setUpdated(String updated) {
	this.updated = updated;
    }

    public String getCreated() {
	return created;
    }

    public void setCreated(String created) {
	this.created = created;
    }

    public Id getPriority() {
	return priority;
    }

    public void setPriority(Id priority) {
	this.priority = priority;
    }

    @Override
    public String toString() {
	return "Fields [progress=" + progress + ", subtasks="
		+ Arrays.toString(subtasks) + ", issuetype=" + issuetype
		+ ", status=" + status + ", workratio=" + workratio
		+ ", assignee=" + assignee + ", resolutiondate="
		+ resolutiondate + ", aggregatetimeestimate="
		+ aggregatetimeestimate + ", reporter=" + reporter
		+ ", aggregatetimeoriginalestimate="
		+ aggregatetimeoriginalestimate + ", project=" + project
		+ ", updated=" + updated + ", created=" + created
		+ ", priority=" + priority + "]";
    }

    public String[] getLabels() {
        return labels;
    }

    public void setLabels( String[] labels ) {
        this.labels = labels;
    }
}

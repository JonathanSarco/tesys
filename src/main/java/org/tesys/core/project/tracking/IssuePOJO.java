package org.tesys.core.project.tracking;

import java.util.Arrays;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class IssuePOJO implements IssueInterface {

    private String key;

    private Progress progress;

    private String[] subtasks;

    private String issuetype;

    private String status;

    private String workratio;

    private String assignee;

    private String resolutiondate;

    private String aggregatetimeestimate;

    private String reporter;

    private String aggregatetimeoriginalestimate;

    private String project;

    private String updated;

    private String created;

    private String priority;
    
    private String[] labels;

    public void setKey(String key) {
	this.key = key;
    }

    @Override
    public Progress getProgress() {
	return progress;
    }

    public void setProgress(Progress progress) {
	this.progress = progress;
    }

    @Override
    public String[] getSubtasks() {
	return subtasks;
    }

    public void setSubtasks(String[] subtasks) {
	this.subtasks = subtasks;
    }

    @Override
    public String getIssuetype() {
	return issuetype;
    }

    public void setIssuetype(String issuetype) {
	this.issuetype = issuetype;
    }

    @Override
    public String getStatus() {
	return status;
    }

    public void setStatus(String status) {
	this.status = status;
    }

    @Override
    public String getWorkratio() {
	return workratio;
    }

    public void setWorkratio(String workratio) {
	this.workratio = workratio;
    }

    @Override
    public String getAssignee() {
	return assignee;
    }

    public void setAssignee(String assignee) {
	this.assignee = assignee;
    }

    @Override
    public String getResolutiondate() {
	return resolutiondate;
    }

    public void setResolutiondate(String resolutiondate) {
	this.resolutiondate = resolutiondate;
    }

    @Override
    public String getAggregatetimeestimate() {
	return aggregatetimeestimate;
    }

    public void setAggregatetimeestimate(String aggregatetimeestimate) {
	this.aggregatetimeestimate = aggregatetimeestimate;
    }

    @Override
    public String getReporter() {
	return reporter;
    }

    public void setReporter(String reporter) {
	this.reporter = reporter;
    }

    @Override
    public String getAggregatetimeoriginalestimate() {
	return aggregatetimeoriginalestimate;
    }

    public void setAggregatetimeoriginalestimate(
	    String aggregatetimeoriginalestimate) {
	this.aggregatetimeoriginalestimate = aggregatetimeoriginalestimate;
    }

    @Override
    public String getProject() {
	return project;
    }

    public void setProject(String project) {
	this.project = project;
    }

    @Override
    public String getUpdated() {
	return updated;
    }

    public void setUpdated(String updated) {
	this.updated = updated;
    }

    @Override
    public String getCreated() {
	return created;
    }

    public void setCreated(String created) {
	this.created = created;
    }

    @Override
    public String getPriority() {
	return priority;
    }

    public void setPriority(String priority) {
	this.priority = priority;
    }

    @Override
    public String getKey() {
	return key;
    }

    @Override
    public String toString() {
	return "IssuePOJO [key=" + key + ", progress=" + progress
		+ ", subtasks=" + Arrays.toString(subtasks) + ", issuetype="
		+ issuetype + ", status=" + status + ", workratio=" + workratio
		+ ", assignee=" + assignee + ", resolutiondate="
		+ resolutiondate + ", aggregatetimeestimate="
		+ aggregatetimeestimate + ", reporter=" + reporter
		+ ", aggregatetimeoriginalestimate="
		+ aggregatetimeoriginalestimate + ", project=" + project
		+ ", updated=" + updated + ", created=" + created
		+ ", priority=" + priority + "]";
    }

    @Override
    public String[] getLabels() {
        return labels;
    }
    
    public void setLabels(String[] labels) {
        this.labels = labels;
    }


}

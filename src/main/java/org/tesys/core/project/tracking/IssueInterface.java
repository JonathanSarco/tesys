package org.tesys.core.project.tracking;

public interface IssueInterface {

    public String getKey();

    public String getResolutiondate();

    public String getAggregatetimeoriginalestimate();

    public String getUpdated();

    public String getCreated();

    public String getWorkratio();

    public String getAggregatetimeestimate();

    public String getReporter();

    public String[] getSubtasks();

    public Progress getProgress();

    public String getIssuetype();

    public String getStatus();

    public String getAssignee();

    public void setResolutiondate(String resolutiondate);

    public String getProject();

    public String getPriority();
    
    public String[] getLabels() ;
}

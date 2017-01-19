package org.tesys.connectors.tracking.jira;

import java.util.List;

import org.tesys.core.project.tracking.IssueInterface;
import org.tesys.core.project.tracking.IssueTypePOJO;
import org.tesys.core.project.tracking.User;

public interface JiraAdaptor {

    IssueInterface[] getIssues();

    User[] getUsers();

    IssueInterface getIssue(String key);

    User getUser(String key);

    List<String> getMetrics();

    List<IssueTypePOJO> getIssueTypes();
    
    boolean isIssueAssignedToUser( String issueKey , String userName );

}

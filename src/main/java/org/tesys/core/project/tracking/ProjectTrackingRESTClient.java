package org.tesys.core.project.tracking;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.core.GenericType;

import org.tesys.core.estructures.Metric;
import org.tesys.core.estructures.MetricFactory;
import org.tesys.util.RESTClient;

/**
 * Este ProjectTracking es un cliente REST para el Connector de un Project
 * Manager.
 * 
 * @author leandro
 * 
 */
public class ProjectTrackingRESTClient implements ProjectTracking {

    private RESTClient client;

    private static final Logger LOG = Logger
	    .getLogger(ProjectTrackingRESTClient.class.getName());

    // RESEARCH Discovery Resources from Connector
    private static String RESOURCE_ISSUES = "issues/";
    private static String RESOURCE_USERS = "users/";
    private static String RESOURCE_METRIC = "metric/";
    private static String RESOURCE_ISSUE_TYPE = "issues/types/";
    private static String RESOURCE_ASSIGNED = "assigned/";

    public ProjectTrackingRESTClient() {
	try {
	    client = new RESTClient(getConnectorLocation());
	} catch (MalformedURLException e) {
	    LOG.log(Level.SEVERE, e.toString(), e);
	}

    }

    public String getConnectorLocation() {
	// RESEARCH Discovery Services
	return "http://localhost:8080/tesys/rest/connectors/jira";
    }

    @Override
    public IssueInterface[] getIssues() {
	IssuePOJO[] issues = client.GET(RESOURCE_ISSUES).readEntity(
		IssuePOJO[].class);
	return issues;
    }

    @Override
    public User[] getUsers() {
	UserPOJO[] users = client.GET(RESOURCE_USERS).readEntity(
		UserPOJO[].class);
	return users;
    }

    @Override
    public boolean existUser(String key) {
	UserPOJO u = client.GET(RESOURCE_USERS + key)
		.readEntity(UserPOJO.class);
	return (u != null);
    }

    @Override
    public boolean existIssue(String key) {
	IssuePOJO i = client.GET(RESOURCE_ISSUES + key).readEntity(
		IssuePOJO.class);
	return (i != null);
    }

    @Override
    public IssueInterface getIssue(String key) {
	IssuePOJO i = client.GET(RESOURCE_ISSUES + key).readEntity(
		IssuePOJO.class);
	return i;
    }

    @Override
    public List<Metric> getMetrics() {
	try {
	    List<Metric> result = new ArrayList<Metric>();
	    List<String> metrics = client.GET(RESOURCE_METRIC).readEntity(
		    new GenericType<List<String>>() {
		    });
	    MetricFactory mf = new MetricFactory();
	    for (String m : metrics) {
		result.add(mf.getMetric(m));
	    }
	    return result;
	} catch (Exception e) {
	    LOG.log(Level.SEVERE, e.toString(), e);
	    return new ArrayList<Metric>();
	}
    }

    @Override
    public List<String> getIssuesKeys() {
	// TODO hacerlo en el Connector.
	IssueInterface[] issues = this.getIssues();
	List<String> keys = new ArrayList<String>();
	for (int i = 0; i < issues.length; i++) {
	    if (issues[i] != null && issues[i].getKey() != null) {
		keys.add(issues[i].getKey());
	    }
	}
	return keys;
    }

    @Override
    public List<IssueTypePOJO> getIssueTypes() {
	try {
	    return client.GET(RESOURCE_ISSUE_TYPE).readEntity(
		    new GenericType<List<IssueTypePOJO>>() {
		    });
	} catch (Exception e) {
	    LOG.log(Level.SEVERE, e.toString(), e);
	    return new ArrayList<IssueTypePOJO>();
	}

    }

    @Override
    public boolean isIssueAssignedToUser(String issueKey, String userName) {
	try {
	    return client.GET(RESOURCE_ASSIGNED + userName + "/" + issueKey ).readEntity(boolean.class);
	} catch (Exception e) {
	    LOG.log(Level.SEVERE, e.toString(), e);
	}
	return false;
    }
}

package org.tesys.connectors.tracking.jira;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.tesys.core.project.tracking.IssueTypePOJO;
import org.tesys.util.RESTClient;

import com.atlassian.jira.rest.client.JiraRestClient;
import com.atlassian.jira.rest.client.domain.IssueType;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;


public class JiraRESTClient {

    private static final String API = "rest/api/2/";

    private static final Logger LOG = Logger.getLogger(JiraRESTClient.class
	    .getName());

    // resources from Jira API
    private static final String RESOURCE_GROUP = "group";
    private static final String RESOURCE_SEARCH = "search";
    private static final String RESOURCE_USER = "user";
    // parameters for each resource
    // group
    private static final String RGROUP_GROUPNAME = "groupname";
    private static final String RGROUP_EXPAND = "expand";

    // search
    private static final String RSEARCH_QUERY = "jql";
    private static final String RSEARCH_START = "startAt";
    private static final String RSEARCH_MAX = "maxResults";

    // group
    private static final String RUSER_USERNAME = "username";

    private RESTClient client;
    private static URI jiraServerUri = URI
	    .create("http://ing.exa.unicen.edu.ar:8086/atlassian-jira-6.0/");
    // JRJA
    private JiraRestClient restClient;


    public JiraRESTClient(String url, String user, String pass) {
	try {
	    client = new RESTClient(url, user, pass);
	} catch (MalformedURLException e) {
	    LOG.log(Level.SEVERE, e.toString(), e);
	}
	try {

	    // URI jiraServerUri = URI.create(
	    // "http://ing.exa.unicen.edu.ar:8086/atlassian-jira-6.0/" );
	    final AsynchronousJiraRestClientFactory factory = new AsynchronousJiraRestClientFactory();
	    restClient = factory.createWithBasicHttpAuthentication(
		    jiraServerUri, "grodriguez", "654321");
	} catch (Exception e) {
	    LOG.log(Level.SEVERE, e.toString(), e);
	}
    }

    public String path(String resource) {
	return API + resource;
    }

    public String getUsers(String group, Integer min, Integer max) {
	// TODO validate params ?
	Map<String, String> params = new HashMap<String, String>();
	params.put(RGROUP_GROUPNAME, group);
	params.put(RGROUP_EXPAND,
		"users[" + min.toString() + ":" + max.toString() + "]");
	return client.GET(this.path(RESOURCE_GROUP), params).readEntity(
		String.class);
    }

    public String getUser(String userKey) {
	Map<String, String> params = new HashMap<String, String>();
	params.put(RUSER_USERNAME, userKey);
	return client.GET(this.path(RESOURCE_USER), params).readEntity(
		String.class);
    }
    

    /**
     * Consulta las Issues que cumplen con un determinado criterio, dicho
     * criterio esta definido en Java Query Language (JQL).
     * 
     * @param query
     *            Consulta a realizar en JQL
     * @return Lista de Basic Issues que satisfacen la consulta, un basic Issue
     *         tiene poca informacion del issue en si, pero tiene la Key.
     * 
     */
    public String getIssues(String query, Integer min, Integer max) {
	HashMap<String, String> params = new HashMap<String, String>();
	params.put(RSEARCH_QUERY, query);
	params.put(RSEARCH_START, min.toString());
	params.put(RSEARCH_MAX, max.toString());
	return client.GET(this.path(RESOURCE_SEARCH), params).readEntity(
		String.class);
    }

    /**
     * Retorna todos los issues de JIRA
     * 
     * @param min
     * @param max
     * @return
     */
    public String getIssues(Integer min, Integer max) {
	return getIssues("", min, max);
    }

    public List<IssueTypePOJO> getIssueTypes() {
	try {
	    Iterable<IssueType> response = restClient.getMetadataClient()
		    .getIssueTypes().claim();
	    List<IssueTypePOJO> issueTypes = new ArrayList<IssueTypePOJO>();
	    for (IssueType type : response) {
		issueTypes.add(new IssueTypePOJO(type.getId(), type.getName(),
			type.getDescription()));
	    }
	    return issueTypes;
	} catch (Exception e) {
	    return new ArrayList<IssueTypePOJO>();
	}

    }
    
    public boolean isIssueAssignedToUser( String issueKey, String userName ) {
	return userName.equals(restClient.getIssueClient().getIssue( issueKey ).claim().getAssignee().getName());
    }

}

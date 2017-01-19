package org.tesys.connectors.tracking.jira;

import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;
import javax.servlet.ServletContext;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.PathParam;

import org.tesys.core.project.tracking.IssueInterface;
import org.tesys.core.project.tracking.IssueTypePOJO;
import org.tesys.core.project.tracking.User;
import org.tesys.util.Strings;

/**
 * Web Service REST ofrecido por el conector de Jira. Tiene como objetivo daptar
 * la interface de Jira a un Project Tracking.
 * 
 * @author rulo
 * 
 */


@Path("/connectors/jira")
@Singleton
public class JiraConnector implements JiraAdaptor {
    private static String ISSUE_SCHEMA_PATH = "WEB-INF/jira-connector/issueSchema";
    private static String USER_SCHEMA_PATH = "WEB-INF/jira-connector/userSchema";

    private static String JIRA_CONNECTOR_PROPERTIES_FILE = "WEB-INF/jira-connector/jira-connector.properties";

    private static String PROP_URL = "host";
    private static String PROP_USER = "username";
    private static String PROP_PASS = "password";

    private static final Logger LOG = Logger.getLogger(JiraConnector.class
	    .getName());

    private JiraAdaptation jira;

    private String issueSchema;
    private String userSchema;

    private String URL;
    private String user;
    private String pass;

    @Context
    ServletContext servletContext;

    /**
     * Load URL, user and pass from properties file
     * 
     * @throws IOException
     */
    public void loadProperties() throws IOException {
	Properties prop = new Properties();
	prop.load(servletContext
		.getResourceAsStream(JIRA_CONNECTOR_PROPERTIES_FILE));
	URL = prop.getProperty(PROP_URL);
	user = prop.getProperty(PROP_USER);
	pass = prop.getProperty(PROP_PASS);
	issueSchema = Strings.convertStreamToString(servletContext
		.getResourceAsStream(ISSUE_SCHEMA_PATH));
	userSchema = Strings.convertStreamToString(servletContext
		.getResourceAsStream(USER_SCHEMA_PATH));
    }

    @PostConstruct
    public void Init() {

	try {
	    loadProperties();
	} catch (IOException e1) {
	    LOG.log(Level.SEVERE, e1.toString(), e1);
	}

	JiraRESTClient client = new JiraRESTClient(URL, user, pass);

	try {
	    jira = new JiraAdaptation(client, userSchema, issueSchema);
	} catch (IOException e) {
	    LOG.log(Level.SEVERE, e.toString(), e);
	}

    }

    @GET
    @Path("/issues/{issue}")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public IssueInterface getIssue(@PathParam("issue") String key) {
	try {
	    return jira.getIssue(key);
	} catch (Exception e) {
	    LOG.log(Level.SEVERE, e.toString(), e);
	    return null;
	}
    }

    @GET
    @Path("/users/{user}")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public User getUser(@PathParam("user") String name) {
	try {
	    return jira.getUser(name);
	} catch (IOException e) {
	    LOG.log(Level.SEVERE, e.toString(), e);
	    return null;
	}

    }

    @GET
    @Path("/users")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public User[] getUsers() {
	try {
	    return jira.getAllUsers();
	} catch (IOException e) {
	    LOG.log(Level.SEVERE, e.toString(), e);
	    return null;
	}
    }

    @GET
    @Path("/issues")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public IssueInterface[] getIssues() {
	try {
	    return jira.getAllIssues();
	} catch (IOException e) {
	    LOG.log(Level.SEVERE, e.toString(), e);
	    return null;
	}
    }

    @GET
    @Path("/metric")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public List<String> getMetrics() {
	return jira.getMetrics();
    }

    @GET
    @Path("/issues/types")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public List<IssueTypePOJO> getIssueTypes() {
	return jira.getIssueTypes();
    }
    
    
    @GET
    @Path("/assigned/{user}/{issue}")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public boolean isIssueAssignedToUser( @PathParam("issue") String issueKey, @PathParam("user") String userName ) {
	return jira.isIssueAssignedToUser(issueKey, userName);
    }
    

}

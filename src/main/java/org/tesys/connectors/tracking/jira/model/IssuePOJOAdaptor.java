package org.tesys.connectors.tracking.jira.model;

import org.tesys.core.project.tracking.IssuePOJO;

/**
 * Adapta un JiraIssue a un IssuePOJO.
 *  
 * Un JiraIssue sigue la estructura del JSON original del Issue 
 * que devuelve la API de Jira y el IssuePOJO es el que se usa 
 * en nuestro proyecto.
 * 
 * @author leandro
 *
 */
public class IssuePOJOAdaptor {

    public IssuePOJOAdaptor() {
    }

    public IssuePOJO adapt(JiraIssue jIssue) {
	IssuePOJO issue = new IssuePOJO();
	issue.setAggregatetimeestimate(jIssue.getFields()
		.getAggregatetimeestimate());
	issue.setAggregatetimeoriginalestimate(jIssue.getFields()
		.getAggregatetimeoriginalestimate());
	issue.setAssignee(jIssue.getFields().getAssignee().getName());
	issue.setCreated(jIssue.getFields().getCreated());
	issue.setIssuetype(jIssue.getFields().getIssuetype().getId());
	issue.setKey(jIssue.getKey());
	issue.setPriority(jIssue.getFields().getPriority().getId());
	issue.setProgress(jIssue.getFields().getProgress());
	issue.setReporter(jIssue.getFields().getReporter().getName());
	issue.setResolutiondate(jIssue.getFields().getResolutiondate());

	// TODO hacer adaptor por cada clase hija
	String[] subtasks = new String[jIssue.getFields().getSubtasks().length];
	for (int i = 0; i < jIssue.getFields().getSubtasks().length; i++) {
	    subtasks[i] = new String(
		    jIssue.getFields().getSubtasks()[i].getKey());
	}
	issue.setSubtasks(subtasks);

	issue.setUpdated(jIssue.getFields().getUpdated());
	issue.setProject(jIssue.getFields().getProject().getKey());
	issue.setWorkratio(jIssue.getFields().getWorkratio());
	issue.setLabels( jIssue.getFields().getLabels() );
	
	return issue;
    }

}

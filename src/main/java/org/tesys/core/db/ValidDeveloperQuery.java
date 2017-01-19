package org.tesys.core.db;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.tesys.core.project.scm.MappingPOJO;

public class ValidDeveloperQuery implements GenericQuery<String> {

    private String repository = "";
    private String scmUser = "";
    private static final Logger LOG = Logger
	    .getLogger(ValidDeveloperQuery.class.getName());
    private ElasticsearchDao<MappingPOJO> dao;

    public ValidDeveloperQuery(String scmUser, String repository) {
	this.dao = new ElasticsearchDao<MappingPOJO>(MappingPOJO.class,
		ElasticsearchDao.DEFAULT_RESOURCE_MAPPING);
	this.scmUser = scmUser;
	this.repository = repository;
    }

    public ValidDeveloperQuery() {
	this.dao = new ElasticsearchDao<MappingPOJO>(MappingPOJO.class,
		ElasticsearchDao.DEFAULT_RESOURCE_MAPPING);
	;
    }

    /**
     * Devuelve el nombre del user del jira previamente mapeado o null si no se mapeo 
     */
    
    @Override
    public String execute() {
	String query = "{ \"query\": { \"bool\": { \"must\": [ { \"term\": { \"scmUser\": \""
		+ scmUser
		+ "\" }}, { \"match\": {\"repository\": \""
		+ repository + "\" }} ] } } }";
	try {
	    return dao.search(query).get(0).getProjectTrackingUser();
	} catch (Exception e) {
	    LOG.log(Level.INFO, e.getMessage());
	}
	return null;
    }

    public String getRepository() {
	return repository;
    }

    public void setRepository(String repository) {
	this.repository = repository;
    }

    public String getScmUser() {
	return scmUser;
    }

    public void setScmUser(String scmUser) {
	this.scmUser = scmUser;
    }
}

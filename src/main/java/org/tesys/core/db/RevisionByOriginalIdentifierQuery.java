package org.tesys.core.db;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.tesys.core.project.scm.RevisionPOJO;

/*
 * Busca a las revisiones por su identificar (por ejemplo r1) en ves de por el id
 * de la db (que es la fecha en md5)
 */

public class RevisionByOriginalIdentifierQuery implements GenericQuery<RevisionPOJO> {

    private static final Logger LOG = Logger
	    .getLogger(RevisionByOriginalIdentifierQuery.class.getName());
    
    private ElasticsearchDao<RevisionPOJO> dao ;
    private String originalID; 
    private String repository = "";
    
    public RevisionByOriginalIdentifierQuery(String originalID, String repository) {
	dao = new ElasticsearchDao<RevisionPOJO>(
		RevisionPOJO.class, ElasticsearchDao.DEFAULT_RESOURCE_REVISION);
	
	this.originalID = originalID;
	this.repository = repository;
	
    }
    
    @Override
    public RevisionPOJO execute() {
	String query = "{ \"query\": { \"bool\": { \"must\": [ { \"term\": { \"revision\": \""
		+ originalID
		+ "\" }}, { \"match\": {\"repository\": \""
		+ repository + "\" }} ] } } }";
	try {
	    return dao.search(query).get(0);
	} catch (IndexOutOfBoundsException e) {
	    LOG.log(Level.SEVERE, e.toString());
	}
	return null;
    }

}

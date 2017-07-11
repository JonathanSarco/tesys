package org.tesys.core.db;

import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.tesys.core.estructures.Developer;

public class SearchDeveloperByIssue implements GenericQuery<Developer> {
	
	private static final Logger LOG = Logger.getLogger(SearchDeveloperByIssue.class.getName());
	private ElasticsearchDao<Developer> dao;
	String issueId;
	
	 public SearchDeveloperByIssue(String issueId) {
			this.dao = new ElasticsearchDao<Developer>(Developer.class,
						ElasticsearchDao.DEFAULT_RESOURCE_DEVELOPERS);
			this.issueId = issueId;
	    }

	@Override
	public Developer execute() {
		StringTokenizer tokens = new StringTokenizer(issueId, "-");
		String query = "{ \"query\" : { \"query_string\": { \"default_field\": \"issueId\", \"query\": \"";
		while(tokens.hasMoreTokens()){
            String str = tokens.nextToken();
            query +=  str + " AND ";
        } 
		query = query.substring(0, query.length()-5) + "\"}}}";
		try {
		    return dao.search(query).get(0);
		} catch (Exception e) {
		    LOG.log(Level.INFO, e.getMessage());
		}
		return null;
	}
	
	

}

package org.tesys.core.db;

import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.tesys.core.estructures.UnassignedDeveloper;

public class SearchDeveloperByIssueNewIssues implements GenericQuery<UnassignedDeveloper> {
	private static final Logger LOG = Logger.getLogger(SearchDeveloperByIssueNewIssues.class.getName());
	private ElasticsearchDao<UnassignedDeveloper> dao;
	String issueId;
	
	 public SearchDeveloperByIssueNewIssues(String issueId) {
			this.dao = new ElasticsearchDao<UnassignedDeveloper>(UnassignedDeveloper.class,
						ElasticsearchDao.DEFAULT_RESOURCE_UNASSIGNED_ISSUES);
			this.issueId = issueId;
	    }

	@Override
	public UnassignedDeveloper execute() {
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

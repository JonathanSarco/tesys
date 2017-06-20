package org.tesys.core.db;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.tesys.core.estructures.Case;
import org.tesys.core.estructures.Developer;

public class SearchCasesByIssueQuery implements GenericQuery<Case>  {
	  private static final Logger LOG = Logger.getLogger(SearchCasesByIssueQuery.class.getName());
	    private ElasticsearchDao<Case> dao;
	    private String issueId;

	    public SearchCasesByIssueQuery(String issueId) {
			this.dao = new ElasticsearchDao<Case>(Case.class,
					ElasticsearchDao.DEFAULT_RESOURCE_CASEQUERY);
			this.issueId = issueId;
	    }

		@Override
		public Case execute() {
			String query = "{ \"query\": { \"bool\": { \"must\":  [" +
	        "{\"match\": {\"issueId\": \""+ issueId +"\"  }} ] } }, \"sort\": { \"timestamp\": {  \"order\": \"desc\" } }, \"size\" : \"1\" }";
			try {
			    return dao.search(query).get(0);
			} catch (Exception e) {
			    LOG.log(Level.INFO, e.getMessage());
			}
			return null;
		}
		

}

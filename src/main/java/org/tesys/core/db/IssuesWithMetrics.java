package org.tesys.core.db;


import java.util.List;

import org.tesys.core.estructures.Issue;



public class IssuesWithMetrics implements GenericQuery<List<Issue>>  {

	private ElasticsearchDao<Issue> dao;
	private int sprint;
	
	public IssuesWithMetrics(int sprint) {
		this.sprint = sprint;
		if(sprint==0) {
			this.dao = new ElasticsearchDao<Issue>(Issue.class,
					ElasticsearchDao.DEFAULT_RESOURCE_ISSUE_METRIC);
		} else if(sprint==1) {
			this.dao = new ElasticsearchDao<Issue>(Issue.class,
					ElasticsearchDao.DEFAULT_RESOURCE_SPRINT1);
		} else if(sprint == -1){
			this.dao = new ElasticsearchDao<Issue>(Issue.class,
					ElasticsearchDao.DEFAULT_RESOURCE_ESTIMATION_ISSUE);
		} else {
			this.dao = new ElasticsearchDao<Issue>(Issue.class,
					ElasticsearchDao.DEFAULT_RESOURCE_SPRINT2);
		}

	}
	
	
	@Override
	public List<Issue> execute() {
		//TODO el 500 habria que sacarlo con un getSize del elasticsearcdAO
		String query = "{\"size\" : 500,\"query\" : {\"constant_score\" : {\"filter\" : {\"exists\" : {\"field\" : \"lines\"}} } } }";
		try {
			if (sprint == -1)
				return dao.search("{\"query\" : {\"bool\":{\"must\": { \"match_all\":{ } } } } }");
			else
				return dao.search(query);
		} catch (Exception e) {
			System.out.println("error");
		}
		return null;
	}
	
	

}

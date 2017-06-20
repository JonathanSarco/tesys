package org.tesys.core.db;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.tesys.core.analysis.skilltraceability.Skill;
import org.tesys.core.estructures.Case;

public class SearchCaseByIssueAndSkillsQuery implements GenericQuery<List<Case>>  {
	private static final Logger LOG = Logger.getLogger(SearchCaseByIssueAndSkillsQuery.class.getName());
	private ElasticsearchDao<Case> dao;
	private List<String> skills = new LinkedList<String>();
	private List<String> labels =new LinkedList<String>();
	
	public SearchCaseByIssueAndSkillsQuery(String[] labels, List<Skill> skilluser) {
		this.dao = new ElasticsearchDao<Case>(Case.class,
				ElasticsearchDao.DEFAULT_RESOURCE_CASEQUERY);
		for(Skill sk: skilluser){
			skills.add(sk.getName()) ;
		}
		this.labels = Arrays.asList(labels);
	}

	@Override
	public List<Case> execute() {
		String query = "{ \"query\": { \"bool\": { \"must\":  [";
		for (String s : labels){
			query += "{\"match\": {\"labels\": \"" + s + "\"  }},";
		}
		for(String s: skills){
			query += "{\"match\": {\"skillName\": \"" + s + "\"}},";
		}
		if (query.charAt(query.length()-1) == ','){
			query = query.substring(0, query.length()-1);
		}
		query +=" ] } }}";
		try {
			return dao.search(query);
		} catch (Exception e) {
			LOG.log(Level.INFO, e.getMessage());
		}
		return null;
	}
}

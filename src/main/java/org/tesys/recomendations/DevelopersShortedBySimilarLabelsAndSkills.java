package org.tesys.recomendations;


import java.util.LinkedList;
import java.util.List;

import org.tesys.core.db.ElasticsearchDao;
import org.tesys.core.estructures.Developer;
import org.tesys.core.estructures.Issue;
import org.tesys.core.estructures.SimilarIssue;

public class DevelopersShortedBySimilarLabelsAndSkills {
	
	
	public static List<SimilarIssue> getDevelopersShortedBySimilarLabelsAndSkills(Issue e, double factorLabel, double factorSkill){
		ElasticsearchDao<Developer> daoi = new ElasticsearchDao<Developer>(Developer.class, ElasticsearchDao.DEFAULT_RESOURCE_DEVELOPERS);
		List<Developer> ld  = daoi.readAll();
		IssueSimilarityLabels isl = new IssueSimilarityLabels();
		SkillsSimilarity iss = new SkillsSimilarity();
		//Obtengo las issues similares a la que enviío por parámetro
		List<SimilarIssue>similarIssuesLabel=isl.getSimilarIssuesTo(e, ld);
		List<SimilarIssue>similarIssuesSkill=iss.getIssuesShortedBySkills(e.getSkills(), ld);
		List<SimilarIssue>similars = new LinkedList<SimilarIssue>();
		
		for(SimilarIssue si : similarIssuesLabel){
			if(similarIssuesSkill.size()>0){
				for(SimilarIssue sis : similarIssuesSkill){
					if(si.getIssue().getIssueId().equals(sis.getIssue().getIssueId())){
						si.setSimilarSkills(sis.getSimilarSkills());
						//similarIssuesSkill.remove(sis);
						si.setFactorOfSimilarity(si.getSimilarLabels()*factorLabel+sis.getSimilarSkills()*factorSkill);
						similars.add(si);
					}
					else{
						sis.setFactorOfSimilarity(si.getSimilarLabels()*factorLabel+sis.getSimilarSkills()*factorSkill);
						similars.add(sis);
					}
				}
			}
			else {
				si.setFactorOfSimilarity(si.getSimilarLabels()*factorLabel + 0);
				similars.add(si);
			}
			
		}
		
		
		return similars;
	}

}

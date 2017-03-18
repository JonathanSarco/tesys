package org.tesys.recomendations;


import java.util.LinkedList;
import java.util.List;

import org.tesys.core.estructures.Developer;
import org.tesys.core.estructures.Issue;
import org.tesys.core.estructures.SimilarDeveloper;
import org.tesys.core.estructures.SimilarIssue;

public class DevelopersShortedBySimilarLabelsAndSkills {
	
	
	public List<SimilarIssue> getDevelopersShortedBySimilarLabelsAndSkills(Issue e, double factorLabel, double factorSkill, List<Developer> ld){
		
		IssueSimilarityLabels isl = new IssueSimilarityLabels();
		SkillsSimilarity iss = new SkillsSimilarity();
		//Obtengo las issues similares a la que enviío por parámetro
		List<SimilarIssue>similarIssuesLabel=isl.getSimilarIssuesTo(e, ld);
		List<SimilarIssue>similarIssuesSkill=iss.getIssuesShortedBySkills(e.getSkills(), ld);
		
		for(SimilarIssue si : similarIssuesLabel){
			for(SimilarIssue sis : similarIssuesSkill){
				if(si.getIssue().getIssueId().equals(sis.getIssue().getIssueId())){
					si.setSimilarSkills(sis.getSimilarSkills());
					similarIssuesSkill.remove(sis);
					si.setFactorOfSimilarity(si.getSimilarLabels()*factorLabel+sis.getSimilarSkills()*factorSkill);
				}
				else{
					sis.setFactorOfSimilarity(si.getSimilarLabels()*factorLabel+sis.getSimilarSkills()*factorSkill);
					similarIssuesLabel.add(sis);
				}
			}
		}
		
		
		return similarIssuesLabel;
	}

}

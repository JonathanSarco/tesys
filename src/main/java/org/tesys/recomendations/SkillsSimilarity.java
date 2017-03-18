package org.tesys.recomendations;
import java.util.LinkedList;
import java.util.List;

import org.tesys.core.analysis.skilltraceability.Skill;
import org.tesys.core.estructures.Developer;
import org.tesys.core.estructures.Issue;
import org.tesys.core.estructures.SimilarIssue;

public class SkillsSimilarity {
//	final static int matching=2;
	public List<SimilarIssue> getIssuesShortedBySkills(List<Skill> skills, List<Developer>devs) {
		List<SimilarIssue> similarIssues = new LinkedList<SimilarIssue>();
		
		//Calcular
		for (Skill s : skills){
			for (Developer d : devs) {
				SimilarIssue similarIssue = new SimilarIssue();
				for (Issue i : d.getIssues()) {
					if(i.getSkills() != null) {					
						for (Skill skill : i.getSkills()) {
							if( s == skill ) {
								//contar +1
								similarIssue.setSimilarSkills(similarIssue.getSimilarSkills()+1);	
								similarIssue.setDeveloper(d);
							}
						}			
					}	
					similarIssue.setIssue(i);
				}
				if(similarIssue.getSimilarSkills()>0){
					similarIssues.add(similarIssue);
				}	
			}
		}
		return similarIssues;
	}
	
	
	//Los mismos Métodos pero para Los Casos almacenados en la base
}

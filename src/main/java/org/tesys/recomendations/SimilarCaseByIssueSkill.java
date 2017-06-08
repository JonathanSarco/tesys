package org.tesys.recomendations;

import java.util.List;

import org.tesys.core.analysis.skilltraceability.Skill;
import org.tesys.core.analysis.telemetry.SkillsAggregator;
import org.tesys.core.estructures.Issue;

public class SimilarCaseByIssueSkill {
	final static int machingLabels = 3;
	public static boolean areSimilar(Issue issueCase, Issue issue) {
		
		String[] labelsIssueCase = issueCase.getLabels();
		String[] labelsIssue = issue.getLabels();
		int similars=0;
		if(labelsIssue.length > 0 && labelsIssue.length > 0 ){
			for(int i = 0 ; i < labelsIssueCase.length ; i++ ){
				for(int j =0; j<labelsIssue.length; j++){
					if(labelsIssueCase[i].equals(labelsIssue[j])){
						similars++;
						if(similars >= machingLabels){
							return true;
						}
					}
				}
			}
		}
		
		List<Skill> skillsIssueCase = issueCase.getSkills();
		List<Skill> skillsIssue = issue.getSkills();
		if(skillsIssueCase != null && skillsIssue != null){
			for(Skill sc: skillsIssueCase){
				for(Skill si: skillsIssue){
					if(sc.getName().equals(si.getName())){
						return true;
					}
				}
			}
		}
		return false;
	}

}

package org.tesys.core.interceptingFilter;

import java.util.LinkedList;
import java.util.List;

import org.tesys.core.analysis.skilltraceability.Skill;
import org.tesys.core.estructures.Developer;
import org.tesys.core.estructures.Issue;
import org.tesys.core.estructures.SimilarIssue;

public class SkillsFilter extends Filter {
	
	public SkillsFilter() {
		// TODO Auto-generated constructor stub
	}
	
	private List<SimilarIssue> prePorcessFilter (List<Skill> skills, List<Developer>devs) {		
		List<SimilarIssue>similars=new LinkedList<SimilarIssue>();
		//Calcular
		//Ver como agrega al vector porq aca rompe cuando encuentra una issue con skills!
		if(skills != null){
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
						similars.add(similarIssue);
					}					
				}
			}
		}
		return similars;
	}
	@Override
	public ResponseFilter execute(RequestFilter request, ResponseFilter response) {
		//Obtengo las issues similares a la que enviío por parámetro
		List<SimilarIssue>similarIssuesSkill=this.prePorcessFilter(request.getIssue().getSkills(), request.getDevelopers());
		for(SimilarIssue si: response.getSimilarIssuesResponse()){
			if(similarIssuesSkill != null && !similarIssuesSkill.isEmpty()){
				for(SimilarIssue sis : similarIssuesSkill){
					if(si.getIssue().getIssueId().equals(sis.getIssue().getIssueId())){
						si.setSimilarSkills(sis.getSimilarSkills());
						si.setFactorOfSimilarity(si.getSimilarLabels()*request.getFactorLabel()+sis.getSimilarSkills()*request.getFactorSkill());
						response.addSimilarIssues(si);
					}
					else{
						sis.setFactorOfSimilarity(si.getSimilarLabels()*request.getFactorLabel()+sis.getSimilarSkills()*request.getFactorSkill());
						response.addSimilarIssues(sis);
					}
				}
			}
			
		}	
		return response;
	}

}

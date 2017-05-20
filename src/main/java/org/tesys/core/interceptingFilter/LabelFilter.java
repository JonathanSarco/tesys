package org.tesys.core.interceptingFilter;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.tesys.core.analysis.skilltraceability.Skill;
import org.tesys.core.estructures.Developer;
import org.tesys.core.estructures.Issue;
import org.tesys.core.estructures.SimilarIssue;

public class LabelFilter extends Filter {
	
	final static int matching=1;
	
	public LabelFilter() {
		// TODO Auto-generated constructor stub
		similarIssues = new LinkedList<SimilarIssue>();
	}
	
	private boolean areSimilar (Issue ip, Issue i, int similarLabels) {
		List<String> labelsip = Arrays.asList(ip.getLabels());
		List<String> labelsi = Arrays.asList(i.getLabels());
			
		for (String l : labelsi) {
			if(labelsip.contains(l)){
				similarLabels++;
			}
		}
		if(similarLabels>=matching){
			return true;
		}
		return false;
	}
		
	public List<SimilarIssue> prePorcessFilter(Issue ip, List<Developer>ld) {
		List<SimilarIssue> similarIssues = new LinkedList<SimilarIssue>();
		
		for (Developer d : ld) {
				
			List<Issue> li = d.getIssues();
			SimilarIssue similarIssue = new SimilarIssue();
			for (Issue i : li) {
				int similarLabels = 0;
				if(areSimilar(ip, i, similarLabels)){
					similarIssue.setIssue(i);
					similarIssue.setSimilarLabels(similarLabels);
					similarIssues.add(similarIssue);
					similarIssue.setDeveloper(d);
				}			
			}	
		}
		return similarIssues;		
	}
	@Override
	public ResponseFilter execute(RequestFilter request, ResponseFilter response) {
		//Obtengo las issues similares a la que enviío por parámetro
		List<SimilarIssue>similarIssuesLabel=this.prePorcessFilter(request.getIssue(), request.getDevelopers());
		if(response.getSimilarIssuesResponse().isEmpty() || response.getSimilarIssuesResponse() == null){
			for(SimilarIssue si : similarIssuesLabel){
				si.setFactorOfSimilarity(si.getSimilarLabels()*request.getFactorLabel());
				response.addSimilarIssues(si);
			}
		}	
		else{
			for(SimilarIssue si : similarIssuesLabel){
				for(SimilarIssue sis : response.getSimilarIssuesResponse()){
					if(si.getIssue().getIssueId().equals(sis.getIssue().getIssueId())){
						si.setSimilarSkills(sis.getSimilarSkills());
						//similarIssuesSkill.remove(sis);
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

package org.tesys.recomendations;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.tesys.core.estructures.Developer;
import org.tesys.core.estructures.Issue;
import org.tesys.core.estructures.SimilarIssue;

public class IssueSimilarityLabels {
	
	final static int matching=2;
	
	public boolean areSimilar(Issue ip, Issue i, int similarLabels) {
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
	
	public List<SimilarIssue> getSimilarIssuesTo(Issue ip, List<Developer>ld) {
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
	
	//Los mismos Métodos pero para Los Casos almacenados en la base
	
}

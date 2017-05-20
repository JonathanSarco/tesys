package org.tesys.core.interceptingFilter;

import java.util.LinkedList;
import java.util.List;

import org.tesys.core.estructures.Developer;
import org.tesys.core.estructures.Issue;
import org.tesys.core.estructures.SimilarIssue;

public class RequestFilter {
	Issue issue;
	double factorLabel;
	double factorSkill;
	List<Developer> developers; 
	List<SimilarIssue> similarissues;
	
	public RequestFilter(){
		issue = new Issue();
		factorLabel = 0.0;
		factorSkill = 0.0;
		developers = new LinkedList<Developer>();
		similarissues = new LinkedList<SimilarIssue>();
	}

	public Issue getIssue() {
		return issue;
	}

	public void setIssue(Issue e) {
		this.issue = e;
	}

	public double getFactorLabel() {
		return factorLabel;
	}

	public void setFactorLabel(double factorLabel) {
		this.factorLabel = factorLabel;
	}

	public double getFactorSkill() {
		return factorSkill;
	}

	public void setFactorSkill(double factorSkill) {
		this.factorSkill = factorSkill;
	}

	public List<Developer> getDevelopers() {
		return developers;
	}

	public void setDevelopers(List<Developer> developers) {
		this.developers = developers;
	}

	public List<SimilarIssue> getSimilarissues() {
		return similarissues;
	}

	public void setSimilarissues(List<SimilarIssue> similarissues) {
		this.similarissues = similarissues;
	}

}

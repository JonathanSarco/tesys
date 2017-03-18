package org.tesys.core.estructures;

import java.util.LinkedList;
import java.util.List;

public class SimilarIssue {

	Issue issue;
	int similarSkills;
	int similarLabels;
	double factorOfSimilarity;
	Developer developer;


	public SimilarIssue(){
		similarSkills=0;
		similarLabels=0;
	}
	
	public Issue getIssue() {
		return issue;
	}
	public void setIssue(Issue issue) {
		this.issue = issue;
	}
	public int getSimilarSkills() {
		return similarSkills;
	}
	public void setSimilarSkills(int similarSkills) {
		this.similarSkills = similarSkills;
	}
	public int getSimilarLabels() {
		return similarLabels;
	}
	public void setSimilarLabels(int similarLabels) {
		this.similarLabels = similarLabels;
	}
	public double getFactorOfSimilarity() {
		return factorOfSimilarity;
	}

	public void setFactorOfSimilarity(double factorOfSimilarity) {
		this.factorOfSimilarity = factorOfSimilarity;
	}

	public Developer getDeveloper() {
		return developer;
	}

	public void setDeveloper(Developer developer) {
		this.developer = developer;
	}
}

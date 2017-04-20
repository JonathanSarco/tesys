package org.tesys.core.estructures;

import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.tesys.core.analysis.skilltraceability.Skill;
import org.tesys.correlations.DeveloperPrediction;
import org.tesys.util.MD5;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Case {
	
	static int idCase=0;
	// *** Problema ***
	Issue idIssue;
	String[] labels;
	Skill[] neededSkills;
	// *** Fin Problema ***
	
	// *** Solucion ***
	DeveloperPrediction[] recommendedDevelopers;
	//String displayName;
	Metric[] estimatedMetrics;
	// *** Fin Solucion ***
	
	// *** Resultado ***
	Developer performDeveloper;
	Map<String, Double> realMetrics;
	Skill[] realSkills;
	// *** Fin Resultado ***
	
	public Case(){
		// for jason
	}
	
    public Case(Issue idIssue) {
	super();
	this.idIssue = idIssue;
	this.idCase++;
    }
    
    public Case(Issue idIssue, String[] labels, Metric[] estimatedMetrics, DeveloperPrediction[] recommendedDevelopers, 
    		Developer performDeveloper, Map<String, Double> realMetrics) {
    	// *** Problema ***
    	this.idIssue = idIssue;
    	this.labels = labels;
    	this.estimatedMetrics = estimatedMetrics;
    	// *** Fin Problema ***
    	
    	// *** Solucion ***
    	this.recommendedDevelopers = recommendedDevelopers;
    	// *** Fin Solucion ***
    	
    	// *** Resultado ***
    	this.performDeveloper = performDeveloper;
    	this.realMetrics = realMetrics;
    	// *** Fin Resultado ***
    	this.idCase++;
    }

	public Issue getIdIssue() {
		return idIssue;
	}

	public void setIdIssue(Issue i) {
		this.idIssue = i;
	}

	public String[] getLabels() {
		return labels;
	}

	public void setLabels(String[] labels) {
		this.labels = labels;
	}

	public Metric[] getEstimatedMetrics() {
		return estimatedMetrics;
	}

	public void setEstimatedMetrics(Metric[] estimatedMetrics) {
		this.estimatedMetrics = estimatedMetrics;
	}

	public DeveloperPrediction[] getRecommendedDevelopers() {
		return recommendedDevelopers;
	}

	public void setRecommendedDevelopers(DeveloperPrediction[] devPredictionSimilar2) {
		this.recommendedDevelopers = devPredictionSimilar2;
	}

	public Developer getPerformDeveloper() {
		return performDeveloper;
	}

	public void setPerformDeveloper(Developer performDeveloper) {
		this.performDeveloper = performDeveloper;
	}

	public Map<String, Double> getRealMetrics() {
		return realMetrics;
	}

	public void setRealMetrics(Map<String, Double> map) {
		this.realMetrics = map;
	}
	public Skill[] getNeededSkills() {
		return neededSkills;
	}

	public void setNeededSkills(Skill[] neededSkills) {
		this.neededSkills = neededSkills;
	}

	public Skill[] getRealSkills() {
		return realSkills;
	}

	public void setRealSkills(Skill[] realSkills) {
		this.realSkills = realSkills;
	}

    public String getId() {
	return MD5.generateId( idCase + idIssue.getIssueId());
    }	
}

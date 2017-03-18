package org.tesys.developersRecomendations;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.tesys.core.analysis.skilltraceability.Skill;
import org.tesys.core.estructures.Developer;
import org.tesys.core.estructures.Metric;
import org.tesys.util.MD5;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Case {
	
	static int idCase=0;
	// *** Problema ***
	String idIssue;
	String[] labels;
	Skill[] neededSkills;
	// *** Fin Problema ***
	
	// *** Solucion ***
	Developer[] recommendedDevelopers;
	Metric[] estimatedMetrics;
	// *** Fin Solucion ***
	
	// *** Resultado ***
	Developer performDeveloper;
	Metric[] realMetrics;
	Skill[] realSkills;
	// *** Fin Resultado ***
	
	public Case(){
		// for jason
	}
	
    public Case(String idIssue) {
	super();
	this.idIssue = idIssue;
	this.idCase++;
    }
    
    public Case(String idIssue, String[] labels, Metric[] estimatedMetrics, Developer[] recommendedDevelopers, 
    		Developer performDeveloper, Metric[] realMetrics) {
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

	public String getIdIssue() {
		return idIssue;
	}

	public void setIdIssue(String idIssue) {
		this.idIssue = idIssue;
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

	public Developer[] getRecommendedDevelopers() {
		return recommendedDevelopers;
	}

	public void setRecommendedDevelopers(Developer[] recommendedDevelopers) {
		this.recommendedDevelopers = recommendedDevelopers;
	}

	public Developer getPerformDeveloper() {
		return performDeveloper;
	}

	public void setPerformDeveloper(Developer performDeveloper) {
		this.performDeveloper = performDeveloper;
	}

	public Metric[] getRealMetrics() {
		return realMetrics;
	}

	public void setRealMetrics(Metric[] realMetrics) {
		this.realMetrics = realMetrics;
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
	return MD5.generateId( idCase + idIssue);
    }	
}

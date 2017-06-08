package org.tesys.core.estructures;

import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.tesys.core.analysis.skilltraceability.Skill;
import org.tesys.correlations.DeveloperPrediction;
import org.tesys.correlations.MetricPrediction;
import org.tesys.util.MD5;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Case {
	
	int idCase;
	// *** Problema ***
	Issue issue;
	// *** Fin Problema ***
	
	// *** Solucion ***
	/*
	 * Se Hace una lista de developers recomendados, los cuales van a tener "asignada"
	 * sólo la tarea para la cual se hace la recomendación
	 * Se almacena en las issues del Desarrollador la tarea NUEVA
	 * Las Métricas estimadas se almacenan en el Map measures de Issues 
	 */
	Developer[] issuesWithDevelopersRecommended;
	// *** Fin Solucion ***
	
	// *** Resultado ***
	/*
	 * Se Almacenan las Metricas Reales en el Map measures de la Issue Nueva desarrollada por el Developer PerformIssue
	 */
	Developer performIssue;
	// *** Fin Resultado ***

	// *** Criterio ***
	String orderCriteria;
	// *** Fin Criterio ***
	
	boolean goodRecommendation;

	public Case(){
		// for jason
	}
	
    public Case(Issue idIssue) {
	super();
	this.issue = issue;
	this.idCase = 0;
    }

	public Issue getIssue() {
		return issue;
	}

	public void setIssue(Issue issue) {
		this.issue = issue;
	}

	public Developer[] getIssuesWithDevelopersRecommended() {
		return issuesWithDevelopersRecommended;
	}

	public void setIssuesWithDevelopersRecommended(Developer[] issuesWithDevelopersRecommended) {
		this.issuesWithDevelopersRecommended = issuesWithDevelopersRecommended;
	}

	public Developer getPerformIssue() {
		return performIssue;
	}

	public void setPerformIssue(Developer performIssue) {
		this.performIssue = performIssue;
	}
	public int setIdCase(int Value) {
		return idCase = Value;
	}
	public int getIdCase() {
		return idCase;
	}
	
	public String getOrderCriteria() {
		return orderCriteria;
	}

	public void setOrderCriteria(String orderCriteria) {
		this.orderCriteria = orderCriteria;
	}
	
	public boolean isGoodRecommendation() {
		return goodRecommendation;
	}

	public void setGoodRecommendation(boolean goodRecommendation) {
		this.goodRecommendation = goodRecommendation;
	}
	
}

package org.tesys.core.estructures;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.tesys.core.analysis.skilltraceability.Skill;
import org.tesys.correlations.DeveloperPrediction;
import org.tesys.correlations.MetricPrediction;
import org.tesys.orderCriteria.CriteriaBestValue;
import org.tesys.orderCriteria.CriteriaSelector;
import org.tesys.util.MD5;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Case  {
	
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
	static Map<String,String> orderCriteria;
	// *** Fin Criterio ***
	
	boolean goodRecommendation;

	Double value;

	
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
	
	public Map<String,String> getOrderCriteria() {
		return orderCriteria;
	}

	public static  void setOrderCriteria(Map<String,String> criteria) {
		List<String>aux=(List<String>) criteria.keySet(); //Nose si castea bien
		orderCriteria.put(aux.get(0), criteria.get(aux.get(0)));
	}
	
	public static void setInverseOrderCriteria(Map<String, String> criteria) {
		List<String>aux=(List<String>) criteria.keySet(); // Nose si castea bien
		// Invierte el orden de los desarrolladores
		if((criteria.get(aux.get(0))).equals("mayor"))
			orderCriteria.put(aux.get(0), "menor");
		else
			orderCriteria.put(aux.get(0), "mayor");
	}
	
	public boolean isGoodRecommendation() {
		return goodRecommendation;
	}

	public void setGoodRecommendation(boolean goodRecommendation) {
		this.goodRecommendation = goodRecommendation;
	}
	
	public Map<String,String> getOrderCriterion(){
		return orderCriteria;
	}


}

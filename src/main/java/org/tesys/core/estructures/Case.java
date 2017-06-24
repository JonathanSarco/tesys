package org.tesys.core.estructures;


import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
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
import org.tesys.orderDeveloper.OrderDevByValue;
import org.tesys.orderDeveloper.OrderDevbyName;
import org.tesys.util.MD5;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Case  {
	
	String idCase;

	Date timestamp;
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
	String metric;
	String precedence;
	// *** Fin Criterio ***

	boolean goodRecommendation;

	//Double value;


	public Case(){
		// for jason
	}
	public Case(Date idCase) {
		super();
		this.timestamp = idCase;
	}
	public Case(Issue idIssue, Date idCase) {
		super();
		this.issue = issue;
		this.timestamp = idCase;
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
	public Date settimestamp() {
		return timestamp = new Date();
	}
	public Date gettimestamp() {
		return timestamp;
	}

	public Map<String,String> getOrderCriteria() {
		Map<String, String> criteria = new HashMap<String,String>();
		if(metric != null && precedence != null)
			criteria.put(metric, precedence);
		return criteria;
	}

	public  void setOrderCriteria(Map<String,String> criteria) {
		
		metric = criteria.keySet().toArray()[0].toString();
		precedence = criteria.get(metric);
	}

	public void setInverseOrderCriteria(Map<String, String> criteria) {
		if(!criteria.isEmpty() && criteria != null){
			// Invierte el orden de los desarrolladores
			if(criteria.get(criteria.keySet().toArray()[0].toString()).equals("mayor")){
				metric = criteria.keySet().toArray()[0].toString();
				precedence =  "menor";
			}
			else{
				metric = criteria.keySet().toArray()[0].toString();
				precedence = "mayor";
			}
		}
	}

	public boolean isGoodRecommendation() {
		return goodRecommendation;
	}

	public void setGoodRecommendation(boolean goodRecommendation) {
		this.goodRecommendation = goodRecommendation;
	}

	public void orderDeveloper(List<Case> similarCases) {
		if(similarCases.isEmpty() || similarCases.size() == 0){
			List<Developer> developers = Arrays.asList(issuesWithDevelopersRecommended);
			Collections.sort(developers, new OrderDevbyName()); 
			Developer deveoperComplete[] = new Developer[developers.size()];
			deveoperComplete = (Developer[]) developers.toArray();
			issuesWithDevelopersRecommended = deveoperComplete;
		}
		else{
			orderDevelopersByCriteria(similarCases);
		}

	}
	private Developer[] orderDevelopersByCriteria(List<Case> similarCases) {

		//Busca si alguno de los casos similares es una buena recomendancion
		boolean found=false;
		Case similarCaseGood=null;
		for(int i=0;i<similarCases.size() && !found;i++){
			if(similarCases.get(i).isGoodRecommendation()){
				similarCaseGood=similarCases.get(i);
				found=true;
			}
		}

		//Si existe un caso similar que sea una buena recomendacion, obtengo el criterio por el cual ordeno, y sino ordeno al reves por ese criterio
		if(similarCaseGood != null){
			Map<String,String> orderCriteria = similarCaseGood.getOrderCriteria();
			setOrderCriteria(orderCriteria);
			//Establezco el criterio por el cual se va a ordenar
		}
		else{
			//Elijo el primero, ya que cualquiera es una mala recomendacion
			Case similarCaseBad=similarCases.get(0);
			Map<String,String> orderCriteria = similarCaseBad.getOrderCriteria();
			//Establezco el criterio por el cual se va a ordenar
			setInverseOrderCriteria(orderCriteria);
		}
		List<Developer> developers = Arrays.asList(issuesWithDevelopersRecommended);	
		//Ordeno por ese criterio (el primer String me indica la metrica, y el segundo si debo ordenar ascendente o descendente a los desarrolladores)
		if(metric != null && precedence != null){
			Collections.sort(developers, new OrderDevByValue(metric, precedence));	
		}
		else{
			Collections.sort(developers, new OrderDevbyName()); 
		}
		Developer deveoperComplete[] = new Developer[developers.size()];
		deveoperComplete = (Developer[]) developers.toArray();
		return deveoperComplete;

	}

	public String getMetric() {
		return metric;
	}

	public void setMetric(String metric) {
		this.metric = metric;
	}

	public String getPrecedence() {
		return precedence;
	}

	public void setPrecedence(String precedence) {
		this.precedence = precedence;
	}
	
	public String getIdCase() {
		return idCase;
	}
	
	public void setIdCase(String idCase) {
		this.idCase = idCase;
	}
}

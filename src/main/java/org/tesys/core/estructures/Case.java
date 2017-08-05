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
import org.tesys.orderDeveloper.OrderByWeight;
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
	MetricWeight[] orderCriteria;

	// *** Fin Criterio ***

	int goodRecommendation;
	//String _id;



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

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	public void setCriteria(MetricWeight[] orderCriteria) {
			this.orderCriteria = orderCriteria;
			
	}
	public void setGoodRecommendation(int goodRecommendation) {
		this.goodRecommendation = goodRecommendation;
	}
	
	public int getGoodRecommendation() {
		return goodRecommendation;
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
			//orderDevelopersByCriteria(similarCases);
		}

	}
	/*private Developer[] orderDevelopersByCriteria(List<Case> similarCases) {

		//Busca si alguno de los casos similares es una buena recomendancion
		boolean found=false;
		Case similarCaseGood=null;
		for(int i=0;i<similarCases.size() && !found;i++){
			if(similarCases.get(i).isGoodRecommendation()){
				similarCaseGood=similarCases.get(i);
				found=true;
			}
		}
		boolean invert=false;
		//Si existe un caso similar que sea una buena recomendacion, obtengo el criterio por el cual ordeno, y sino ordeno al reves por ese criterio
		if(similarCaseGood != null){
			Object[] metrics=similarCaseGood.getCriteria();
			setCriteria(metrics);
		}
		else{
			//Elijo el primero, ya que cualquiera es una mala recomendacion

					Case similarCaseBad=similarCases.get(0);
					Object[] metrics = similarCaseBad.getCriteria();
					setCriteria(metrics);
					invert=true;
			}
		List<Developer> developers = Arrays.asList(issuesWithDevelopersRecommended);	
		//Ordeno por ese criterio (el primer String me indica la metrica, y el segundo si debo ordenar ascendente o descendente a los desarrolladores)
		if(this.getCriteria() != null && this.getCriteria().length>0){
			Collections.sort(developers, new OrderDevByValue(criteria,invert));	
		}
		else{
			Collections.sort(developers, new OrderDevbyName()); 
		}
		Developer deveoperComplete[] = new Developer[developers.size()];
		deveoperComplete = (Developer[]) developers.toArray();
		return deveoperComplete;
		
	}*/

	
	public Date getTimestamp() {
		return timestamp;
	}
	public MetricWeight[] getCriteria() {
		return this.orderCriteria;
	}
	public String getIdCase() {
		return idCase;
	}
	
	public void setIdCase(String idCase) {
		this.idCase = idCase;
	}
	
	public MetricWeight[] convertHashToVector(HashMap<String, Double> metricWeight){
		List<MetricWeight> converted = new LinkedList<MetricWeight>();
		Set<String> keys = metricWeight.keySet(); 
		for(String s: keys){
			MetricWeight weight = new MetricWeight();
			weight.setMetricName(s);
			weight.setWeight(metricWeight.get(s));
			converted.add(weight);
		}
		
		MetricWeight metricsOrder[] = new MetricWeight[converted.size()];
		metricsOrder = converted.toArray(metricsOrder);
		
		return metricsOrder;
	}
	public void orderDeveloperByWeight(List<Case> similarCases) {
		List<Developer>devBadRecommendation = new LinkedList<Developer>();
		List<Developer> similarDev = new LinkedList<Developer>();
		for(Case c: similarCases){
			if(c.getGoodRecommendation() == 0){
				similarDev = Arrays.asList(c.issuesWithDevelopersRecommended);
				similarDev.remove(c.getPerformIssue());
				devBadRecommendation.add(c.getPerformIssue());
			}
		}
		Collections.sort(similarDev, new OrderByWeight(this.orderCriteria));
		
	}

}

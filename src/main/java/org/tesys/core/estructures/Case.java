package org.tesys.core.estructures;


import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.tesys.core.Normalizer.NormalizedError;
import org.tesys.orderDeveloper.OrderByWeight;
import org.tesys.orderDeveloper.OrderDevbyName;

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
	MetricWeight[] metricWeight;

	// *** Fin Criterio ***

	int goodRecommendation;
	
	double errorCuadraticoMedio;
	
	double porcentajeErrorRelativoPromedio;

	public Case(){
		// for jason
	}
	public Case(Date idCase) {
		super();
		this.timestamp = idCase;
	}
	public Case(Issue idIssue, Date idCase) {
		super();
		this.issue = idIssue;
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
	public void setCriteria(MetricWeight[] metricWeight) {
			this.metricWeight = metricWeight;
			
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
	public MetricWeight[] getMetricWeight() {
		return this.metricWeight;
	}
	
	public String getIdCase() {
		return idCase;
	}
	
	public void setIdCase(String idCase) {
		this.idCase = idCase;
	}
	
	public MetricWeight[] convertHashToVector(Map<String, Double> metricWeight){
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
		List<Developer> similarDevNoMetrics = new LinkedList<Developer>();
		
		for (int i=0; i<this.issuesWithDevelopersRecommended.length;i++) {
			similarDev.add(issuesWithDevelopersRecommended[i]);
		}
		
		for(Case c: similarCases){
			if(c.getGoodRecommendation() == 0){
				for(Developer d: c.issuesWithDevelopersRecommended){
					if(!conteinsDev(similarDev, d) && !conteinsDev(devBadRecommendation, d))
						similarDev.add(d);				
				}
				similarDev = deletePerformIssue(c.getPerformIssue(), similarDev, devBadRecommendation);
			}
		}
		/*
		 * Busco el ultimo caso similar Agregado a CBR
		 */
		Case latestCase = new Case();
		if(similarCases != null && !similarCases.isEmpty()){
			latestCase = getLastCase(similarCases);
		}
		/* 
		List<MetricWeight> metricOrdered = Arrays.asList(latestCase.getMetricWeight());
		Collections.sort(metricOrdered);
		MetricWeight metricOrderedArray[] = new MetricWeight[metricOrdered.size()];
		latestCase.setCriteria((MetricWeight[]) metricOrdered.toArray());
		//Saco de similarDev, aquellos desarrolladores que no tengan metricas estimadas y los almaceno en similarDevNoMetrics
		*/
		List<Developer>aux = new LinkedList<Developer>();
		for(Developer d: similarDev){
			aux.add(d);
		}
		for (int i=0; i<similarDev.size();i++) {
			if(similarDev.get(i).getIssues().get(0).getMetrics()==null || similarDev.get(i).getIssues().get(0).getMetrics().isEmpty()) {
				similarDevNoMetrics.add(similarDev.get(i));
				aux.remove(similarDev.get(i));
			}
			if(latestCase.getMetricWeight() != null && similarDev.get(i).getIssues().get(0).getMetrics()!=null && containsBestMetric(latestCase.getMetricWeight(), similarDev.get(i).getIssues().get(0).getMetrics())){
				similarDevNoMetrics.add(similarDev.get(i));
				aux.remove(similarDev.get(i));
			}
		}
		similarDev = aux;
		if(latestCase.getMetricWeight() != null && latestCase.getMetricWeight().length>0){
			OrderByWeight order = new OrderByWeight(latestCase.getMetricWeight());
			similarDev = order.compare(similarDev);
			
			for(Developer d: similarDevNoMetrics){
				similarDev.add(d);
			}
			
			for(Developer d: devBadRecommendation){
				similarDev.add(d);
			}		
		}
		else{
			 Collections.sort(similarDev);
		}
		Developer developerComplete[] = new Developer[similarDev.size()];
		
		for (int i=0; i<similarDev.size();i++) {
			developerComplete[i] = similarDev.get(i);
		}
		
		this.issuesWithDevelopersRecommended = developerComplete; 
		
	}
	
	private List<Developer> deletePerformIssue(Developer performIssue, List<Developer> similarDev,
			List<Developer> devBadRecommendation) {
		List<Developer> aux = similarDev;
		for(int i=0; i<aux.size(); i++){
			if(aux.get(i).getName().equals(performIssue.getName())){
				similarDev.remove(i);
				devBadRecommendation.add(performIssue);
			}
		}
		return aux;
	}
	private boolean containsBestMetric(MetricWeight[] metricWeight2, Map<String, Double> metrics) {
		
		return (metricWeight2[0] != null && metrics.get(metricWeight2[0].getMetricName()) == null && metricWeight2[1] != null && 
				metrics.get(metricWeight2[1].getMetricName()) == null && metricWeight2[2] != null && metrics.get(metricWeight2[2].getMetricName()) == null && 
				metricWeight2[3] != null && metrics.get(metricWeight2[3].getMetricName()) == null);
	}
	private static boolean conteinsDev(List<Developer> developers, Developer developer) {
		for(Developer dev: developers){
			if(dev.getName().equals(developer.getName()))
				return true;
		}
		return false;
	}
	
	private Case getLastCase(List<Case> similarCases){
		Case c = new Case();
		Date timeStamp = new Date("01/01/1991");
		for(Case caso : similarCases){
			if(caso.getMetricWeight() != null){
				c = caso;
				timeStamp = caso.gettimestamp();
				break;
			}
			
		}
		
		for(Case ca : similarCases){
			if (ca.metricWeight != null){
				Date date2 = new Date(ca.getTimestamp().getYear(), ca.getTimestamp().getMonth(), ca.getTimestamp().getDate(), ca.getTimestamp().getHours(), ca.getTimestamp().getMinutes(), ca.getTimestamp().getSeconds());
				if(timeStamp.before(date2)){
					timeStamp = date2;
					c = ca;
				}
			}
		}
		return c;
	}
	
	public double getErrorCuadraticoMedio() {
		return errorCuadraticoMedio;
	}
	public void setErrorCuadraticoMedio(double errorCuadraticoMedio) {
		this.errorCuadraticoMedio = errorCuadraticoMedio;
	}
	public double calculateMSEError() {
		double error= 0;
		double cantidad = 0;
//		double promedio = 0;
		/*
		 * Busco el developer que llevo a cabo la issue para el cual cargo las metricas
		 */
		Developer devEstimatedMetrics = new Developer();
		for(Developer d : this.issuesWithDevelopersRecommended){
			if(d.getName().equals(this.performIssue.getName())){
				devEstimatedMetrics = d;
			}
		}
		
		Map<String, Double> realMetrics = this.performIssue.getIssues().get(0).getMetrics();
		Map<String, Double> estimatedMetrics = devEstimatedMetrics.getIssues().get(0).getMetrics();
		NormalizedError normalizeMetrics = new NormalizedError();
		realMetrics = normalizeMetrics.calculateNorm(realMetrics);
		estimatedMetrics = normalizeMetrics.calculateNorm(estimatedMetrics);
				
		Set<String> keysReal = realMetrics.keySet();
		Set<String> keysEstimated = estimatedMetrics.keySet();
		
		for(String m: keysReal){
			if(keysEstimated != null && keysEstimated.contains(m)){
//				promedio += realMetrics.get(m);
				error +=  Math.pow((realMetrics.get(m) - estimatedMetrics.get(m)), 2);
				cantidad++;
			}		
		}
		if(cantidad>1){
//			promedio = promedio / cantidad;
//			error = Math.sqrt(error/cantidad);
			error = error/cantidad;
			return (error);
		}
		else 
			return -1;
	}
	
	public void setPorcentajeErrorRelativoPromedio(double valor){
		this.porcentajeErrorRelativoPromedio = valor;
	}
	
	public double getPorcentajeErrorRelativoPromedio(){
		return this.porcentajeErrorRelativoPromedio;
	}
	public double calculateERP() {
		double error= 0;
		double cantidad = 0;
		/*
		 * Busco el developer que llevo a cabo la issue para el cual cargo las metricas
		 */
		Developer devEstimatedMetrics = new Developer();
		for(Developer d : this.issuesWithDevelopersRecommended){
			if(d.getName().equals(this.performIssue.getName())){
				devEstimatedMetrics = d;
			}
		}
		
		Map<String, Double> realMetrics = this.performIssue.getIssues().get(0).getMetrics();
		Map<String, Double> estimatedMetrics = devEstimatedMetrics.getIssues().get(0).getMetrics();
				
		Set<String> keysReal = realMetrics.keySet();
		Set<String> keysEstimated = estimatedMetrics.keySet();
		
		for(String m: keysReal){
			if(keysEstimated != null && keysEstimated.contains(m)){
				error += Math.abs(estimatedMetrics.get(m) - realMetrics.get(m))/Math.abs(estimatedMetrics.get(m))*100;
				cantidad++;
			}		}
		if(cantidad>1){
			return (error/cantidad);
		}
		else 
			return -1;
	}
}

package org.tesys.correlations;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.tesys.core.estructures.Developer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeveloperPrediction implements Comparable {
	
	private String name;
	private String displayName;
	private List<MetricPrediction> issues;
	/*Agregados*/
	private Double value;
	private static  String precedenceOrder;
	private static String metricOrder;
	
	public DeveloperPrediction() {}


	public DeveloperPrediction(String user, String userDisplay, MetricPrediction metricPred) {
		super();
		this.name = user;
		this.displayName = userDisplay;
		this.issues = new LinkedList<MetricPrediction>();
		this.issues.add(metricPred);
	}
	
	
	public String getDisplayName() {
		return displayName;
	}


	public void setDisplayName(String userDisplay) {
		this.displayName = userDisplay;
	}



	public String getName() {
		return name;
	}


	public void setName(String user) {
		this.name = user;
	}


	public List<MetricPrediction> getIssues() {
		return issues;
	}


	public void setIssues(List<MetricPrediction> metricPred) {
		this.issues = metricPred;
	}
	
	/*Metodos agregados*/
	
	//Ordena los developers segun una metrica
	public int compareTo(Object dev) {
		for(MetricPrediction m:issues){
	 		if(m.getMetrics().containsKey(metricOrder)){
		         value=m.getMetrics().get(metricOrder);
	 		}
	 	}
	 	 DeveloperPrediction developer = (DeveloperPrediction)dev; 
			if(precedenceOrder.equals("menor"))
				//Ordena de menor a mayor
				return this.value.compareTo(developer.value);
			else
				//Ordena de mayor a menor
				return developer.value.compareTo(this.value);
	}


	public static void setOrderCriteria(Map<String, String> criterion) {
		List<String>aux=(List<String>) criterion.keySet(); //Nose si va a andar
		metricOrder=aux.get(0);
		precedenceOrder=criterion.get(metricOrder);	
	}


	public static void setInverseOrderCriteria(Map<String, String> criterion) {
		List<String>aux=(List<String>) criterion.keySet(); //Nose si va a andar
		metricOrder=aux.get(0);
		// Invierte el orden de los desarrolladores
		if((criterion.get(metricOrder)).equals("mayor"))
			precedenceOrder="menor";
		else
			precedenceOrder="mayor";
	}
	  
}

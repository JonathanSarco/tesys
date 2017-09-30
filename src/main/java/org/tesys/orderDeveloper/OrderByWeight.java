package org.tesys.orderDeveloper;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.tesys.OrderWeight.MatrixWeight;
import org.tesys.core.estructures.Developer;
import org.tesys.core.estructures.MetricWeight;

import com.sun.xml.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

public class OrderByWeight {
	
	MetricWeight[] weightMetrics;
	MatrixWeight bestMetrics;
	
	public OrderByWeight(MetricWeight[] wMetrics){
		this.weightMetrics = wMetrics;
		this.bestMetrics = new MatrixWeight();
	}

	
	public List<Developer> compare(List<Developer> toOrder) {
		List<MetricWeight> metrics = Arrays.asList(weightMetrics);
		List<Developer>orderDev = new LinkedList<Developer>();
		String m0=metrics.get(0).getMetricName();
		String m1=metrics.get(1).getMetricName();
		String m2=metrics.get(2).getMetricName();
		String m3=metrics.get(3).getMetricName();
		//obtengo dev que tienen metrica 0
		List<Developer> metric0 = getDevContains(toOrder, m0);
		// los elimino de toOrder
		toOrder = deleteDevs(metric0, toOrder);
		//Ordeno
		OrderbyMetricValue orderM0 = new OrderbyMetricValue(m0, this.bestMetrics.getBestMetrics().get(m0));
		Collections.sort(metric0, orderM0);
		
		//obtengo dev que tienen metrica 1
		List<Developer> metric1 = getDevContains(toOrder, m1);
		// los elimino de toOrder
		toOrder = deleteDevs(metric1, toOrder);
		//Ordeno
		OrderbyMetricValue orderM1 = new OrderbyMetricValue(m1, this.bestMetrics.getBestMetrics().get(m1));
		Collections.sort(metric1, orderM1);
		
		//obtengo dev que tienen metrica 2
		List<Developer> metric2 = getDevContains(toOrder, m2);
		// los elimino de toOrder
		toOrder = deleteDevs(metric2, toOrder);
		//Ordeno
		OrderbyMetricValue orderM2 = new OrderbyMetricValue(m2, this.bestMetrics.getBestMetrics().get(m2));
		Collections.sort(metric2, orderM2);
		
		//obtengo dev que tienen metrica 3
		List<Developer> metric3 = getDevContains(toOrder, m3);
		// los elimino de toOrder
		toOrder = deleteDevs(metric3, toOrder);
		//Ordeno
		OrderbyMetricValue orderM3 = new OrderbyMetricValue(m3, this.bestMetrics.getBestMetrics().get(m3));
		Collections.sort(metric3, orderM3);
		
		orderDev.addAll(metric0);
		orderDev.addAll(metric1);
		orderDev.addAll(metric2);
		orderDev.addAll(metric3);
		orderDev.addAll(toOrder);
				
		return orderDev;
	}


	private List<Developer> deleteDevs(List<Developer> metricDev, List<Developer> toOrder) {
		List<Developer> aux = toOrder; 
		for(Developer d: metricDev){
			if(contains(toOrder, d)){
				aux.remove(d);
			}
		}
		return aux;
	}


	private boolean contains(List<Developer> toOrder, Developer d) {
		for(Developer dev :toOrder){
			if(dev.getName().equals(d.getName()))
				return true;
		}
		return false;
	}


	private List<Developer> getDevContains(List<Developer> toOrder, String m) {
		List<Developer> containsM= new LinkedList<Developer>();
		for(Developer d : toOrder){
			if(d.getIssues().get(0).getMetrics().containsKey(m)){
				containsM.add(d);
			}
		}
		return containsM;
	}	
	
}

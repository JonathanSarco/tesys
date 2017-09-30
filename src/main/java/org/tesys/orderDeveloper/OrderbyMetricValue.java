package org.tesys.orderDeveloper;

import java.util.Comparator;

import org.tesys.core.estructures.Developer;

public class OrderbyMetricValue implements Comparator<Developer>{
	String metricName;
	int order;
	
	public OrderbyMetricValue(String name, int order){
		metricName=name;
		this.order=order;
	}
	@Override
	public int compare(Developer o1, Developer o2) {
		//Si el orden es de menor a mayor
		if(order == 1)
			return (o1.getIssues().get(0).getMetrics().get(metricName).compareTo(o2.getIssues().get(0).getMetrics().get(metricName)));
		//Si es de mayor a menor
		return (o2.getIssues().get(0).getMetrics().get(metricName).compareTo(o1.getIssues().get(0).getMetrics().get(metricName)));
	}

}

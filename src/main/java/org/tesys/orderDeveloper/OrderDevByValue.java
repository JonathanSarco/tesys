package org.tesys.orderDeveloper;

import java.util.Comparator;

import org.tesys.core.estructures.Developer;

public class OrderDevByValue implements Comparator<Developer>{

	String metric;
	String precedence;
	
	public OrderDevByValue(String metric,String precedence){
		this.metric=metric;
		this.precedence=precedence;
	}

	public int compare(Developer dev1, Developer dev2) {
		double valor1 = dev1.getIssues().get(0).getMetrics() == null || dev1.getIssues().get(0).getMetrics().get(metric) == null ? 0 : dev1.getIssues().get(0).getMetrics().get(metric);
		double valor2 = dev2.getIssues().get(0).getMetrics() == null || dev2.getIssues().get(0).getMetrics().get(metric) == null ? 0 : dev2.getIssues().get(0).getMetrics().get(metric);
		if(precedence.equals("menor")){
			return (int) (valor1 - valor2);
		}
		return (int) (valor2 - valor1);
	}
	
	
}


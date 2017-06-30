package org.tesys.orderDeveloper;

import java.util.Comparator;
import java.util.Set;

import org.tesys.core.estructures.Developer;
import org.tesys.orderCriteria.CriteriaBestValues;
import org.tesys.orderCriteria.CriteriaSelector;

public class OrderDevByValue implements Comparator<Developer>{

	Object[] metrics;
	CriteriaSelector criteria;
	boolean invert;
	
	public OrderDevByValue(Object[] criteria2, boolean invert){
		this.metrics=criteria2;
		criteria=new CriteriaBestValues();
		this.invert=invert;
	}

	public int compare(Developer dev1, Developer dev2) {
		
		/* Si hay mas de un criterio, en el cual el developer elegido es el mejor*/
		if(metrics.length>1){
			for(Object m:metrics){
				if(dev1.getIssues().get(0).getMetrics()!= null && dev1.getIssues().get(0).getMetrics().get(m)!=null && dev2.getIssues().get(0).getMetrics()!= null && dev2.getIssues().get(0).getMetrics().get(m)!=null){
					String precedence=criteria.getCriteria().get(m);
					if(precedence.equals("menor") && !invert){
						return ((int)(dev1.getIssues().get(0).getMetrics().get(m) - dev2.getIssues().get(0).getMetrics().get(m)));
					}
					else{
						if(precedence.equals("mayor") && !invert)
							return ((int)(dev2.getIssues().get(0).getMetrics().get(m) - dev1.getIssues().get(0).getMetrics().get(m)));	
					else{
						if(precedence.equals("menor") && invert){
							return ((int)(dev2.getIssues().get(0).getMetrics().get(m) - dev1.getIssues().get(0).getMetrics().get(m)));
						}
						else{
							if(precedence.equals("mayor") && invert){
								return ((int)(dev1.getIssues().get(0).getMetrics().get(m) - dev2.getIssues().get(0).getMetrics().get(m)));
							}
						}
					}
				}
			 }
		  }
		}
		/*Si solo hay un criterio por el cual el developer elegido es el mejor, o los developers no tienen valor para esas metricas*/
		Object metric= metrics[0];
		double valor1 = dev1.getIssues().get(0).getMetrics() == null || dev1.getIssues().get(0).getMetrics().get(metric) == null ? 0 : dev1.getIssues().get(0).getMetrics().get(metric);
		double valor2 = dev2.getIssues().get(0).getMetrics() == null || dev2.getIssues().get(0).getMetrics().get(metric) == null ? 0 : dev2.getIssues().get(0).getMetrics().get(metric);
		String precedence=criteria.getCriteria().get(metric);
		
		if(precedence.equals("menor") && !invert){
			return (int) (valor1 - valor2);
			}
		else{
			if(precedence.equals("mayor") && !invert){
				return (int) (valor2 - valor1);
			}
			else
			{	if(precedence.equals("menor") && invert){
					return (int) (valor2 - valor1);
				}
			else{
				//if(precedence.equals("mayor") && invert){
					return (int) (valor1 - valor2);
				}
			  }
			 }
		  }
				
		
	
}


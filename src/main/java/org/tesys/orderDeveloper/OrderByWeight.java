package org.tesys.orderDeveloper;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.tesys.core.estructures.Developer;
import org.tesys.core.estructures.MetricWeight;
import org.tesys.orderCriteria.CriteriaBestValues;
import org.tesys.orderCriteria.CriteriaSelector;

import com.sun.xml.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

public class OrderByWeight implements Comparator<Developer> {
	
	MetricWeight[] weightMetrics;
	CriteriaSelector oCriteriaSelector;
	
	public OrderByWeight(MetricWeight[] wMetrics){
		this.weightMetrics = wMetrics;
		this.oCriteriaSelector = new CriteriaBestValues();
	}
	
	private List<MetricWeight> orderWightMetrics(){
		List<MetricWeight> metricsOrder = new LinkedList<MetricWeight>();
		for(MetricWeight m : weightMetrics){
			metricsOrder.add(m);
		}
		Collections.sort(metricsOrder); 
		return metricsOrder;
	}

	public int compare(Developer o1, Developer o2) {
		List<MetricWeight> metrics = Arrays.asList(weightMetrics);
		double result;
		for(MetricWeight m: metrics){
			if(o1.getIssues().get(0).getMetrics().get(m.getMetricName()) != null && o2.getIssues().get(0).getMetrics().get(m.getMetricName()) != null){
				result = ((o2.getIssues().get(0).getMetrics().get(m) * this.oCriteriaSelector.getCriteria().get(m)) - o1.getIssues().get(0).getMetrics().get(m) * this.oCriteriaSelector.getCriteria().get(m));
				if(result != 0)
					return (int)result;
			}
		}
		return 0;
	}
	
	
}

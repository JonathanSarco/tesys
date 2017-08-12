package org.tesys.orderCriteria;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Normalize {

	public Double calculate(Map<String, Double> metrics) {
		Double sum=0.0;
		Set<String> keys = metrics.keySet();
		for(String k:keys){
			sum+= Math.pow(metrics.get(k),2);
		}
		return Math.sqrt(sum);
	}
	
	public Map<String,Double> calculateNorm(Map<String, Double> metrics) {
		Map<String,Double> valuesByDev=new HashMap<String, Double>();
		Double sum=0.0;
		Set<String> keys = metrics.keySet();
		for(String k1:keys){
			for(String k:keys){
				sum+= Math.pow(metrics.get(k),2);
			}
		double dividendo= Math.sqrt(sum);
		valuesByDev.put(k1, (metrics.get(k1)/dividendo));
	}
		return valuesByDev;	

}

	
	
}
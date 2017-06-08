package org.tesys.distanceFunctions;

import java.util.Map;
import java.util.Set;


public class ManhattanFunction extends FunctionSelector { 
  	
	public double calculate(Map<String, Double> valores, Map<String, Double> desiredMetrics) {
		
		double sum = 0.0; 
		Set<String> keysVal = valores.keySet();
		for(String s : keysVal){
			sum += (Math.abs((valores.get(s) == null ? 0 : valores.get(s)) - (desiredMetrics.get(s) == null ? 0 : desiredMetrics.get(s))));
		}
		return sum; 
	}     

}
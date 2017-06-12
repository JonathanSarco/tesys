package org.tesys.distanceFunctions;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Vector; 
 
public class EuclideanFunction extends FunctionSelector { 

	@Override
	public double calculate(Map<String, Double> valores, Map<String, Double> desiredMetrics) {
		double sum = 0.0; 
		Set<String> keysVal = valores.keySet();
		for(String s : keysVal){
			sum += (Math.pow((valores.get(s) == null ? 0 : valores.get(s)) - (desiredMetrics.get(s) == null ? 0 : desiredMetrics.get(s)), 2));
		}
		return Math.sqrt(sum);
	} 
         
}
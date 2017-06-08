package org.tesys.distanceFunctions;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Vector; 
 
public class EuclideanFunction extends FunctionSelector { 
     
    public double calculate(List<Double>x){ 
        double sum = 0; 
        for (int i=0; i< x.size()-1; i++){ 
            sum += Math.pow(x.get(i) - x.get(i+1), 2); 
        } 
         
        return Math.sqrt(sum); 
    }

	@Override
	public double calculate(Map<String, Double> valores, Map<String, Double> desiredMetrics) {
		// TODO Auto-generated method stub
		return 0;
	} 
         
}
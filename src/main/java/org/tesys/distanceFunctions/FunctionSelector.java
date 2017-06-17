package org.tesys.distanceFunctions;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.tesys.correlations.MetricPrediction;

public abstract class FunctionSelector {

	public abstract double calculate(Map<String, Double>valores, Map<String, Double>desiredMetrics);
	
	public MetricPrediction getDistanceFunctionEstimationForDevelopers(List<MetricPrediction> metrics, FunctionSelector function, Map<String, Double> desiredmetrics) {
		
		Map<MetricPrediction, Double> distancias = new HashMap<MetricPrediction, Double>();
		for (MetricPrediction metric : metrics){
			Set<String> keys = metric.getMetrics().keySet();
			Map<String, Double> values = new HashMap<String, Double>();
			for(String s : keys){
				if(desiredmetrics.containsKey(s)){
					values.put(s, metric.getMetrics().get(s));
				}
			}
			distancias.put(metric, function.calculate(values, desiredmetrics));
		}
		
		Set<MetricPrediction> keysDistancias = distancias.keySet();
		double distanciaMin = 999999999;
		MetricPrediction minimo = new MetricPrediction();
		for (MetricPrediction mp : keysDistancias){
			if(distancias.get(mp) < distanciaMin){
				distanciaMin = distancias.get(mp);
				minimo = mp;
			}
		}
		
		return minimo;
	}
	

	private static String arrayToString(Vector<Double>array){ 
		StringBuilder res = new StringBuilder("[ "); 
		for (int i=0; i<array.size(); i++){ 
			res.append(" "); 
			res.append(String.format("%5.2f", array.get(i))); 
		} 
		res.append(" ]"); 
		return res.toString(); 
	} 

}
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
	
	public Map<String, Double> getDistanceFunctionEstimationForDevelopers(List<MetricPrediction> metrics, FunctionSelector function, Map<String, Double> desiredmetrics) {
		
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
		
		return minimo.getMetrics();
		
		
		
		
		
		
		
		/*
		
		
		//List<Double>functionValues=new LinkedList<Double>();
		Map<String, Double> result = new HashMap<String, Double>();
		List<Double>values=new LinkedList<Double>();
		List<String>keys=new LinkedList<String>();//nuevo
		int cantFilas = metrics.size();
		
		for (MetricPrediction m : metrics){
			Collection<String> claves=m.getMetrics().keySet();//nuevo
			for(String key : claves){
				if(!keys.contains(key)){
					keys.add(key);
				}
			}
		}

		
		int cantColumnas = keys.size();
		//Inicializo Matriz	
		double[][] matValues = new double[cantFilas][cantColumnas];
		for(int l=0;l<cantFilas;l++){
			for(int j=0;j<cantColumnas;j++){
				matValues[l][j]=0.0;
			}
		}
		//Completo Matriz con los valores de las Métricas estimadas
		for(int k=0;k<cantFilas;k++){
			for(int j=0;j<cantColumnas;j++){
				Set<String> keysm = metrics.get(k).getMetrics().keySet();
				for(String s : keysm){
					int poss = keys.indexOf(s);
					matValues[k][poss]=metrics.get(k).getMetrics().get(s);
				}
			}
		}

		//Recorrer matriz
		Vector<Double>aux= new Vector<Double>();
		for(int j=0;j<cantColumnas;j++){
			for(int m=0;m<cantFilas;m++){
				aux.add(matValues[m][j]);		
			}
			
			//manhattanValues.add(ManhattanFunction.manhattan(aux));
			//FunctionSelector function=new ManhattanFunction(); // se puede elegir otra función
			//functionValues.add(function.calculate(aux));
			//functionValues.add(function.calculate(aux));
			result.put(keys.get(j), function.calculate(aux));
			}
		
		return result;*/
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
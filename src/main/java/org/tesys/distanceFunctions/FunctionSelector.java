package org.tesys.distanceFunctions;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.tesys.correlations.MetricPrediction;

public abstract class FunctionSelector {

	public abstract double calculate(List<Double>valores);
	
	public Map<String, Double> getDistanceFunctionEstimationForDevelopers(List<MetricPrediction> metrics, FunctionSelector function) {
		
		//List<Double>functionValues=new LinkedList<Double>();
		Map<String, Double> result = new HashMap<String, Double>();
		List<Double>values=new LinkedList<Double>();
		List<String>keys=new LinkedList<String>();//nuevo
		int cantFilas = metrics.size();
		
		for (MetricPrediction m : metrics){
			Collection<Double>valores=m.getMetrics().values();
			for(Double val : valores){
				values.add(val);
			}
			Collection<String> claves=m.getMetrics().keySet();//nuevo
			for(String key : claves){
				keys.add(key);
			}
		}

		
		int cantColumnas = values.size();
		//Inicializo Matriz	
		double[][] matValues = new double[cantFilas][cantColumnas];
		for(int l=0;l<metrics.size();l++){
			for(int j=0;j<values.size();j++){
				matValues[l][j]=0.0;
			}
		}
		//Completo Matriz con los valores de las M�tricas estimadas
		for(int k=0;k<metrics.size();k++){
			for(int j=0;j<values.size();j++){
				matValues[k][j]=values.get(j);
			}
		}

		//Recorrer matriz
		Vector<Double>aux= new Vector<Double>();
		for(int j=0;j<values.size();j++){
			for(int m=0;m<metrics.size();m++){
				aux.add(matValues[m][j]);								
			}
			
			//manhattanValues.add(ManhattanFunction.manhattan(aux));
			//FunctionSelector function=new ManhattanFunction(); // se puede elegir otra funci�n
			//functionValues.add(function.calculate(aux));
			//functionValues.add(function.calculate(aux));
			result.put(keys.get(j), function.calculate(aux));
			}
		
		return result;
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
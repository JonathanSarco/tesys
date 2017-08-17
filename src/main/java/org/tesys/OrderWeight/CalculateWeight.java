package org.tesys.OrderWeight;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CalculateWeight {


	//Calcula los pesos de la matriz
	public  Map<String,Double> calculate(Map<String, Map<String, Double>> metricsWithValuesByDev) {
		
		Map<String,Double>weightsColumns=new HashMap<String, Double>(); 
		Set<String>metrics=metricsWithValuesByDev.keySet();		
		//Sumatoria de todas las columnas de (1-Entropia:Diversidad)
		Double sumDivEntropyCol=sumDivEntropyCol(metricsWithValuesByDev);
		
		for(String m:metrics){
			//Obtiene de cada columna (1- Entropia:Diversidad)
			Double DivEntropyCol=DivEntropyCol(m,metricsWithValuesByDev);
			//Peso de cada columna
			Double weightCol=(DivEntropyCol/sumDivEntropyCol);
			weightsColumns.put(m,weightCol);
		}		
		return weightsColumns;
	}

	//Sumatoria de todas las columnas de (1-Entropia:Diversidad)
	private Double sumDivEntropyCol(Map<String, Map<String, Double>> metricsWithValuesByDev) {
		Set<String>metrics=metricsWithValuesByDev.keySet();
		Double sum=0.0;
		for(String m:metrics){
			Double DivEntropyCol=DivEntropyCol(m, metricsWithValuesByDev);
			sum=sum+DivEntropyCol;
			}
		
		return sum;
	}

	//Obtiene de cada columna (1- Entropia:Diversidad)
	private Double DivEntropyCol(String k, Map<String, Map<String, Double>> metricsWithValuesByDev) {
		Double entropy=0.0;
		Double entropyCol=0.0;
		Double divEntropiaCol=0.0;

			Map<String,Double> values=metricsWithValuesByDev.get(k);
			for(String dev: values.keySet()){
							if(values.get(dev)!=0.0){
								entropy+=-((values.get(dev))*(Math.log((values.get(dev)))));
								}
							else{
								entropy+=values.get(dev);
								}
				}
			if(values.size()>1)
				entropyCol=entropy/Math.log(values.size());
			else
				entropyCol=0.0;
			divEntropiaCol=1-entropyCol;
			
			return divEntropiaCol;
	}
	
}

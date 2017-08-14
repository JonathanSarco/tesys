package org.tesys.OrderWeight;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CalculateWeight {


	//Calcula los pesos de la matriz
	public  Map<String,Double> calculate(Map<String, Map<String, Double>> metricsWithValuesByDev) {
		
		Map<String,Double>pesosColumnas=new HashMap<String, Double>(); 
		Set<String>metrics=metricsWithValuesByDev.keySet();		
		//Sumatoria de todas las columnas de (1-Entropia)
		Double sumDifEntropiaCol=sumDifEntropiaCol(metricsWithValuesByDev);
		
		for(String m:metrics){
			//Obtiene de cada columna (1- Entropia)
			Double DifEntropiaCol=DifEntropiaCol(m,metricsWithValuesByDev);
			//Peso de cada columna
			Double pesoCol=(DifEntropiaCol/sumDifEntropiaCol);
			pesosColumnas.put(m,pesoCol);
		}		
		return pesosColumnas;
	}

	//Sumatoria de todas las columnas de (1-Entropia)
	private Double sumDifEntropiaCol(Map<String, Map<String, Double>> metricsWithValuesByDev) {
		Set<String>metrics=metricsWithValuesByDev.keySet();
		Double suma=0.0;
		for(String m:metrics){
			Double InvEntropiaCol=DifEntropiaCol(m, metricsWithValuesByDev);
			suma=suma+InvEntropiaCol;
			}
		
		return suma;
	}

	//Obtiene de cada columna (1- Entropia)
	private Double DifEntropiaCol(String k, Map<String, Map<String, Double>> metricsWithValuesByDev) {
		Double entropia=0.0;
		Double entropiaCol=0.0;
		Double difEntropiaCol=0.0;

			Map<String,Double> values=metricsWithValuesByDev.get(k);
			for(String dev: values.keySet()){
							if(values.get(dev)!=0.0){
								entropia+=(values.get(dev))*(Math.log((values.get(dev))));
								}
							else{
								entropia+=values.get(dev);
								}
				}
			if(values.size()>1)
				entropiaCol=entropia/Math.log(values.size());
			else
				entropiaCol=0.0;
			difEntropiaCol=1-entropiaCol;
			
			return difEntropiaCol;
	}
	
}

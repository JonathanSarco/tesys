package org.tesys.OrderWeight;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CalculateWeight {


	//Calcula los pesos de la matriz
	public  Map<String,Double> calculate(Map<String, Map<String, Double>> metricsWithValuesByDev) {
		
		Map<String,Double>pesosColumnas=new HashMap<String, Double>(); 
		Set<String>keys=metricsWithValuesByDev.keySet();		
		Double sumEntropiaCol=sumEntropiaCol(metricsWithValuesByDev);
		
		for(String k:keys){
			Double InvEntropiaCol=InvEntropiaCol(k,metricsWithValuesByDev);
			//Peso de cada columna
			Double pesoCol=(InvEntropiaCol/sumEntropiaCol);
			pesosColumnas.put(k,pesoCol);
		}		
		return pesosColumnas;
	}

	//Sumatoria de la Entropia Inversa de todas las columnas
	private Double sumEntropiaCol(Map<String, Map<String, Double>> metricsWithValuesByDev) {
		Set<String>keys=metricsWithValuesByDev.keySet();
		Double suma=0.0;
		for(String k:keys){
			Double InvEntropiaCol=InvEntropiaCol(k, metricsWithValuesByDev);
			suma=suma+InvEntropiaCol;
			}
		
		return suma;
	}

	//Entropia Inversa de cada columna
	private Double InvEntropiaCol(String k, Map<String, Map<String, Double>> metricsWithValuesByDev) {
		Double entropia=0.0;
		Double entropiaCol=0.0;
		Double invEntropiaCol=0.0;

			Map<String,Double> values=metricsWithValuesByDev.get(k);
			double higher=(double) values.values().toArray()[0];
			for(String dev: values.keySet()){
							higher=values.get(dev);
							entropia+=-(higher*Math.log(higher));
							}
			entropiaCol=entropia/Math.log(values.size());
			invEntropiaCol=1-entropiaCol;
			
			return invEntropiaCol;
	}
	
}

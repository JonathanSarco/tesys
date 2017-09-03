package org.tesys.core.Normalizer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.sun.xml.bind.v2.schemagen.xmlschema.List;

public class NormalizeWeight {

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
		

			Object[]values=metrics.values().toArray();
			
			//Busca el menor valor de la columna
			Set<String> keys = metrics.keySet();
			Double less=(Double) values[0];
				for(String k:keys){
					if(metrics.get(k)<less){
						less=metrics.get(k);
					}
				}
				
				//Busca el mayor valor de la columna
				Double higher=(Double) values[0];
					for(String k:keys){
						if(metrics.get(k)>higher){
							higher=metrics.get(k);
						}
					}
					
				//Obtiene el valor normalizado de cada columna, si solo existen una fila, devuelve valor 1.0	
				for(String k:keys){
					if(higher!=less){
						Double valueNormalized= (metrics.get(k)-less)/(higher-less);
						valuesByDev.put(k,valueNormalized);
						}
					else{
						valuesByDev.put(k,1.0);
						}
				 }				
			return valuesByDev;

	}
	
}
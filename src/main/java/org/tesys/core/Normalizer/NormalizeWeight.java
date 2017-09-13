package org.tesys.core.Normalizer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.tesys.core.estructures.Developer;

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
	
	public Map<Developer,Double> calculateNorm(Map<Developer, Double> valuesByDev2) {

		Map<Developer,Double> valuesByDev=new HashMap<Developer, Double>();
		

			Object[]values=valuesByDev2.values().toArray();
			
			//Busca el menor valor de la columna
			Set<Developer> keys = valuesByDev2.keySet();
			Double less=(Double) values[0];
				for(Developer k:keys){
					if(valuesByDev2.get(k)<less){
						less=valuesByDev2.get(k);
					}
				}
				
				//Busca el mayor valor de la columna
				Double higher=(Double) values[0];
					for(Developer k:keys){
						if(valuesByDev2.get(k)>higher){
							higher=valuesByDev2.get(k);
						}
					}
					
				//Obtiene el valor normalizado de cada columna, si solo existen una fila, devuelve valor 1.0	
				for(Developer k:keys){
					if(higher!=less){
						Double valueNormalized= (valuesByDev2.get(k)-less)/(higher-less);
						valuesByDev.put(k,valueNormalized);
						}
					else{
						valuesByDev.put(k,1.0);
						}
				 }				
			return valuesByDev;

	}
	
}
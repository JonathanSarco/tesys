package org.tesys.core.Normalizer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.sun.xml.bind.v2.schemagen.xmlschema.List;

public class Normalize {

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
		
		
		//Busca el menor valor de la columna
		if(metrics != null){
			Set<String> keys = metrics.keySet();
			
			//Obtiene el valor normalizado de cada columna, si solo existen una fila, devuelve valor 1.0	
			for(String k:keys){
				//Busco el mayor valor de la metrica k
				ObteinMaxMetricValue maxMetricValue = new ObteinMaxMetricValue(k);
				String higherVal = maxMetricValue.execute().toString();
				Double higher = Double.parseDouble(higherVal);
				//Busco el menor valor de la metrica k
				ObteinMinMetricValue minMetricValue = new ObteinMinMetricValue(k);
				String lessVal = minMetricValue.execute().toString();
				Double less = Double.parseDouble(lessVal);
				
				if(higher!=less && !(less == 0.0 && higher == 0.0)){
					Double valueNormalized= (metrics.get(k)-less)/(higher-less);
					valuesByDev.put(k,valueNormalized);
				}
				else{
					valuesByDev.put(k,1.0);
				}
			 }				
		}
		return valuesByDev;

	}
	
}
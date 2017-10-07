package org.tesys.OrderWeight;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.tesys.core.estructures.Developer;

public class CalculateWeight {


	//Calcula los pesos de la matriz
	public  Map<String,Double> calculate(Map<String, Map<Developer, Double>> metricsWithValuesByDevNormalized) {
		
		Map<String,Double>weightsColumns=new HashMap<String, Double>(); 
		Set<String>metrics=metricsWithValuesByDevNormalized.keySet();		
		//Sumatoria de todas las columnas de (1-Entropia:Diversidad)
		Double sumDivEntropyCol=sumDivEntropyCol(metricsWithValuesByDevNormalized);
		
		for(String m:metrics){
			//Obtiene de cada columna (1- Entropia:Diversidad)
			Double DivEntropyCol=DivEntropyCol(m,metricsWithValuesByDevNormalized);
			//Peso de cada columna
			Double weightCol=(DivEntropyCol/sumDivEntropyCol);
			weightsColumns.put(m,weightCol);
		}		
		return weightsColumns;
	}

	//Sumatoria de todas las columnas de (1-Entropia:Diversidad)
	private Double sumDivEntropyCol(Map<String, Map<Developer, Double>> metricsWithValuesByDevNormalized) {
		Set<String>metrics=metricsWithValuesByDevNormalized.keySet();
		Double sum=0.0;
		for(String m:metrics){
			Double DivEntropyCol=DivEntropyCol(m, metricsWithValuesByDevNormalized);
			sum=sum+DivEntropyCol;
			}
		
		return sum;
	}

	//Obtiene de cada columna (1- Entropia:Diversidad)
	private Double DivEntropyCol(String k, Map<String, Map<Developer, Double>> metricsWithValuesByDevNormalized) {
		Double entropy=0.0;
		Double entropyCol=0.0;
		Double divEntropiaCol=0.0;

			Map<Developer, Double> values=metricsWithValuesByDevNormalized.get(k);
			for(Developer dev: values.keySet()){
							if(values.get(dev)!=0.0){
								entropy+=-((values.get(dev))*(Math.log((values.get(dev)))));
								}
							else{
								//VER
								entropy+=values.get(dev);
								}
				}
			if(values.size()>1)
				entropyCol=entropy/Math.log(values.size());
			else
				//ACA NO DEBERIA ENTRAR NUNCA!
				entropyCol=0.0;
			divEntropiaCol=1-entropyCol;
			
			return divEntropiaCol;
	}
	
}

package org.tesys.orderCriteria;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.tesys.core.estructures.Developer;

public class CriteriaEntropy extends CriteriaSelector {

	@Override
	public Map<String, String> obtenerValor(Developer chosenDeveloper,Map<String, Map<String, Double>> metricsWithValuesByDev) {
		
		Set<String>keys=metricsWithValuesByDev.keySet();
		Double entropia=0.0;
		Double entropiaCol=0.0;
		Double invEntropiaCol=0.0;
		Double sumEntropiaCol=0.0;
		List<Double>pesosCol=new LinkedList<Double>();
		List<Double>pesos=new LinkedList<Double>();
		
		for(String k:keys){
			Map<String,Double> values=metricsWithValuesByDev.get(k);
			double higher=(double) values.values().toArray()[0];
			for(String dev: values.keySet()){
							higher=values.get(dev);
							entropia+=-(higher*Math.log(higher));
							}
			entropiaCol=entropia/Math.log(values.size());
			invEntropiaCol=1-entropiaCol;
			sumEntropiaCol+=invEntropiaCol;
			pesosCol.add(invEntropiaCol);
			}
		//Vector de pesos para cada metrica
		for(Double peso:pesosCol){
			pesos.add(peso/sumEntropiaCol);
		}
		
		return null;
	}
	
}

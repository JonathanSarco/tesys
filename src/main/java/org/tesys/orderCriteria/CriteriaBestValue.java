package org.tesys.orderCriteria;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.tesys.core.estructures.Developer;


public class CriteriaBestValue extends CriteriaSelector{

	public Map<String,String> obtenerValor(Developer chosenDeveloper, Map<String,Map<String,Double>> metricsWithValuesByDev){
	
	Set<String>keys=metricsWithValuesByDev.keySet();
	Map<String,String>criteria=new HashMap<String, String>();
	Map<String,String>bestCriteria=new HashMap<String, String>();

	for(String k:keys){
			// Se devuelve en precedence si se debe comparar por mayor o menor
			String precedence=bestMetrics.get(k); 
			// Se obtienen en values todos los vectores <Desarrollador,Valor> que corresponden a esa metrica(k)
			Map<String,Double> values=metricsWithValuesByDev.get(k);
			if(precedence.equals("mayor")){
				String developerHigher=new String();
				double higher=(double) values.values().toArray()[0];
				for(String dev: values.keySet()){
							if(values.get(dev)>=higher){
								higher=values.get(dev);
								developerHigher=dev;
								}
							}
				//Si el desarrollador que se selecciono corresponde al desarrollador que tiene el mayor valor en esa metrica(k)
				//Se guarda esa metrica, y como se debe ordenar
				if(chosenDeveloper.getDisplayName().equals(developerHigher)){ 
					criteria.put(k, precedence);
					}
				}
			else{
				String developerLowest=new String();
				double lowest=(double) values.values().toArray()[0];
				for(String dev: values.keySet()){
						if(values.get(dev)<=lowest){
								lowest=values.get(dev);
								developerLowest=dev;
								}
							}
				//Si el desarrollador que se selecciono corresponde al desarrollador que tiene el menor valor en esa metrica(k)
				//Se guarda esa metrica, y como se debe ordenar
				if(chosenDeveloper.getDisplayName().equals(developerLowest)){ 
					criteria.put(k, precedence);
				}
			}
		}
	
		// Si hay mas de una metrica, en el cual ese desarrollador tiene mejor valor, se elige una sola de forma aleatoria
		int cont=0;	
		if(criteria.size()>1){
			//Devuelve una posicion entre 0 y el tamaño maximo de criteria	
			int pos=(int) (Math.random()* (criteria.size() - 0) + 0);
				for(String k:criteria.keySet()){
					if (pos==cont){
						bestCriteria.put(k,bestMetrics.get(k));
					}
					cont++;
				}
			return bestCriteria;
			}
		else
		{
			return criteria;
		}		
	}
}	

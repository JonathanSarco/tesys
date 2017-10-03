package orderCriteria;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import org.tesys.core.estructures.Developer;


public class CriteriaBestValue{

	public String obtenerValor(Developer chosenDeveloper, Map<String,Map<Developer,Double>> metricsWithValuesByDev, Hashtable<String, Integer> bestMetrics){
	
	Set<String>keys=metricsWithValuesByDev.keySet();
	String criteria=new String();

	for(String k:keys){
			// Se devuelve en precedence si se debe comparar por mayor o menor
			int precedence=bestMetrics.get(k); 
			// Se obtienen en values todos los vectores <Desarrollador,Valor> que corresponden a esa metrica(k)
			Map<Developer,Double> values=metricsWithValuesByDev.get(k);
			if(precedence==1){
				Developer developerHigher=new Developer();
				double higher=(double) values.values().toArray()[0];
				for(Developer dev: values.keySet()){
							if(values.get(dev)>=higher){
								higher=values.get(dev);
								developerHigher=dev;
								}
							}
				//Si el desarrollador que se selecciono corresponde al desarrollador que tiene el mayor valor en esa metrica(k)
				//Se guarda esa metrica, y como se debe ordenar
				if(chosenDeveloper.getDisplayName().equals(developerHigher)){ 
					criteria=k;
					}
				}
			else{
				Developer developerLowest=new Developer();
				double lowest=(double) values.values().toArray()[0];
				for(Developer dev: values.keySet()){
						if(values.get(dev)<=lowest){
								lowest=values.get(dev);
								developerLowest=dev;
								}
							}
				//Si el desarrollador que se selecciono corresponde al desarrollador que tiene el menor valor en esa metrica(k)
				//Se guarda esa metrica, y como se debe ordenar
				if(chosenDeveloper.getDisplayName().equals(developerLowest)){ 
					criteria=k;
				}
			}
		}
			return criteria;
			
	}
}
package org.tesys.orderCriteria;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;



//siempre busque el mejor valor por mayor, ver por cada metrica como es realmente
public class CriteriaBestValue extends CriteriaSelector{

	//CAMBIAR 3 por cantFilas y 4 por cantColumnas, son para la prueba esos valores
	public List<Integer> getCriterio(double[][] values, int developer,String value) {
		Map<Integer,Double>criteria=new HashMap<Integer,Double>();
		if(value.equals("mayor")){
			for(int j=0;j<4;j++){
			double higher=0.0;
			int developerHigher=-1;
			 //es mejor si es mayor
				for(int i=0;i<3;i++){
					if(values[i][j]>=higher){
						higher=values[i][j];
						developerHigher=i;}
					}
			//si el desarrollador corresponde al desarrollador que tiene el mayor valor en esa columna(criterio), guardo ese criterio
			if(developer==developerHigher){ 
				//agrego que se selecciono a ese developer por el criterio j(cada criterio es un numero de columna)
				//criteria.add(j);
				criteria.put(j,higher);
			}
		}		
	}
			else
			{ 	  for(int j=0;j<4;j++){
					double lowest=100.0;
					int developerLowest=-1;
					for(int i=0;i<3;i++){
					if(values[i][j]<=lowest){
						lowest=values[i][j];
						developerLowest=i;}	
					}
					//si el desarrollador corresponde al desarrollador que tiene el mayor valor en esa columna(criterio), guardo ese criterio
					if(developer==developerLowest){ 
						//agrego que se selecciono a ese developer por el criterio j(cada criterio es un numero de columna)
						//criteria.add(j);
						criteria.put(j,lowest);
					}
				}
			}
		//Si hay mas de un criterio(osea mas de un j en un vector, me quedo con el mejor criterio o j)
		List<Integer>bestCriteria = new LinkedList<Integer>();
		Iterator<Integer> criteriaKeys = criteria.keySet().iterator();
		if(criteria.size()>1){
			if(value.equals("mayor")){
				int bestHigher=0;			
				while( criteriaKeys.hasNext() ) {
					Integer key = criteriaKeys.next();
					Double valueKey = criteria.get(key);
					if(valueKey>bestHigher){
						bestHigher=key;}
					}
				/*for(int k=0;k<criteria.size();k++){
					if(criteria.get(k)>bestHigher){
						bestHigher=criteria.get(k);
					}
				}*/
				bestCriteria.add(bestHigher);
				return bestCriteria;
			}
			else
			{ //si es menor
				int bestHigher=0;
				while( criteriaKeys.hasNext() ) {
					Integer key = criteriaKeys.next();
					Double valueKey = criteria.get(key);
					if(valueKey>bestHigher){
						bestHigher=key;}
					}
				/*for(int k=0;k<criteria.size();k++){
					if(criteria.get(k)<bestHigher){
						bestHigher=criteria.get(k);
					}
				}*/
				bestCriteria.add(bestHigher);
				return bestCriteria;
			}
		}
		else
		{
			criteriaKeys = criteria.keySet().iterator();
			bestCriteria.add(criteriaKeys.next());
			return bestCriteria;
		}
	}


}	

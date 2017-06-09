package org.tesys.orderCriteria;

import java.util.List;
import java.util.Vector;

public class CriteriaBestValueReally extends CriteriaSelector {

	@Override
	public List<Integer> getCriterio(double[][] values, int developer) {
	//	List<Integer>criteria=new Vector<Integer>();
		double higher=0.0;
		int developerHigher=-1;
		for(int j=0;j<cantColumnas;j++){
			for(int i=0;i<cantFilas;i++){
				if(values[i][j]>higher){
					higher=values[i][j];
					developerHigher=i;
				}
				//si el desarrollador corresponde al desarrollador que tiene el mayo valor en esa columna(criterio), guardo ese criterio
				if(developer==developerHigher){ 
					//agrego que se selecciono a ese developer por el criterio j(cada criterio es un numero de columna)
					criteria.add(j);
				}
			}
		}
		//Si hay mas de un criterio, debo obtener aquel que tenga mayor correlacion con sus pares
		//falta plantearlo
			return criteria;
		
	}

	
}

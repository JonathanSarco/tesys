package org.tesys.orderCriteria;

import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

public class CriteriaBestValues extends CriteriaSelector {

	//CAMBIAR 3 por cantFilas y 4 por cantColumnas, son para la prueba esos valores
	@Override
	public List<Integer> getCriterio(double[][] values, int developer,String value) {
		List<Integer>criteria=new LinkedList<Integer>();
		if(value.equals("mayor")){
			for(int j=0;j<4;j++){
			double higher=0.0;
			int developerHigher=-1;
			 //es mejor si es mayor
				for(int i=0;i<3;i++){
					if(values[i][j]>higher){
						higher=values[i][j];
						developerHigher=i;}
					}
			//si el desarrollador corresponde al desarrollador que tiene el mayor valor en esa columna(criterio), guardo ese criterio
			if(developer==developerHigher){ 
				//agrego que se selecciono a ese developer por el criterio j(cada criterio es un numero de columna)
				criteria.add(j);
			}
		}		
	}
			else
			{ 	  for(int j=0;j<4;j++){
					double lowest=100.0;
					int developerLowest=-1;
					for(int i=0;i<3;i++){
					if(values[i][j]<lowest){
						lowest=values[i][j];
						developerLowest=i;}	
					}
					//si el desarrollador corresponde al desarrollador que tiene el mayor valor en esa columna(criterio), guardo ese criterio
					if(developer==developerLowest){ 
						//agrego que se selecciono a ese developer por el criterio j(cada criterio es un numero de columna)
						criteria.add(j);
					}
				}						
			}
			
		//Si hay mas de un criterio(osea mas de un j en un vector, devuelvo todos, no solo el mejor)	
			return criteria;	
	}

}

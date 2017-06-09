package org.tesys.orderCriteria;

import java.util.List;

public class prueba {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		CriteriaBestValue criterion=new CriteriaBestValue();
		CriteriaBestValues criterion1=new CriteriaBestValues();

		//criterion.buildArray(metrics);
		double[][] values = new double[3][4];
		values[0][0]=1.0; //metrica 1,desarrollador 1
		values[1][0]=3.0; //metrica 1,desarrollador 2
		values[2][0]=7.0; //metrica 1desarrollador 3

		values[0][1]=0.9; //metrica 2,desarrollador 1
		values[1][1]=0.7; //metrica 2,desarrollador 2
		values[2][1]=0.5; //metrica 2,desarrollador 3

		values[0][2]=2.0; //metrica 3,desarrollador 1
		values[1][2]=1.5; //metrica 3,desarrollador 2
		values[2][2]=1.8; //metrica 3,desarrollador 3

		values[0][3]=0.1; //metrica 4,desarrollador 1
		values[1][3]=0.3; //metrica 4,desarrollador 2
		values[2][3]=0.2; //metrica 4,desarrollador 3
		List<Integer>criteria=criterion.getCriterio(values, 0,"mayor");

		System.out.println("Matriz             m1   m2   m3   m4");
		for(int i=0;i<3;i++){
				System.out.println("Desarrollador "+i+"    "+values[i][0]+"  "+values[i][1]+"  "+values[i][2]+"  "+values[i][3]);
		}
		for(int i=0;i<criteria.size();i++){
			System.out.println("El mejor criterio para el desarrollador 2 es el que pertence a la columna:"+criteria.get(i));
		}

		List<Integer>criteria1=criterion1.getCriterio(values, 0,"mayor");
		for(int i=0;i<criteria1.size();i++){
			System.out.println("Los mejores criterios para el desarrollador 2 son los que pertencen a las columnas:"+criteria1.get(i));
		}
	}
}

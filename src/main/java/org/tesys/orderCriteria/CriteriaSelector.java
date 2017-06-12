package org.tesys.orderCriteria;

import java.util.Collection;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.tesys.correlations.MetricPrediction;

public abstract class CriteriaSelector {

	protected int cantFilas;
	protected int cantColumnas;
	//establece si la metrica es mejor por mayor o por menor
	protected Hashtable<String,String> bestMetrics= new Hashtable<String, String>(); 
	
	public void completeHash(List<MetricPrediction> metrics){
		
			//SonarQube
			bestMetrics.put("complexity", "mayor");
			bestMetrics.put("class_complexity","mayor");
			bestMetrics.put("function_complexity", "mayor");
			bestMetrics.put("file_complexity","mayor");
			
			bestMetrics.put("comment_lines_density","menor");  
			bestMetrics.put("comment_lines","menor"); 
			bestMetrics.put("public_documented_api_density","mayor");
			bestMetrics.put("public_undocumented_api","menor");

			bestMetrics.put("duplicated_blocks","menor");
			bestMetrics.put("duplicated_files","menor");
			bestMetrics.put("duplicated_lines","menor");
			bestMetrics.put("duplicated_lines_density","menor");

			bestMetrics.put("violations", "mayor");
			bestMetrics.put("critical_violations", "mayor");
			bestMetrics.put("minor_violations","");
			bestMetrics.put("major_violations","");
			bestMetrics.put("blocker_violations","mayor");

			bestMetrics.put("open_issues", "");
			bestMetrics.put("reopened_issues","");
			bestMetrics.put("confirmed_issues","mayor");
			bestMetrics.put("false_positive_issues","menor");
			bestMetrics.put("sqale_index","menor");
			
			bestMetrics.put("accessors", "mayor");
			bestMetrics.put("classes","mayor");
			bestMetrics.put("directories","mayor");
			bestMetrics.put("files","mayor");
			bestMetrics.put("lines","");
			bestMetrics.put("ncloc","menor");
			bestMetrics.put("functions","mayor");
			bestMetrics.put("statements","mayor");
			bestMetrics.put("public_api","mayor");

			// Jira
			bestMetrics.put("progress", "menor");
			bestMetrics.put("estimated","menor");

			//Git?
			bestMetrics.put("complexity_in_functions","mayor");
			bestMetrics.put("file_complexity_distribution", "mayor");
			bestMetrics.put("function_complexity_distribution","mayor");
			bestMetrics.put("sqale_debt_ratio","menor");
	}

		
	
	public double[][] buildArray(List<MetricPrediction> metrics/*,CriteriaSelector criterion*/){
		List<Double>values=new LinkedList<Double>();
		cantFilas = metrics.size();
		for (MetricPrediction m : metrics){
			Collection<Double>valores=m.getMetrics().values();
			for(Double val : valores){
				values.add(val);
			}
		}
		cantColumnas = values.size();
		//Inicializo Matriz	
		double[][] matValues = new double[cantFilas][cantColumnas];
		for(int l=0;l<metrics.size();l++){
			for(int j=0;j<values.size();j++){
				matValues[l][j]=0.0;
			}
		}
		//Completo Matriz con los valores de las Métricas estimadas
		for(int k=0;k<metrics.size();k++){
			for(int j=0;j<values.size();j++){
				matValues[k][j]=values.get(j);
			}
		}
		
		//Recorrer matriz->lo hago en el metodo de getCriterio
		/*		Vector<Double>aux= new Vector<Double>();
				for(int j=0;j<values.size();j++){
					for(int m=0;m<metrics.size();m++){
						aux.add(matValues[m][j]);								
					}
					
					//manhattanValues.add(ManhattanFunction.manhattan(aux));
					//FunctionSelector function=new ManhattanFunction(); // se puede elegir otra función
					//functionValues.add(function.calculate(aux));
					criteria.addAll(criterion.getCriterio(aux));
								
				}*/
				return matValues;
	}

	//devuelve las columnas(criterios) en los que el desarrollador seleccionado, tuvo el mejor valor
	public abstract List<Integer> getCriterio(double[][]values, int developer,String value);
	//el int en developers corresponde al nro de fila seleccionada por el usuario

}

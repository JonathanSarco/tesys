package org.tesys.orderCriteria;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.tesys.core.estructures.Developer;
import org.tesys.correlations.MetricPrediction;

public abstract class CriteriaSelector {

	//Establece si la metrica es mejor por mayor o por menor
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

		
	
	//Recibe las metricas estimadas de cada desarrollador, el desarrollador seleccionado para hacer la tarea y el criterio de mejor valor
	//Devuelve el nombre de la metrica por el cual ese desarrollador es mejor y si se debe ordenar por mayor o menor
	public Map<String,String> getMetricsToOrder(List<MetricPrediction> metrics, Developer chosenDeveloper, CriteriaSelector criterion){
				
		//Se obtiene en allKeys todas los nombres(keys) de las metricas estimadas por todos los desarrolladores
		List<String>allKeys=new LinkedList<>();
		for (MetricPrediction metric : metrics){
			Set<String> keys = metric.getMetrics().keySet();
			for(String k:keys){
				if(!allKeys.contains(k))
					allKeys.add(k);
			}
		}
		
		//Se arma un map metricsWithValuesByDev que va a tener por cada metrica, un conjunto de valores estimados por cada desarrollador
		Map<String,Map<String,Double>> metricsWithValuesByDev=new HashMap<String, Map<String,Double>>();
		Map<String,Double>ValuesByDev=new HashMap<String,Double>(); //el double es valor, y el String es el desarrollador
		for(int i=0;i<allKeys.size();i++){
			for (MetricPrediction metric : metrics){
				if(metric.getMetrics().containsKey(allKeys.get(i)))
					ValuesByDev.put(metric.getUser(),metric.getMetrics().get(allKeys.get(i)));
				metricsWithValuesByDev.put(allKeys.get(i),ValuesByDev);
			}
		}
		
		//Se obtiene en que metrica(por menor o por mayor segun corresponda), es mejor el desarrollador seleccionado
		Map<String,String>criterios=criterion.obtenerValor(chosenDeveloper, metricsWithValuesByDev);			
				
		return criterios;				
	}
	

	public abstract Map<String,String> obtenerValor(Developer chosenDeveloper, Map<String,Map<String,Double>> metricsWithValuesByDev);

}

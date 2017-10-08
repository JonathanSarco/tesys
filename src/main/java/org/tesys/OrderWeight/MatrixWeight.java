package org.tesys.OrderWeight;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.tesys.core.db.SearchCaseByIssueAndSkillsQuery;
import org.tesys.core.estructures.Case;
import org.tesys.core.estructures.Developer;
import org.tesys.core.estructures.Issue;
import org.tesys.core.Normalizer.NormalizeWeight;
import org.tesys.recomendations.SimilarCaseByIssueSkill;

import orderCriteria.CriteriaBestValue;

public class MatrixWeight {
	
	Hashtable<String,Integer> bestMetrics= new Hashtable<String, Integer>();
	
	public MatrixWeight(){
		//SonarQube
		bestMetrics.put("complexity",1);
		bestMetrics.put("class_complexity",1);
		bestMetrics.put("function_complexity",1);
		bestMetrics.put("file_complexity",1);
		
		bestMetrics.put("comment_lines_density",-1);  
		bestMetrics.put("comment_lines",-1); 
		bestMetrics.put("public_documented_api_density",1);
		bestMetrics.put("public_undocumented_api",-1);

		bestMetrics.put("duplicated_blocks",-1);
		bestMetrics.put("duplicated_files",-1);
		bestMetrics.put("duplicated_lines",-1);
		bestMetrics.put("duplicated_lines_density",-1);

		bestMetrics.put("violations",1);
		bestMetrics.put("critical_violations",1);
		bestMetrics.put("minor_violations",1);
		bestMetrics.put("major_violations",1);
		bestMetrics.put("blocker_violations",1);

		bestMetrics.put("open_issues",1);
		bestMetrics.put("reopened_issues",-1);
		bestMetrics.put("confirmed_issues",1);
		bestMetrics.put("false_positive_issues",-1);
		bestMetrics.put("sqale_index",-1);
		
		bestMetrics.put("accessors",1);
		bestMetrics.put("classes",1);
		bestMetrics.put("directories",1);
		bestMetrics.put("files",1);
		bestMetrics.put("lines",-1);
		bestMetrics.put("ncloc",-1);
		bestMetrics.put("functions",1);
		bestMetrics.put("statements",1);
		bestMetrics.put("public_api",1);

		// Jira
		bestMetrics.put("progress", -1);
		bestMetrics.put("estimated",-1);

		//Otras
		bestMetrics.put("complexity_in_functions",-1);
		bestMetrics.put("file_complexity_distribution",-1);
		bestMetrics.put("function_complexity_distribution",-1);
		bestMetrics.put("sqale_debt_ratio",-1);
		bestMetrics.put("info_violations",-1);
	}

	public Map<String,Double> getMetricsToOrder(Case newCase, Developer chosenDeveloper) {
		
		Map<Developer, Map<String, Double>>matrix = new HashMap<Developer, Map<String,Double>>();
				
				//Obtengo los casos de la base
				SearchCaseByIssueAndSkillsQuery dnq = new SearchCaseByIssueAndSkillsQuery(newCase.getIssue().getLabels(), newCase.getIssue().getSkills());
				List<Case> cases = dnq.execute();
								
				 //Se Buscan Casos similares al nuevo caso
				 				
				List<Case> similarCases = new LinkedList<Case>();
				for(Case c : cases){
					if(c.getIssue()!= null){
						if(SimilarCaseByIssueSkill.areSimilar(c.getIssue(), newCase.getIssue())){
							similarCases.add(c);
						}
					}
				}				
				
				//Construye matriz con los desarrrolladores asignados de los casos similares
				for(Case similarCase:similarCases){
					if(similarCase.getGoodRecommendation()!=0){
						if(similarCase.getPerformIssue() != null){
							for(Developer d :similarCase.getIssuesWithDevelopersRecommended()){
								if(d.getName().equals(similarCase.getPerformIssue().getName())){
									List<Issue>issues=d.getIssues();
									Map<String,Double>metrics=issues.get(0).getMetrics();
									matrix.put(d, metrics);
										}
									}
								}
					}	
				}
				
				//Si no hay casos similares, devuelve pesos con valor 1
				Set<Developer> keysSimilarCase = matrix.keySet();
				if(keysSimilarCase.isEmpty()){
					Map<String,Double>weights = new HashMap<String,Double>();
					for(Developer d : newCase.getIssuesWithDevelopersRecommended()){
						if(d.getName().equals(chosenDeveloper.getName())){
							Map<String,Double>metrics = d.getIssues().get(0).getMetrics();
							if(metrics!=null){
								Set<String>keysEstimated = metrics.keySet();
								for(String s: keysEstimated){
									weights.put(s, 1.0);
								}
							}
						}
					}
					return weights;
				}
				
				//Se agrega a la matriz el desarrollador asignado con sus métricas estimadas
				matrix.put(chosenDeveloper, chosenDeveloper.getIssues().get(0).getMetrics());
										
				/*Construcción de Matriz de Pesos*/
				
				//Se obtiene en allKeys todas los nombres(keys) de las metricas estimadas en comun por todos los desarrolladores asignados de los casos similares
				List<String>allKeys=new LinkedList<>();
				for(Developer d:matrix.keySet()){
					for(Issue issue:d.getIssues()){
							if(issue.getMetrics()!=null){
								Map<String,Double> metrics=issue.getMetrics();
								Set<String> keys = metrics.keySet();
								for(String k:keys){
									if(allConstainsMetric(k,matrix) && !allKeys.contains(k))
										allKeys.add(k);
								}
							}
					}
				}
				
				Map<String,Map<Developer,Double>> metricsWithValuesByDev=new HashMap<String, Map<Developer,Double>>();
				Map<String,Map<Developer,Double>> metricsWithValuesByDevNormalized=new HashMap<String, Map<Developer,Double>>();
				NormalizeWeight normalize=new NormalizeWeight();

				List<String> metricsWithout = new LinkedList<String>();
				//Se arma un map metricsWithValuesByDev que va a tener por cada metrica, un conjunto de valores estimados para cada desarrollador asignado en los casos similares
				for(String k:allKeys){
					Map<Developer,Double>ValuesByDev=new HashMap<Developer,Double>(); 
					
					for(Developer developer:matrix.keySet()){
						for( Issue issue :developer.getIssues()){
								if(issue.getMetrics()!=null){
									if(issue.getMetrics().containsKey(k)){
										Map<String,Double> metrics=issue.getMetrics();
										Double value=metrics.get(k);
										ValuesByDev.put(developer,value);
										metricsWithValuesByDev.put(k,ValuesByDev);
									}
									//Si no tienen valor para esa métrica, se les coloca cero como valor para la metrica
									//Solo se considera los valores 0 para la matriz de la entropia
									else{
										//ACA NO DE DEBERIA ENTRAR NUNCA
										ValuesByDev.put(developer,0.0);
										metricsWithValuesByDev.put(k,ValuesByDev);
									}
							}
						}
					}
					//Normalizacion de los valores de la matriz por columna entre 0 y1 
					Map<Developer,Double> ValuesByDevNormalized = normalize.calculateNorm(ValuesByDev);
					metricsWithValuesByDevNormalized.put(k,ValuesByDevNormalized);
				
				}
									
				CalculateWeight m=new CalculateWeight();
				Map<String,Double> weights= m.calculate(metricsWithValuesByDevNormalized);
							


				return weights;
					
	}
	
	private boolean allConstainsMetric(String k, Map<Developer, Map<String, Double>> matrix) {
		for(Developer d:matrix.keySet()){
			for(Issue issue:d.getIssues()){
					if(issue.getMetrics()!=null){
						Map<String,Double> metrics=issue.getMetrics();
						Set<String> keys = metrics.keySet();
						if(!keys.contains(k))
							return false;

					}
			}
		}
		return true;
	}

	public Hashtable<String, Integer> getBestMetrics(){
		return this.bestMetrics;
	}
}

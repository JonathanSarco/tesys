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
import org.tesys.recomendations.SimilarCaseByIssueSkill;

public class MatrixWeight {

	public Map<String,Double> getMetricsToOrder(Case newCase, Developer chosenDeveloper) {
		
		Map<Developer, Map<String, Double>>matrix = new HashMap<Developer, Map<String,Double>>();
		Hashtable<String,Integer> bestMetrics= new Hashtable<String, Integer>(); 
		
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
				
				//Obtengo los casos similares de la base
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
				
				for(Case similarCase:similarCases){
					Developer developer=similarCase.getPerformIssue();
					Developer[]developers= similarCase.getIssuesWithDevelopersRecommended();
					for(Developer d :developers){
						if(d.getName().equals(developer.getName())){
							List<Issue>issues=d.getIssues();
							Map<String,Double>metrics=issues.get(0).getMetrics();
							matrix=new HashMap<Developer, Map<String,Double>>();
							matrix.put(d, metrics);
						}
					}
				}
							
				//Construcci�n de Matriz de Pesos
				//Se obtiene en allKeys todas los nombres(keys) de las metricas estimadas por todos los desarrolladores
				List<String>allKeys=new LinkedList<>();
				for(Developer d:matrix.keySet()){
					//Se supone que tiene una sola issue, que es la nueva aun no asignada
					for(Issue issue:d.getIssues()){
							if(issue.getMetrics()!=null){
								Map<String,Double> metrics=issue.getMetrics();
								Set<String> keys = metrics.keySet();
								for(String k:keys){
									if(!allKeys.contains(k))
										allKeys.add(k);
								}
							}
					}
				}
				
				//Preprocesamiento para obtener el mejor valor de metrica del desarrollador elegido
				Map<String,Map<String,Double>> metricsWithValuesByDev=new HashMap<String, Map<String,Double>>();

				//Se arma un map metricsWithValuesByDev que va a tener por cada metrica, un conjunto de valores estimados por cada desarrollador
				for(String k:allKeys){
					Map<String,Double>ValuesByDev=new HashMap<String,Double>(); 
					for(Developer developer:matrix.keySet()){
						for( Issue issue :developer.getIssues()){
								if(issue.getMetrics()!=null /*&& issue.getMetrics().containsKey(k)*/){
									if(issue.getMetrics().containsKey(k)){
										Map<String,Double> metrics=issue.getMetrics();
										Double value=metrics.get(k)*bestMetrics.get(k);
										ValuesByDev.put(developer.getName(),value);
										metricsWithValuesByDev.put(k,ValuesByDev);
									}
									//Si no tienen valor para esa m�trica, se los coloca cero como valor para la metrica
									//Solo se considera los valores 0 para la matriz de la entropia
									else{
										ValuesByDev.put(developer.getDisplayName(),0.0);
										metricsWithValuesByDev.put(k,ValuesByDev);
									}
							}
						}
					}
				}
				
					CalculateWeight m=new CalculateWeight();
					return m.calculate(metricsWithValuesByDev);
					
	}
}
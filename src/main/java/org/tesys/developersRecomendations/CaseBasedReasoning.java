package org.tesys.developersRecomendations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.tesys.OrderWeight.MatrixWeight;
import org.tesys.core.analysis.skilltraceability.Skill;
import org.tesys.core.db.SearchCaseByIssueAndSkillsQuery;
import org.tesys.core.estructures.Case;
import org.tesys.core.estructures.Developer;
import org.tesys.core.estructures.Issue;
import org.tesys.core.estructures.MetricWeight;
import org.tesys.core.estructures.SimilarIssue;
import org.tesys.correlations.MetricPrediction;
import org.tesys.correlations.DeveloperPrediction;
import org.tesys.correlations.Predictions;
import org.tesys.distanceFunctions.FunctionSelector;
import org.tesys.distanceFunctions.ManhattanFunction;
import org.tesys.recomendations.DevelopersShortedBySimilarLabelsAndSkills;
import org.tesys.recomendations.SimilarCaseByIssueSkill;

public class CaseBasedReasoning {

	List<Case> cases= new ArrayList<Case>();
	
	public CaseBasedReasoning(){
	}	

	public static Case getRecommendation(double factorLabel, double factorSkill, int sprint, Issue issue, Map<String, Double> desiredmetrics, List<String> skills){
		
		List<DeveloperPrediction> similarDevelopersPredictions;

		/*
		 * Se Crea La Issue Nueva en base a la Issue que tengo por parametro
		 */
		
		Issue newIssue = new Issue();
		newIssue.setLabels(issue.getLabels());
		newIssue.setIssueId(issue.getIssueId());
		newIssue.setIssueType(issue.getIssueType());
		newIssue.setUser("");
		newIssue.setMetrics(desiredmetrics);
		
		/*
		 * Agrego las Skills en la nueva tarea
		 */
		
		List<Skill> desiredSkills = new LinkedList<Skill>();
		for (String s : skills){
			Skill newSkill = new Skill();
			newSkill.setName(s);
			desiredSkills.add(newSkill);
		}
		
		newIssue.setSkills(desiredSkills);
		//*** FIN NUEVA ISSUE ***
		
		/*
		 * Se Crea el Nuevo Caso Incompleto
		 */
		
		Case newCase = new Case();
		newCase.setIssue(newIssue);
		newCase.settimestamp();
		newCase.setIdCase(newCase.gettimestamp().toString());
		
		//Obtengo los casos similares de la base
		SearchCaseByIssueAndSkillsQuery dnq = new SearchCaseByIssueAndSkillsQuery(newIssue.getLabels(), newIssue.getSkills());
		List<Case> cases = dnq.execute();
		
		/*
		 * Se Buscan Casos similares a la Issue nueva
		 */
		
		List<Case> similarCases = new LinkedList<Case>();
		for(Case c : cases){
			if(c.getIssue()!= null){
				if(SimilarCaseByIssueSkill.areSimilar(c.getIssue(), issue)){
					similarCases.add(c);
				}
			}

		}
		//** FIN NUEVO CASO ***
		
		List<SimilarIssue> similarIssues= new LinkedList<SimilarIssue>();
		List<Developer> similarDevelopers = new LinkedList<>();
		Predictions predictions = new Predictions();
		
		/*
		 * Busco Las Issues Similares y obtengo de esas issues los desarrolladores 
		 */
		
		similarIssues.addAll(DevelopersShortedBySimilarLabelsAndSkills.getDevelopersShortedBySimilarLabelsAndSkills(issue,factorLabel,factorSkill));
		
		//ver si nos quedamos con los que tengan mejor coeficiente
		
		similarDevelopers = getAllSimilarDevelopers(similarIssues);
		for(Case c: similarCases){
			if(c.getPerformIssue() != null){
				Developer d = addDevOrIssue(c.getPerformIssue(), similarDevelopers);
				if(d != null && !similarDevelopers.contains(d)){
					similarDevelopers.add(d);
				}
			}
		}
		
		/*
		 *Recupero los desarrolladores similares de los casos teniendo en cuenta los desarrolladores de los casos similares 
		 */
		if(similarCases != null && similarCases.size() > 0){
			similarDevelopers = addDevelopersInSimilarDevelopers(similarDevelopers, similarCases);
		}
		
		/*
		 * Recorro los desarrolladores similares para obtener la correlación entre las tareas del mismo
		 */
		
		double correlationVariation=0.1;
		List<DeveloperPrediction> developerPredictions = new LinkedList<DeveloperPrediction>();
		Iterator<String> metricsKeys = desiredmetrics.keySet().iterator();
		while( metricsKeys.hasNext() ) {
			String key = metricsKeys.next();
		    Double valueKey = desiredmetrics.get(key);
			//*** Issue se completa con las metricas estimadas y luego se agrega al developer ***
		    developerPredictions = predictions.getPredictions(key, valueKey, correlationVariation, sprint, skills);
		}
		List<Developer> developerWithNewIssue = new LinkedList<Developer>();
		similarDevelopersPredictions=new LinkedList<DeveloperPrediction>();
		
		for(Developer developer: similarDevelopers){
			/*
			 * Copio el Desarrollador en una nuevo objeto sin tareas asignadas
			 * Creo una copia de la Tarea Nueva Para cada desarrollador
			 */
			List<MetricPrediction> metrics = new LinkedList<MetricPrediction>();
			Issue issueDev = new Issue ();
			issueDev.setIssueId(newIssue.getIssueId()); 
			issueDev.setIssueType(newIssue.getIssueType());
			issueDev.setLabels(newIssue.getLabels());
			issueDev.setPuntuaciones(newIssue.getPuntuaciones());
			issueDev.setSkills(newIssue.getSkills());
			Developer similarDev = new Developer();
			similarDev.setDisplayName(developer.getDisplayName());
			similarDev.setName(developer.getName());
			
			
			/*
			 * Obtengo las metricas Estimadas para cada developer Similar
			 * 
			 */
			for(DeveloperPrediction dv : developerPredictions){
				if(dv.getName().equals(developer.getName())){
					metrics.addAll(dv.getIssues());
					/* Guardo en similarDevelopersPredictions, solo aquellos developerPredicition que son similarDeveloper*/
					similarDevelopersPredictions.add(dv);
				}
			}
			/*
			 * Se puede Cambiar a distancia euclidea o cambiar por otra función extensible
			 */
			FunctionSelector function=new ManhattanFunction(); // Acá se define el tipo de funcion: manhattan, euclidea, otra
			//Aca modifique para que nos devuelva una MetricPrediction, en vez de MetricPrediction.getMetrics()
			MetricPrediction mp=function.getDistanceFunctionEstimationForDevelopers(metrics,function, desiredmetrics); // Construccion de matriz
			Map<String, Double> values=	mp.getMetrics();		
			issueDev.setMetrics(values);
			List<Issue>unasignedIssues = new LinkedList <Issue>();
			unasignedIssues.add(issueDev);
			similarDev.setIssues(unasignedIssues);
			developerWithNewIssue.add(similarDev);
		}
	
		/*Se guardan los desarrolladores similares del nuevo caso*/
		Developer deveoperComplete[] = new Developer[developerWithNewIssue.size()];
		deveoperComplete = developerWithNewIssue.toArray(deveoperComplete);
		newCase.setIssuesWithDevelopersRecommended(deveoperComplete);	
		
		/*Devuelve desarrolladores similares ordenados por algun criterio(solo si existen)*/
		if(newCase.getIssuesWithDevelopersRecommended() != null && newCase.getIssuesWithDevelopersRecommended().length > 0){
			newCase.orderDeveloperByWeight(similarCases);
		}
		return newCase;	
	}

	private static Developer addDevOrIssue(Developer performIssue, List<Developer> similarDevelopers) {
		for(Developer d : similarDevelopers){
			if(d.getName().equals(performIssue.getName())){
				d.getIssues().addAll(performIssue.getIssues());
				return d;
			}
		}
		return performIssue;
	}

	private static List<Developer> addDevelopersInSimilarDevelopers(List<Developer> similarDevelopers, List<Case> similarCases) {
		List<Developer>similarDevWithCases = new LinkedList<Developer>();
		for(Case c: similarCases){
			for(Developer d: c.getIssuesWithDevelopersRecommended()){
				if(similarDevelopers.contains(d)){
					Developer devAddIssue = getDeveloper(similarDevelopers, d, c);
					List<Issue> newIssues = d.getIssues();
					newIssues.addAll(devAddIssue.getIssues());
					devAddIssue.setIssues(newIssues);
					
				}
			}
		}
		return similarDevWithCases;
	}

	private static Developer getDeveloper(List<Developer> similarDevelopers, Developer d, Case c) {
		for(Developer dev :similarDevelopers){
			if(dev.getName().equals(d.getName())){
				if(c.getPerformIssue().getName().equals(d.getName()) && c.getGoodRecommendation() != -1){
					return c.getPerformIssue();
				}
				return dev;
			}
		}
		return null;
	}

	private List<String> getDevSkillsForIssue(Developer d) {
		List<String>skills=new LinkedList<String>();
		List<Issue> issues = d.getIssues();
		for (Issue i : issues){
			List<Skill> skillsIssue = i.getSkills();
			for(Skill s : skillsIssue){
				skills.add(s.getName());
			}
		}
		return skills;
	}

	private static List<Developer> getAllSimilarDevelopers(List<SimilarIssue> similarIssues) {
		List<Developer> developers = new LinkedList<Developer>();
		for(SimilarIssue si : similarIssues){
			if(!developers.contains(si.getDeveloper())){
				developers.add(si.getDeveloper());
			}
		}
		return developers;
	}

	//Para setear el criterio de orden a los nuevos casos, ver donde lo llamo
	public static Case setOrderCriteriaNewCase(Developer chosenDeveloper, Case newCase){

//		Map<String,String> orderCriteria=criterion.getMetricsToOrder(newCase.getIssuesWithDevelopersRecommended(),chosenDeveloper, criterion);
		MatrixWeight weights=new MatrixWeight();
		Map<String,Double> pesos=weights.getMetricsToOrder(newCase, chosenDeveloper);
		MetricWeight[] metric = newCase.convertHashToVector(pesos);
		List<MetricWeight> metricOrdered = Arrays.asList(metric);
		Collections.sort(metricOrdered);
		MetricWeight metricOrderedArray[] = new MetricWeight[metricOrdered.size()];
		metricOrderedArray = (MetricWeight[]) metricOrdered.toArray();
		newCase.setCriteria(metricOrderedArray);
		
//		Set<String>criteria= orderCriteria.keySet();
//		newCase.setCriteria(pesos);
		return newCase;
	}
}
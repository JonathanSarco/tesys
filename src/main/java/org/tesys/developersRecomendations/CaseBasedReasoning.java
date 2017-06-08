package org.tesys.developersRecomendations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.tesys.core.analysis.skilltraceability.Skill;
import org.tesys.core.db.AnalysisVersionsQuery;
import org.tesys.core.db.ElasticsearchDao;
import org.tesys.core.estructures.Case;
import org.tesys.core.estructures.Developer;
import org.tesys.core.estructures.Issue;
import org.tesys.core.estructures.Metric;
import org.tesys.core.estructures.SimilarIssue;
import org.tesys.core.estructures.metrictypes.NumericMetric;
import org.tesys.core.estructures.metricvalue.Constant;
import org.tesys.core.estructures.metricvalue.SimpleValue;
import org.tesys.correlations.MetricPrediction;
import org.tesys.correlations.DeveloperPrediction;
import org.tesys.correlations.Predictions;
import org.tesys.distanceFunctions.FunctionSelector;
import org.tesys.distanceFunctions.ManhattanFunction;
import org.tesys.recomendations.DevelopersShortedBySimilarLabelsAndSkills;
import org.tesys.recomendations.IssueSimilarityLabels;
import org.tesys.recomendations.SimilarCaseByIssueSkill;

import com.atlassian.util.concurrent.Function;


public class CaseBasedReasoning {

	List<Case> cases= new ArrayList<Case>();
	//ManhattanFunction manhattan=new ManhattanFunction();
	public CaseBasedReasoning(){



	}

	public static List<Case> getDevRecommendationbyIssue(double factorLabel, double factorSkill, String metricKey, double value, int sprint)	{

		AnalysisVersionsQuery avq = new AnalysisVersionsQuery();
		List<Long> versiones = avq.execute();
		ElasticsearchDao<Case> dao;
		ResponseBuilder response = Response.ok("{\"status\":\"404\"}");
		List<Case> cases = new LinkedList<Case>();
		List<Case>dbCases = new LinkedList<Case>();

		try {
			dao = new ElasticsearchDao<Case>(
					Case.class, 
					ElasticsearchDao.DEFAULT_RESOURCE_CASE); //devuelve la version mas actualizada de los analisis.
		} catch (Exception e) {
			return cases;
		}

		cases = dao.readAll();
		if(cases.isEmpty()){
			//dbCases = getRecommendation(factorLabel, factorSkill, metricKey, value, sprint);
			if(dbCases != null && !dbCases.isEmpty()){
				for(Case c : dbCases){
					//dao.create(c.getId(), c);
				}			
			}
		}
		//response = Response.ok(cases);

		return cases;



	}

	public static Case getRecommendation(double factorLabel, double factorSkill, String metricKey, double value, int sprint, Issue issue, Map<String, Double> desiredmetrics){
		/*
		 * Se Crea La Issue Nueva en base a la Issue que tengo por parametro
		 */
		
		Issue newIssue = new Issue();
		newIssue.setLabels(issue.getLabels());
		newIssue.setIssueId(issue.getIssueId());
		newIssue.setIssueType(issue.getIssueType());
		newIssue.setUser("");;
		//newIssue.setSkills(skills);
		newIssue.setMetrics(desiredmetrics);
		
		//*** FIN NUEVA ISSUE ***
		
		/*
		 * Se Crea el Nuevo Caso Incompleto
		 */
		
		Case newCase = new Case();
		newCase.setIssue(newIssue);
		
		//Obtengo los casos de la base
		ElasticsearchDao<Case> dao;
		dao = new ElasticsearchDao<Case>(Case.class,ElasticsearchDao.DEFAULT_RESOURCE_CASE);
		List<Case> cases = dao.readAll();
		
		newCase.setIdCase(cases.size()+1);
		
		/*
		 * Se Buscan Casos similares a la Issue nueva
		 */
		List<Case> similarCases = new LinkedList<Case>();
		for(Case c : cases){
			if(SimilarCaseByIssueSkill.areSimilar(c.getIssue(), issue)){
				similarCases.add(c);
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

		List<Metric>metricsEstimateForDev = new LinkedList<Metric>();

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
		    developerPredictions = predictions.getPredictions(key, valueKey, correlationVariation, sprint, null);
		}
		List<Developer> developerWithNewIssue = new LinkedList<Developer>();
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
			similarDev.setTimestamp(developer.getTimestamp());
			
			/*
			 * Obtengo las metricas Estimadas para cada developer Similar
			 * 
			 */
			for(DeveloperPrediction dv : developerPredictions){
				if(dv.getName().equals(developer.getName())){
					metrics.addAll(dv.getIssues());
				}
			}
			/*
			 * Se puede Cambiar a distancia euclidea o cambiar por otra función extensible
			 */
			FunctionSelector function=new ManhattanFunction(); // Acá se define el tipo de funcion: manhattan, euclidea, otra
			Map<String, Double> values=function.getDistanceFunctionEstimationForDevelopers(metrics,function, desiredmetrics); // Construccion de matriz
			issueDev.setMetrics(values);
			List<Issue>unasignedIssues = new LinkedList <Issue>();
			unasignedIssues.add(issueDev);
			similarDev.setIssues(unasignedIssues);
			developerWithNewIssue.add(similarDev);
		}
		Developer deveoperComplete[] = new Developer[developerWithNewIssue.size()];
		deveoperComplete = developerWithNewIssue.toArray(deveoperComplete);
		newCase.setIssuesWithDevelopersRecommended(deveoperComplete);
		/*
		 * Agregar La Lógica para comparar con el casoNuevo y el caso de la base de casos 
		 */
	return newCase;	
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

}

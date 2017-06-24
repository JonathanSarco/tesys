package org.tesys.developersRecomendations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
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
import org.tesys.core.db.SearchCaseByIssueAndSkillsQuery;
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
import org.tesys.orderCriteria.*;
import org.tesys.orderDeveloper.OrderDevbyName;

import com.atlassian.util.concurrent.Function;


public class CaseBasedReasoning {

	List<Case> cases= new ArrayList<Case>();
	//ManhattanFunction manhattan=new ManhattanFunction();

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
		//	newSkill.setWeight(1);
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
	
		/*Se guardan los desarrolladores similares del nuevo caso( si existen)*/
		
		/*Si no existen desarrolladores similares en la base de datos, se guarda en el nuevo caso los desarrolladores de un caso similar que sea buena recomendacion*/
		/*Si no hay ningun caso que sea buena recomendacion, se devuelve el primer caso que encuentre*/
		if(developerWithNewIssue.isEmpty() && developerWithNewIssue.size()==0 && !similarCases.isEmpty() && similarCases.size()>0 ){
			boolean found=false;
			Case similarCaseGood=null;
			for(int i=0;i<similarCases.size() && !found;i++){
				if(similarCases.get(i).isGoodRecommendation()){
					similarCaseGood=similarCases.get(i);
					found=true;
				}
			}
			if(similarCaseGood != null){
				newCase.setIssuesWithDevelopersRecommended(similarCaseGood.getIssuesWithDevelopersRecommended());

			}
			else{
				//Elijo el primero, ya que cualquiera es una mala recomendacion
				Case similarCaseBad=similarCases.get(0);
				newCase.setIssuesWithDevelopersRecommended(similarCaseBad.getIssuesWithDevelopersRecommended());
			}
		}
		else{
			/*
			 *  * Adicionalmente se modifica la lista de desarrolladores similares según la clasificación del caso
			 * - Si fue bueno agrego el desarrollador seleccionado al caso nuevo si es q no esta entre los desarrolladores similares
			 * - Si fue malo elimino al desarrollador seleccionado de los similares. 
			 */
			if(!developerWithNewIssue.isEmpty() && developerWithNewIssue.size()>0 &&!similarCases.isEmpty() && similarCases.size() > 0){
				Developer deveoperComplete[] = new Developer[developerWithNewIssue.size()];
				deveoperComplete = developerWithNewIssue.toArray(deveoperComplete);
				newCase.setIssuesWithDevelopersRecommended(deveoperComplete);
				adaptNewCase(similarCases, newCase);
			}
			else{
				/*Si existen desarrolladores similares en la base de datos y no hay casos similares, los guardo en el nuevo caso*/
				if(!developerWithNewIssue.isEmpty() && developerWithNewIssue.size()>0 && similarCases.isEmpty() && similarCases.size()==0){
					Developer deveoperComplete[] = new Developer[developerWithNewIssue.size()];
					deveoperComplete = developerWithNewIssue.toArray(deveoperComplete);
					newCase.setIssuesWithDevelopersRecommended(deveoperComplete);
			}
				else{
					newCase.setIssuesWithDevelopersRecommended(null);
				}
		}
	}		
		
		/*Devuelve desarrolladores similares ordenados por algun criterio(solo si existen)*/
		if(newCase.getIssuesWithDevelopersRecommended()!=null){
			newCase.orderDeveloper(similarCases);
			return newCase;	
		}
		else{
			return newCase;
		}
	
}

	private static Case adaptNewCase(List<Case> similarCases, Case newCase) {
		List<Developer> developers = Arrays.asList(newCase.getIssuesWithDevelopersRecommended());
		for(Case c: similarCases){
			if(c.getPerformIssue() != null){	
				if(c.isGoodRecommendation()){
					boolean existsDeveloper = false;
					if(!developers.contains(c.getPerformIssue())){
						Developer developer = c.getPerformIssue();
						developer.setIssues(developers.get(0).getIssues());
						developers.add(developer);
					}			
				}
				else{
					if(developers.contains(c.getPerformIssue())){
						developers.remove(c.getPerformIssue());
					}		
				}
			}
		}
		Developer deveoperComplete[] = new Developer[developers.size()];
		deveoperComplete = (Developer[]) developers.toArray();
		newCase.setIssuesWithDevelopersRecommended(deveoperComplete);
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

//Para setear el criterio de orden a los nuevos casos, ver donde lo llamo
public static Case setOrderCriteriaNewCase(Developer chosenDeveloper, Case newCase){
	//Por pantalla quizas no voy a tener MetricPrediction, lo que si voy a tener son todos los desarrolladores similares
	//le paso todos los desarrolladores similares, y por cada uno puedo obtener un vector de  private List<Issue> issues, y cada issue
	//tiene un Map<String, Double> measures, debo buscar la issue correspondiente al nuevo caso, pq es la no asignada aun;
	CriteriaSelector criterion=new CriteriaBestValue();
	Map<String,String> orderCriteria=criterion.getMetricsToOrder(newCase.getIssuesWithDevelopersRecommended(),chosenDeveloper, criterion);
	newCase.setOrderCriteria(orderCriteria);
	return newCase;
}

}

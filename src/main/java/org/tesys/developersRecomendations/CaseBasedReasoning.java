package org.tesys.developersRecomendations;

import java.util.ArrayList;
import java.util.Collection;
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
import org.tesys.core.estructures.Developer;
import org.tesys.core.estructures.Issue;
import org.tesys.core.estructures.Metric;
import org.tesys.core.estructures.SimilarIssue;
import org.tesys.core.estructures.metrictypes.NumericMetric;
import org.tesys.core.estructures.metricvalue.SimpleValue;
import org.tesys.correlations.MetricPrediction;
import org.tesys.correlations.DeveloperPrediction;
import org.tesys.correlations.ManhattanFunction;
import org.tesys.correlations.Predictions;
import org.tesys.recomendations.DevelopersShortedBySimilarLabelsAndSkills;

public class CaseBasedReasoning {

	List<Case> cases= new ArrayList<Case>();
	ManhattanFunction manhattan=new ManhattanFunction();

	public CaseBasedReasoning(){



	}

	public static List<Case> getDevRecommendationbyIssue(double factorLabel, double factorSkill, String metricKey, double value, int sprint)	{

		AnalysisVersionsQuery avq = new AnalysisVersionsQuery();
		List<Long> versiones = avq.execute();
		ElasticsearchDao<Case> dao;
		ResponseBuilder response = Response.ok("{\"status\":\"404\"}");
		
		List<Case>dbCases=new LinkedList<Case>();

		try {
			dao = new ElasticsearchDao<Case>(
					Case.class, 
					ElasticsearchDao.DEFAULT_RESOURCE_CASE ); //devuelve la version mas actualizada de los analisis.
		} catch (Exception e) {
			return (List<Case>) response.build();
		}

		List<Case> cases = dao.readAll();
		if(cases.isEmpty()){
		//	dbCases = getRecommendation(factorLabel, factorSkill, metricKey, value, sprint);
			if(dbCases != null && !dbCases.isEmpty()){
				for(Case c : dbCases){
					dao.create(c.getId(), c);;
				}			
			}
		}
		response = Response.ok();

		return (List<Case>) response.build();



	}

	public static List<Developer> getRecommendation(double factorLabel, double factorSkill, String metricKey, double value, int sprint){

		//aca los factores no deberian ser cero para que no influya despues durante la recomendacion??
		ElasticsearchDao<Developer> daoi = new ElasticsearchDao<Developer>(Developer.class,
				ElasticsearchDao.DEFAULT_RESOURCE_DEVELOPERS);
		List<SimilarIssue> similarIssues= new LinkedList<SimilarIssue>();
		List<Developer> ld  = daoi.readAll();
		List<Developer> similarDevelopers = new LinkedList<>();
		Vector<Double>manhattanValues=new Vector<Double>(); 
		Predictions predictions = new Predictions();
		
		List<Case>dbCases=new LinkedList<Case>();

		for (Developer d : ld) {			
			List<Issue> li = d.getIssues();
			for (Issue i : li) {
				Case caseDevSmilar = new Case();
				similarIssues.addAll(DevelopersShortedBySimilarLabelsAndSkills.getDevelopersShortedBySimilarLabelsAndSkills(i,factorLabel,factorSkill,ld));
				//ver si nos quedamos con los que tengan mejor coeficiente
				similarDevelopers = getAllSimilarDevelopers(similarIssues);	
				
				List<Metric>metricsEstimateForDev = new LinkedList<Metric>();

				//Recorro los desarrolladores similares para obtener la correlación entre las tareas del mismo
				List<MetricPrediction> metrics = new LinkedList<MetricPrediction>();
				double correlationVariation=0.3;
				List<DeveloperPrediction> devPredictionSimilar = new LinkedList<DeveloperPrediction>();
				
				for(Developer developer: similarDevelopers){
					caseDevSmilar.setIdIssue(i);
					DeveloperPrediction dev = new DeveloperPrediction();				
					MetricPrediction m = predictions.getPredictionsDeveloper(metricKey, value, correlationVariation, sprint, developer);
					metrics.add(m);
					m.getMetrics().keySet();
					if(m != null){
						Map<String, Double>metricPredictionMetrics = m.getMetrics();
						Set<String> keys = metricPredictionMetrics.keySet();
						
						for(String s : keys){
							Metric metricDevSimilar = new Metric(s, s, s, s, null , new NumericMetric());
							metricsEstimateForDev.add(metricDevSimilar);
						}
					}
					dev.setIssues(metrics);
					dev.setName(d.getName());
					dev.setDisplayName(d.getDisplayName());	
					/*
					 * Lo agrego al Vector que se va a setear en el caso 
					 */
					devPredictionSimilar.add(dev);
				}
				List<Double> manhattan = getManhattanEstimationForDevelopers(metrics);
				/*
				 * Creo el Caso para esa issue con los valores estimados y los reales 
				 */
				if(!devPredictionSimilar.isEmpty() && devPredictionSimilar != null && manhattan != null && !manhattan.isEmpty()){
					for(Double dou : manhattan){
						for(Metric metric : metricsEstimateForDev){
							metric.setValue(new SimpleValue(dou.toString()));
						}
					}
					if(metricsEstimateForDev != null){
						Metric metricsEstimateForDev2[] = new Metric[metricsEstimateForDev.size()];
						metricsEstimateForDev2 = metricsEstimateForDev.toArray(metricsEstimateForDev2);
						caseDevSmilar.setEstimatedMetrics(metricsEstimateForDev2);
					}
					caseDevSmilar.setPerformDeveloper(d);
					caseDevSmilar.setLabels(i.getLabels());
					if(i.getSkills() != null){
						Skill skillsArray[] = new Skill[i.getSkills().size()];
						skillsArray = i.getSkills().toArray(skillsArray);
						caseDevSmilar.setNeededSkills(skillsArray);
						caseDevSmilar.setRealSkills(skillsArray);
					}
					DeveloperPrediction devPredictionSimilar2[] = new DeveloperPrediction[devPredictionSimilar.size()];
					devPredictionSimilar2 = devPredictionSimilar.toArray(devPredictionSimilar2);
					caseDevSmilar.setRecommendedDevelopers(devPredictionSimilar2);
					caseDevSmilar.setRealMetrics(i.getMetrics());					
					//caseDevSmilar.setEstimatedMetrics(manhattan);
					dbCases.add(caseDevSmilar);
				}				
			}
			
		}

		return similarDevelopers;	
	}
	
	private static List<Double> getManhattanEstimationForDevelopers(List<MetricPrediction> metrics) {
		List<Double> manhattanValues = new LinkedList<Double>();
		List<Double>values=new LinkedList<Double>();
		int cantFilas = metrics.size();
		for (MetricPrediction m : metrics){
			Collection<Double>valores=m.getMetrics().values();
			for(Double val : valores){
				values.add(val);
			}
		}
		int cantColumnas = values.size();
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

		//Recorrer matriz
		Vector<Double>aux= new Vector<Double>();
		for(int j=0;j<values.size();j++){
			for(int m=0;m<metrics.size();m++){
				aux.add(matValues[m][j]);								
			}
			manhattanValues.add(ManhattanFunction.manhattan(aux));
		}			
		return manhattanValues;
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

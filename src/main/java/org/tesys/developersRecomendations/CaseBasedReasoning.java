package org.tesys.developersRecomendations;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.tesys.core.analysis.skilltraceability.Skill;
import org.tesys.core.db.AnalysisVersionsQuery;
import org.tesys.core.db.ElasticsearchDao;
import org.tesys.core.estructures.Developer;
import org.tesys.core.estructures.Issue;
import org.tesys.core.estructures.Metric;
import org.tesys.core.estructures.SimilarIssue;
import org.tesys.correlations.DeveloperPrediction;
import org.tesys.correlations.Predictions;
import org.tesys.recomendations.DevelopersShortedBySimilarLabelsAndSkills;

public class CaseBasedReasoning {
	
	DevelopersShortedBySimilarLabelsAndSkills developers;
	List<Case> cases= new ArrayList<Case>();

	
	public CaseBasedReasoning(){
		
		
		
	}
	
	public List<Case> getDevRecommendationbyIssue()	{
        
		AnalysisVersionsQuery avq = new AnalysisVersionsQuery();
        List<Long> versiones = avq.execute();
        ElasticsearchDao<Case> dao;
        ResponseBuilder response = Response.ok("{\"status\":\"404\"}");

        try {
            dao = new ElasticsearchDao<Case>(
                    Case.class, 
                    ElasticsearchDao.DEFAULT_RESOURCE_CASE ); //devuelve la version mas actualizada de los analisis.
        } catch (Exception e) {
            return (List<Case>) response.build();
        }
        
        List<Case> cases = dao.readAll();
        if(cases.isEmpty()){
        	Case cdp = new Case("idIssuPrueba");
        	dao.create("caso de prueba", cdp);
        }

        response = Response.ok();

        return (List<Case>) response.build();
        
        
        
	}
	
	List<Case> getRecommendation(double factorLabel, double factorSkill, List<Metric>metrics){
		
		//aca los factores no deberian ser cero para que no influya despues durante la recomendacion??
		ElasticsearchDao<Developer> daoi = new ElasticsearchDao<Developer>(Developer.class,
		ElasticsearchDao.DEFAULT_RESOURCE_DEVELOPERS);
		List<SimilarIssue> similarIssues= new LinkedList<SimilarIssue>();
		List<Developer> ld  = daoi.readAll();
		Predictions predictions = new Predictions();
		List<Developer> similarDevelopers= new LinkedList<Developer>();
		for (Developer d : ld) {			
			List<Issue> li = d.getIssues();
			for (Issue i : li) {
					similarIssues.addAll(developers.getDevelopersShortedBySimilarLabelsAndSkills(i,factorLabel,factorSkill,ld));
					//ver si nos quedamos con los que tengan mejor coeficiente
					similarDevelopers = getAllSimilarDevelopers(similarIssues);	
					
			}			
		}

		for(Metric m: metrics)
		for(Developer d: similarDevelopers ){
			//predictions.getPredictionsDeveloper(metricKey, value, correlationVariation, sprint, d);	

		}
		/*
		for (Developer d : similarDeveloper){
			if(metrics.size()>0){
				for(Metric m : metrics){
					List<String> devSkillsForIssue = getDevSkillsForIssue(d);
					List<DeveloperPrediction> developerPredictions = predictions.getPredictions(m.getKey(), m.getValue().evaluate(null), 0.95, 1, s);
				}
		
			}
			
		}*/		
		return cases;	
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

	private List<Developer> getAllSimilarDevelopers(List<SimilarIssue> similarIssues) {
		List<Developer> developers = new LinkedList<Developer>();
		for(SimilarIssue si : similarIssues){
			if(!developers.contains(si.getDeveloper())){
				developers.add(si.getDeveloper());
			}
		}
		return developers;
	}
	
}

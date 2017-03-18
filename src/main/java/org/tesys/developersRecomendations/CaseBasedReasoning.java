package org.tesys.developersRecomendations;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import org.tesys.core.db.AnalysisVersionsQuery;
import org.tesys.core.db.ElasticsearchDao;
import org.tesys.core.estructures.Developer;
import org.tesys.core.estructures.Issue;
import org.tesys.core.estructures.Metric;
import org.tesys.core.estructures.SimilarIssue;
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
		
		for (Developer d : ld) {			
			List<Issue> li = d.getIssues();
			for (Issue i : li) {
					similarIssues.addAll(developers.getDevelopersShortedBySimilarLabelsAndSkills(i,factorLabel,factorSkill,ld));
				}			
		}
		List<Developer> similarDeveloper = getAllSimilarDevelopers(similarIssues);
		
		
			
		return cases;
		
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

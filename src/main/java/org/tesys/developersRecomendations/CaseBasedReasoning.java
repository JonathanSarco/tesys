package org.tesys.developersRecomendations;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.tesys.core.analysis.sonar.AnalisisPOJO;
import org.tesys.core.db.AnalysisVersionsQuery;
import org.tesys.core.db.ElasticsearchDao;

public class CaseBasedReasoning {
	
	CaseBasedReasoning(){
		
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

}

package org.tesys.developersRecomendations;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

import org.tesys.core.analysis.skilltraceability.Skill;
import org.tesys.core.db.*;
import org.tesys.core.estructures.Case;
import org.tesys.core.estructures.Developer;
import org.tesys.core.estructures.Metric;

import com.atlassian.httpclient.api.Response;

public class CbrCases{
	ElasticsearchDao<Case> client;
	
	public CbrCases(){
		this.client = new ElasticsearchDao<Case>(Case.class,
				ElasticsearchDao.DEFAULT_RESOURCE_CASE);
	}

	/*
	 * con el comando put http://localhost:9200/cbr/case/{version}  -> genero el indice y cargo los datos
	 * para cargar la base, como hago el put, ya debe tener los datos a guardar -> realizo un get
	 * get http://localhost:9200/cbr/case/{Version} -> debe responder 
	 * {  "error": "IndexMissingException[[cbr] missing]",  "status": 404 }
	 * si da ese error en el response entonces genero la base. 
	 * para esto
	 * -> definir como vamos a hacer el algoritmo para calcular la estimacion de las metricas
	 * -> generar los casos
	 * -> recorrer la lista de casos y realizar el put para crear el indice y cargar la base.
	 * -> para probar si anda voy a hacer una version trucha de peliculas 
	 */
	
	public static Map<String, Object> putJsonDocument(String title, String content, Date postDate, 
		String[] tags, String author){
		Map<String, Object> jsonDocument = new HashMap<String, Object>();
		jsonDocument.put("title", title);
		jsonDocument.put("conten", content);
		jsonDocument.put("postDate", postDate);
		jsonDocument.put("tags", tags);
		jsonDocument.put("author", author);
		return jsonDocument;
	}
	
		
	
	
	
}

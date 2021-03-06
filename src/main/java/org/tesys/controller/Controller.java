package org.tesys.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.tesys.core.analysis.Analyzer;
import org.tesys.core.analysis.skilltraceability.Skill;
import org.tesys.core.analysis.skilltraceability.SkillIndicator;
import org.tesys.core.analysis.sonar.SonarAnalizer;
import org.tesys.core.analysis.sonar.SonarAnalysisRequest;
import org.tesys.core.db.AnalysisVersionsQuery;
import org.tesys.core.db.DisplayNameQuery;
import org.tesys.core.db.ElasticsearchDao;
import org.tesys.core.db.IssuesWithMetrics;
import org.tesys.core.db.MetricDao;
import org.tesys.core.db.SearchCaseByIssueAndSkillsQuery;
import org.tesys.core.db.SearchCasesByIssueQuery;
import org.tesys.core.db.SearchDeveloperByIssue;
import org.tesys.core.db.SearchDeveloperByIssueNewIssues;
import org.tesys.core.estructures.Case;
import org.tesys.core.estructures.Developer;
import org.tesys.core.estructures.Issue;
import org.tesys.core.estructures.Metric;
import org.tesys.core.estructures.MetricFactory;
import org.tesys.core.estructures.Puntuacion;
import org.tesys.core.estructures.UnassignedDeveloper;
import org.tesys.core.project.scm.SCMManager;
import org.tesys.core.project.scm.ScmPostCommitDataPOJO;
import org.tesys.core.project.scm.ScmPreCommitDataPOJO;
import org.tesys.core.project.tracking.IssueTypePOJO;
import org.tesys.correlations.Predictions;
import org.tesys.developersRecomendations.CaseBasedReasoning;
import org.tesys.recomendations.DeveloperWithOneAcumMetric;
import org.tesys.recomendations.DevelopersCriteriaIssues;
import org.tesys.recomendations.DevelopersShortedByMetric;
import org.tesys.recomendations.DevelopersShortedBySkills;
import org.tesys.recomendations.IssueSimilarity;
import org.tesys.recomendations.IssuesaAlike;
import org.tesys.recomendations.RecomendedDeveloper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Path("/controller")
@Singleton
public class Controller {

	private static final String FAIL_CODE = "0";
	private static final String OK_CODE = "1";
	protected static final Logger LOG = Logger.getLogger(ElasticsearchDao.class
			.getName());
	// Componente encargado de las tareas relacionas con el SCM
	private SCMManager scmManager;
	// Componenete encargado con las tareas de recolectar e interpretar datos
	private Analyzer analizer;

	@PostConstruct
	public void init() {
		scmManager = SCMManager.getInstance();
		analizer = Analyzer.getInstance();
	}



	/**
	 * Metodo que dada la informacion sobre un commit devuelve Si el sistema
	 * Tesys lo puede computar o no
	 */

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/scm")
	public String isCommitAllowed(ScmPreCommitDataPOJO scmData) {

		try {
			if (scmManager.isCommitAllowed(scmData)) {
				return OK_CODE;
			}
		} catch (Exception e) {
			return e.getMessage();
		}
		return FAIL_CODE;
	}

	/**
	 * ALmacena en el sistema la informacion relacionada con un commit que debe
	 * ser previamente verificado por el metodo anterior (Estos dos se
	 * encuantran separados ya que por lo general no se dispone de toda la
	 * informacion necesaria para almacenar un commit antes de hacerlo)
	 */

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/scm")
	public String storeCommit(ScmPostCommitDataPOJO scmData) {

		try {
			if (scmManager.storeCommit(scmData)) {
				return OK_CODE;
			}
		} catch (RuntimeException e) {
			return e.getMessage();
		}
		return FAIL_CODE;
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/sonar")
	public Response storeSonarAnalysis(SonarAnalysisRequest sonarAnalysisRequest) {

		SonarAnalizer sa = SonarAnalizer.getInstance();
		boolean exito = sa.executeSonarAnalysis(sonarAnalysisRequest);

		ResponseBuilder response;
		if(exito) {
			response = Response.ok("{\"status\":\"200\"}");
			return response.build();
		}
		response = Response.ok("{\"status\":\"500\"}");

		return response.build();
	}



	/**
	 * Cuando se llama se recolectan todos los datos esparcidos a lo largo del
	 * sistema Este metodo no realiza ningun tipo de calculo solo junta toda la
	 * informacion existente en estructuras convenientes que luego se utilizaran
	 * para hacer recomandaciones
	 */

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/analyzer")
	public Response performAnalysis() {
		analizer.performAnalysis();

		ResponseBuilder response = Response.ok("{\"status\":\"200\"}");
		return response.build();
	}

	/**
	 * Devuelve todos los developers que existen en el project tracking, una ves
	 * que se haya ejecutado un analisis, esta infoamcion es util para obserbar
	 * los issues que tiene cada developer y para poder conocer el conjunto por
	 * el cual se recomienda
	 */

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/developers/{version}")
	public Response getDevelopers(@PathParam("version") Integer version) {

		AnalysisVersionsQuery avq = new AnalysisVersionsQuery();

		List<Long> versiones = avq.execute();

		ElasticsearchDao<Developer> dao;
		try {
			dao = new ElasticsearchDao<Developer>(
					Developer.class, ElasticsearchDao.DEFAULT_RESOURCE_DEVELOPERS + versiones.get(version));
		} catch (Exception e) {
			ResponseBuilder response = Response.ok("{\"status\":\"404\"}");
			return response.build();
		}

		List<Developer> developers = dao.readAll();

		GenericEntity<List<Developer>> entity = new GenericEntity<List<Developer>>(
				developers) {
		};
		ResponseBuilder response = Response.ok();
		response.entity(entity);

		return response.build();

	}	

	/**
	 * Dado un developer, devuelve todos los issues asociados a ese developer.
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/issues/{developer}")
	public Response getIssuesByDeveloper(@PathParam("developer") String developer) {

		AnalysisVersionsQuery avq = new AnalysisVersionsQuery();
		List<Long> versiones = avq.execute();
		ElasticsearchDao<Developer> dao;
		ResponseBuilder response = Response.ok("{\"status\":\"404\"}");

		try {
			dao = new ElasticsearchDao<Developer>(
					Developer.class, 
					ElasticsearchDao.DEFAULT_RESOURCE_DEVELOPERS 
					+ versiones.get( versiones.size()-1 )); //devuelve la version mas actualizada de los analisis.
		} catch (Exception e) {
			return response.build();
		}

		List<Developer> developers = dao.readAll();

		for (Developer d: developers) {
			if (developer.equals( d.getName() )) {
				List<Issue> issues = d.getIssues();
				GenericEntity<List<Issue>> entity = new GenericEntity<List<Issue>>(issues) {};
				response = Response.ok();
				response.entity(entity);
			}
		}
		return response.build();

	}   


	/**
	 * Dado un developer, devuelve todos los issues asociados a ese developer.
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/issue/{developer}/{issue}")
	public Response getIssue(@PathParam("developer") String developer, @PathParam("issue") String issue) {

		Integer version = 0; // Or last version
		AnalysisVersionsQuery avq = new AnalysisVersionsQuery();

		List<Long> versiones = avq.execute();

		ElasticsearchDao<Developer> dao;
		try {
			dao = new ElasticsearchDao<Developer>(
					Developer.class, ElasticsearchDao.DEFAULT_RESOURCE_DEVELOPERS + versiones.get(version));
		} catch (Exception e) {
			ResponseBuilder response = Response.ok("{\"status\":\"404\"}");
			return response.build();
		}

		List<Developer> developers = dao.readAll();
		ResponseBuilder response = Response.ok("{\"status\":\"404\"}");
		for (Developer d: developers) {
			if (d.getName().equals( developer )) {
				List<Issue> issues = d.getIssues();
				for (Issue i: issues) {
					if (i.getIssueId().equals(issue) ) {
						GenericEntity<Issue> entity = new GenericEntity<Issue>(i) {};
						response = Response.ok();
						response.entity(entity);
					}
				}
			}
		}

		return response.build();
	}   


	/**
	 * Este metodo devuelve los tipos de metricas que el programa maneja, esto
	 * es Las metrics que definene los programas (conocidas como simples, que
	 * son por ejemplo lineas de codigo) y las metricas definidas por el usuario
	 * ( conocidas como compuestas que son convinaciones de simples)
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/metrics")
	public Response getMetrics() {
		MetricDao dao = new MetricDao();
		List<Metric> metrics = dao.readAll();
		List<ObjectNode> metricsJson = new LinkedList<ObjectNode>();

		for (Metric m : metrics) {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode jsonNode = null;
			try {
				jsonNode = mapper.readTree(m.toString());
			} catch (IOException e) {
			}
			metricsJson.add((ObjectNode) jsonNode);
		}

		GenericEntity<List<ObjectNode>> entity = new GenericEntity<List<ObjectNode>>(
				metricsJson) {
		};
		ResponseBuilder response = Response.ok();
		response.entity(entity);

		return response.build();

	}


	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/metricsavailable")
	public Response getMetricsAvailable() {
		IssuesWithMetrics is = new IssuesWithMetrics(0);
		List<Issue> l = is.execute();

		List<String> metrics = new ArrayList<String>();

		metrics.addAll(l.get(0).getMetrics().keySet());
		metrics.remove("quacode");
		metrics.remove("prec");

		MetricDao dao = new MetricDao();
		List<Metric> metrics2 = new LinkedList<Metric>();

		for (String metric : metrics) {
			metrics2.add(dao.read(metric));
		}

		List<ObjectNode> metricsJson = new LinkedList<ObjectNode>();

		for (Metric m : metrics2) {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode jsonNode = null;
			try {
				jsonNode = mapper.readTree(m.toString());
			} catch (IOException e) {
			}
			metricsJson.add((ObjectNode) jsonNode);
		}

		GenericEntity<List<ObjectNode>> entity = new GenericEntity<List<ObjectNode>>(
				metricsJson) {
		};
		ResponseBuilder response = Response.ok();
		response.entity(entity);

		return response.build();

	}



	/**
	 * Devuleve los tipos de issues que existen en el project tracking, de esta
	 * forma se puede saber sobre el conjuto de restricciones que se puede
	 * ejecutar una recomendacion
	 */

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/issuestype")
	public Response getIssuesTypes() {
		ElasticsearchDao<IssueTypePOJO> dao = new ElasticsearchDao<IssueTypePOJO>(
				IssueTypePOJO.class,
				ElasticsearchDao.DEFAULT_RESOURCE_ISSUE_TYPE);
		List<IssueTypePOJO> issuesType = dao.readAll();

		GenericEntity<List<IssueTypePOJO>> entity = new GenericEntity<List<IssueTypePOJO>>(
				issuesType) {
		};
		ResponseBuilder response = Response.ok();
		response.entity(entity);

		return response.build();

	}

	/**
	 * Define una nueva metrica compuesta, que el usaurio desee por ejemplo
	 * lines por hora como productividad o productividad dividido bugs como
	 * seguridad
	 */

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/newmetric")
	public Response addMetric(String metric) {



		MetricFactory mf = new MetricFactory();
		Metric m;
		try {
			m = mf.getMetric(metric);
		} catch (Exception e) {
			ResponseBuilder response = Response.ok("{\"status\":\"500\"}");
			return response.build();
		}



		MetricDao dao = new MetricDao();

		dao.create(m.getKey(), m);

		ResponseBuilder response = Response.ok("{\"status\":\"200\"}");
		return response.build();

	}

	/**
	 * Elimina una metrica definida por el usuario, tambien pude eliminar una
	 * simple pero en el proximo analisis se volvera a crear
	 */

	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/deletemetric")
	public Response deleteMetric(String metricKey) {

		MetricDao dao = new MetricDao();

		dao.delete(metricKey);

		ResponseBuilder response = Response.ok();
		return response.build();
	}

	/**
	 * Almacena una puntuacion de un usuario a la tarea de otro
	 */

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/punt")
	public Response storePuntuation(@QueryParam("puntuador") String puntuador,
			@QueryParam("puntuado") String puntuado,
			@QueryParam("issue") String issue,
			@QueryParam("puntuacion") String puntuacion) {

		ElasticsearchDao<Puntuacion> dao = new ElasticsearchDao<Puntuacion>(
				Puntuacion.class, ElasticsearchDao.DEFAULT_RESOURCE_PUNTUATION);

		Puntuacion p = new Puntuacion(puntuador, puntuado, issue, puntuacion);
		dao.create(p.getId(), p);

		ResponseBuilder response = Response.ok("{\"status\":\"200\"}");
		return response.build();
	}

	/**
	 * Este metodo devuelve los tipos de skill que el programa maneja.
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/skills")
	public Response getSkills() {

		ElasticsearchDao<SkillIndicator> dao = new ElasticsearchDao<SkillIndicator>(SkillIndicator.class, ElasticsearchDao.DEFAULT_RESOURCE_SKILL);
		List<SkillIndicator> SkillIndicator = dao.readAll();
		GenericEntity<List<SkillIndicator>> entity = new GenericEntity<List<SkillIndicator>>(SkillIndicator) {};

		ResponseBuilder response = Response.ok();
		response.entity(entity);

		return response.build();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/newskill")
	public Response addSkill(SkillIndicator skillIndicator) {

		ElasticsearchDao<SkillIndicator> dao = new ElasticsearchDao<SkillIndicator>(
				SkillIndicator.class, ElasticsearchDao.DEFAULT_RESOURCE_SKILL);

		ResponseBuilder response;

		if (skillIndicator == null || skillIndicator.getSkillName() == null
				|| skillIndicator.getIndicator() == null) {
			response = Response.ok("{\"error\":\"null not expected\"}");
		} else {
			dao.create(skillIndicator.getId(), skillIndicator);
			response = Response.ok("{\"status\":\"200\"}");
		}

		return response.build();
	}


	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getdevmetric/{metric}")
	public Response getDevelopersShortedByMetric(@PathParam("metric") String metricKey) {

		ResponseBuilder response;

		//Verificar que existe
		MetricDao dao = new MetricDao();
		Metric m;
		try {
			m = dao.read(metricKey);
		} catch (Exception e) {
			response = Response.ok("{\"error\":\"metric doesn't exist\"}");
			return response.build();
		}

		if( m == null ) {
			response = Response.ok("{\"error\":\"metric doesn't exist\"}");
			return response.build();
		}

		DevelopersShortedByMetric d = new DevelopersShortedByMetric(m);

		response = Response.ok( d.getDevelopersShortedByMetric() );
		return response.build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getdevskill/{skills}")
	public Response getDevelopersShortedBySkills(@PathParam("skills") String skills) {

		ResponseBuilder response;
		//se espera o un skill o varios separados por &
		//de la forma: localhost:8080/getdevskill/java&c++

		//se verifica que todos esten
		List<String> lskills = Arrays.asList(skills.split("&"));

		ElasticsearchDao<SkillIndicator> dao = new ElasticsearchDao<SkillIndicator>(
				SkillIndicator.class, ElasticsearchDao.DEFAULT_RESOURCE_SKILL);

		for (String id : lskills) {

			if ( dao.search("{\"query\": { \"term\": {\"skillName\":  \"server\" }}}").isEmpty() ) {
				response = Response.ok("{\"error\":\"skill "+ id +" doesn't exist\"}");
				return response.build();
			}
		}

		DevelopersShortedBySkills d = new DevelopersShortedBySkills(lskills);

		response = Response.ok( d.getDevelopersShortedBySkills() );

		return response.build();
	}


	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getdevtask/{task}/{criteria}")
	public Response getRecomendedDevelopersForTask(@PathParam("task") String task,
			@PathParam("criteria") String criteria) {

		ResponseBuilder response;

		//transformo el issue key a el issue que exista en el jira con esa key
		/*ProjectTrackingRESTClient pt = new ProjectTrackingRESTClient();
		IssuePOJO ip = (IssuePOJO) pt.getIssue(task);

		if( ip == null ) {
			response = Response.ok("{\"error\":\"task "+ task +" doesn't exist\"}");
			return response.build();
		}*/

		//conseguir metrica
		//Verificar que existe
		MetricDao dao = new MetricDao();
		Metric m;
		try {
			m = dao.read(criteria);
		} catch (Exception e) {
			response = Response.ok("{\"error\":\"metric doesn't exist\"}");
			return response.build();
		}

		if( m == null ) {
			response = Response.ok("{\"error\":\"metric doesn't exist\"}");
			return response.build();
		}

		//---- completa labels del issue de jira con los de ES si es que ya existe, por el problema de que en jira no hay
		ElasticsearchDao<Developer> daoi = new ElasticsearchDao<Developer>(Developer.class,
				ElasticsearchDao.DEFAULT_RESOURCE_DEVELOPERS);

		List<Developer> ld  = daoi.readAll();

		Issue ip = null;

		for (Developer d : ld) {
			List<Issue> li = d.getIssues();

			for (Issue i : li) {
				if(i.getIssueId().equals(task)) {
					ip = i;
				}
			}

		}
		//-------

		List<Developer> l = new IssuesaAlike().getSimilarIssuesTo(ip, new IssueSimilarity());

		List<RecomendedDeveloper> dr = new DevelopersCriteriaIssues().getBestDeveloperIssue(m, l, ip);

		response = Response.ok(dr);

		return response.build();
	}



	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getpredic/{metricKey}/{value}/{correlation}/{sprint}")
	public Response getPrediction(@PathParam("metricKey") String metricKey,
			@PathParam("value") Double value,
			@PathParam("correlation") Double correlation,
			@PathParam("sprint") Integer sprint,
			@QueryParam("s") List<String> skills) {

		ResponseBuilder response;

		response = Response.ok(
				Predictions.getPredictions(metricKey, value, correlation, sprint, skills)
				);


		return response.build();
	}
	/**
	 * Funcion que devuelve todos los issues
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/issues")
	public Response getAllIssues()	{

		ElasticsearchDao<UnassignedDeveloper> dao;
		ResponseBuilder response = Response.ok("{\"status\":\"404\"}");

		try {
			dao = new ElasticsearchDao<UnassignedDeveloper>(
					UnassignedDeveloper.class, 
					ElasticsearchDao.DEFAULT_RESOURCE_UNASSIGNED_ISSUES );
		} catch (Exception e) {
			return response.build();
		}

		List<UnassignedDeveloper> developers = dao.readAll();
		List<Issue> issues = new ArrayList<Issue>();
		for (UnassignedDeveloper d: developers) {
			if(d.getUnassignedIssues() != ""){
				issues.addAll(getIssuesNotInUnassigned(d.getUnassignedIssues(), d.getIssues()));
			}
			else{
				issues.addAll(d.getIssues());
			}
		}
		GenericEntity<List<Issue>> entity = new GenericEntity<List<Issue>>(issues) {};
		response = Response.ok();
		response.entity(entity);
		return response.build();
	}

	private List<Issue> getIssuesNotInUnassigned(String unassignedIssues, List<Issue> issues) {
		List<Issue>forList = new LinkedList<Issue>();
		for(Issue i : issues){
			if(unassignedIssues.indexOf(i.getIssueId()) == -1){
				forList.add(i);
			}
		}
		return forList;
	}



	private Issue getIssue(List<Developer> developers, String issueId) {

		for(Developer d: developers){
			List<Issue> unasignesIssues = d.getIssues();
			for(Issue i : unasignesIssues){
				if(i.getIssueId().equals(issueId)){
					return i;
				}
			}
		}
		return null;
	}
	/**
	 * Convierto a un Map el string recibido desde la View, cargado con las metricas y sus valores.
	 * @param metrics
	 * @return Map<String,Double>
	 */
	private Map<String,Double> convertToMap(String metrics)	{
		Map<String,Double> m = new HashMap<String,Double>();
		String key="";
		String value="";
		for (int i=0; i < metrics.length(); i++) {
			if (metrics.charAt(i) != ':')
				//Genero la key
				key += metrics.charAt(i);
			else {
				int aux;
				for (aux = i+1; aux < metrics.length(); aux++) {
					//Cuando llego a la , luego del :, o al final del string
					if ( (metrics.charAt(aux) != ','))	{
						//Genero el valor luego del ;
						value += metrics.charAt(aux);
					}
					else { 
						i = aux+1;
						aux = metrics.length(); }
				}
				m.put(key, Double.valueOf(value));
				key = value = "";		
			}
		}	
		return m;
	}

	public List<Developer> convertToDevelopers (List<UnassignedDeveloper> devs) {
		List<Developer> convert = new LinkedList<Developer>();
		for (UnassignedDeveloper d : devs) {
			convert.add(new UnassignedDeveloper().cloneToDeveloper(d));
		}
		return convert;
	}
	
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/getDevRecommendationbyIssue/{label}/{skill}/{metrics}/{sprint}/{issue}")
	public Response getDevRecommendationbyIssue(	
			@PathParam("label") Double label,
			@PathParam("skill") Double skill,
			@PathParam("metrics") String metrics,
			@PathParam("sprint") Integer sprint,
			@PathParam("issue") String issue,
			@QueryParam("s") List<String> skills) {

		Map<String,Double> metricsRecommendation = this.convertToMap(metrics);
		ElasticsearchDao<Case> dao;
		//Lista de desarrolladores de Issues
		ElasticsearchDao<UnassignedDeveloper> daoUnassignedIssue;
		ResponseBuilder response = Response.ok("{\"status\":\"404\"}");
		Case dbCases = new Case();

		try {
			dao = new ElasticsearchDao<Case>(Case.class,ElasticsearchDao.DEFAULT_RESOURCE_CASE);
			daoUnassignedIssue = new ElasticsearchDao<UnassignedDeveloper>(UnassignedDeveloper.class, ElasticsearchDao.DEFAULT_RESOURCE_UNASSIGNED_ISSUES);

		} catch (Exception e) {
			return response.build();
		}

		List<Developer> developers = convertToDevelopers(daoUnassignedIssue.readAll());
		Issue unasignedIssue = getIssue(developers, issue);

		dbCases = CaseBasedReasoning.getRecommendation(label,skill, sprint, unasignedIssue, metricsRecommendation, skills);
		if(dbCases != null){
			dao.create(dbCases.getIdCase(), dbCases);
		}
		List<Developer> developersCase = new LinkedList<Developer>();
		if(dbCases.getIssuesWithDevelopersRecommended() != null){
			developersCase = Arrays.asList(dbCases.getIssuesWithDevelopersRecommended());
		}
		GenericEntity<List<Developer>> entity = new GenericEntity<List<Developer>>(
				developersCase) {
		};
		response.entity(entity);
		return response.build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/updateOrderCriteria/{selectedDeveloper}/{issue}")
	public Response updateOrderCriteria(	
			@PathParam("selectedDeveloper") String selectedDeveloper,
			@PathParam("issue") String issue){
		ResponseBuilder response = Response.ok("{\"status\":\"404\"}");
		/*
		 * Busco el caso por IsuueID
		 */

		SearchCasesByIssueQuery dnq = new SearchCasesByIssueQuery(issue);
		Case similarIssueCase = dnq.execute();
		/*
		 * Obtengo el Developer Seleccionado
		 */
		Developer selectedDev = new Developer();
		for(Developer d : similarIssueCase.getIssuesWithDevelopersRecommended()){
			if(d.getName().equals(selectedDeveloper)){
				selectedDev = d;
			}
		}
		
		Case modifCase = CaseBasedReasoning.setOrderCriteriaNewCase(selectedDev, similarIssueCase );
		

		ElasticsearchDao<Case> dao;
		ElasticsearchDao<UnassignedDeveloper> daoDevUnasigned;
		try {
			dao = new ElasticsearchDao<Case>(Case.class,ElasticsearchDao.DEFAULT_RESOURCE_CASE);
			daoDevUnasigned = new ElasticsearchDao<UnassignedDeveloper>(UnassignedDeveloper.class, ElasticsearchDao.DEFAULT_RESOURCE_UNASSIGNED_ISSUES);
			
		} catch (Exception e) {
			return response.build();
		}
		/*
		 * Metodo para hacer el update del caso
		 */	
		
		/*
		 * Busco los developers por issue
		 * Elimino la issue de los developers y actualizo el desarrollador en los index eliminando la issue nueva
		 * Si no existe en tesis la issue no pasa nda por no elimina ninguna issue. 
		 */
		selectedDev.getIssues().get(0).setUser(selectedDeveloper);
		modifCase.setPerformIssue(selectedDev);
		
		/* Busco el developer por name, El developer Seleccionado para asignarle la tarea y agrego la issue al vector 
		 * Actualizo el desarrollador en el index de analysis.
		 * Se actualiza el caso con el Criterio y el desarrollador que va a hacer la issue nueva.
		 */

		if(modifCase.getMetricWeight()!=null){
			dao.update(modifCase.getIdCase(), modifCase);
		}
		
		/*
		 * Una vez que modifica el caso, elimino de Unnasigned_Issue la elimina.
		 */
		SearchDeveloperByIssueNewIssues searchDevIssue = new SearchDeveloperByIssueNewIssues(issue);
		UnassignedDeveloper devUnassigned = searchDevIssue.execute();
		if(devUnassigned.getUnassignedIssues() == ""){
			devUnassigned.setUnassignedIssues(issue);
		}
		else{
			devUnassigned.setUnassignedIssues(devUnassigned.getUnassignedIssues() + ", " + issue );
		}
		List<UnassignedDeveloper> allUnassigned = daoDevUnasigned.readAll();
		//daoDevUnasigned.delete("");
		//daoDevUnasigned.delete(devUnassigned.getName());
		//daoDevUnasigned.create(devUnassigned);
		daoDevUnasigned.update(devUnassigned.getId(), devUnassigned);
		return response.build();
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/updateRealMetrics/{issuesSelected}/{metrics}")
	public Response updateRealMetrics(	
			@PathParam("issuesSelected") String issue,
			@PathParam("metrics") String metrics){
		ResponseBuilder response = Response.ok("{\"status\":\"404\"}");
	
		/*
		 * Busco el caso por IsuueID
		 */

		SearchCasesByIssueQuery dnq = new SearchCasesByIssueQuery(issue);
		Case similarIssueCase = dnq.execute();

		
		/*
		 * Se le asignan las metricas reales al desarrollador asignado
		 */
		Map<String,Double> metricsRecommendation = this.convertToMap(metrics);
		similarIssueCase.getPerformIssue().getIssues().get(0).setMetrics(metricsRecommendation);
		
		/**
		 * Obtengo la Issue con las metricas estimadas del caso.
		 * Tambien obtengo la Issue con metricas reales. 
		 */
		Developer devEstimatedMetrics = new Developer();
		for(Developer d : similarIssueCase.getIssuesWithDevelopersRecommended()){
			if(similarIssueCase.getPerformIssue()!=null)
				if(d.getName().equals(similarIssueCase.getPerformIssue().getName())){
					devEstimatedMetrics = d;
				}
		}
		Issue realMetrics = similarIssueCase.getPerformIssue().getIssues().get(0);
		Issue estimatedMetrics = devEstimatedMetrics.getIssues().get(0);
		

	
		/*
		 * Metodo para hacer el update del caso
		 */
		
		ElasticsearchDao<Case> dao;
		ElasticsearchDao<Issue> daoEstimation;
		try {
			dao = new ElasticsearchDao<Case>(Case.class,ElasticsearchDao.DEFAULT_RESOURCE_CASE);
			daoEstimation = new ElasticsearchDao<Issue>(Issue.class, ElasticsearchDao.DEFAULT_RESOURCE_ESTIMATION_ISSUE);
			
		} catch (Exception e) {
			return response.build();
		}
		/*
		 * C�lculo del Error Cuadr�tico Medico 
		 */
		
		similarIssueCase.setErrorCuadraticoMedio(similarIssueCase.calculateMSEError());
		if(similarIssueCase.getErrorCuadraticoMedio() != -1 && similarIssueCase.getErrorCuadraticoMedio() < 0.0005)
			similarIssueCase.setGoodRecommendation(1);
		else{
			if(similarIssueCase.getErrorCuadraticoMedio() != -1)
				similarIssueCase.setGoodRecommendation(0);
			else
				similarIssueCase.setGoodRecommendation(-1);
		}
		dao.update(similarIssueCase.getIdCase(), similarIssueCase);
		daoEstimation.update(similarIssueCase.getPerformIssue().getIssues().get(0).getIssueId(), similarIssueCase.getPerformIssue().getIssues().get(0));
		
		/**
		 * Genero el vector de Issues con la Issue estimada y real, respectivamente para luego retornarlo
		 */
		List<Issue> issues = new LinkedList<Issue>();
		issues.add(estimatedMetrics);
		issues.add(realMetrics);
		GenericEntity<List<Issue>> entity = new GenericEntity<List<Issue>>(issues) {};
		response = Response.ok();
		response.entity(entity);
		
		return response.build();
	}
	
	
	private Developer removeIssue(Developer deveoperWithNew, String newIssue) {
		List<Issue> updateDev = new LinkedList<Issue>();
		for(Issue i: deveoperWithNew.getIssues()){
			if(!i.getIssueId().equals(newIssue)){
				updateDev.add(i);
			}
		}
		deveoperWithNew.setIssues(updateDev);
		return deveoperWithNew;
	}
	
	/**
	 * Obtengo las Issues del CBR
	 **/
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/cbrIssues")
	public Response getAllCbrIssues() throws Exception	{
		//AnalysisVersionsQuery avq = new AnalysisVersionsQuery();
		ResponseBuilder response = Response.ok("{\"status\":\"404\"}");
		ElasticsearchDao<Case> dao = new ElasticsearchDao<Case>(Case.class, ElasticsearchDao.DEFAULT_RESOURCE_CASEQUERY );
		List<Case> cases = dao.readAll();
		List<Issue> issues = new ArrayList<Issue>();
		//Por cada caso en el CBR, obtengo las issues asignadas para los desarrolladores
		if (cases != null && cases.size() > 0) {
			for (Case caso : cases) {
				if (caso.getPerformIssue() != null)
					issues.addAll(caso.getPerformIssue().getIssues());
			}
			GenericEntity<List<Issue>> entity = new GenericEntity<List<Issue>>(issues) {};
			response = Response.ok();
			response.entity(entity);
			return response.build();
		} else {
			return null;
		}
	}
	
}

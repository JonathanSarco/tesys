package org.tesys.core.analysis.telemetry;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.tesys.core.db.ElasticsearchDao;
import org.tesys.core.db.MetricDao;
import org.tesys.core.estructures.Developer;
import org.tesys.core.estructures.Issue;
import org.tesys.core.estructures.Metric;
import org.tesys.core.project.tracking.IssueInterface;
import org.tesys.core.project.tracking.IssueTypePOJO;
import org.tesys.core.project.tracking.ProjectTracking;
import org.tesys.core.project.tracking.ProjectTrackingRESTClient;
import org.tesys.core.project.tracking.User;
import org.tesys.util.MD5;

/**
 * Esta clase, y en particular todo este pquete, es el encargado de recolectar
 * datos de todas partes del sistema y juntarlos todo en una unica unidad de
 * trabajo que va a ser identificada por el issue del project tracking y va a
 * tener metricas de todos los provedores que esten acoplados al sistema
 * 
 */

public class ProcessData {

	private ElasticsearchDao<Issue> daoi;
	private MetricDao daom;
	private ElasticsearchDao<Developer> daod;
	private ElasticsearchDao<IssueTypePOJO> daoit;

	private static ProcessData instance = null;

	private ProcessData() {
		daoi = new ElasticsearchDao<Issue>(Issue.class,
				ElasticsearchDao.DEFAULT_RESOURCE_ISSUE_METRIC);
		daom = new MetricDao();
		daod = new ElasticsearchDao<Developer>(Developer.class,
				ElasticsearchDao.DEFAULT_RESOURCE_DEVELOPERS
						+ Calendar.getInstance().getTimeInMillis());

		daoit = new ElasticsearchDao<IssueTypePOJO>(IssueTypePOJO.class,
				ElasticsearchDao.DEFAULT_RESOURCE_ISSUE_TYPE);
	}

	public static ProcessData getInstance() {
		if (instance == null) {
			instance = new ProcessData();
		}
		return instance;
	}

	public void executeProcessor() {

		ProjectTracking pt = new ProjectTrackingRESTClient();

		AggregatorFactory aggregatorFactory = new ConcreteAggregatorFactory();
		Aggregator aggregator = aggregatorFactory.getAggregator();

		this.processIssues(pt, aggregator);

		this.processMetrics(aggregator);

		this.processDevelopers(pt);

		this.processIssuesTypes(pt);

	}

	/**
	 * Recolecta los diferentes tipos de issues, estos solo se sacan del project
	 * tracking los guarda en la base de datos
	 */

	private void processIssuesTypes(ProjectTracking pt) {
		List<IssueTypePOJO> issuesType = pt.getIssueTypes();

		for (IssueTypePOJO it : issuesType) {
			daoit.create(String.valueOf(it.getId()), it);
		}

	}

	/**
	 * Recolecta todos los developers que existen (en el project tracking) y los
	 * guarda en la base de datos indicando que issues corresponde a cada uno,
	 * pera esto es necesario que primero se computen los issues sino se van a
	 * guardar issues de un analisis anterior
	 */
	
	
	public static void main(String[] args) {
		ProjectTracking pt = new ProjectTrackingRESTClient();
		
		ProcessData.getInstance().processDevelopers(pt);;
		

		
	}

	private void processDevelopers(ProjectTracking pt) {
		List<User> user = Arrays.asList(pt.getUsers());
		List<Issue> issues = daoi.readAll();

		for (User u : user) {
			Developer d = new Developer();
			List<Issue> dissues = new LinkedList<Issue>();
			d.setName(u.getName()); // jperez = id
			d.setDisplayName(u.getDisplayName()); // Juan Perez != id
			for (Issue i : issues) {
				if (i.getUser().equals(d.getName())) {
					dissues.add(i);
				}
			}
			d.setIssues(dissues);
			d.setTimestamp(new Date());
			daod.create(
					MD5.generateId(d.getName() + d.getTimestamp().toString()),
					d);
		}

	}

	/**
	 * Se obtienen y almacenan todas las metricas que puede manejar tesys se
	 * utiliza un agregador para poder recorrer todos los programas que esten
	 * conectados
	 */

	private void processMetrics(Aggregator aggregator) {
		List<Metric> metrics = aggregator.getMetricsID();

		for (Metric metric : metrics) {
			daom.create(metric.getKey(), metric);
		}

	}

	/**
	 * Se obtienen los issues desde el project tracking con la informacion
	 * asociada y se utiliza un agregador para poder obtener todos los valores
	 * de metricas que tenga cada programa para ofrecer
	 */

	private void processIssues(ProjectTracking pt, Aggregator aggregator) {
		// Aca se traen todos los issues y se agregan a mano los datos desde el
		// project tracking
		List<IssueInterface> issues = Arrays.asList(pt.getIssues());

		/**
		 * La manera correcta de hacer seria trayendo solo las key, y que el
		 * agregator meta las metricas que corresponden, pero por problemas de
		 * performance se realiza de esta manera
		 */

		for (IssueInterface i : issues) {

			Issue issueActual = new Issue(i.getKey());
			issueActual.setIssueType(i.getIssuetype());
			issueActual.setLabels( i.getLabels() );
			issueActual.setUser(i.getAssignee());

			issueActual.addMetric("progress",
					Double.valueOf(i.getProgress().getProgress()));
			issueActual.addMetric("estimated",
					Double.valueOf(i.getProgress().getTotal()));

			Issue issueFinal = aggregator.agregateMetrics(issueActual);
			daoi.create(issueFinal.getIssueId(), issueFinal);
		}

	}

}

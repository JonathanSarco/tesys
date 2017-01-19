package org.tesys.core.analysis.sonar;

import java.util.LinkedList;
import java.util.List;

import org.sonar.wsclient.Host;
import org.sonar.wsclient.Sonar;
import org.sonar.wsclient.connectors.HttpClient4Connector;
import org.sonar.wsclient.services.Metric;
import org.sonar.wsclient.services.MetricQuery;
import org.sonar.wsclient.services.TimeMachine;
import org.sonar.wsclient.services.TimeMachineCell;
import org.sonar.wsclient.services.TimeMachineColumn;
import org.sonar.wsclient.services.TimeMachineQuery;
import org.tesys.core.db.RevisionByOriginalIdentifierQuery;
import org.tesys.core.project.scm.RevisionPOJO;

public class SonarExtractor {

	private Sonar sonar;
	private List<Metric> metricList;


	public SonarExtractor(String host, String user, String pass) {

		sonar = new Sonar(new HttpClient4Connector(new Host(host, user, pass)));
		metricList = sonar.findAll(MetricQuery.all());

	}

	public AnalisisPOJO getResults(String revision, String repository, String sonarKey) {


		RevisionByOriginalIdentifierQuery dao = new RevisionByOriginalIdentifierQuery(revision, repository);
		
		RevisionPOJO rev = dao.execute();
		AnalisisPOJO resultadoDeRevision = null;
		
		if( rev != null ) {
			
			String[] met = new String[metricList.size()];
			int index = 0;
			for (Metric metric : metricList) {
				met[index++] = metric.getKey();
			}

			TimeMachine struts = sonar.find(TimeMachineQuery.createForMetrics(
					sonarKey, met));
			TimeMachineCell[] tmc = struts.getCells();
			TimeMachineColumn[] tmco = struts.getColumns();

			Object[] v = tmc[tmc.length-1].getValues();
			
			resultadoDeRevision = new AnalisisPOJO( rev );

			for (int i = 0; i < tmco.length; i++) {

				if (v[i] != null) {
					resultadoDeRevision.add(new KeyValuePOJO(tmco[i]
							.getMetricKey(), v[i].toString()));
				} else {
					resultadoDeRevision.add(new KeyValuePOJO(tmco[i]
							.getMetricKey(), "null"));
				}

			}
			
		}
		

		return resultadoDeRevision;
	}
	

	public List<SonarMetricPOJO> getMetrics() {

		List<SonarMetricPOJO> metrics = new LinkedList<SonarMetricPOJO>();
		for (Metric m : metricList) {
			metrics.add(new SonarMetricPOJO(m.getKey(), m.getName(), m
					.getDescription(), m.getType(), m.getDomain()));
		}

		return metrics;
	}

}

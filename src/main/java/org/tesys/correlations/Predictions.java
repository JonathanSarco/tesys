package org.tesys.correlations;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.tesys.core.analysis.skilltraceability.Skill;
import org.tesys.core.db.DisplayNameQuery;
import org.tesys.core.db.IssuesWithMetrics;
import org.tesys.core.estructures.Developer;
import org.tesys.core.estructures.Issue;



public class Predictions {

	public static List<DeveloperPrediction> getPredictions(String metricKey,
			Double value, Double correlationVariation, int sprint, List<String> skills) {


		MetricPrediction metricPrediction;

		IssuesWithMetrics is = new IssuesWithMetrics(sprint);
		List<Issue> l = is.execute();


		List<Double> pearson1 = new ArrayList<Double>();
		List<Double> pearson2 = new ArrayList<Double>();
		List<String> metrics = new ArrayList<String>();
		List<DeveloperPrediction> developerPrediction = new LinkedList<DeveloperPrediction>();

		Set<String> users = new HashSet<String>();

		for (Issue issue : l) {
			users.add(issue.getUser());
		}

		metrics.addAll(l.get(0).getMetrics().keySet());
		metrics.remove("quacode");
		metrics.remove("prec");

		correlationVariation = 0.1;
		for (String userKey : users) {

			metricPrediction = new MetricPrediction(userKey, userKey);

			for (int i = 0; i < metrics.size(); i++) {
				for (int j = 0; j < metrics.size(); j++) {
					if( i != j ) {

						pearson1 = new ArrayList<Double>();
						pearson2 = new ArrayList<Double>();

						for (Issue issue : l) {
							if( issue.getUser().equals(userKey) && metrics.get(i).equals(metricKey) ) { 
								List<String> isk = new LinkedList<String>();
								if (issue.getSkills() != null)
									for (Skill sk : issue.getSkills()) {
										isk.add(sk.skillName);
									}

							//	if( isk.containsAll(skills) ) {
									pearson1.add(issue.getMetrics().get(metrics.get(i)));
									pearson2.add(issue.getMetrics().get(metrics.get(j)));
							//	}

							}

						}


						Double dou = Pearson.getCorrelation(pearson1, pearson2);

						if (dou>correlationVariation || dou < -correlationVariation) {

							List<Double> lr = LinearRegression.getRegression(pearson1, pearson2);
							metricPrediction.putMetric(metrics.get(j), lr.get(0)*value+lr.get(1));
							metricPrediction.putDeviation(metrics.get(j), lr.get(2));
						}

					}

				}
			}

			DisplayNameQuery dnq = new DisplayNameQuery(userKey);
			if(!metricPrediction.getMetrics().isEmpty()) {
				developerPrediction.add( new DeveloperPrediction(userKey, dnq.execute(), metricPrediction) );
			}
		}
		return developerPrediction;

	}

	public static MetricPrediction  getPredictionsDeveloper(String metricKey,
			Double value, Double correlationVariation, int sprint, Developer developer ) {


		MetricPrediction metricPrediction;

		IssuesWithMetrics is = new IssuesWithMetrics(sprint);
		//Recorro las issues del developer!
		List<Issue> l = is.execute();


		List<Double> pearson1 = new ArrayList<Double>();
		List<Double> pearson2 = new ArrayList<Double>();
		List<String> metrics = new ArrayList<String>();
		List<DeveloperPrediction> developerPrediction = new LinkedList<DeveloperPrediction>();

		Set<String> users = new HashSet<String>();

		for (Issue issue : l) {
			users.add(issue.getUser());
		}

		metrics.addAll(l.get(0).getMetrics().keySet());
		metrics.remove("quacode");
		
		metrics.remove("prec");
		
		metricPrediction = new MetricPrediction(developer.getName(), developer.getName());

		for (int i = 0; i < metrics.size(); i++) {
			for (int j = 0; j < metrics.size(); j++) {
				if( i != j ) {

					pearson1 = new ArrayList<Double>();
					pearson2 = new ArrayList<Double>();

					for (Issue issue : l) {
						if( issue.getUser().equals(developer.getName()) && metrics.get(i).equals(metricKey) ) { 						
							pearson1.add(issue.getMetrics().get(metrics.get(i)));
							pearson2.add(issue.getMetrics().get(metrics.get(j)));

						}

					}


					Double dou = Pearson.getCorrelation(pearson1, pearson2);

					if (dou>correlationVariation || dou < -correlationVariation) {

						List<Double> lr = LinearRegression.getRegression(pearson1, pearson2);
						metricPrediction.putMetric(metrics.get(j), lr.get(0)*value+lr.get(1));
						metricPrediction.putDeviation(metrics.get(j), lr.get(2));
					}

				}

			}
		}

		DisplayNameQuery dnq = new DisplayNameQuery(developer.getName());
		if(!metricPrediction.getMetrics().isEmpty()) {
			developerPrediction.add( new DeveloperPrediction(developer.getName(), dnq.execute(), metricPrediction) );
		}
		return metricPrediction;

	}

}

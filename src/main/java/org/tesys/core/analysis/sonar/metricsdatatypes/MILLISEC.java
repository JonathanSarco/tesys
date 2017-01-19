package org.tesys.core.analysis.sonar.metricsdatatypes;

public class MILLISEC implements Metrics {

    Double actual, anterior;

    public MILLISEC(String actual, String anterior) {
	this.actual = Double.valueOf(actual);
	if ("null".equals(anterior)) {
	    this.anterior = 0.0;
	} else {
	    this.anterior = Double.valueOf(anterior);
	}
    }

    public MILLISEC(Double actual, Double anterior) {
	this.actual = actual;
	this.anterior = anterior;
    }

    public Double getDifferenceBetweenAnalysis() {
	return Double.valueOf(actual - anterior);
    }

    public Double getNewAnalysisPerTask() {
	return actual + anterior;
    }

}

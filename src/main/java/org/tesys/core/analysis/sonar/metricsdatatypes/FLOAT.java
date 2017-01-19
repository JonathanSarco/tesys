package org.tesys.core.analysis.sonar.metricsdatatypes;

public class FLOAT implements Metrics {

    Double actual, anterior;

    public FLOAT(String actual, String anterior) {
	this.actual = Double.valueOf(actual);
	if ("null".equals(anterior)) {
	    this.anterior = 0.0;
	} else {
	    this.anterior = Double.valueOf(anterior);
	}
    }

    public FLOAT(Double actual, Double anterior) {
	this.actual = actual;
	this.anterior = anterior;
    }

    public Double getDifferenceBetweenAnalysis() {
	return actual - anterior;
    }

    public Double getNewAnalysisPerTask() {
	return actual + anterior;
    }
}

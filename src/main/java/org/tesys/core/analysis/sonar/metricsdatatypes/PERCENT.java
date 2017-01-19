package org.tesys.core.analysis.sonar.metricsdatatypes;

public class PERCENT implements Metrics {

    Double actual, anterior;

    public PERCENT(String actual, String anterior) {
	this.actual = Double.valueOf(actual);
	if ("null".equals(anterior)) {
	    this.anterior = 0.0;
	} else {
	    this.anterior = Double.valueOf(anterior);
	}
    }

    public PERCENT(Double actual, Double anterior) {
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

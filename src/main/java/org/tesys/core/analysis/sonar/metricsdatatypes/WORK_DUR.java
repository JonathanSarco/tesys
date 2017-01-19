package org.tesys.core.analysis.sonar.metricsdatatypes;

public class WORK_DUR implements Metrics {

    Double actual, anterior;

    public WORK_DUR(String actual, String anterior) {
	this.actual = Double.valueOf(actual);
	if ("null".equals(anterior)) {
	    this.anterior = 0.0;
	} else {
	    this.anterior = Double.valueOf(anterior);
	}
    }

    public WORK_DUR(Double actual, Double anterior) {
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

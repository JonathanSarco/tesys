package org.tesys.core.analysis.sonar.metricsdatatypes;

public class DATA implements Metrics {
	
    Double actual, anterior;

    public DATA(String actual, String anterior) {
	this.actual = Double.valueOf(actual);
	if ("null".equals(anterior)) {
	    this.anterior = 0.0;
	} else {
	    this.anterior = Double.valueOf(anterior);
	}

    }
    
    public DATA(Double actual, Double anterior) {
	this.actual = actual;
	this.anterior = anterior;
    }


    public Double getDifferenceBetweenAnalysis() {
	return null;
    }

    public Double getNewAnalysisPerTask() {
	return null;
    }

}

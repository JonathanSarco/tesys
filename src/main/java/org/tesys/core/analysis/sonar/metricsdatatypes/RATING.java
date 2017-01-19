package org.tesys.core.analysis.sonar.metricsdatatypes;

public class RATING implements Metrics {
	
    Double actual, anterior;

    public RATING(String actual, String anterior) {
	this.actual = Double.valueOf(actual);
	if ("null".equals(anterior)) {
	    this.anterior = 0.0;
	} else {
	    this.anterior = Double.valueOf(anterior);
	}

    }
    
    public RATING(Double actual, Double anterior) {
	this.actual = actual;
	this.anterior = anterior;
    }

	
	@Override
	public Double getDifferenceBetweenAnalysis() {
		return null;
	}

	@Override
	public Double getNewAnalysisPerTask() {
		return null;
	}

}

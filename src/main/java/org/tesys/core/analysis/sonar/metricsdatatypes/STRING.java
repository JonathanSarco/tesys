package org.tesys.core.analysis.sonar.metricsdatatypes;

public class STRING  implements Metrics {
	
    Double actual, anterior;

    public STRING(String actual, String anterior) {
	this.actual = Double.valueOf(actual);
	if ("null".equals(anterior)) {
	    this.anterior = 0.0;
	} else {
	    this.anterior = Double.valueOf(anterior);
	}

    }
    
    public STRING(Double actual, Double anterior) {
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

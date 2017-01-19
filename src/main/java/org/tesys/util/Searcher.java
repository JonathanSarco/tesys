package org.tesys.util;

import java.util.List;

import org.tesys.core.analysis.sonar.AnalisisPOJO;
import org.tesys.core.analysis.sonar.KeyValuePOJO;
import org.tesys.core.analysis.sonar.SonarMetricPOJO;

public class Searcher {

    private Searcher() {
	// to avoid the implicit one
    }

    public static AnalisisPOJO searchIssue(List<AnalisisPOJO> analisis,
	    String issue) {

	for (AnalisisPOJO analisisPOJO : analisis) {
	    if (analisisPOJO.getRevision().getProjectTrackingTask()
		    .equals(issue)) {
		return analisisPOJO;
	    }
	}

	return null;
    }

    public static Double searchMetricValue(List<KeyValuePOJO> resultados,
	    String metricKey) {

	for (KeyValuePOJO resultado : resultados) {
	    if (resultado.getKey().equals(metricKey)) {
		return (Double) resultado.getValue();
	    }
	}

	return null;
    }

    /**
     * Dado un String key busca en lo que devuelve sonar /api/metrics esa key y
     * devuelve Toda la informacion asociada a la misma
     * 
     * @param field
     * @param metrics
     * @return
     */
    public static SonarMetricPOJO searchMetric(String field,
	    List<SonarMetricPOJO> metrics) {
	for (SonarMetricPOJO m : metrics) {
	    if (m.getKey().equals(field)) {
		return m;
	    }
	}
	return null;
    }

}

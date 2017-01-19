package org.tesys.core.analysis.sonar.metricsdatatypes;

/**
 * Cada clase que implemente esta interfaz debe utilizarse para manejar un tipo
 * de dato diferente de sonar, una restriccion que hay que tener en cuenta es
 * que el nombre de la clase que implemente esta interfaz debe ser igual al
 * nombre del campo val_type de las metricas de sonar (/api/metrics) y que se
 * llaman por reflexion
 */
public interface Metrics {
    Double getDifferenceBetweenAnalysis();

    Double getNewAnalysisPerTask();
}

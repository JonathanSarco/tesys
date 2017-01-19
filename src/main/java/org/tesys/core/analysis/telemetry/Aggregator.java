package org.tesys.core.analysis.telemetry;

import java.util.List;

import org.tesys.core.estructures.Issue;
import org.tesys.core.estructures.Metric;

/**
 * Un agregador es una estructura que es capas de recorrer todos los programas
 * conectados a tesys para:
 * 
 * 1- Obtener las metricas que dicho pragrama puede proporcionar
 * 
 * 2- Obtener los valores de un issue asociado, de cada una de la metricas
 * 
 * Para eso se utiliza el patron decorate, donde ProjectTrackingAggregator es la
 * hoja
 * 
 * y todos los hijos debren heredar de AggregatorDecorator para poder agregar
 * sus metricas a la par de los demas programas
 * 
 */

public interface Aggregator {
    Issue agregateMetrics(Issue i);

    List<Metric> getMetricsID();
}

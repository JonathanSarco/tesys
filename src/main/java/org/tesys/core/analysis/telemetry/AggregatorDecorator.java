package org.tesys.core.analysis.telemetry;

import java.util.List;
import org.tesys.core.estructures.Issue;
import org.tesys.core.estructures.Metric;

/**
 * Decorator de aggregator, cada programa que se agrege a tesys debe tener al
 * menos un hijo de esta clase que sera encargado de adjuntar los datos a los de
 * los demas programas
 * 
 */

public abstract class AggregatorDecorator implements Aggregator {
    protected Aggregator aggregator;

    public AggregatorDecorator(Aggregator aggregator) {
	this.aggregator = aggregator;
    }

    @Override
    public Issue agregateMetrics(Issue i) {
	return aggregator.agregateMetrics(i);
    }

    @Override
    public List<Metric> getMetricsID() {
	return aggregator.getMetricsID();
    }

}

package org.tesys.core.analysis.telemetry;

import java.util.List;

import org.tesys.core.db.ElasticsearchDao;
import org.tesys.core.estructures.Issue;
import org.tesys.core.estructures.Metric;
import org.tesys.core.estructures.Puntuacion;

public class PuntuacionAggregator extends AggregatorDecorator {

    private ElasticsearchDao<Puntuacion> dao;
    
    
    public PuntuacionAggregator(Aggregator aggregator) {
	super(aggregator);
	dao = new ElasticsearchDao<Puntuacion>(
		Puntuacion.class, ElasticsearchDao.DEFAULT_RESOURCE_PUNTUATION);
    }
    
    @Override
    public Issue agregateMetrics(Issue issueMetrics) {
	issueMetrics = super.agregateMetrics(issueMetrics);
	List<Puntuacion> puntuaciones = dao.readAll();
	for (Puntuacion puntuacion : puntuaciones) {
	    if( issueMetrics.getIssueId().equals(puntuacion.getIssue()) ) {
		issueMetrics.addPuntuation(puntuacion);
	    }
	}
	return issueMetrics;
    }

    
    /**
     * Las puntuaciones no son consideradas una medida ya que las realizan subjetivamente
     * por eso nomas se pasa los datos que otros programas agregan y listo 
     */
    @Override
    public List<Metric> getMetricsID() {
	List<Metric> metrics = super.aggregator.getMetricsID();
	return metrics;
    }
}

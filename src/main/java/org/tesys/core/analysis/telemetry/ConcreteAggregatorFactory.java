package org.tesys.core.analysis.telemetry;

/**
 * Constructor de aggregator por defecto (agrega a todos los programas que
 * existen) se debe instanciar un agregador arrancado por
 * ProjectTrackingAggregator
 * 
 * y luego agregando cada una de los hijos de AggregatroDecorator que se desea
 * 
 * por ejemplo para recolectar datos de jira y de sonar seria:
 * 
 * new SonarAnalisisAggregator( new ProjectTrackingAggregator() )
 * 
 */

public class ConcreteAggregatorFactory implements AggregatorFactory {

	@Override
	public Aggregator getAggregator() {
		return 
			 new ComplexMetricsAggregator(
			   new SkillsAggregator(
			   new PuntuacionAggregator(
			//   new ActivityAggregator(
			   new SonarAnalisisAggregator(
			   new ProjectTrackingAggregator()	
			)
	    //  )
			)
			)
			);
	}

}

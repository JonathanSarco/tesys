package org.tesys.core.analysis.telemetry;

/**
 * Es el encargado de instanciar un aggregator, dado que si hay muchos programas
 * conectados a tesys la conplejidad de la instanciacion puede resultar elevada
 * 
 * Ademas se puede hacer muchos hijos para tener difeerentes perfiles de
 * recoleccion de datos
 * 
 */

public interface AggregatorFactory {
    Aggregator getAggregator();
}

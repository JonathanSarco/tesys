package org.tesys.core.estructures.metrictypes;

public class PercentMetric implements MetricTypeDescriptor {

	@Override
	public Double add(Double a, Double b) {
		return avg(a,b);
	}

	@Override
	public Double avg(Double a, Double b) {
		return (a+b)/2.0;
	}

	@Override
	public String toString() {
		return "percent";
	}
}

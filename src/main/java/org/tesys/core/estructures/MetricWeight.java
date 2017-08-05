package org.tesys.core.estructures;

public class MetricWeight implements Comparable {
	
	String metricName;
	Double weight;
	
	public String getMetricName() {
		return metricName;
	}
	
	public void setMetricName(String metricName) {
		this.metricName = metricName;
	}
	
	public Double getWeight() {
		return weight;
	}
	
	public void setWeight(Double weight) {
		this.weight = weight;
	}

	@Override
	public int compareTo(Object o) {
		MetricWeight metric = (MetricWeight)o; 
		return this.weight.compareTo(metric.getWeight());
	}
	
}

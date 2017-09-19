package org.tesys.orderDeveloper;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.tesys.OrderWeight.MatrixWeight;
import org.tesys.core.estructures.Developer;
import org.tesys.core.estructures.MetricWeight;

import com.sun.xml.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

public class OrderByWeight implements Comparator<Developer> {
	
	MetricWeight[] weightMetrics;
	MatrixWeight bestMetrics;
	
	public OrderByWeight(MetricWeight[] wMetrics){
		this.weightMetrics = wMetrics;
		this.bestMetrics = new MatrixWeight();
	}

	
	public int compare(Developer o1, Developer o2) {
		List<MetricWeight> metrics = Arrays.asList(weightMetrics);
				
				String m0=metrics.get(0).getMetricName();
				String m1=metrics.get(1).getMetricName();
				String m2=metrics.get(2).getMetricName();
				String m3=metrics.get(3).getMetricName();

				//De menor a mayor
				if(this.bestMetrics.getBestMetrics().get(m0) ==-1) {
					if (o1.getIssues().get(0).getMetrics().get(m0) !=null && o2.getIssues().get(0).getMetrics().get(m0) != null) {
						int resultado = Double.compare( o1.getIssues().get(0).getMetrics().get(m0),o2.getIssues().get(0).getMetrics().get(m0));
			        	if ( resultado != 0 ) { return resultado; }}
				}
				if(this.bestMetrics.getBestMetrics().get(m1) ==-1) {
					if (o1.getIssues().get(0).getMetrics().get(m1) !=null && o2.getIssues().get(0).getMetrics().get(m1) != null) {
						int resultado = Double.compare( o1.getIssues().get(0).getMetrics().get(m1),o2.getIssues().get(0).getMetrics().get(m1));
						if ( resultado != 0 ) { return resultado; }}
				}
				if(this.bestMetrics.getBestMetrics().get(m2) ==-1) {
					if (o1.getIssues().get(0).getMetrics().get(m2) !=null && o2.getIssues().get(0).getMetrics().get(m2) != null) {
						int resultado = Double.compare( o1.getIssues().get(0).getMetrics().get(m2),o2.getIssues().get(0).getMetrics().get(m2)); 
			        	if ( resultado != 0 ) { return resultado; }}
				}
				if(this.bestMetrics.getBestMetrics().get(m3) ==-1) {
					if (o1.getIssues().get(0).getMetrics().get(m3) !=null && o2.getIssues().get(0).getMetrics().get(m3) != null) {
					int resultado = Double.compare( o1.getIssues().get(0).getMetrics().get(m3),o2.getIssues().get(0).getMetrics().get(m3));
		        		if ( resultado != 0 ) { return resultado; }}
				}
				//De mayor a menor
				if(this.bestMetrics.getBestMetrics().get(m0) ==1) {
					if (o1.getIssues().get(0).getMetrics().get(m0) !=null && o2.getIssues().get(0).getMetrics().get(m0) != null) {
						int resultado = Double.compare( o2.getIssues().get(0).getMetrics().get(m0),o1.getIssues().get(0).getMetrics().get(m0));
						if ( resultado != 0 ) { return resultado; }}
				}
				if(this.bestMetrics.getBestMetrics().get(m1) ==1) {
					if (o1.getIssues().get(0).getMetrics().get(m1) !=null && o2.getIssues().get(0).getMetrics().get(m1) != null) {
						int resultado = Double.compare( o2.getIssues().get(0).getMetrics().get(m1),o1.getIssues().get(0).getMetrics().get(m1));
				        if ( resultado != 0 ) { return resultado; }}
				}
				if(this.bestMetrics.getBestMetrics().get(m2) ==1) {
					if (o1.getIssues().get(0).getMetrics().get(m2) !=null && o2.getIssues().get(0).getMetrics().get(m2) != null) {
						int resultado = Double.compare( o2.getIssues().get(0).getMetrics().get(m2),o1.getIssues().get(0).getMetrics().get(m2)); 
						if ( resultado != 0 ) { return resultado; }}
				}
				if(this.bestMetrics.getBestMetrics().get(m3) ==1) {
					if (o1.getIssues().get(0).getMetrics().get(m3) !=null && o2.getIssues().get(0).getMetrics().get(m3) != null) {
						int resultado = Double.compare( o2.getIssues().get(0).getMetrics().get(m3),o1.getIssues().get(0).getMetrics().get(m3));
		        		if ( resultado != 0 ) { return resultado; }}
				}
				//if (this.bestMetrics.getBestMetrics().get(m0) == 1)
				//	return -1;
				return 1;
	}	
	
}

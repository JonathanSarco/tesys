package org.tesys.orderCriteria;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.tesys.core.estructures.Developer;

public class prueba {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Developer developerChosen=new Developer();
		developerChosen.setDisplayName("pablo");
		Map<String,Map<String,Double>> metricsWithValuesByDev=new HashMap<String, Map<String,Double>>();
		
		Map<String,Double>ValuesByDev=new HashMap<String, Double>();
		ValuesByDev.put("diego", 0.1);
		ValuesByDev.put("pedro", 0.1);
		ValuesByDev.put("pablo", 0.1);
		metricsWithValuesByDev.put("complexity", ValuesByDev);
		
		ValuesByDev=new HashMap<String, Double>();
		ValuesByDev.put("pablo", 0.5);
		ValuesByDev.put("diego", 0.5);
		ValuesByDev.put("juan", 0.5);
		metricsWithValuesByDev.put("ncloc", ValuesByDev);
		
		CriteriaSelector c=new CriteriaBestValues();
		c.completeHash();
		Map<String,String>criteria=c.obtenerValor(developerChosen, metricsWithValuesByDev);
		System.out.println(criteria);
	
	}
}

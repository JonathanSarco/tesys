package org.tesys.core.Normalizer;

import org.tesys.core.db.ElasticsearchDao;
import org.tesys.core.db.GenericQuery;
import org.tesys.core.estructures.Issue;


import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.tesys.core.estructures.UnassignedDeveloper;

public class ObteinMinMetricValue implements GenericQuery<Object> {
	private static final Logger LOG = Logger.getLogger(Object.class.getName());
	private ElasticsearchDao<Object> dao;
	String metricName;
	
	 public ObteinMinMetricValue(String name) {
			this.dao = new ElasticsearchDao<Object>(Object.class,
						ElasticsearchDao.DEFAULT_RESOURCE_ESTIMATION_ISSUE);
			this.metricName = name;
	    }

	@Override
	public Object execute() {
		String query = "{ \"aggs\" : { \"min_value\": { \"min\": { \"field\" : \"" + metricName + "\"}}}}";
		String valueName = "min_value";
		try {
		    return dao.searchCount(query, valueName);
		} catch (Exception e) {
		    LOG.log(Level.INFO, e.getMessage());
		}
		return null;
	}
}

package org.tesys.core.db;

import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.tesys.core.estructures.Developer;

public class DeleteDeveloperByName implements GenericQuery<Developer> {
		
		private static final Logger LOG = Logger.getLogger(DeleteDeveloperByName.class.getName());
		private ElasticsearchDao<Developer> dao;
		String name;
		
		 public DeleteDeveloperByName(String name) {
				this.dao = new ElasticsearchDao<Developer>(Developer.class,
							ElasticsearchDao.DEFAULT_RESOURCE_DEVELOPERS);
				this.name = name;
		    }

		@Override
		public Developer execute() {
			String query = "? -d '{ \"query\": { \"bool\": { \"must\":  [" + "{\"match\": {\"name\": \"" + name + "\" }}"+ "] } }}'";
			try {
			    dao.deleteByQuery(query);
			    //Si devuelve un developer nuevo, borro bien.
			    return new Developer();
			} catch (Exception e) {
			    LOG.log(Level.INFO, e.getMessage());
			}
			//Sino retorna null
			return null;
		}
}

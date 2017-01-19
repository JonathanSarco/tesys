package org.tesys.core.db;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.core.UriBuilder;

import org.tesys.util.RESTClient;

import com.fasterxml.jackson.databind.JsonNode;

public class AnalysisVersionsQuery implements GenericQuery<List<Long>> {
    
    private static final Logger LOG = Logger
            .getLogger(RevisionByOriginalIdentifierQuery.class.getName());

    
    @Override
    public List<Long> execute() {
        try {
            RESTClient rc = new RESTClient( ElasticsearchDao.ES_URL );
            JsonNode node =  rc.GET(UriBuilder.fromPath("analysis").path("_mapping").toString()).readEntity( JsonNode.class ) ;
            Iterator<?> it = node.get( "analysis" ).get( "mappings" ).fieldNames() ;
            List<Long> dates = new ArrayList<Long>() ;
            
            while (it.hasNext()) {
                dates.add( Long.parseLong( (String)it.next() )  );
            }

            Collections.sort( dates ) ;
            return dates ;
 
        } catch (MalformedURLException e) {
            LOG.log(Level.SEVERE, e.toString(), e);
        }
        return new ArrayList<Long>();
        
    }

}

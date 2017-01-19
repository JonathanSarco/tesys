package org.tesys.core.project.scm;

import java.io.File;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.core.Response;

import org.tesys.connectors.scm.svn.SvnPathRevisionPOJO;
import org.tesys.util.RESTClient;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Esta clase es la interfaz que utiliza el core para poder acceder al conector
 * del SCM que tendria la implementacion del SCM especifico que se este usando
 * 
 * POr lo tanto debe contener la localizacion del conector como informacion
 * adicional para poder comunicarse, pero no logica del modelo
 */

public class SCMFacade {

    private static final String DEFAULT_URL_SCM_CONNECTOR = "http://localhost:8080/tesys/rest/connectors/svn/"; //$NON-NLS-1$

    private static final Logger LOG = Logger.getLogger( SCMFacade.class.getName() );

    private RESTClient client;

    private static SCMFacade instance = null;

    private SCMFacade() {
        try {
            client = new RESTClient( DEFAULT_URL_SCM_CONNECTOR );
        } catch (MalformedURLException e) {
            LOG.log( Level.SEVERE, e.toString(), e );
        }
    }

    public static SCMFacade getInstance() {
        if (instance == null) {
            instance = new SCMFacade();
        }
        return instance;
    }
    
    
    /**
     * 
     * @param revision el numero de revision que se quiere hacer checkout
     * @param pathToCheckOut el path dentro del repositorio a escanear
     * @param workspace (donde se guardaran los archivos)
     * @return si fue correcto
     */
    public boolean doCheckout( String revision, String pathToCheckOut, File workspace ) {
        
        JsonFactory factory = new JsonFactory();
        ObjectMapper om = new ObjectMapper( factory );
        factory.setCodec( om );
        ObjectNode data = om.createObjectNode();
        data.put( "repository", pathToCheckOut );
        data.put( "workspace", workspace.getAbsolutePath() );

        if (client.PUT( "checkout/" + revision, data.toString() ).getStatus() / 100 == 2) {
            return true;
        }
        return false;
    }
    
    public String getPath(String revision, String repository ) {
        JsonFactory factory = new JsonFactory();
        ObjectMapper om = new ObjectMapper( factory );
        factory.setCodec( om );
        ObjectNode data = om.createObjectNode();
        data.put( "repository", repository );

        Response resp = client.PUT( "path/" + revision, data.toString() ) ;
        if (resp.getStatus() / 100 == 2) {
            return resp.readEntity(String.class);
        }
        return null; 
    }
    
    public String getDiff( String revision1, String revision2, String repository  ) {
        JsonFactory factory = new JsonFactory();
        ObjectMapper om = new ObjectMapper( factory );
        factory.setCodec( om );
        ObjectNode data = om.createObjectNode();
        data.put( "repository", repository );

        Response resp = client.PUT( "diff/" + revision1 + "/" + revision2, data.toString() ) ;
        if (resp.getStatus() / 100 == 2) {
            return resp.readEntity(String.class);
        }
        return null; 
        
        
    }
    
    public SvnPathRevisionPOJO getAncestry(String url, String revision) {
    	 JsonFactory factory = new JsonFactory();
         ObjectMapper om = new ObjectMapper( factory );
         factory.setCodec( om );
         ObjectNode data = om.createObjectNode();
         data.put( "repository", url );

         Response resp = client.PUT( "ancestry/" + revision, data.toString() ) ;
         if (resp.getStatus() / 100 == 2) {
             return resp.readEntity(SvnPathRevisionPOJO.class);
         }
         return null; 
    }
    
}

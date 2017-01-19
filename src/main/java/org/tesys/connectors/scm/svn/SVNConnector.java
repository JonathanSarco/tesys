package org.tesys.connectors.scm.svn;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.wc.SVNRevision;

@Path("/connectors/svn")
public class SVNConnector {

    private static final Logger LOG = Logger.getLogger(SVNConnector.class
	    .getName());


    /**
     * Servicio REST para hacer checkouts del SVN
     * 
     * El servidor svn se guarda en esta clase pero el repositorio y la version
     * tienen que pasarse como parametro
     * 
     * Cualquier checkout que se haga se hace en la carpeta home dentro de la
     * carpeta ".tesys"
     * 
     * No hay una forma facil de cambiar esto porque el servidor tiene rutas
     * relativas Asi que si se desea cambiar hay que analizar bien como
     * implementarlo
     * 
     * El repositorio debe ser una ruta absolata de donde se puede realizar un
     * checkout pe: svn://localhost/repo
     * 
     * Depende de como este cofigurado el svnserve
     * 
     * @param revision
     *            se pone en la url un numero que indica la revision que se
     *            quiere hacer co
     * @param svnco
     *            Este POJO basicamente tiene el repositorio del que se quiere
     *            hacer co
     * @return
     */

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("checkout/{revision}")
    public long checkout(@PathParam("revision") String revision,
	    SvnCheckoutPOJO svnco) {

	SVNURL location = null;
	try {
	    location = SVNURL.parseURIEncoded(svnco.getRepository());
	} catch (SVNException e) {
	    LOG.log(Level.SEVERE, e.toString(), e);
	}

	SVNRevision svnr = SVNRevision.create(Integer.parseInt(revision));
	final File destPath = new File(svnco.getWorkspace());

	try {
	    return SVNImplementation.getInstance().checkout(location, svnr, destPath, false);
	} catch (SVNException e) {
	    LOG.log(Level.SEVERE, e.toString(), e);
	}
	return 0;

    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("diff/{revision1}/{revision2}")
    public String diff( @PathParam("revision1") String rev1, 
	    @PathParam("revision2") String rev2, SvnPathPOJO url ) {
	
	return SVNImplementation.getInstance().diff(url.getRepository(), Integer.valueOf( rev1 ), Integer.valueOf( rev2 ));
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("path/{revision}")
    public String path( @PathParam("revision") String rev, SvnPathPOJO repo ) {
        return SVNImplementation.getInstance().getSvnBasePath( repo.getRepository(), Integer.parseInt( rev ) ) ;
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("ancestry/{revision}")
    public SvnPathRevisionPOJO getAncestry( @PathParam("revision") String rev, SvnPathPOJO repo ) {
        return SVNImplementation.getInstance().getAncestry( repo.getRepository(), Integer.parseInt( rev ) ) ;
    }
    
       
    
}

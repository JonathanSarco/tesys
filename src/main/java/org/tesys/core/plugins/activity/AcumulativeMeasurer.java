package org.tesys.core.plugins.activity;

import java.util.concurrent.TimeUnit;

import org.tesys.core.db.ElasticsearchDao;
import org.tesys.core.project.tracking.ProjectTrackingRESTClient;

public class AcumulativeMeasurer implements Measurer {

    private ElasticsearchDao<ActivityPOJO> dao;
    private ProjectTrackingRESTClient projectTracker ;

    
    public AcumulativeMeasurer( ElasticsearchDao<ActivityPOJO> tempDao ) {
        this.dao = tempDao;
        projectTracker = new ProjectTrackingRESTClient() ;
    }

    public void set( String user, Integer mins, Integer count ) {
        if ( projectTracker.existUser( user )) { 
            Long timestamp = System.currentTimeMillis();
            ActivityPOJO oldAct = dao.read( user );
            if (oldAct == null) {
                dao.create( user, new ActivityPOJO( user, mins, count, timestamp ) );
            } else {
                if (timestamp - oldAct.getTimeStamp() >= TimeUnit.MINUTES.toMillis( mins )) {
                    // Aca valido que los minutos indicadas no sea menor a la diferencia
                    // horaria entre los datos
                    // Ej: El activity configurado para que envie cada mins (en los
                    // parametros del llamado al WS), manda
                    // el dato de forma repetida dos veces seguidas, esta condicion
                    // evita que se sobreacumulen actividad
                    dao.update( user, new ActivityPOJO( user, oldAct.getMinsAcum() + mins, oldAct.getCount() + count, timestamp ) );
                } 
            }
        } 
    }
    
    @Override
    public void persist( String issueKey, String user ) {
        ActivityPOJO tempAct = dao.read( user );
        dao.delete( user ); // borro la actividad asociada al usuario para asociarla con un issue
        if (tempAct == null) {
            ActivityPOJO nil = new ActivityPOJO( user, 0, 0, (long)0 );
            nil.setIssueKey( issueKey );
            ActivityPOJO oldAct = dao.read( nil.getId() );
            if (oldAct == null) {
                dao.create( nil.getId(), nil );
            }
        } else {
            tempAct.setIssueKey( issueKey );
            ActivityPOJO oldAct = dao.read( tempAct.getId() );

            if (oldAct == null) {
                dao.create( tempAct.getId(), tempAct );
            } else {
                tempAct.add( oldAct );
                dao.update( tempAct.getId(), tempAct );
            }
        }
    }
}

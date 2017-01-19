package org.tesys.core.project.tracking;

import java.util.List;

import org.tesys.core.estructures.Metric;

/**
 * Interface para Connector de un Gestor de proyectos (Project Tracking). El
 * cual sera el proovedor de Issues y Users de un proyecto.
 * 
 * Sinonimos:
 * 
 * Issue=Tarea, User=Usuario=Desarrollador, Gestor de Proyectos=Project Tracking
 * 
 * @author rulo
 * 
 */
public interface ProjectTracking {

    /**
     * Consulta al Gestor de Proyectos todos los issues existentes.
     * 
     * @return Un arreglo de todos issues existentes.
     */
    public IssueInterface[] getIssues();

    /**
     * Consulta al Gestor de Proyectos todos los usuarios existentes.
     * 
     * @return Un arreglo de todos usuarios existentes.
     */
    public User[] getUsers();

    /**
     * Consulta al Gestor de proyectos por la existencia de un issue con una
     * clave determinada.
     * 
     * @param key
     *            Campo clave del issue.
     * @return Devuelve 'true' solo si existe un issue asociado a esa clave.
     */
    public boolean existIssue(String key);

    /**
     * Consulta al Gestor de proyectos por la existencia de un usuario con una
     * clave determinada.
     * 
     * @param key
     *            Campo clave del usuario
     * @return Devuelve 'true' solo si existe un usuario con esa clave.
     */
    public boolean existUser(String key);

    /**
     * Retorna un Issue del Gestor de Proyectos dada una key.
     * 
     * @param key
     *            Campo clave del usuario
     * @return Devuelve el Issue con esa clave, o null si no existe Issue con
     *         dicha clave.
     */
    public IssueInterface getIssue(String key);

    /**
     * Consulta las metricas brindadas del Gestor de Proyectos.
     * 
     * @param key
     * 
     * @return Devuelve las metricas asociadas al Gestor de Proyectos. dicha
     *         clave.
     */
    public List<Metric> getMetrics();

    public abstract List<String> getIssuesKeys();

    public List<IssueTypePOJO> getIssueTypes();
    
    public boolean isIssueAssignedToUser( String issueKey , String userName );

}

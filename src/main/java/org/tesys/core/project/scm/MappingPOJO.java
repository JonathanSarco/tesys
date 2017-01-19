package org.tesys.core.project.scm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.tesys.util.MD5;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Esta clase es para el mapeo SCM - Project tracking
 * 
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MappingPOJO {

    /**
     * Usuario en el project tracking que normalmemte se consigue desde el mensaje del commit
     */
    private String projectTrackingUser;
    
    /**
     * Usuario de scm y repositorio, ya que puede haber dos usaurio con mismo nombre en 
     * diferentes repos que son distintos
     */
    private String scmUser;
    private String repository;

    public MappingPOJO() {
	// needed by jackson
    }

    public MappingPOJO(String project_tracking_user, String scm_user,
	    String repository) {
	this.projectTrackingUser = project_tracking_user;
	this.scmUser = scm_user;
	this.repository = repository;
    }

    public String getID() {
	return MD5.generateId(scmUser + repository);
    }

    @Override
    public String toString() {
	return "MappingPOJO [project_tracking_user=" + projectTrackingUser
		+ ", scm_user=" + scmUser + ", repository=" + repository + "]";
    }

    public String getProjectTrackingUser() {
	return projectTrackingUser;
    }

    public void setProjectTrackingUser(String project_tracking_user) {
	this.projectTrackingUser = project_tracking_user;
    }

    public String getScmUser() {
	return scmUser;
    }

    public void setScmUser(String scm_user) {
	this.scmUser = scm_user;
    }

    public String getRepository() {
	return repository;
    }

    public void setRepository(String repository) {
	this.repository = repository;
    }

}

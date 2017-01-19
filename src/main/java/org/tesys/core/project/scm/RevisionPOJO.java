package org.tesys.core.project.scm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.tesys.util.MD5;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RevisionPOJO implements Comparable<RevisionPOJO> {

    /**
     * Fecha en millis en la cual fue realizado el commit, o almacenada la revision
     */
    protected long date;

    
    /**
     * El usuario del scm que realizo el commit
     */
    @JsonProperty("scm_user")
    protected String scmUser;

    
    /**
     * La tarea del project tracking que debe estar especificada en el mensaje
     * del commit ya parseada
     */
    @JsonProperty("project_tracking_task")
    protected String projectTrackingTask;

    /**
     * Identificador de commit o revision
     */
    protected String revision;
    
    /**
     * Repositorio al que pertenece el usuario y commit
     */
    protected String repository;
    
    /**
     * Variable interna para saber si esta revision fue escaneada con sonar o no
     */
    protected boolean scaned;
    
    /**
     * Path que fue modificado dentro de el repositorio 
     * (se refiere el mas general que abarca todos los cambios)
     */
    protected String path;
    
    
    /**
     * svn diff sobre esta revision con la inmediata anterior
     */
    protected String diff;
    
    /**
     * path con el que se debe comparar este
     */
    
    protected String ancestry;
    
    
    /**
     * Revision con la que se debe comparar esta
     */
    protected String ancestryRevision;
    
  
	public RevisionPOJO() {
	// needed for jackson
    }

    public RevisionPOJO(long date, String scmUser, String projectTrackingTask,
	    String revision, String repository) {
	this.date = date;
	this.scmUser = scmUser;
	this.projectTrackingTask = projectTrackingTask;
	this.revision = revision;
	this.repository = repository;
	this.scaned = false;
	this.path = null;
	this.diff = null;
	this.ancestry = null;
	this.ancestryRevision = null;
    }
    
    public String getDiff() {
		return diff;
	}

	public void setDiff(String diff) {
		this.diff = diff;
	}

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getID() {
	return MD5.generateId(String.valueOf(date));
    }

    public long getDate() {
	return date;
    }

    public void setDate(long date) {
	this.date = date;
    }
    
	public String getAncestry() {
		return ancestry;
	}

	public void setAncestry(String ancestry) {
		this.ancestry = ancestry;
	}

	public String getAncestryRevision() {
		return ancestryRevision;
	}

	public void setAncestryRevision(String ancestryRevision) {
		this.ancestryRevision = ancestryRevision;
	}


    public String getScmUser() {
	return scmUser;
    }

    public void setScmUser(String scmUser) {
	this.scmUser = scmUser;
    }

    public String getProjectTrackingTask() {
	return projectTrackingTask;
    }

    public void setprojectTrackingTask(String projectTrackingTask) {
	this.projectTrackingTask = projectTrackingTask;
    }

    public String getRevision() {
	return revision;
    }

    public void setRevision(String revision) {
	this.revision = revision;
    }

    public String getRepository() {
	return repository;
    }

    public void setRepository(String repository) {
	this.repository = repository;
    }

    public boolean isScaned() {
	return scaned;
    }

    public void setScaned(boolean scaned) {
	this.scaned = scaned;
    }

    @Override
    public int compareTo(RevisionPOJO rev) {
	if (rev.getDate() < this.date) {
	    return 1;
	}
	return -1;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + (int) (date ^ (date >>> 32));
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj) {
	    return true;
	}
	if (obj == null) {
	    return false;
	}
	if (getClass() != obj.getClass()) {
	    return false;
	}
	RevisionPOJO other = (RevisionPOJO) obj;
	if (date != other.date) {
	    return false;
	}
	return true;
    }

}

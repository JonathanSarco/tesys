package org.tesys.core.estructures;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.tesys.util.MD5;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Puntuacion {
	public String puntuador;
	public String puntuado;
	public String issue;
	public String puntuacion;

    public Puntuacion(String puntuador, String puntuado, String issue,
	    String puntuacion) {
	super();
	this.puntuador = puntuador;
	this.puntuado = puntuado;
	this.issue = issue;
	this.puntuacion = puntuacion;
    }
    
    public Puntuacion() {
	// jackson
    }
    
    public String getId() {
	return MD5.generateId( puntuador + puntuado + issue );
    }
    
    public String getPuntuador() {
        return puntuador;
    }
    public void setPuntuador(String puntuador) {
        this.puntuador = puntuador;
    }
    public String getPuntuado() {
        return puntuado;
    }
    public void setPuntuado(String puntuado) {
        this.puntuado = puntuado;
    }
    public String getIssue() {
        return issue;
    }
    public void setIssue(String issue) {
        this.issue = issue;
    }
    public String getPuntuacion() {
        return puntuacion;
    }
    public void setPuntuacion(String puntuacion) {
        this.puntuacion = puntuacion;
    }
    
    
}

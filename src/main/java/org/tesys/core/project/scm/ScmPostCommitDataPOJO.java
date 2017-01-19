package org.tesys.core.project.scm;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ScmPostCommitDataPOJO {
    private String author;
    private String message;
    private String repository;
    private String revision;
    private String date;

    public String getDate() {
	return date;
    }

    public void setDate(String date) {
	this.date = date;
    }

    public String getRevision() {
	return revision;
    }

    public void setRevision(String revision) {
	this.revision = revision;
    }

    @Override
    public String toString() {
	return "ScmPostCommitDataPOJO [author=" + author + ", message="
		+ message + ", repository=" + repository + ", revision="
		+ revision + ", date=" + date + "]";
    }

    public String getAuthor() {
	return author;
    }

    public void setAuthor(String author) {
	this.author = author;
    }

    public String getMessage() {
	return message;
    }

    public void setMessage(String message) {
	this.message = message;
    }

    public String getRepository() {
	return repository;
    }

    public void setRepository(String repository) {
	this.repository = repository;
    }
}

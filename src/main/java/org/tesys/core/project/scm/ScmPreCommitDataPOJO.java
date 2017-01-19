package org.tesys.core.project.scm;

public class ScmPreCommitDataPOJO {

    private String author;
    private String message;
    private String repository;

    @Override
    public String toString() {
	return "ScmData [author=" + author + ", message=" + message
		+ ", repository=" + repository + "]";
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

package org.tesys.connectors.scm.svn;

public class SvnCheckoutPOJO {

    private String repository;
    private String workspace;

    public String getWorkspace() {
	return workspace;
    }

    public void setWorkspace(String workspace) {
	this.workspace = workspace;
    }

    public String getRepository() {
	return repository;
    }

    public void setRepository(String repository) {
	this.repository = repository;
    }

}

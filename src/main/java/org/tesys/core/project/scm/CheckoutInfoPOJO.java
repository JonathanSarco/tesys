package org.tesys.core.project.scm;

public class CheckoutInfoPOJO {

    private String pathDestination;
    private String revision;
    private String url;

    @Override
    public String toString() {
	return "CheckoutInfoPOJO [pathDestination=" + pathDestination
		+ ", revision=" + revision + ", url=" + url + "]";
    }

    public String getPathDestination() {
	return pathDestination;
    }

    public void setPathDestination(String pathDestination) {
	this.pathDestination = pathDestination;
    }

    public String getRevision() {
	return revision;
    }

    public void setRevision(String revision) {
	this.revision = revision;
    }

    public String getUrl() {
	return url;
    }

    public void setUrl(String url) {
	this.url = url;
    }

}

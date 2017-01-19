package org.tesys.core.analysis.sonar;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;

import org.tesys.core.project.scm.RevisionPOJO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AnalisisPOJO implements Comparable<AnalisisPOJO> {

    private RevisionPOJO revision;
    private List<KeyValuePOJO> results;
    private boolean scaned;

    public AnalisisPOJO() {
	// needed by jackson
    }

    public AnalisisPOJO(RevisionPOJO rev) {
	results = new ArrayList<KeyValuePOJO>();
	this.revision = rev;
	this.revision.setDiff("");
	scaned = false;
    }

    public List<KeyValuePOJO> getResults() {
	return results;
    }

    public void setResults(List<KeyValuePOJO> results) {
	this.results = results;
    }

    public void add(KeyValuePOJO k) {
	results.add(k);
    }

    public String getID() {
	return this.revision.getProjectTrackingTask();
    }

    public RevisionPOJO getRevision() {
	return revision;
    }

    public void setRevision(RevisionPOJO revision) {
	this.revision = revision;
    }

    public boolean isScaned() {
	return scaned;
    }

    public void setScaned(boolean scaned) {
	this.scaned = scaned;
    }

    @Override
    public int compareTo(AnalisisPOJO o) {
	if (this.getRevision().getDate() < o.getRevision().getDate()) {
	    return -1;
	}
	return 1;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result
		+ ((revision == null) ? 0 : revision.hashCode());
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
	AnalisisPOJO other = (AnalisisPOJO) obj;
	if (revision == null) {
	    if (other.revision != null) {
		return false;
	    }
	} else if (this.revision.getDate() != (other.getRevision().getDate())) {
	    return false;
	}
	return true;
    }

}

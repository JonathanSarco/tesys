package org.tesys.core.analysis.sonar;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.tesys.util.MD5;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SonarMetricPOJO {

    private String key;
    private String name;
    private String description;
    private String type;
    private String domain;

    public SonarMetricPOJO() {
    }

    public SonarMetricPOJO(String key, String name, String description,
	    String type, String domain) {
	this.key = key;
	this.name = name;
	this.description = description;
	this.type = type;
	this.domain = domain;
    }

    public String getID() {
	return MD5.generateId(key);
    }

    public String getKey() {
	return key;
    }

    public void setKey(String key) {
	this.key = key;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public String getType() {
	return type;
    }

    public void setType(String type) {
	this.type = type;
    }

    public String getDomain() {
	return domain;
    }

    public void setDomain(String domain) {
	this.domain = domain;
    }

    @Override
    public String toString() {
	return "MetricPOJO [key=" + key + ", name=" + name + ", description="
		+ description + ", type=" + type + ", domain=" + domain + "]";
    }

}

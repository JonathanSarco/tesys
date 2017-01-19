package org.tesys.core;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;

@XmlRootElement(name = "account")
public class CombinedAnnotationBean {

    @JsonProperty("value")
    int x;

    public CombinedAnnotationBean(int x) {
	this.x = x;
    }

    public CombinedAnnotationBean() {
	this(15);
    }
}
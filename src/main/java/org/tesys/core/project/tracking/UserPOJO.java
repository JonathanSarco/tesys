package org.tesys.core.project.tracking;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UserPOJO implements User {

    public String name;

    public String emailAddress;

    public String displayName;

    public boolean active;

    @Override
    public String toString() {
	return "UserPOJO [name=" + name + ", emailAddress=" + emailAddress
		+ ", displayName=" + displayName + ", active=" + active + "]";
    }

    @Override
    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    @Override
    public String getEmailAddress() {
	return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
	this.emailAddress = emailAddress;
    }

    @Override
    public String getDisplayName() {
	return displayName;
    }

    public void setDisplayName(String displayName) {
	this.displayName = displayName;
    }

    @Override
    public boolean isActive() {
	return active;
    }

    public void setActive(boolean active) {
	this.active = active;
    }

}

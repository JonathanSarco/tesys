package org.tesys.core.estructures;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public class UnassignedDeveloper {

	private List<Issue> issues;
	private String id;
	protected String name;
    protected String DisplayName;
    
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd,HH:00", timezone="ART")
    protected Date timestamp;
	String unassignedIssues;

	public UnassignedDeveloper() {
		//Jackson
	}
	
	/**
	 * Convierte un UnassignedDeveloper a un Developer para poder relizar
	 * los analisis
	 * @param d UnassignedDeveloper a convertir
	 * @return Des
	 */
	public Developer cloneToDeveloper(UnassignedDeveloper d) {
		Developer toDev = new Developer();
		toDev.setName(d.getName());
		toDev.setDisplayName(d.getDisplayName());
		toDev.setIssues(d.getIssues());
		toDev.setTimestamp(d.getTimestamp());
		return toDev;
	}
	
/*	private List<Issue> convertIssues(List<UnassignedIssue> unassignedIssues){
		List<Issue> issues = new LinkedList<Issue>();
		for(UnassignedIssue i : unassignedIssues){
			issues.add(i.cloneToIssue(i));
		}
		return issues;
	}
*/	
	public UnassignedDeveloper(List<Issue> ui) {
		this.issues = ui;
	}

	public List<Issue> getIssues() {
		return issues;
	}

	public void setIssues(List<Issue> unassignedIssues) {
		this.issues = unassignedIssues;
	}
	
    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisplayName() {
		return DisplayName;
	}

	public void setDisplayName(String displayName) {
		DisplayName = displayName;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	
	public String getUnassignedIssues() {
		return unassignedIssues;
	}

	public void setUnassignedIssues(String unassignedIssues) {
		this.unassignedIssues = unassignedIssues;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}

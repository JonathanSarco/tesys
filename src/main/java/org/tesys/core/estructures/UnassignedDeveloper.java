package org.tesys.core.estructures;

import java.util.List;

public class UnassignedDeveloper extends Developer {

	private List<UnassignedIssue> unassignedIssues;
	
	
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
	public UnassignedDeveloper(List<UnassignedIssue> ui) {
		this.unassignedIssues = ui;
	}

	public List<UnassignedIssue> getUnassignedIssues() {
		return unassignedIssues;
	}

	public void setUnassignedIssues(List<UnassignedIssue> unassignedIssues) {
		this.unassignedIssues = unassignedIssues;
	}
}

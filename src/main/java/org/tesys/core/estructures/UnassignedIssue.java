package org.tesys.core.estructures;
/**
 * Clase que hereda de Issue
 * @author BBS
 *
 */
public class UnassignedIssue extends Issue{
	
	private boolean isDeleted = false;
	
	public UnassignedIssue() {
		//Jackson
	}

	public UnassignedIssue clone (Issue e) {
		//Clona una Issue a Unassigned Issue
		UnassignedIssue ui = new UnassignedIssue();
		ui.setIssueId(e.getIssueId());
		ui.setUser(e.getUser());
		ui.setIssueType(e.getIssueType());
		ui.setLabels(e.getLabels());
		ui.setMetrics(e.getMetrics());
		ui.setPuntuaciones(e.getPuntuaciones());
		ui.setSkills(e.getSkills());
		return ui;
	}
	
	public boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	
}

package org.tesys.core.interceptingFilter;
import java.util.LinkedList;
import java.util.List;

import org.tesys.core.estructures.SimilarIssue;

public class ResponseFilter {
	List<SimilarIssue> similarIssuesResponse;
	
	public ResponseFilter(){
		similarIssuesResponse = new LinkedList<SimilarIssue>();
	}

	public List<SimilarIssue> getSimilarIssuesResponse() {
		return similarIssuesResponse;
	}

	public void setSimilarIssuesResponse(List<SimilarIssue> similarIssuesResponse) {
		this.similarIssuesResponse = similarIssuesResponse;
	}
	
	public void addSimilarIssues(SimilarIssue similar){
		similarIssuesResponse.add(similar);
	}
	
}

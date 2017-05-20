package org.tesys.core.interceptingFilter;

import java.util.List;

import org.tesys.core.estructures.SimilarIssue;

public abstract class Filter {
	
	List<SimilarIssue> similarIssues;
	
	public abstract ResponseFilter execute(RequestFilter request, ResponseFilter response);
	    // Pass control to the next filter in the chain or 
	    // to the target resource
/*	// Common processing for all filters can go here 
	  public void doPreProcessing(ServletRequest request,   
	    ServletResponse response, FilterChain chain) {
	  }

	 // Common processing for all filters can go here 
	  public void doPostProcessing(ServletRequest request, 
	    ServletResponse response, FilterChain chain) {
	  }

	 // Common processing for all filters can go here
	  public abstract void doMainProcessing(ServletRequest 
	   request, ServletResponse response, FilterChain 
	   chain);*/
}

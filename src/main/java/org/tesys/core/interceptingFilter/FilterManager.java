package org.tesys.core.interceptingFilter;

public class FilterManager {
	FilterChain filterChain;
	
	public FilterManager(){
		filterChain = new FilterChain();
	}
	public ResponseFilter processFilter(RequestFilter requestFilter) {                    
	    // The filter manager builds the filter chain here 
	    // if necessary
		ResponseFilter responseFilter = new ResponseFilter();
	    // Pipe request through Filter Chain
		filterChain.processFilter(requestFilter, responseFilter);
		return responseFilter;
	    //process target resource
	   // target.execute(request, response);
	}
	
	public void addNewFilter(Filter filter){
		filterChain.addFilter(filter);
	}
		  
}

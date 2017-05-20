package org.tesys.core.interceptingFilter;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.tesys.core.estructures.Case;
import org.tesys.core.estructures.Developer;
import org.tesys.core.estructures.Issue;
import org.tesys.core.estructures.SimilarIssue;


public class FilterChain {
	// filter chain 
	 List<Filter> filters;
	  // Creates new FilterChain 
	  public FilterChain()  {
		  filters= new LinkedList<Filter>();
	  }

	  public void processFilter(RequestFilter request, ResponseFilter response){
	    // apply filters
		
	    for(Filter filter : filters)
	    {
	    	// pass request through various filters
	    	filter.execute(request, response);
	    }
	  }

	  public void addFilter(Filter filter)  {
	    filters.add(filter);
	  }
	
	
}

package org.tesys.distanceFunctions;

import java.util.List;
import java.util.Random;
import java.util.Vector;

public class ManhattanFunction extends FunctionSelector { 
  	
	public double calculate(List<Double>x){ 
        double sum = 0; 
        for (int i=0; i< x.size()-1; i++){  
	    sum += Math.abs(x.get(i)- x.get(i+1)) ;
        } 
     
        return sum; 
    }     

}
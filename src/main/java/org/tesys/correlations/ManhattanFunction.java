package org.tesys.correlations;

import java.util.Random;
import java.util.Vector;



public class ManhattanFunction { 
  
	/*public static double manhattan(Vector<Double>x, Vector<Double> y){ 
        double sum = 0; 
        for (int i=0; i< Math.min(x.size(), y.size()); i++){  
	    sum += Math.abs(x.get(i)- y.get(i)) ;
        } 
     
        return sum; 
    } */
    
	//Ver si corta bien, capaz de errror por el i+1-
	public static double manhattan(Vector<Double>x){ 
        double sum = 0; 
        for (int i=0; i< x.size(); i++){  
	    sum += Math.abs(x.get(i)- x.get(i+1)) ;
        } 
     
        return sum; 
    } 
    
	
     
    public static void main(String[] args) { 
    	Vector<Double>x=new Vector<Double>();
    	Vector<Double> y=new Vector<Double>();
    	x.add(1.0);
    	x.add(5.0);
    	x.addElement(10.0);
    	y.add(2.0);
    	y.add(2.0);
    	y.addElement(8.0);
       // double manhattan = manhattan(x, y);      
        System.out.println("Array x: "+arrayToString(x)); 
        System.out.println("Array y: "+arrayToString(y)); 
       // System.out.println(String.format("Manhattan distance: %f", manhattan)); 
    } 
     
    private static String arrayToString(Vector<Double>array){ 
        StringBuilder res = new StringBuilder("[ "); 
        for (int i=0; i<array.size(); i++){ 
            res.append(" "); 
            res.append(String.format("%5.2f", array.get(i))); 
        } 
        res.append(" ]"); 
        return res.toString(); 
    } 
     
 
}
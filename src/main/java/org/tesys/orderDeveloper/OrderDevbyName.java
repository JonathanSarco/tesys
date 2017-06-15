package org.tesys.orderDeveloper;

import java.util.Comparator;

import org.tesys.core.estructures.Developer;

public class OrderDevbyName implements Comparator<Developer> {

	 public int compare(Developer dev1, Developer dev2) {
		    return dev1.getDisplayName().compareTo(dev2.getDisplayName()); // Devuelve un entero positivo si la altura de o1 es mayor que la de o2
	}
}

package org.tesys.util;

import java.io.InputStream;
import java.util.Scanner;

/**
 * Clase de utilidades para la manipulacion de Strings
 * 
 * @author rulo
 * 
 */
final public class Strings {

    private Strings() {
	// to avoid the implicit one
    }

    /**
     * Genera un String a partir de un InputStream
     * 
     * @param is
     * @return String correspondiente al InputStream
     */
    public static String convertStreamToString(InputStream is) {
	@SuppressWarnings("resource")
	Scanner s = new Scanner(is).useDelimiter("\\A");
	return s.hasNext() ? s.next() : "";
    }

}

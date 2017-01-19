package org.tesys.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.nio.file.Files;

/**
 * Clase de utilidades para la manipulacion de archivos de entrada y salida
 * 
 * @author rulo
 * 
 */
public class InputOutput {

    private InputOutput() {
	// to avoid the implicit one
    }

    /**
     * Genera un String a partir de un archivo dado
     * 
     * @param path
     *            Ruta del archivo
     * @param encoding
     *            Codificacion de caracteres
     * @return El String correspondiente al archivo
     * @throws IOException
     */
    public static String readFile(String path, Charset encoding)
	    throws IOException {
	byte[] encoded = Files.readAllBytes(Paths.get(path));
	return new String(encoded, encoding);
    }

    /**
     * Redirecciona la salida estandar del System.out.print a un archivo
     * 
     * @param path
     *            Ruta del archivo
     * @throws FileNotFoundException
     */
    public static void systemOutToFile(String path)
	    throws FileNotFoundException {
	File file = new File(path);
	FileOutputStream foStream = new FileOutputStream(file);
	PrintStream out = new PrintStream(foStream);
	System.setOut(out);
    }

}

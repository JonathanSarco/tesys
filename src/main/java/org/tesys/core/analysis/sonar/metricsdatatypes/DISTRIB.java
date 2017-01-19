package org.tesys.core.analysis.sonar.metricsdatatypes;

/**
 * Clase paras tratar con un tipo de dato DISTRIB que es de la forma
 * 0=1;5=0;10=0 Donde por ejemplo eso indica que hay un archivo de complejidad 0
 * y de las demas complejidades no hay archivos (aunque depende la metrica para
 * conocer su significado, esa es la interpertacion)
 */

public class DISTRIB implements Metrics {

    private static final String SEMI_COLON = ";";
    private static final String EQUAL = "=";

    String actual, anterior;
    Double dactual, danterior;

    public DISTRIB(String actual, String anterior) {
	this.actual = actual;

	if (anterior == null || "null".equals(anterior)) {
	    // Crear una nueva complejidad, se agarra como modelo la nueva y
	    // pone todos los valores en 0
	    this.anterior = this.actual.replaceAll("=[0-9]+", "=0");
	} else {
	    this.anterior = anterior;
	}
    }

    public DISTRIB(Double dactual, Double danterior) {
	this.dactual = dactual;
	this.danterior = danterior;
    }

    /**
     * La diferencia se hace restando cada numero a la derecha del = Por lo que
     * puede dar numeros negativos indicando que saco a archivo o lo que fuera
     * de esa categoria y probablemente este en otra categoria a menos que lo
     * haya eliminado
     */

    public Double getDifferenceBetweenAnalysis() {

	Double ret = new Double(0.0);
	String[] actCom = actual.split(SEMI_COLON);
	String[] antCom = anterior.split(SEMI_COLON);

	if (actCom.length == antCom.length) {
	    for (int i = 0; i < actCom.length; i++) {
		actCom[i].split(EQUAL);
		antCom[i].split(EQUAL);
		ret += Double.valueOf(actCom[i].split(EQUAL)[0])
			* Double.valueOf(Integer.parseInt(actCom[i]
				.split(EQUAL)[1])
				- Integer.parseInt(antCom[i].split(EQUAL)[1]));
	    }

	    return ret;

	}

	// caso de que el anterior con el nuevo sean diferentes en longitud
	// (puede pasar??)
	return null;
    }

    /**
     * La suma se maneja igual que la resta, suma todos los valores a la derecha
     * del =
     */

    public Double getNewAnalysisPerTask() {
	return dactual + danterior;
    }

    /*
     * public Double getNewAnalysisPerTask() {
     * 
     * StringBuilder ret = new StringBuilder(); String[] actCom =
     * actual.split(SEMI_COLON); String[] antCom = anterior.split(SEMI_COLON);
     * 
     * if (actCom.length == antCom.length) {
     * 
     * for (int i = 0; i < actCom.length; i++) { String[] aux =
     * actCom[i].split(EQUAL); ret.append(aux[0] + EQUAL +
     * (Integer.parseInt(aux[1]) + Integer.parseInt(antCom[i].split(EQUAL)[1]))
     * + SEMI_COLON); } return ret.substring(0, ret.length() - 1); }
     * 
     * // caso de que el anterior con el nuevo sean diferentes en longitud
     * (puede pasar??) return null; }
     */

}

package org.tesys.util;

import java.net.MalformedURLException;
import java.util.Map;

import javax.ws.rs.core.Response;

/**
 * Interfaz para Cliente HTTP
 * 
 * @author rulo
 * 
 */
public interface HTTPClient {

    public String getURL();

    public void setURL(String url) throws MalformedURLException;

    /**
     * Metodo GET HTTP con parametros
     * 
     * @param resource
     *            Recurso al cual se le va a realizar el GET
     * @param params
     *            Parametros con los cuales se realizara al GET, esto equivale a
     *            "?param1=value1&param2=value2"
     * @return La respuesta desde el Servidor, la cual incluye el dato y/o
     *         informacion del codigo de estado
     */
    public Response GET(String resource, Map<String, String> params);

    /**
     * Metodo GET HTTP sin parametros
     * 
     * @param resource
     *            Recurso al cual se le va a realizar el GET
     * @return La respuesta desde el Servidor, la cual incluye el dato y/o
     *         informacion del codigo de estado
     */
    public Response GET(String resource);

    /**
     * Metodo PUT HTTP el cual envia la serealizacion de un objeto
     * 
     * @param resource
     *            Recurso al cual se le va a realizar el PUT
     * @param serealizable
     *            Objeto el cual se serealizara y se enviara al servidor.
     * @return La respuesta desde el Servidor, la cual incluye el dato y/o
     *         informacion del codigo de estado
     */
    public Response PUT(String resource, Object serealizable);

    /**
     * Metodo POST HTTP el cual envia la serealizacion de un objeto
     * 
     * @param resource
     *            Recurso al cual se le va a realizar el POST
     * @param serealizable
     *            Objeto el cual se serealizara y se enviara al servidor.
     * @return La respuesta desde el Servidor, la cual incluye el dato y/o
     *         informacion del codigo de estado
     */
    public Response POST(String resource, Object serealizable);

    /**
     * Metodo DELETE HTTP.
     * 
     * @param resource
     *            Recurso al cual se le va a realizar el DELETE
     * @return La respuesta desde el Servidor, la cual incluye el dato y/o
     *         informacion del codigo de estado
     */
    public Response DELETE(String resource);

}

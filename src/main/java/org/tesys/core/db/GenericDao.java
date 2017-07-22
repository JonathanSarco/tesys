package org.tesys.core.db;

/**
 * Esta es una una interfaz CRUD entre la con la cual se accedera al componente
 * de almacenamiento de datos.
 * 
 * @author leandro
 * 
 * @param <T>
 *            Donde T es el tipo de dato el cual se querra manipular a travez
 *            del CRUD.
 */
public interface GenericDao<T> {

    /**
     * Persist the object into database
     * 
     * @param newInstance
     * @return
     */
    void create(String id, T object);

    /**
     * Retrieve an object that was previously persisted to the database using
     * the indicated id as primary key.
     * 
     * @param id
     * @return
     */
    T read(String id);

    /**
     * Save changes made to a persistent object.
     * 
     * @param id
     * @param object
     */
    void update(String id, T object);

    /**
     * Remove an object from persistent storage in the database
     * 
     * @param object
     */
    void delete(String id);
    
    /**
     * Remove an object using a query
     */
    void deleteByQuery(String query);

}
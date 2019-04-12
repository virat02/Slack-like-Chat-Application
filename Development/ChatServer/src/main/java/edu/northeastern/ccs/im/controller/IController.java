package edu.northeastern.ccs.im.controller;

import edu.northeastern.ccs.im.communication.NetworkResponse;
/**
 * Template for all the entity controllers which include the basic CRUD operations
 * @param <T>
 */
public interface IController<T>{

    /**
     * A method for adding an entity.
     * @param entity the entity to be added.
     * @return a Network response with either pass or fail depending if the
     * entity was added.
     */
    NetworkResponse addEntity(T entity);

    /**
     * A method for search for an entity.
     * @param username of the entity to be searched.
     * @return a Network response with either pass or fail depending if the
     * entity was found.
     */
    NetworkResponse searchEntity(String username);

    /**
     * A method for updating an entity.
     * @param entity the entity to be updated.
     * @return a Network response with either pass or fail depending if the
     * entity was updated.
     */
    NetworkResponse updateEntity(T entity);

    /**
     * A method for deleting an entity.
     * @param entity the entity to be deleted.
     * @return a Network response with either pass or fail depending if the
     * entity was deleted.
     */
    NetworkResponse deleteEntity(T entity);

}
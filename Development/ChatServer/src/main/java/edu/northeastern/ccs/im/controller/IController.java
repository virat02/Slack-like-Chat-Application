package edu.northeastern.ccs.im.controller;

import edu.northeastern.ccs.im.communication.NetworkResponse;
/**
 * Template for all the entity controllers which include the basic CRUD operations
 * @param <T>
 */
public interface IController<T>{

    NetworkResponse addEntity(T entity);

    NetworkResponse searchEntity(String username);

    NetworkResponse updateEntity(T entity);

    NetworkResponse deleteEntity(T entity);

}
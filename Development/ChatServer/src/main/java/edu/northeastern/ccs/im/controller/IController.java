package edu.northeastern.ccs.im.controller;

public interface IController<T>{

    T addEntity(T entity);

    T searchEntity(String username);

    T updateEntity(T entity);

    T deleteEntity(T entity);

}

package edu.northeastern.ccs.im.customexceptions;

public class UserAlreadyPresentInGroupException extends Exception {

    // Constructor that accepts a message
    public UserAlreadyPresentInGroupException(String message)
    {
        super(message);
    }
}

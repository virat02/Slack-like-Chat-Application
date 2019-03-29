package edu.northeastern.ccs.im.customexceptions;

public class UsernameTooSmallException extends Exception {

    // Constructor that accepts a message
    public UsernameTooSmallException(String message)
    {
        super(message);
    }
}

package edu.northeastern.ccs.im.customexceptions;

public class UsernameTooLongException extends Exception {
    // Constructor that accepts a message
    public UsernameTooLongException(String message)
    {
        super(message);
    }
}

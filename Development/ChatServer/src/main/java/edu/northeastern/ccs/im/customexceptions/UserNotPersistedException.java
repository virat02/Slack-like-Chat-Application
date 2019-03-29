package edu.northeastern.ccs.im.customexceptions;

public class UserNotPersistedException extends Exception {

    // Constructor that accepts a message
    public UserNotPersistedException(String message)
    {
        super(message);
    }
}

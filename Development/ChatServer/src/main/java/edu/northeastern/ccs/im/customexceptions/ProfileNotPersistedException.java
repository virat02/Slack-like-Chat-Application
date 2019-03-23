package edu.northeastern.ccs.im.customexceptions;

public class ProfileNotPersistedException extends Exception {

    // Constructor that accepts a message
    public ProfileNotPersistedException(String message)
    {
        super(message);
    }
}

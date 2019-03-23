package edu.northeastern.ccs.im.customexceptions;

public class ProfileNotFoundException extends Exception {

    // Constructor that accepts a message
    public ProfileNotFoundException(String message)
    {
        super(message);
    }
}

package edu.northeastern.ccs.im.customexceptions;

public class ProfileNotDeletedException extends Exception{

    // Constructor that accepts a message
    public ProfileNotDeletedException(String message)
    {
        super(message);
    }
}

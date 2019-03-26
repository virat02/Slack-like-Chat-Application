package edu.northeastern.ccs.im.customexceptions;

public class GroupNotDeletedException extends Exception {

    // Constructor that accepts a message
    public GroupNotDeletedException(String message)
    {
        super(message);
    }
}

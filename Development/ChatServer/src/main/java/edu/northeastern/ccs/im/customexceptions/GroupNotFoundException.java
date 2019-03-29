package edu.northeastern.ccs.im.customexceptions;

/**
 * Exception thrown when a Group is not found
 */
public class GroupNotFoundException extends Exception{

    /**
     * Constructor for GroupNotFoundException
     * @param message
     */
    public GroupNotFoundException(String message)
    {
        super(message);
    }
}

package edu.northeastern.ccs.im.customexceptions;

/**
 * Exception thrown when a Group is not found
 */
public class GroupNotFoundException extends Exception{

    /**
     * Constructor for GroupNotFoundException
     * @param message the message attached to the exception.
     */
    public GroupNotFoundException(String message)
    {
        super(message);
    }
}

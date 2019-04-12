package edu.northeastern.ccs.im.customexceptions;

/**
 * Exception thrown when a Group is not deleted
 */
public class GroupNotDeletedException extends Exception {

    /**
     * Contructor for GroupNotDeletedException
     * @param message the message attached to the exception.
     */
    public GroupNotDeletedException(String message)
    {
        super(message);
    }
}

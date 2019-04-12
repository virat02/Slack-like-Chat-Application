package edu.northeastern.ccs.im.customexceptions;

/**
 * Exception thrown when a Group is not persisted By JPA
 */
public class GroupNotPersistedException extends Exception {

    /**
     * Constructor for GroupNotPersistedException
     * @param message
     */
    public GroupNotPersistedException(String message)
    {
        super(message);
    }
}

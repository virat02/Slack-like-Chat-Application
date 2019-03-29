package edu.northeastern.ccs.im.customexceptions;

/**
 * Exception thrown when JPA is unable to persist a Group
 */
public class GroupNotPersistedException extends Exception {

    /**
     * Contructor for GroupNotPersistedException
     * @param message
     */
    public GroupNotPersistedException(String message)
    {
        super(message);
    }
}

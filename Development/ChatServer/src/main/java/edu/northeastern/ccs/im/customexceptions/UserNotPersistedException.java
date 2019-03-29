package edu.northeastern.ccs.im.customexceptions;

/**
 * Exception thrown when JPA is unable to persist a User
 */
public class UserNotPersistedException extends Exception {

    /**
     * Constructor for UserNotPersistedException
     * @param message
     */
    public UserNotPersistedException(String message)
    {
        super(message);
    }
}

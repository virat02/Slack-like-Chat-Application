package edu.northeastern.ccs.im.customexceptions;

/**
 * Exception thrown when JPA is unable to persist a Profile
 */
public class ProfileNotPersistedException extends Exception {

    /**
     * Constructor for ProfileNotPersistedException
     * @param message
     */
    public ProfileNotPersistedException(String message)
    {
        super(message);
    }
}

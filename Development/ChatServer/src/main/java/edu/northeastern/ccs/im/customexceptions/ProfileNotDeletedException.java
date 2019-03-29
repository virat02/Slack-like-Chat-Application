package edu.northeastern.ccs.im.customexceptions;

/**
 * Exception thrown when JPA is unable to delete a profile
 */
public class ProfileNotDeletedException extends Exception{

    /**
     * Constructor for ProfileNotDeletedException
     * @param message
     */
    public ProfileNotDeletedException(String message)
    {
        super(message);
    }
}

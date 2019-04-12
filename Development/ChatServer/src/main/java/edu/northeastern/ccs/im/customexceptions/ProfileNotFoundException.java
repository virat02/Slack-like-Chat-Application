package edu.northeastern.ccs.im.customexceptions;

/**
 * Exception thrown when JPA is unable to find a profile
 */
public class ProfileNotFoundException extends Exception {

    /**
     * Constructor for ProfileNotFoundException
     * @param message the message attached to the exception.
     */
    public ProfileNotFoundException(String message)
    {
        super(message);
    }
}

package edu.northeastern.ccs.im.customexceptions;

/**
 * Exception thrown when JPA is unable to find a profile
 */
public class ProfileNotFoundException extends Exception {

    /**
     * Constructor for ProfileNotFoundException
     * @param message
     */
    public ProfileNotFoundException(String message)
    {
        super(message);
    }
}

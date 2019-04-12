package edu.northeastern.ccs.im.customexceptions;

/**
 * Exception thrown when user is not found
 */
public class UserNotFoundException extends Exception {

    /**
     * Constructor for UserNotFoundException
     * @param message the message attached to the exception.
     */
    public UserNotFoundException(String message)
    {
        super(message);
    }
}

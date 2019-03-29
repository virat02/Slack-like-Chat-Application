package edu.northeastern.ccs.im.customexceptions;

/**
 * Exception thrown when user is not found
 */
public class UserNotFoundException extends Exception {

    /**
     * Constructor for UserNotFoundException
     * @param message
     */
    public UserNotFoundException(String message)
    {
        super(message);
    }
}

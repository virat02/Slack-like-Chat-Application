package edu.northeastern.ccs.im.customexceptions;

/**
 * Exception thrown when Username does not contain lower case letters
 */
public class UsernameInvalidException extends Exception {

    /**
     * Constructor for UsernameInvalidException
     * @param message of the exception.
     */
    public UsernameInvalidException(String message)
    {
        super(message);
    }
}

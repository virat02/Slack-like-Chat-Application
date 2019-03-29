package edu.northeastern.ccs.im.customexceptions;

/**
 * Exception thrown when Username does not contain any number
 */
public class UsernameDoesNotContainNumberException extends Exception {

    /**
     * Constructor for UsernameDoesNotContainNumberException
     * @param message
     */
    public UsernameDoesNotContainNumberException(String message)
    {
        super(message);
    }
}

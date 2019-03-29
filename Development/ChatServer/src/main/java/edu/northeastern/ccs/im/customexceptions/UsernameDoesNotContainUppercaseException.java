package edu.northeastern.ccs.im.customexceptions;

/**
 * Exception thrown when Username does not contain any upper case letters
 */
public class UsernameDoesNotContainUppercaseException extends Exception {

    /**
     * Constructor for UsernameDoesNotContainUppercaseException
     * @param message
     */
    public UsernameDoesNotContainUppercaseException(String message)
    {
        super(message);
    }
}

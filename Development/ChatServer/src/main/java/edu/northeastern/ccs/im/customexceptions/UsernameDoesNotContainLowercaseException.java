package edu.northeastern.ccs.im.customexceptions;

/**
 * Exception thrown when Username does not contain lower case letters
 */
public class UsernameDoesNotContainLowercaseException extends Exception {

    /**
     * Constructor for UsernameDoesNotContainLowercaseException
     * @param message
     */
    public UsernameDoesNotContainLowercaseException(String message)
    {
        super(message);
    }
}

package edu.northeastern.ccs.im.customexceptions;

/**
 * Exception thrown when Password does not contain any lower case letters
 */
public class PasswordDoesNotContainLowercaseException extends Exception {

    /**
     * Constructor for PasswordDoesNotContainLowercaseException
     * @param message
     */
    public PasswordDoesNotContainLowercaseException(String message)
    {
        super(message);
    }
}

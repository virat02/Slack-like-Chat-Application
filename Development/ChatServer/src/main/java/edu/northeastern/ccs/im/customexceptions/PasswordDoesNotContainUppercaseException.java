package edu.northeastern.ccs.im.customexceptions;

/**
 * Exception thrown when Password does not contain any upper case letters
 */
public class PasswordDoesNotContainUppercaseException extends Exception {

    /**
     * Constructor for PasswordDoesNotContainUppercaseException
     * @param message
     */
    public PasswordDoesNotContainUppercaseException(String message)
    {
        super(message);
    }
}

package edu.northeastern.ccs.im.customexceptions;

/**
 * Exception thrown when Password does not contain any numbers
 */
public class PasswordDoesNotContainNumberException extends Exception {

    /**
     * Constructor for PasswordDoesNotContainNumberException
     * @param message
     */
    public PasswordDoesNotContainNumberException(String message)
    {
        super(message);
    }
}

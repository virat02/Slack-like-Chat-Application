package edu.northeastern.ccs.im.customexceptions;

/**
 * Exception thrown when password length is less than 4
 */
public class PasswordTooSmallException extends Exception {

    /**
     * Constructor for PasswordTooSmallException
     * @param message
     */
    public PasswordTooSmallException(String message)
    {
        super(message);
    }
}

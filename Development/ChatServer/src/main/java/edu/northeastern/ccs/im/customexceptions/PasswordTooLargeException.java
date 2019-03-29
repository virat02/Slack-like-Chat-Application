package edu.northeastern.ccs.im.customexceptions;

/**
 * Exception thrown when Password length is greater than 20
 */
public class PasswordTooLargeException extends Exception{

    /**
     * Constructor for PasswordTooLargeException
     * @param message
     */
    public PasswordTooLargeException(String message)
    {
        super(message);
    }
}

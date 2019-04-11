package edu.northeastern.ccs.im.customexceptions;

/**
 * Exception thrown when password length is less than 4
 */
public class PasswordInvalidException extends Exception {

    /**
     * Constructor for PasswordInvalidException
     * @param message of the exception
     */
    public PasswordInvalidException(String message)
    {
        super(message);
    }
}

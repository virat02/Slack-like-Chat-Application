package edu.northeastern.ccs.im.customexceptions;

/**
 * Exception for an invalid email
 */
public class InvalidEmailException extends Exception {

    // Constructor that accepts a message
    public InvalidEmailException(String message)
    {
        super(message);
    }
}
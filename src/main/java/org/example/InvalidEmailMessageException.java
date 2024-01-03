package org.example;

public class InvalidEmailMessageException extends Exception{
    public static final String  INVALID_INPUT = "Invalid Input";
    public static final String  LOGIN_FAILURE = "Login Failure";




    public InvalidEmailMessageException(String message) {
        super(message);
    }
}

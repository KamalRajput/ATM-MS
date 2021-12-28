package com.zinkworks.exception;

public class InsufficientDenominationException extends Exception{
    public InsufficientDenominationException() {
        super();
    }

    public InsufficientDenominationException(String message) {
        super(message);
    }

    public InsufficientDenominationException(String message, Throwable cause) {
        super(message, cause);
    }

    public InsufficientDenominationException(Throwable cause) {
        super(cause);
    }
}

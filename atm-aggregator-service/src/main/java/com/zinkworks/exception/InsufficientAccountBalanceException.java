package com.zinkworks.exception;

public class InsufficientAccountBalanceException extends Exception{
    public InsufficientAccountBalanceException() {
        super();
    }

    public InsufficientAccountBalanceException(String message) {
        super(message);
    }

    public InsufficientAccountBalanceException(String message, Throwable cause) {
        super(message, cause);
    }

    public InsufficientAccountBalanceException(Throwable cause) {
        super(cause);
    }
}

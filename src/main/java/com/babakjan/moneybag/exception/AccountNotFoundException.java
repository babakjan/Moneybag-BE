package com.babakjan.moneybag.exception;

public class AccountNotFoundException extends Exception {
    public AccountNotFoundException() {
    }

    public AccountNotFoundException(Long id) {
        super("Account of id: " + id + " not found.");
    }

    public AccountNotFoundException(String message) {
        super(message);
    }

    public AccountNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public AccountNotFoundException(Throwable cause) {
        super(cause);
    }

    public AccountNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

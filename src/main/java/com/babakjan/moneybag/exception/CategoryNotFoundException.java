package com.babakjan.moneybag.exception;

public class CategoryNotFoundException extends Exception {
    public CategoryNotFoundException() {
        super();
    }

    public CategoryNotFoundException(Long id) {
        super("Category of id: " + id + " not found.");
    }

    public CategoryNotFoundException(String message) {
        super(message);
    }

    public CategoryNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public CategoryNotFoundException(Throwable cause) {
        super(cause);
    }

    public CategoryNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

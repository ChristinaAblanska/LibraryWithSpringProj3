package com.academy.LibraryProjectSpring_2.excetionHandling;

public class BusinessNotFound extends RuntimeException{
    public BusinessNotFound() {
    }

    public BusinessNotFound(String message) {
        super(message);
    }

    public BusinessNotFound(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessNotFound(Throwable cause) {
        super(cause);
    }

    public BusinessNotFound(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

package com.biit.rest.exceptions;

public class NotAuthorizedException extends RuntimeException {

    private static final long serialVersionUID = 3682334777038371416L;

    public NotAuthorizedException(String message) {
        super(message);
    }

    public NotAuthorizedException(String message, Throwable e) {
        super(message, e);
    }
}

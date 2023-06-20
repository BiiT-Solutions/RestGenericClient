package com.biit.rest.exceptions;

public class NotFoundException extends EmptyResultException {

    private static final long serialVersionUID = 3682934777038371416L;

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable e) {
        super(message, e);
    }
}

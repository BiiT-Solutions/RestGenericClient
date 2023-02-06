package com.biit.rest.exceptions;

public class UnprocessableEntityException extends RuntimeException {
    private static final long serialVersionUID = 1375636360322118631L;

    public UnprocessableEntityException(String message) {
        super(message);
    }

    public UnprocessableEntityException(String message, Throwable e) {
        super(message, e);
    }

    public UnprocessableEntityException(Throwable e) {
        super(e);
    }
}

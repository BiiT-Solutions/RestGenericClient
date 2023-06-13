package com.biit.rest.exceptions;

public class InvalidResponseException extends RuntimeException {

    private static final long serialVersionUID = 3682934852038371416L;

    public InvalidResponseException(String message) {
        super(message);
    }

    public InvalidResponseException(String message, Throwable e) {
        super(message, e);
    }

    public InvalidResponseException(Throwable e) {
        super(e);
    }
}

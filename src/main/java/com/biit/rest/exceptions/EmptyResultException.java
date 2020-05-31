package com.biit.rest.exceptions;

public class EmptyResultException extends Exception {

	private static final long serialVersionUID = 3681934777038371416L;

	public EmptyResultException(String message) {
		super(message);
	}

	public EmptyResultException(String message, Throwable e) {
		super(message, e);
	}
}

package com.biit.rest.exceptions;

public class UnprocessableEntityException extends Exception {
	private static final long serialVersionUID = 1375636360322118631L;

	public UnprocessableEntityException(String message) {
		super(message);
	}
}

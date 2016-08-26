package com.tenx.ms.retail.exceptions;

public class UpdateViolationException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public UpdateViolationException(String message, Throwable cause) {
		super(message, cause);
	}

	public UpdateViolationException(String message) {
		super(message);
	}
}

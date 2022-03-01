package com.koray.finago.serviceimpl;

public class APIException extends Exception {

	/**
	 * Serial version UID.
	 */
	private static final long serialVersionUID = 1904924954995479356L;
	
	public APIException(final String message) {
		super(message);
	}

	public APIException(final Exception e) {
		super(e);
	}
}
package com.nt.exception;

public class InvalidEmailIdException extends RuntimeException {

	public InvalidEmailIdException(String msg) {
		super(msg);
	}
}

package com.nt.exception;

public class InventoryServiceException extends RuntimeException{
	
	public InventoryServiceException(String msg) {
		super(msg);
	}
	
	public InventoryServiceException(String msg, Throwable cause) {
		super(msg,cause);
	}

}

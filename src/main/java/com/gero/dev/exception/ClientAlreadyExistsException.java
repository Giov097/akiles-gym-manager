package com.gero.dev.exception;

public class ClientAlreadyExistsException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4263485315071838744L;

	public ClientAlreadyExistsException(Long dni) {
		super(String.format("Cliente con dni %d ya existe", dni));
	}
}

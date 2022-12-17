package com.gero.dev.exception;

public class UserNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3508499480010840069L;
	
	public UserNotFoundException() {
		super("El usuario ingresado no existe");
	}

}

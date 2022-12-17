package com.gero.dev.exception;

public class WrongPasswordException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2469259518024328406L;

	public WrongPasswordException() {
		super("Contraseña incorrecta");
	}

}

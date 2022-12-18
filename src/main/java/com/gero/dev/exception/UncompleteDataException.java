package com.gero.dev.exception;

public class UncompleteDataException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8312376367347111097L;

	public UncompleteDataException() {
		super("Deben completarse todos los campos");
	}

}

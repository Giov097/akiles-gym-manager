package com.gero.dev.exception;

import java.time.Year;

public class PaymentExistsException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3758470620598942844L;
	
	public PaymentExistsException(Year month, Integer year) {
		super(String.format("El pago para el período %d-%d ya existe", month.getValue(), year));
	}

}

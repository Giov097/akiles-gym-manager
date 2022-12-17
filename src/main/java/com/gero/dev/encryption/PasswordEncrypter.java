package com.gero.dev.encryption;

import at.favre.lib.crypto.bcrypt.BCrypt;

/**
 * A class designed to encrypt and decrypt the users' passwords
 * 
 */
public class PasswordEncrypter {

	/**
	 * Private constructor to avoid instantiation
	 */
	private PasswordEncrypter() {
	}

	/**
	 * Encrypts a password and gives the hashed value
	 * 
	 * @param password The original password
	 * @return hashed password
	 */
	public static String encrypt(String password) {
		return BCrypt.withDefaults().hashToString(12, password.toCharArray());
	}

	/**
	 * Checks if a password matches its encrypted hash
	 * 
	 * @param hashed   The hashed string
	 * @param password The non-hashed string
	 * @return A boolean stating if the two arguments are equals
	 */
	public static Boolean verify(String hashed, String password) {
		return BCrypt.verifyer().verify(password.toCharArray(), hashed).verified;

	}
}

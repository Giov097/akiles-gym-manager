package com.gero.dev.utils;

import lombok.Getter;

public enum MembershipStatus {

	BAJA("Baja"), ACTIVA("Activa");

	@Getter
	private String value;

	MembershipStatus(String value) {
		this.value = value;
	}

}

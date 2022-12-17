package com.gero.dev.domain;

import java.io.Serializable;
import java.time.Month;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeeId implements Serializable {

	/**
	 * 
	 */
	private static final transient long serialVersionUID = -1821565317662512160L;

	private Client client;

	private Month month;

	private Integer year;
}

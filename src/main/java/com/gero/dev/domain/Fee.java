package com.gero.dev.domain;

import java.time.Month;
import java.time.Year;
import java.time.ZonedDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity(name = "fees")
public class Fee {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Month month;
	
	private Year year;
	
	private ZonedDateTime paymentDate;

	private Double paymentAmmount;

}

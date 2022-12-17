package com.gero.dev.domain;

import java.time.Month;
import java.time.ZonedDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;

import lombok.Data;

@Data
@Entity(name = "fees")
@IdClass(FeeId.class)
public class Fee {

	@Id
	@ManyToOne
	private Client client;
	
	@Id
	private Month month;
	
	@Id
	private Integer year;

	private ZonedDateTime paymentDate;

	private Double paymentAmmount;

}

package com.gero.dev.domain;

import java.time.ZonedDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity(name = "clients")
public class Client {
	
	@Id
	private Long dni;
	
	private String fullName;
		
	private ZonedDateTime createdAt;
	
	private Boolean enabled;
}

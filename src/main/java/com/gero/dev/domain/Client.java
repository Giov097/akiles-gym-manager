package com.gero.dev.domain;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity(name = "clients")
public class Client {
	
	@Id
	private Long dni;
	
	private String fullName;
		
	private LocalDateTime createdAt;
	
	private Boolean enabled;
}

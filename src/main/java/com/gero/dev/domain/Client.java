package com.gero.dev.domain;

import java.time.ZonedDateTime;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Data;

@Data
@Entity(name = "clients")
public class Client {
	
	@Id
	private Long dni;
	
	private String fullName;
	
	@OneToMany
	private List<Fee> fees;
	
	private ZonedDateTime createdAt;
	
	private Boolean enabled;
}

package com.gero.dev.domain;

import java.time.ZonedDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity(name = "users")
public class User {

	@Id
	private String username;

	private String password;

	private ZonedDateTime createdAt;

}

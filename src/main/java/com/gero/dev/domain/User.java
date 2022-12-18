package com.gero.dev.domain;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity(name = "users")
public class User {

	@Id
	private String username;

	private String password;

	private LocalDateTime createdAt;

}

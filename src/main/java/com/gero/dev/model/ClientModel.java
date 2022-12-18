package com.gero.dev.model;

import java.time.LocalDateTime;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
public class ClientModel {

	@Setter
	private SimpleLongProperty dni;
	
	@Setter
	private SimpleStringProperty fullName;
	
	@Getter @Setter
	private LocalDateTime createdAt;

	@Setter
	private SimpleStringProperty enabled;
	
//	@Setter
//	private SimpleStringProperty feeStatus;

	public Long getDni() {
		return dni.get();
	}
	
	public String getFullName() {
		return fullName.get();
	}
	
	public String getEnabled() {
		return enabled.get();
	}
	
//	public String getFeeStatus() {
//		return feeStatus.get();
//	}
}

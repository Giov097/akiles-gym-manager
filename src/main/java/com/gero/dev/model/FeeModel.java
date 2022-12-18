package com.gero.dev.model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
public class FeeModel {

	@Setter
	private SimpleStringProperty period;

	@Setter
	private SimpleStringProperty paymentDate;

	@Setter
	private SimpleDoubleProperty paymentAmmount;
	
	@Setter
	private SimpleStringProperty observations;

	public String getPeriod() {
		return period.get();
	}

	public String getPaymentDate() {
		return paymentDate.get();
	}

	public Double getPaymentAmmount() {
		return paymentAmmount.get();
	}

	public String getObservations() {
		return observations.get();
	}

}

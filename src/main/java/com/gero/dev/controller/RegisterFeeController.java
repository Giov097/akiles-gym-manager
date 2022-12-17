package com.gero.dev.controller;

import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.ResourceBundle;

import org.hibernate.Session;

import com.gero.dev.application.data.SelectedData;
import com.gero.dev.domain.Client;
import com.gero.dev.domain.Fee;
import com.gero.dev.model.ClientModel;
import com.gero.dev.persistence.HibernateConnection;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class RegisterFeeController implements Initializable {

	private Session session = HibernateConnection.getCurrentSession();

	private ClientModel selectedClient;

	@FXML
	private TextField dniInput;

	@FXML
	private TextField clientInput;

	@FXML
	private ComboBox<Integer> monthInput;

	@FXML
	private ComboBox<Year> yearInput;

	@FXML
	private DatePicker paymentDateInput;

	@FXML
	private TextField paymentAmmountInput;

	@FXML
	protected void createFee(ActionEvent actionEvent) {
		Fee fee = new Fee();
		fee.setPaymentDate(paymentDateInput.getValue().atStartOfDay(ZoneId.systemDefault()));
		fee.setMonth(Month.of(monthInput.getValue()));
		fee.setYear(yearInput.getValue());
		fee.setPaymentAmmount(Double.valueOf(paymentAmmountInput.getText()));
		session.beginTransaction();
		session.merge(fee);
		session.getTransaction().commit();
		Client client = session.find(Client.class, Long.valueOf(dniInput.getText()));
		client.getFees().add(fee);
		session.beginTransaction();
		session.merge(client);
		session.getTransaction().commit();
	}

	@FXML
	private void close() {
		yearInput.getScene().getWindow().hide();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		monthInput.setItems(
				FXCollections.observableArrayList(Arrays.stream(Month.values()).map(Month::getValue).toList()));
		yearInput.setItems(
				FXCollections.observableArrayList(Year.now().minusYears(1l), Year.now(), Year.now().plusYears(1l)));
		paymentDateInput.setDayCellFactory(picker -> new DateCell() {
			@Override
			public void updateItem(LocalDate date, boolean empty) {
				super.updateItem(date, empty);
				setDisable(empty || date.compareTo(LocalDate.now().minusMonths(1l)) < 0);
			}
		});
		paymentAmmountInput.addEventFilter(KeyEvent.KEY_TYPED, keyEvent -> {
			char[] ar = keyEvent.getCharacter().toCharArray();
			char ch = ar[keyEvent.getCharacter().toCharArray().length - 1];
			if (!(ch >= '0' && ch <= '9')) {
				keyEvent.consume();
			}
		});
		System.out.println(selectedClient);
		clientInput.setText(SelectedData.getClient().getFullName());
		dniInput.setText(SelectedData.getClient().getDni().toString());
	}

	public void initData(ClientModel client) {
		this.selectedClient = client;
		System.out.println(selectedClient);
	}

}

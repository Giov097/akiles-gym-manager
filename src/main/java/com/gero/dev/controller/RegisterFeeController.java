package com.gero.dev.controller;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.Year;
import java.util.Arrays;
import java.util.Optional;
import java.util.ResourceBundle;

import javax.persistence.EntityExistsException;

import org.hibernate.Session;
import org.modelmapper.ModelMapper;

import com.gero.dev.application.data.SelectedData;
import com.gero.dev.domain.Client;
import com.gero.dev.domain.Fee;
import com.gero.dev.domain.FeeId;
import com.gero.dev.exception.PaymentExistsException;
import com.gero.dev.persistence.HibernateConnection;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class RegisterFeeController implements Initializable {

	private Session session = HibernateConnection.getCurrentSession();

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
	private Button registerPaymentButton;

	private ModelMapper modelMapper = new ModelMapper();

	@FXML
	protected void createFee(ActionEvent actionEvent) {
		try {
			Fee fee = new Fee();
			fee.setPaymentDate(paymentDateInput.getValue().atTime(LocalTime.now()));
			fee.setMonth(Month.of(monthInput.getValue()));
			fee.setYear(yearInput.getValue().getValue());
			fee.setPaymentAmmount(Double.valueOf(paymentAmmountInput.getText()));
			Client client = session.find(Client.class, Long.valueOf(dniInput.getText()));
			fee.setClient(client);
			Optional.ofNullable(session.get(Fee.class, modelMapper.map(fee, FeeId.class))).ifPresent(f -> {
				throw new EntityExistsException();
			});
			session.beginTransaction();
			session.persist(fee);
			session.getTransaction().commit();
			creationSuccessful();
			close();
		} catch (EntityExistsException e) {
			if (session.getTransaction().isActive())
				session.getTransaction().rollback();
			throw new PaymentExistsException(yearInput.getValue(), monthInput.getValue());
		}
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
		clientInput.setText(SelectedData.getClient().getFullName());
		dniInput.setText(SelectedData.getClient().getDni().toString());
	}

	private void creationSuccessful() {
		Alert alert = new Alert(AlertType.INFORMATION, "Pago registrado exitosamente", ButtonType.OK);
		alert.showAndWait();
	}
}

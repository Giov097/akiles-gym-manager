package com.gero.dev.controller;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ResourceBundle;

import org.hibernate.Session;

import com.gero.dev.application.App;
import com.gero.dev.domain.Client;
import com.gero.dev.exception.ClientAlreadyExistsException;
import com.gero.dev.persistence.HibernateConnection;
import com.gero.dev.utils.Scenes;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class CreateClientController implements Initializable {

	@FXML
	private TextField fullNameInput;

	@FXML
	private TextField dniInput;

	private Session session = HibernateConnection.getCurrentSession();

	@FXML
	protected void handleCreateClient(ActionEvent actionEvent) {
		Optional<Client> foundClient = Optional
				.ofNullable(session.find(Client.class, Long.valueOf(dniInput.getText())));
		foundClient.ifPresent(client -> {
			throw new ClientAlreadyExistsException(client.getDni());
		});
		Client newClient = new Client();
		newClient.setFullName(fullNameInput.getText());
		newClient.setDni(Long.valueOf(dniInput.getText()));
		newClient.setCreatedAt(LocalDateTime.now());
		newClient.setEnabled(true);
		session.beginTransaction();
		session.merge(newClient);
		session.getTransaction().commit();
		creationSuccessful();
		close();
	}

	private void creationSuccessful() {
		Alert alert = new Alert(AlertType.INFORMATION, "Cliente creado exitosamente", ButtonType.OK);
		alert.showAndWait();
	}

	@FXML
	private void close() {
		fullNameInput.getScene().getWindow().hide();
		App.setScene(Scenes.DASHBOARD);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		dniInput.addEventFilter(KeyEvent.KEY_TYPED, keyEvent -> {
			char[] ar = keyEvent.getCharacter().toCharArray();
			char ch = ar[keyEvent.getCharacter().toCharArray().length - 1];
			if (!(ch >= '0' && ch <= '9')) {
				keyEvent.consume();
			}
		});
	}

}

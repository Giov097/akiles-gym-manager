package com.gero.dev.controller;

import java.util.Optional;

import org.hibernate.Session;

import com.gero.dev.application.App;
import com.gero.dev.domain.User;
import com.gero.dev.exception.UserNotFoundException;
import com.gero.dev.exception.WrongPasswordException;
import com.gero.dev.persistence.HibernateConnection;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

public class LoginController {

	@FXML
	private TextField usernameInput;

	@FXML
	private PasswordField passwordInput;

	private Session session = HibernateConnection.getCurrentSession();

	@FXML
	protected void handleLogin(ActionEvent event) {
		Optional<User> user = Optional.ofNullable(session.find(User.class, usernameInput.getText()));
		user.ifPresent(foundUser -> {
			if (!foundUser.getPassword().equals(passwordInput.getText())) {
				throw new WrongPasswordException();
			}
			App.setScene("/dashboard.fxml");
		});
		if (user.isEmpty()) {
			throw new UserNotFoundException();
		}
	}

	@FXML
	protected void onEnter(ActionEvent event) {
		usernameInput.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.ENTER) {
				handleLogin(event);
			}
		});
		passwordInput.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.ENTER) {
				handleLogin(event);
			}
		});
	}
}

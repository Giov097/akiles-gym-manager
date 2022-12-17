package com.gero.dev.controller;

import java.util.Objects;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ErrorController {

	@FXML
	private Label errorMessage;

	public void setErrorText(String text) {
		errorMessage.setText(text);
	}

	@FXML
	private void close() {
		errorMessage.getScene().getWindow().hide();
	}

	public static Throwable findCause(Throwable throwable) {
		Objects.requireNonNull(throwable);
		Throwable rootCause = throwable;
		while (rootCause.getCause() != null && rootCause.getCause() != rootCause) {
			rootCause = rootCause.getCause();
		}
		return rootCause;
	}

	public static void showError(Thread t, Throwable e) {
		log.error("***Default exception handler***");
		log.error(e.getClass().getSimpleName());
		e.printStackTrace();
		if (Platform.isFxApplicationThread()) {
			showErrorDialog(e);
		} else {
			log.error("An unexpected error occurred in " + t);
		}
	}

	private static void showErrorDialog(Throwable e) {
		Throwable cause = e.getCause() != null ? ErrorController.findCause(e.getCause()) : e;
		Alert alert = new Alert(AlertType.ERROR, cause.getMessage(), ButtonType.OK);
		alert.showAndWait();
	}
}

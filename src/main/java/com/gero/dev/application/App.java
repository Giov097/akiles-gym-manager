package com.gero.dev.application;

import com.gero.dev.controller.ErrorController;
import com.gero.dev.persistence.HibernateConnection;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

	private static Stage primaryStage;

	@Override
	public void start(Stage stage) {
		Thread.setDefaultUncaughtExceptionHandler(ErrorController::showError);
		App.primaryStage = stage;
		setScene("/login.fxml");
	}

	public static void main(String[] args) {
		HibernateConnection.openCurrentSession();
		launch();
	}

	public static void setScene(String fxml) {
		try {
			Parent root = FXMLLoader.load(App.class.getResource(fxml));
			Scene scene = new Scene(new StackPane(root));
			primaryStage.setTitle("Akiles Gym Manager");
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
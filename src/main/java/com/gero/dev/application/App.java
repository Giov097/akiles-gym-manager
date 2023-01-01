package com.gero.dev.application;

import java.io.FileReader;
import java.io.IOException;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import com.gero.dev.controller.ErrorController;
import com.gero.dev.persistence.HibernateConnection;
import com.gero.dev.utils.Scenes;

import javafx.application.Application;
import javafx.application.Platform;
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
		stage.setOnCloseRequest(e -> {
			Platform.exit();
			System.exit(0);
		});
		App.primaryStage = stage;
		setScene(Scenes.LOGIN);
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

	public static String getVersion() {
		try {
	        MavenXpp3Reader reader = new MavenXpp3Reader();
	        Model model = reader.read(new FileReader("pom.xml"));
	        return model.getVersion();
		} catch (IOException | XmlPullParserException e) {
			e.printStackTrace();
			return null;
		}
	}
}
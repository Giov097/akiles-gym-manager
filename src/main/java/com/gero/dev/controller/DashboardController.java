package com.gero.dev.controller;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.hibernate.Session;
import org.modelmapper.TypeToken;

import com.gero.dev.application.App;
import com.gero.dev.application.data.SelectedData;
import com.gero.dev.domain.Client;
import com.gero.dev.model.ClientModel;
import com.gero.dev.persistence.HibernateConnection;
import com.gero.dev.utils.Mapper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class DashboardController implements Initializable {

	@FXML
	private Button newClientButton;

	@FXML
	private Button registerFeeButton;

	@FXML
	private Button viewClientButton;

	@FXML
	private TableView<ClientModel> clientsTable;

	@FXML
	private TableColumn<ClientModel, Long> dniColumn;

	@FXML
	private TableColumn<ClientModel, String> clientNameColumn;

	private Session session = HibernateConnection.getCurrentSession();

	@FXML
	protected void handleNewClient(ActionEvent event) {
		try {
			Stage stage = new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.initStyle(StageStyle.UTILITY);
			FXMLLoader loader = new FXMLLoader(App.class.getResource("/create-client.fxml"));
			Parent root = loader.load();
			stage.setScene(new Scene(root));
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	protected void handleFee(ActionEvent actionEvent) {
		try {
			SelectedData.setClient(clientsTable.getSelectionModel().getSelectedItem());
			Stage stage = new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.initStyle(StageStyle.UTILITY);
			FXMLLoader loader = new FXMLLoader(App.class.getResource("/register-payment.fxml"));
			Parent root = loader.load();
			loader.<RegisterFeeController>getController().initData(clientsTable.getSelectionModel().getSelectedItem());
			stage.setScene(new Scene(root));
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loadData();
		clientsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			if (newSelection != null) {
				registerFeeButton.setDisable(false);
			}
		});
	}

	public void loadData() {
		dniColumn.setCellValueFactory(new PropertyValueFactory<>("dni"));
		clientNameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
		List<Client> clients = session.createQuery("from clients", Client.class).list();
		List<ClientModel> model = Mapper.clientModelMapper().map(clients, new TypeToken<List<ClientModel>>() {
		}.getType());
		ObservableList<ClientModel> observableList = FXCollections.observableList(model);
		clientsTable.setItems(observableList);
	}
}

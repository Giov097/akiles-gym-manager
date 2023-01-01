package com.gero.dev.controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import org.hibernate.Session;
import org.modelmapper.TypeToken;

import com.gero.dev.application.App;
import com.gero.dev.application.data.SelectedData;
import com.gero.dev.domain.Client;
import com.gero.dev.model.ClientModel;
import com.gero.dev.persistence.HibernateConnection;
import com.gero.dev.utils.Mapper;
import com.gero.dev.utils.MembershipStatus;
import com.gero.dev.utils.Scenes;
import com.gero.dev.utils.StringUtils;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
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

	@FXML
	private TableColumn<ClientModel, String> membershipColumn;

	@FXML
	private TableColumn<ClientModel, String> lastPaymentColumn;

	@FXML
	private TextField searchInput;

	@FXML
	private MenuItem exitButton;

	@FXML
	private MenuItem logoutButton;
	
	@FXML
	private MenuItem aboutButton;

	private Session session = HibernateConnection.getCurrentSession();

	@FXML
	protected void handleNewClient(ActionEvent event) {
		openSceneWithClientData(Scenes.CREATE_CLIENT);
	}

	@FXML
	protected void handleFee(ActionEvent actionEvent) {
		openSceneWithClientData(Scenes.REGISTER_CLIENT);
	}

	@FXML
	protected void handleViewClient(ActionEvent actionEvent) {
		openSceneWithClientData(Scenes.VIEW_CLIENT);
	}

	private void openSceneWithClientData(String fxml) {
		try {
			SelectedData.setClient(clientsTable.getSelectionModel().getSelectedItem());
			Stage stage = new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.initStyle(StageStyle.UTILITY);
			FXMLLoader loader = new FXMLLoader(App.class.getResource(fxml));
			Parent root = loader.load();
			stage.setScene(new Scene(root));
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	protected void handleExit(ActionEvent actionEvent) {
		Platform.exit();
	}

	@FXML
	protected void handleLogout(ActionEvent actionEvent) {
		App.setScene("/login.fxml");
	}
	
	@FXML
	protected void handleAbout(ActionEvent actionEvent) {
		String msg = String.format("""
				Akiles Gym Manager
				Versión %s
				Giov097
				""", 
				Optional.ofNullable(App.getVersion()).orElse("desconocida"));
		Alert alert = new Alert(AlertType.INFORMATION, msg, ButtonType.OK);
		alert.showAndWait();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		clientsTable.setPlaceholder(new Label("No se han registrado clientes"));
		loadData();
		clientsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			if (newSelection != null) {
				if (newSelection.getEnabled().equals(MembershipStatus.ACTIVA.getValue())) {
					registerFeeButton.setDisable(false);
				}
				viewClientButton.setDisable(false);
			}
		});
		clientsTable.setRowFactory(tv -> {
			TableRow<ClientModel> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && (!row.isEmpty())) {
					openSceneWithClientData(Scenes.VIEW_CLIENT);
				}
			});
			return row;
		});
		clientsTable.setRowFactory(tv -> new TableRow<ClientModel>() {
			@Override
			protected void updateItem(ClientModel client, boolean empty) {
				super.updateItem(client, empty);
				setStyle("");
				Optional.ofNullable(client).ifPresent(c -> {
					Optional.ofNullable(c.getEnabled())
							.filter(e -> e.equalsIgnoreCase(MembershipStatus.BAJA.getValue()))
							.ifPresent(e -> setStyle("-fx-background-color: #A9A9A9;"));
					Optional.ofNullable(c.getLastFee())
							.filter(lf -> (lf.length() > 1
									&& LocalDateTime.parse(lf, DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm"))
											.isBefore(LocalDateTime.now().minusMonths(1l))))
							.ifPresent(lf -> setStyle("-fx-background-color: #FF6347;"));
				});
			}
		});
		clientsTable.setOnKeyPressed(keyEvent -> {
			if (keyEvent.getCode() == KeyCode.ENTER) {
				openSceneWithClientData(Scenes.VIEW_CLIENT);
			}
		});
	}

	public void loadData() {
		dniColumn.setCellValueFactory(new PropertyValueFactory<>("dni"));
		clientNameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
		membershipColumn.setCellValueFactory(new PropertyValueFactory<>("enabled"));
		lastPaymentColumn.setCellValueFactory(new PropertyValueFactory<>("lastFee"));
		List<Client> clients = session.createQuery("from clients", Client.class).list();
		List<ClientModel> model = Mapper.clientModelMapper().map(clients, new TypeToken<List<ClientModel>>() {
		}.getType());
		ObservableList<ClientModel> observableList = FXCollections.observableList(model);
		FilteredList<ClientModel> filteredData = new FilteredList<>(observableList, p -> true);
		searchInput.textProperty().addListener((observable, oldValue, newValue) -> filteredData.setPredicate(client -> {
			if (newValue == null || newValue.isEmpty()) {
				return true;
			}
			String lowerCaseFilter = newValue.toLowerCase();
			return normalize(client.getFullName()).toLowerCase().contains(normalize(lowerCaseFilter))
					|| normalize(client.getDni().toString()).contains(normalize(lowerCaseFilter));
		}));
		SortedList<ClientModel> sortedData = new SortedList<>(filteredData);
		sortedData.comparatorProperty().bind(clientsTable.comparatorProperty());
		clientsTable.setItems(sortedData);
	}

	private String normalize(String text) {
		return StringUtils.removeDiacriticalMarks(text);
	}
}

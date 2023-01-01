package com.gero.dev.controller;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

import org.hibernate.Session;
import org.modelmapper.TypeToken;

import com.gero.dev.application.App;
import com.gero.dev.application.data.SelectedData;
import com.gero.dev.domain.Client;
import com.gero.dev.domain.Fee;
import com.gero.dev.model.FeeModel;
import com.gero.dev.persistence.HibernateConnection;
import com.gero.dev.utils.Mapper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class ViewClientController implements Initializable {

	@FXML
	private Label clientNameLabel;

	@FXML
	private Label latestFeeDateLabel;

	@FXML
	private Label registrationDateLabel;

	@FXML
	private Button toggleEnableButton;

	@FXML
	private TableView<FeeModel> feesTable;

	@FXML
	private TableColumn<FeeModel, String> periodColumn;

	@FXML
	private TableColumn<FeeModel, String> paymentDateColumn;

	@FXML
	private TableColumn<FeeModel, String> feeAmmountDate;

	@FXML
	private TableColumn<FeeModel, String> observationsColumn;

	private Session session = HibernateConnection.getCurrentSession();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		feesTable.setPlaceholder(new Label("No se han registrado pagos"));
		loadData();
		periodColumn.setSortType(TableColumn.SortType.DESCENDING);
		feesTable.getSortOrder().add(periodColumn);
		clientNameLabel.setText(SelectedData.getClient().getFullName());
	}

	public void loadData() {
		periodColumn.setCellValueFactory(new PropertyValueFactory<>("period"));
		paymentDateColumn.setCellValueFactory(new PropertyValueFactory<>("paymentDate"));
		feeAmmountDate.setCellValueFactory(new PropertyValueFactory<>("paymentAmmount"));
		observationsColumn.setCellValueFactory(new PropertyValueFactory<>("observations"));
		List<Fee> fees = session.createQuery("from fees f where f.client.dni = :dni", Fee.class)
				.setParameter("dni", SelectedData.getClient().getDni()).list();
		List<FeeModel> model = Mapper.feeModelMapper().map(fees, new TypeToken<List<FeeModel>>() {
		}.getType());
		ObservableList<FeeModel> observableList = FXCollections.observableList(model);
		feesTable.setItems(observableList);
		registrationDateLabel.setText(
				SelectedData.getClient().getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm")));
		latestFeeDateLabel
				.setText(fees.stream().map(Fee::getPaymentDate).reduce((d1, d2) -> d1.compareTo(d2) > 0 ? d1 : d2)
						.map(date -> date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm"))).orElse("-"));
		toggleEnableButton.setText(SelectedData.getClient().getEnabled().equalsIgnoreCase("Activa") ? "Dar de baja" : "Dar de alta");
	}

	@FXML
	public void handleEnabledStatus(ActionEvent actionEvent) {
		Alert alert = new Alert(AlertType.CONFIRMATION, "¿Desea cambiar el estado del cliente?");
		alert.showAndWait().ifPresent(res -> {
			if (res.equals(ButtonType.OK)) {
				Client client = session.find(Client.class, SelectedData.getClient().getDni());
				client.setEnabled(!client.getEnabled());
				session.beginTransaction();
				session.merge(client);
				session.getTransaction().commit();
				operationSuccessful();
				close();
			}
		});
	}
	
	private void operationSuccessful() {
		Alert alert = new Alert(AlertType.INFORMATION, "Estado modificado exitosamente", ButtonType.OK);
		alert.showAndWait();
	}

	@FXML
	private void close() {
		clientNameLabel.getScene().getWindow().hide();
		App.setScene("/dashboard.fxml");
	}
}

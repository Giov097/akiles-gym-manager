package com.gero.dev.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;

import com.gero.dev.domain.Client;
import com.gero.dev.domain.Fee;
import com.gero.dev.model.ClientModel;
import com.gero.dev.model.FeeModel;
import com.gero.dev.persistence.HibernateConnection;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

public class Mapper {

	private static Converter<Long, SimpleLongProperty> longToProperty = c -> new SimpleLongProperty(c.getSource());
	private static Converter<Double, SimpleDoubleProperty> doubleToProperty = c -> new SimpleDoubleProperty(
			c.getSource());
	private static Converter<String, SimpleStringProperty> stringToProperty = c -> new SimpleStringProperty(
			c.getSource());
	private static Converter<Boolean, SimpleStringProperty> booleanToProperty = c -> new SimpleStringProperty(
			Boolean.TRUE.equals(c.getSource()) ? "Activa" : "Baja");
	private static Converter<LocalDateTime, SimpleStringProperty> localDatetimeToProperty = c -> new SimpleStringProperty(
			c.getSource().format(DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm")));

	private Mapper() {
	}

	public static ModelMapper clientModelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		TypeMap<Client, ClientModel> propertyMapper = modelMapper.createTypeMap(Client.class, ClientModel.class);

		propertyMapper.addMappings(mapper -> {
			mapper.using(longToProperty).map(Client::getDni, ClientModel::setDni);
			mapper.using(stringToProperty).map(Client::getFullName, ClientModel::setFullName);
			mapper.map(Client::getCreatedAt, ClientModel::setCreatedAt);
			mapper.using(booleanToProperty).map(Client::getEnabled, ClientModel::setEnabled);
			mapper.using(ctx -> filterLatest((Client) ctx.getSource())).map(client -> client, ClientModel::setLastFee);
		});
		modelMapper.getConfiguration().setSkipNullEnabled(true);
		return modelMapper;
	}

	public static ModelMapper feeModelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		TypeMap<Fee, FeeModel> propertyMapper = modelMapper.createTypeMap(Fee.class, FeeModel.class);
		propertyMapper.addMappings(mapper -> {
			mapper.using(doubleToProperty).map(Fee::getPaymentAmmount, FeeModel::setPaymentAmmount);
			mapper.using(localDatetimeToProperty).map(Fee::getPaymentDate, FeeModel::setPaymentDate);
			mapper.using(stringToProperty).map(Fee::getObservations, FeeModel::setObservations);
			mapper.using(ctx -> buildPeriod((Fee) ctx.getSource())).map(fee -> fee, FeeModel::setPeriod);
		});
		modelMapper.getConfiguration().setSkipNullEnabled(true);
		return modelMapper;
	}

	private static SimpleStringProperty filterLatest(Client client) {
		List<Fee> fees = HibernateConnection.getCurrentSession().createQuery("from fees f where f.client.dni = :dni", Fee.class)
				.setParameter("dni", client.getDni()).list();
		String latest = fees.stream().map(Fee::getPaymentDate)
				.reduce((d1, d2) -> d1.compareTo(d2) > 0 ? d1 : d2)
				.map(date -> date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm"))).orElse("-");
		return new SimpleStringProperty(latest);
	}

	private static SimpleStringProperty buildPeriod(Fee fee) {
		return new SimpleStringProperty(String.format("%d-%s%d", fee.getYear(),
				fee.getMonth().getValue() > 9 ? "" : "0", fee.getMonth().getValue()));
	}
}

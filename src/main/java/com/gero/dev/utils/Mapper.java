package com.gero.dev.utils;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;

import com.gero.dev.domain.Client;
import com.gero.dev.model.ClientModel;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

public class Mapper {

	public static ModelMapper clientModelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		TypeMap<Client, ClientModel> propertyMapper = modelMapper.createTypeMap(Client.class, ClientModel.class);
		Converter<Long, SimpleLongProperty> longToProperty = c -> new SimpleLongProperty(c.getSource());
		Converter<String, SimpleStringProperty> stringToProperty = c -> new SimpleStringProperty(c.getSource());
		propertyMapper.addMappings(mapper -> {
			mapper.using(longToProperty).map(Client::getDni, ClientModel::setDni);
			mapper.using(stringToProperty).map(Client::getFullName, ClientModel::setFullName);
		});
		modelMapper.getConfiguration().setSkipNullEnabled(true);
		return modelMapper;
	}
}

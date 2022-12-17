package com.gero.dev.application.data;

import com.gero.dev.model.ClientModel;

import lombok.Getter;
import lombok.Setter;

public class SelectedData {

	private SelectedData() {
	}

	@Getter
	@Setter
	private static ClientModel client;

}

package com.gero.dev.utils;

import java.text.Normalizer;
import java.text.Normalizer.Form;

public class StringUtils {

	private StringUtils() {
	}

	public static String removeDiacriticalMarks(String string) {
		return Normalizer.normalize(string, Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
	}
}

package com.brainmentors.los.validations;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PancardValidation {
	private static final String PAN_REGEX = "[A-Za-z]{5}[0-9]{4}[A-Za-z]{1}";

	private static Pattern pattern;
	private Matcher matcher;

	public PancardValidation() {
		pattern = Pattern.compile(PAN_REGEX, Pattern.CASE_INSENSITIVE);
	}
	
	public boolean validatePan(String pan) {
		matcher = pattern.matcher(pan);
		return matcher.matches();
	}
}

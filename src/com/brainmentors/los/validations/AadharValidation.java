package com.brainmentors.los.validations;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AadharValidation {
	private static final String AADHAR_REGEX = "\\d{12}";

	private static Pattern pattern;
	private Matcher matcher;

	public AadharValidation() {
		pattern = Pattern.compile(AADHAR_REGEX, Pattern.CASE_INSENSITIVE);
	}
	
	public boolean validateAadhar(String aadhar) {
		matcher = pattern.matcher(aadhar);
		return matcher.matches();
	}
}

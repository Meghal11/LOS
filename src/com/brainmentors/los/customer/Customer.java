package com.brainmentors.los.customer;

import java.io.Serializable;

public class Customer implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private int stage;
	private PersonalInformation personal; //Has-A Relationship
	private Address address; //Has-A Relationship
	private double income;
	private double liability;
	private double assets;
	public double getAssets() {
		return assets;
	}
	public void setAssets(double assets) {
		this.assets = assets;
	}
	private LoanDetails loandetails;
	private String remarks;
	
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public LoanDetails getLoandetails() {
		return loandetails;
	}
	public void setLoandetails(LoanDetails loandetails) {
		this.loandetails = loandetails;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public PersonalInformation getPersonal() {
		return personal;
	}
	public void setPersonal(PersonalInformation personal) {
		this.personal = personal;
	}
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address2) {
		this.address = address2;
	}
	public double getIncome() {
		return income;
	}
	public void setIncome(double income) {
		this.income = income;
	}
	public double getLiability() {
		return liability;
	}
	public void setLiability(double liability) {
		this.liability = liability;
	}
	public int getStage() {
		return stage;
	}
	public void setStage(int stage) {
		this.stage = stage;
	}	
}

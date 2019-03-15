package com.brainmentors.los.operations;

import java.util.ArrayList;

import com.brainmentors.los.customer.Address;
import com.brainmentors.los.customer.Customer;
import com.brainmentors.los.customer.PersonalInformation;

public class DB {
	public static ArrayList<Customer> getNegativeCustomers(){
		ArrayList<Customer> negativeCustomers = new ArrayList<>();
		Customer customer=new Customer();
		
		customer.setId(1010);
		PersonalInformation pd = new PersonalInformation();
		pd.setFirstName("Tim");
		pd.setLastName("Jackson");
		pd.setPhone("2222");
		pd.setPancard("AWR3456");
		pd.setAadhar("111111111111");
		pd.setEmail("tim@gmail.com");
		Address add = new Address();
		add.setCity("Delhi");
		add.setFullAddress("123, ABC Street, Delhi-110001");
		customer.setPersonal(pd);
		customer.setAddress(add);
		negativeCustomers.add(customer);
		
		customer=new Customer(); //Reinitializing customer
		customer.setId(1011);
		pd = new PersonalInformation();
		pd.setFirstName("Tim");
		pd.setLastName("Dahl");
		pd.setPhone("2233");
		pd.setPancard("BW2000");
		pd.setAadhar("222222222222");
		pd.setEmail("tim@gmail.com");
		add = new Address();
		add.setCity("Delhi");
		add.setFullAddress("23, XYZ Street, Delhi-110010");
		customer.setPersonal(pd);
		customer.setAddress(add);
		negativeCustomers.add(customer);

		return negativeCustomers;
	}
	
	public static ArrayList<String> getDefaulterStates(){
		ArrayList<String> defaulterStates = new ArrayList<>();
		
		defaulterStates.add("jashpur");
		defaulterStates.add("korba");
		defaulterStates.add("dhamtari");
		defaulterStates.add("bastar");
		defaulterStates.add("sambhal");
		defaulterStates.add("tanda");
		defaulterStates.add("shamli");
		defaulterStates.add("lalitpur");
		
		return defaulterStates;		
	}
}

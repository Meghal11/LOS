package com.brainmentors.los.operations;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import com.brainmentors.los.customer.Customer;

public class DatabaseFile{
	public static ArrayList<Customer> list = new ArrayList<Customer>();
	public static int Size = list.size();
	public void addToFile() {
		File outFile =  new File("File.txt");	
		try {
			FileOutputStream fs = new FileOutputStream(outFile);
			ObjectOutputStream os = new ObjectOutputStream(fs);
			os.writeObject(list); 
			os.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public int readFromFile() {
		File readFile =  new File("File.txt");
		try {
			FileInputStream fis = new FileInputStream(readFile);
			ObjectInputStream ois = new ObjectInputStream(fis);
			list  = (ArrayList<Customer>) ois.readObject();
			Size = list.size();
			ois.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return Size;
	}
	
	public Customer searchFromFile(int appno) {
		Customer c =new Customer();
		Boolean present= false;
		for(Customer customer:list) {
			if(customer.getId()==appno) {
				c = customer;
				present = true;
			}
		}
		if(!present) {
			System.out.println("Invalid Application No.");
		}
		return c;
	}
	
	public void createList(Customer customer) {
		boolean present = false;
		for(Customer c:list) {
			if(c.getId()==customer.getId()) {
				present = true;
			}
		}
		if(!present) {
			list.add(customer);
		}
		addToFile();
	}
	
	public void replaceList(Customer customer) {
		for(Customer c:list) {
			if(c.getId()==customer.getId()) {
				list.set(customer.getId(),customer);
			}
		}
		addToFile();
	}
	
}

package com.brainmentors.los.start;

import com.brainmentors.los.customer.Customer;
import com.brainmentors.los.operations.DatabaseFile;
import com.brainmentors.los.operations.Process;
import com.brainmentors.los.utils.Utility;

public class Application {

	public static void main(String[] args) {
		Process process = new Process();
		DatabaseFile db = new DatabaseFile();
		int size, count;
		Customer customer = new Customer();
		while(true) {
			size = db.readFromFile();
			count = size+1;
			System.out.println();
			System.out.println("Welcome to BB-Bank Loan System.");
			System.out.println("If you are an existing customer, please enter your application number." );
			System.out.println("New customers press 0.");
			System.out.println("To exist, press -1.");
			int applicationno = Utility.scanner.nextInt();
			if(applicationno == -1) {
				System.out.println("Thanks for using..");
				System.exit(0);
			}
			if(applicationno == 0) {
				//New Customer
				process.sourcing(count);
			}
			
			else {
				//Existing Customer
				customer = db.searchFromFile(applicationno);
				process.checkStage(customer);
			}
		}
	}
	
}

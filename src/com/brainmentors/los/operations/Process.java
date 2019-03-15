package com.brainmentors.los.operations;

import static com.brainmentors.los.utils.Utility.scanner;
import java.util.ArrayList;

import com.brainmentors.los.customer.Address;
import com.brainmentors.los.customer.Customer;
import com.brainmentors.los.customer.LoanDetails;
import com.brainmentors.los.customer.PersonalInformation;
import com.brainmentors.los.utils.CommonConstants;
import com.brainmentors.los.utils.LoanConstants;
import com.brainmentors.los.utils.StageConstants;
import com.brainmentors.los.utils.Utility;
import com.brainmentors.los.validations.AadharValidation;
import com.brainmentors.los.validations.EmailValidation;
import com.brainmentors.los.validations.PancardValidation;

public class Process implements StageConstants,CommonConstants{
	private ArrayList<Customer> customers = new ArrayList<>();
	
	public void checkStage(Customer customer) {
		if(customer.getStage()!=0) {
			System.out.println("You are on Stage:"+ customer.getStage());
			Utility.getStageName(customer.getStage());
			moveToNextStage(customer);
		}
	}
	
	public void moveToNextStage(Customer customer) {
		int nscore = 0;
		DatabaseFile df = new DatabaseFile();
		while(true) {
			if(customer.getStage()==SOURCING) {
				System.out.println("Want to move to next Stage(Y/N):");
				char choice = scanner.next().toUpperCase().charAt(0);
				System.out.println();
				if(choice==YES) {
					qde(customer);
				}
				else {
					df.createList(customer);
					return;
				}
			}
			else
			if(customer.getStage()==QDE) {
				System.out.println("Want to move to next Stage(Y/N):");
				char choice = scanner.next().toUpperCase().charAt(0);
				System.out.println();
				if(choice==YES) {
					nscore = dedupe(customer);
				}
				else {
					df.replaceList(customer);
					return;
				}
			}
			else
			if(customer.getStage()==DEDUPE) {
				scoring(customer, nscore);
			}
			else
			if(customer.getStage()==SCORING) {
				approval(customer);
			}
			else
			if(customer.getStage()==REJECT) {
				System.out.println(customer.getRemarks());
				df.replaceList(customer);
				System.exit(0);
			}
			else
			if(customer.getStage()==ACCEPT) {
				System.out.println(customer.getRemarks());
				df.replaceList(customer);
				System.exit(0);
			}
		}
	}
	
	private int isNegative(Customer customer, Customer negative) {
		int percentageMatch=0;
		if(customer.getPersonal().getPhone().equalsIgnoreCase(negative.getPersonal().getPhone())) {
			percentageMatch += 10;
		}
		if(customer.getPersonal().getEmail().equalsIgnoreCase(negative.getPersonal().getEmail())) {
			percentageMatch += 20;
		}
		if(customer.getPersonal().getAadhar().equalsIgnoreCase(negative.getPersonal().getAadhar())) {
			percentageMatch += 20;
		}
		if(customer.getPersonal().getPancard().equalsIgnoreCase(negative.getPersonal().getPancard())) {
			percentageMatch += 20;
		}
		if(customer.getAddress().getFullAddress().equalsIgnoreCase(negative.getAddress().getFullAddress())){
			percentageMatch += 20;
		}
		for(String city: DB.getDefaulterStates()) {
			if(customer.getAddress().getCity().equalsIgnoreCase(city)) {
				percentageMatch += 10;
			}
		}
		return percentageMatch;
	}   
	
	private void showEMI(Customer customer, double approveAmount) {
		System.out.println("EMI is: ");
		if(customer.getLoandetails().getType().equalsIgnoreCase(LoanConstants.HOME_LOAN)) {
			customer.getLoandetails().setRoi(LoanConstants.HOME_LOAN_ROI);
		}
		if(customer.getLoandetails().getType().equalsIgnoreCase(LoanConstants.AUTO_LOAN)) {
			customer.getLoandetails().setRoi(LoanConstants.AUTO_LOAN_ROI);
		}
		if(customer.getLoandetails().getType().equalsIgnoreCase(LoanConstants.PERSONAL_LOAN)) {
			customer.getLoandetails().setRoi(LoanConstants.PERSONAL_LOAN_ROI);
		}
		double perMonthPrinciple = approveAmount / customer.getLoandetails().getDuration();
		double interest = perMonthPrinciple * customer.getLoandetails().getRoi();
		double totalEMI = perMonthPrinciple + interest;
		if(totalEMI == customer.getIncome() || totalEMI > customer.getIncome()) {
			customer.setStage(REJECT);
			customer.setRemarks("EMI amount is greater than the monthly income of candidate.");
		}
		else if(totalEMI >= (0.5 * customer.getIncome())) {
			customer.setStage(REJECT);
			customer.setRemarks("EMI amount is more than 50% of the monthly income of candidate.");
		}
		else {
			customer.setStage(ACCEPT);	
			customer.setRemarks("Your total EMI: " + totalEMI);
		}
	}
	
	public void sourcing(int applicationno) {
		Customer customer = new Customer();
		customer.setId(applicationno);
		customer.setStage(SOURCING);
		System.out.println("Enter the First Name:");
		String firstName = scanner.next();
		System.out.println("Enter the Last Name:");
		String lastName = scanner.next();
		System.out.println("Enter the PanCard No.:");
		String panCard = scanner.next();
		PancardValidation panValidator = new PancardValidation();
		boolean valid = panValidator.validatePan(panCard);
		while(!valid) {
			System.out.println("Invalid PAN Card No. Please enter it again:");
			panCard = scanner.next();
			valid = panValidator.validatePan(panCard);
		}
		System.out.println("Enter the Loan Type (HL,AL,PL):");
		String type = scanner.next();
		boolean valid2 = type.equalsIgnoreCase("HL")|| type.equalsIgnoreCase("AL")||  type.equalsIgnoreCase("PL");
		while(!valid2) {
			System.out.println("Invalid Loan Type. Please enter it again:");
			type = scanner.next();
			valid2 =  type.equalsIgnoreCase("HL")|| type.equalsIgnoreCase("AL")||  type.equalsIgnoreCase("PL");
		}
		System.out.println("Enter the Amount of loan:");
		double amount = scanner.nextDouble();
		System.out.println("Enter the Duration of loan (in months):");
		int duration = scanner.nextInt();
		PersonalInformation pd = new PersonalInformation();
		pd.setFirstName(firstName);
		pd.setLastName(lastName);
		pd.setPancard(panCard);
		customer.setPersonal(pd);
		LoanDetails loandetails = new LoanDetails();
		loandetails.setType(type);
		loandetails.setAmount(amount);
		loandetails.setDuration(duration);
		customer.setLoandetails(loandetails);
		customers.add(customer);
		System.out.println("Sourcing done..");
		System.out.println("Your Application No. is "+customer.getId());
		System.out.println();
		moveToNextStage(customer);
	}
	
	public void qde(Customer customer) {
		customer.setStage(QDE);
		System.out.println("Application No.: " + customer.getId());
		System.out.println("Name: "+ customer.getPersonal().getFirstName()+" "+customer.getPersonal().getLastName());
		System.out.println("You applied for "+customer.getLoandetails().getType());
		System.out.println("Duration of loan: "+ customer.getLoandetails().getDuration());
		System.out.println("Amount of loan: "+ customer.getLoandetails().getAmount());
		System.out.println("Enter the Aadhar Card no.:");
		String aadhar = scanner.next();
		AadharValidation panValidator = new AadharValidation();
		boolean valid = panValidator.validateAadhar(aadhar);
		while(!valid) {
			System.out.println("Invalid Aadhar Card No. Please enter it again:");
			aadhar = scanner.next();
			valid = panValidator.validateAadhar(aadhar);
		}
		System.out.println("Enter the Age:");
		int age = scanner.nextInt();
		if(age<=15 || age>= 60) {
			customer.setStage(REJECT);
			customer.setRemarks("Age out of valid range for approval of loan.");
		}
		System.out.println("Enter your State:");
		String state = scanner.next();
		System.out.println("Enter your City:");
		String city = scanner.next();
		System.out.println("Enter your Country:");
		String country = scanner.next();
		System.out.println("Enter your Pincode:");
		String pincode = scanner.next();
		boolean valid3 = pincode.length()==6;
		while(!valid3) {
			System.out.println("Invalid Pincode. Please enter it again:");
			pincode = scanner.next();
			valid3 = pincode.length()==10;
		}
		System.out.println("Enter your Complete Postal Address:");
		String address = scanner.next();
		System.out.println("Enter the Income (per Month):");
		double income = scanner.nextDouble();
		System.out.println("Enter the Liability:");
		double liability = scanner.nextDouble();
		System.out.println("Enter the Assets:");
		double assets = scanner.nextDouble();
		System.out.println("Enter the Phone no.:");
		String phone = scanner.next();
		boolean valid2 = phone.length()==10;
		while(!valid2) {
			System.out.println("Invalid Phone No. Please enter it again:");
			phone = scanner.next();
			valid2 = phone.length()==10;
		}
		System.out.println("Enter the Email:");
		String email = scanner.next();
		EmailValidation emailValidator = new EmailValidation();
		boolean valid1 = emailValidator.validateEmail(email);
		while(!valid1) {
			System.out.println("Email is of invalid pattern. Please enter it again:");
			email = scanner.next();
			valid = emailValidator.validateEmail(email);
		}
		customer.getPersonal().setAge(age);
		customer.getPersonal().setAadhar(aadhar);
		customer.getPersonal().setPhone(phone);
		customer.getPersonal().setEmail(email);
		Address add = new Address();
		add.setState(state);
		add.setCity(city);
		add.setCountry(country);
		add.setPincode(pincode);
		add.setFullAddress(address);
		customer.setAddress(add);
		customer.setIncome(income);
		customer.setLiability(liability);
		customer.setAssets(assets);
		System.out.println();
	}
	
	public int dedupe(Customer customer) {
		System.out.println("Aadhar Card No.: " + customer.getPersonal().getAadhar());
		System.out.println("Email: "+ customer.getPersonal().getEmail());
		System.out.println("Phone: "+customer.getPersonal().getPhone());
		System.out.println("Income: "+ customer.getIncome());
		System.out.println("Assets: "+ customer.getAssets());
		System.out.println("Liability: "+ customer.getLiability());
		customer.setStage(DEDUPE);
		boolean isNegativeFound = false;
		int negativeScore = 0;
		for(Customer negativeCustomer: DB.getNegativeCustomers()) {
			negativeScore= isNegative(customer, negativeCustomer);
			if(negativeScore>0) {
				System.out.println("************* Bank Manager Screen *************");
				System.out.println("Customer Record found in Dedupe and Score is "+ negativeScore);
				isNegativeFound = true;
				break;
			}
		}
		if(isNegativeFound) {
			System.out.println("Do you want to proceed this loan for Application No. "+customer.getId()+" (Y/N)");
			char choice = scanner.next().toUpperCase().charAt(0);
			System.out.println("***************************************");
			if(choice==NO) {
				customer.setRemarks("Loan is Rejected, due to high score in Dedupe");
				customer.setStage(REJECT);
				return 0;
			}
		}
		return negativeScore;
	}
	
	public void scoring(Customer customer, int nscore) {
		customer.setStage(SCORING);
		int score=0;
		double totalIncome = customer.getIncome()-customer.getLiability()+ customer.getAssets();
		if(customer.getPersonal().getAge()<25 || customer.getPersonal().getAge()>40) {
			score += 50;
		}
		if(totalIncome<200000) {
			score += 50;
		}
		score = score + nscore;
		customer.getLoandetails().setScore(score);
	}
	
	public void approval(Customer customer) {
		customer.setStage(APPROVAL);
		int score = customer.getLoandetails().getScore();
		System.out.println("ID: "+ customer.getId());
		System.out.println("Name: "+ customer.getPersonal().getFirstName()+" "+ customer.getPersonal().getLastName());
		System.out.println("Loan Type: "+ customer.getLoandetails().getType());
		System.out.println("Loan Amount: "+ customer.getLoandetails().getAmount());
		System.out.println("Loan Duration: "+ customer.getLoandetails().getDuration());
		double amt = customer.getLoandetails().getAmount();
		double approveAmount = amt - amt * score/200;
		System.out.println("Loan approved for Rs."+ approveAmount);
		System.out.println("Do you want to continue this loan(Y/N):");
		char choice = scanner.next().toUpperCase().charAt(0);
		if(choice==NO) {
			customer.setStage(REJECT);
			customer.setRemarks("Customer denied the approved amount: Rs."+ approveAmount);
			return;
		}
		else {
			showEMI(customer, approveAmount);
		}
	}
}

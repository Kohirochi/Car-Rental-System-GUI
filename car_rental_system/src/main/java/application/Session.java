package application;

import javafx.event.ActionEvent;

public class Session {
	public static boolean status = false;
	public static Customer customer;
	public static Admin admin;
	public static boolean checkedOverdue = false;
	public static String showCustomerBooking = "upcoming";
	public static boolean makePayment = false;
	
	public static void setLogin(String userType, User user) { 
		Session.status = true;
		if (userType.equals("customer")) {
			Session.customer = new Customer(user);
		} else if (userType.equals("admin")){
			Session.admin = new Admin(user);
		}
	}
	
	public static boolean setLogout(Log log) {
		Session.status = false;
		Session.customer = new Customer();
		Session.admin = new Admin();
		if (Session.status == false) {
			return true;
		} else {
			return false;
		}
		
	}
	
	public static boolean checkIfLogin(ActionEvent event, boolean loginStatus) { 
		if(!loginStatus) {
			AlertDialog.showErrorMessage(event, "Please login before booking");
			return false;
		}
		return true;
	}
}

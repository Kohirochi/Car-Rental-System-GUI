package application;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.LocalTime;

import javafx.collections.ObservableList;
import javafx.scene.control.Label;

public class Validation {
	
	// Display error message on label
	public static void displayError(Label errorField, String errorMessage) {
		errorField.setText(errorMessage);
		errorField.setVisible(true);
	}
	
	// Check credentials during login
	public static boolean validateLogin(User user, String loginEmail, String loginPassword, Label emailError, Label passwordError) throws FileNotFoundException {
		boolean blankPassword = false;
		
		// If password is blank
		if (loginPassword.isBlank()) {
			displayError(passwordError, "Please enter password");
			blankPassword = true;
		} else {
			passwordError.setVisible(false);
		}
		
		if (!validateLoginEmail(loginEmail, emailError) || blankPassword) {
			return false;
		}
		
		// When admin entered email and password, extract data from file and validate
		ObservableList<User> users = User.getUsersCredentials();
		
		for (int i = 0; i < users.size(); i++) {
			if (loginEmail.equals(users.get(i).email)) {
				if (loginPassword.equals(users.get(i).password)) {
					// Set user attributes after validation					
					user.setAttributes(users.get(i));
					return true;
				} else {
					displayError(passwordError, "Incorrect password. Please try again");
					return false;
				}
			}
		}
		return true;
	}
	
	// Validate email format and empty only
	public static boolean validateEmail(String email, Label emailError) throws FileNotFoundException {
		if (email.isBlank()) {
			displayError(emailError, "Please enter email");
			return false;
		}
		/** 
		The following restrictions are imposed in the email address' local part by using this regex:
			It allows numeric values from 0 to 9.
			Both uppercase and lowercase letters from a to z are allowed.
			Allowed are underscore “_”, hyphen “-“, and dot “.”
			Dot isn't allowed at the start and end of the local part.
			Consecutive dots aren't allowed.
			For the local part, a maximum of 64 characters are allowed.
		Restrictions for the domain part in this regular expression include:
			It allows numeric values from 0 to 9.
			We allow both uppercase and lowercase letters from a to z.
			Hyphen “-” and dot “.” aren't allowed at the start and end of the domain part.
			No consecutive dots.
		**/
		else if (!email.matches("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@" + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$")) {
			displayError(emailError, "Invalid format");
			return false;
		}
		emailError.setVisible(false);
		return true;
	}
	
	public static boolean validateRegistrationEmail(String email, Label emailError) throws FileNotFoundException {
		if (!validateEmail(email, emailError)) {
			return false;
		} else if (checkIfEmailExists(email, emailError)) {
			displayError(emailError, "Email already exists. Please try again");
			return false;
		}
		emailError.setVisible(false);
		return true;
	}
	
	public static boolean validateUpdateEmail(String ID, String email, Label emailError) throws FileNotFoundException {
		if (!validateEmail(email, emailError)) {
			return false;
		} else if (checkIfEmailExists(ID, email, emailError)) {
			displayError(emailError, "Email already exists. Please try again");
			return false;
		}
		emailError.setVisible(false);
		return true;
	}
	
	public static boolean validateLoginEmail(String email, Label emailError) throws FileNotFoundException {
		if (!validateEmail(email, emailError)) {
			return false;
		} else if (!checkIfEmailExists(email, emailError)) {
			displayError(emailError, "Email is not registered");
			return false;
		}
		emailError.setVisible(false);
		return true;
	}
	
	public static boolean validateUsername(String username, Label usernameError) {
		if (username.isBlank()) {
			displayError(usernameError, "Please enter username");
			return false;
		} else if (!username.matches("^[a-zA-z]\\w{4,19}")) {
			if (!username.matches("^[a-zA-Z].+")) {
				displayError(usernameError, "Username must start with a letter");
				return false;
			} else if (username.length() < 5 || username.length() > 20) {
				displayError(usernameError, "Username must be 5-20 characters long");
				return false;
			} else {
				displayError(usernameError, "Special characters are not allowed except underscore");
				return false;
			}
		}
		usernameError.setVisible(false);
		return true;
	}
	
	public static boolean validatePassword(String password, Label passwordError) {
		if (password.isBlank()) {
			displayError(passwordError, "Please enter password");
			return false;
		} else if (password.length() < 8 || password.length() > 20) {
			displayError(passwordError, "Password must be 8-20 characters long");
			return false;
		}
		passwordError.setVisible(false);
		return true;
	}
	
	public static boolean validateOldPassword(String oldPassword, String enteredPassword, Label passwordError) {
		if (enteredPassword.isBlank()) {
			displayError(passwordError, "Please enter");
			return false;
		} else if (!enteredPassword.equals(oldPassword)) {
			displayError(passwordError, "Incorrect password");
			return false;
		}
		passwordError.setVisible(false);
		return true;
	}
	
	public static boolean validateNewPassword(String oldPassword, String enteredPassword, Label passwordError) {
		if (enteredPassword.isBlank()) {
			displayError(passwordError, "Please enter");
			return false;
		} else if (enteredPassword.length() < 8 || enteredPassword.length() > 20) {
			displayError(passwordError, "Password must be 8-20 characters long");
			return false;
		} else if (enteredPassword.equals(oldPassword)) {
			displayError(passwordError, "New password cannot be the same as old password");
			return false;
		}
		passwordError.setVisible(false);
		return true;
	}
	
	public static boolean validateConfirmPassword(String password, String confirmPassword, Label confirmPasswordError) {
		if (!password.equals(confirmPassword)) {
			displayError(confirmPasswordError, "Confirm password must match your password");
			return false;
		}
		confirmPasswordError.setVisible(false);
		return true;
	}
	
	public static boolean validateVerificationEmail(String email, Label emailError) throws FileNotFoundException {
//		if (email.isBlank()) {
//			displayError(emailError, "Please enter email");
//			return false;
		if (!validateEmail(email, emailError)) {
			return false;
		} else if (!checkIfEmailExists(email, emailError)) {
			displayError(emailError, "Email is not registered");
			return false;
		}
		emailError.setVisible(false);
		return true;
	}

	public static boolean checkIfEmailExists(String email, Label emailError) throws FileNotFoundException {
		ObservableList<User> users = User.getUsersCredentials();
		
		for (User user : users) {
			if (!user.getStatus().equals("Trash") && email.toLowerCase().equals(user.getEmail().toLowerCase())) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean checkIfEmailExists(String ID, String email, Label emailError) throws FileNotFoundException {
		ObservableList<User> users = User.getUsersCredentials();
		
		for (User user : users) {
			if (!ID.equals(String.valueOf(user.getID()))) {
				if (!user.getStatus().equals("Trash") && email.toLowerCase().equals(user.getEmail().toLowerCase())) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static boolean validateNewPlateNumber(String plateNumber, Label plateNumberError) throws FileNotFoundException {
		if (!validateBlank(plateNumber, plateNumberError)) {
			return false;
		} else if (checkIfPlateNumberExists(plateNumber, plateNumberError)) {
			displayError(plateNumberError, "Plate number already exist");
			return false;
		}
		plateNumberError.setVisible(false);
		return true;
	}
	
	public static boolean validateUpdatePlateNumber(String carID, String plateNumber, Label plateNumberError) throws FileNotFoundException {
		if (!validateBlank(plateNumber, plateNumberError)) {
			return false;
		} else if (checkIfPlateNumberExists(carID, plateNumber, plateNumberError)) {
			displayError(plateNumberError, "Plate number already exist");
			return false;
		}
		plateNumberError.setVisible(false);
		return true;
	}
	
	public static boolean checkIfPlateNumberExists(String plateNumber, Label plateNumberError) throws FileNotFoundException {
		ObservableList<Car> cars = Car.getCarsDetails();
		
		for (Car car : cars) {
			if (!car.getStatus().equals("Trash") && plateNumber.toLowerCase().equals(car.getPlateNumber().toLowerCase())) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean checkIfPlateNumberExists(String carID, String plateNumber, Label plateNumberError) throws FileNotFoundException {
		ObservableList<Car> cars = Car.getCarsDetails();
		
		for (Car car : cars) {
			if (!carID.equals(String.valueOf(car.getID()))) {
				if (!car.getStatus().equals("Trash") && plateNumber.toLowerCase().equals(car.getPlateNumber().toLowerCase())) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static boolean validateVerificationCode(String userCode, String validCode, Label verificationCodeError) {
		if (userCode.isBlank()) {
			displayError(verificationCodeError, "Please enter verification code");
			return false;
		} else if (!userCode.equals(validCode)) {
			displayError(verificationCodeError, "Incorrect verification code");
			return false;
		}
		verificationCodeError.setVisible(false);
		return true;
	}
	
	public static boolean validateICPassportNumber(String number, Label numberError) {
		if (number.isBlank()) {
			displayError(numberError, "Please enter");
			return false;
		} else if (!number.matches("^(?!^0+$)[a-zA-Z0-9]{6,9}|\\d{6}\\-\\d{2}\\-\\d{4}")) {
			displayError(numberError, "Invalid format");
			return false;
		}
		numberError.setVisible(false);
		return true;
	}
	
	public static boolean validateContact(String contact, Label contactError) {
		if (contact.isBlank()) {
			displayError(contactError, "Please enter");
			return false;
		} else if (!contact.matches("^(\\+\\d{1,3}( )?)?((\\(\\d{1,3}\\))|\\d{1,3})[- .]?\\d{3,4}[- .]?\\d{4}$")) {
			displayError(contactError, "Invalid format");
			return false;
		}
		contactError.setVisible(false);
		return true;
	}
	
	public static boolean validateBlank(String value, Label labelError) {
		if (value.isBlank()) {
			displayError(labelError, "Please enter");
			return false;
		}
		labelError.setVisible(false);
		return true;
	}
	
	public static boolean validateBlank(String value, Label labelError, String errorMessage) {
		if (value.isBlank()) {
			displayError(labelError, errorMessage);
			return false;
		}
		labelError.setVisible(false);
		return true;
	}
	
	public static boolean validateDate(LocalDate value, Label labelError) {
		if (value == null) {
			displayError(labelError, "Please select");
			return false;
		}
		labelError.setVisible(false);
		return true;
	}
	
	public static boolean validateDouble(String value, Label labelError) {
		if (value.isBlank()) {
			displayError(labelError, "Please enter");
			return false;
		}else if(!value.matches("\\d+(\\.\\d+)?")) {
			displayError(labelError, "Invalid format");
			return false;
		}
		labelError.setVisible(false);
		return true;
	}
	
	public static boolean validateComboBox(Object value, Label labelError) { 
		if (value == null) {
			displayError(labelError, "Please select");
			return false;
		}
		labelError.setVisible(false);
		return true;
	}
	
	public static boolean validatePickUpDate(Car car, LocalDate pickUpDate, Label labelError) {
		if (car == null) {
			displayError(labelError, "Please select");
			return false;
		}
		
		ObservableList<Booking> bookingList;
		bookingList = Booking.getBookingDetails("upcoming");
		
		if (pickUpDate == null) {
			displayError(labelError, "Please select");
			return false;
		}
		
		for (Booking booking : bookingList) { 
			if (booking.getCar().getID() == car.getID() && !(pickUpDate.isBefore(booking.getPickUpDate()) || pickUpDate.isAfter(booking.getDropOffDate())) ) {
				displayError(labelError, "The car is unavailable at that time");
				return false;
			}
		}
		
		labelError.setVisible(false);
		return true; 
	}
	
	public static boolean validateDropOffDate(Car car, LocalDate pickUpDate, LocalDate dropOffDate, Label labelError) {
		if (car == null) {
			displayError(labelError, "Please select");
			return false;
		}
		
		ObservableList<Booking> bookingList;
		bookingList = Booking.getBookingDetails("upcoming");
		
		if (dropOffDate == null) {
			displayError(labelError, "Please select");
			return false;
		} else if (pickUpDate == null) {
			displayError(labelError, "Please select pick-up date first");
			return false;
		} else if (dropOffDate.isBefore(pickUpDate)) {
			displayError(labelError, "Drop-off date must be after pick-up date");
			return false;
		}
		
		for (Booking booking : bookingList) { 
			if (booking.getCar().getID() == car.getID() && !(dropOffDate.isBefore(booking.getPickUpDate()) || dropOffDate.isAfter(booking.getDropOffDate())) ) {
				displayError(labelError, "The car is unavailable at that time");
				return false;
			}
		}
		
		labelError.setVisible(false);
		return true; 
	}
	
	public static boolean validateDropOffTime(LocalDate pickUpDate, LocalDate dropOffDate, LocalTime pickUpTime, LocalTime dropOffTime, Label labelError) {
		if (dropOffTime == null) {
			displayError(labelError, "Please select");
			return false;
		} else if (pickUpDate == null || dropOffDate == null) { 
			displayError(labelError, "Please select pick-up date and drop-off date first");
			return false;
		} else if (pickUpTime == null) {
			displayError(labelError, "Please select pick-up time first");
			return false;
		} else if (pickUpDate.isEqual(dropOffDate) && (dropOffTime.isBefore(pickUpTime) || dropOffTime.equals((pickUpTime)))) {
			displayError(labelError, "Drop-off time must be after pick-up time");
			return false;
		}
		labelError.setVisible(false);
		return true; 
	}
	
	public static boolean validateCVV(String CVV, Label CVVError) {
		if (!validateBlank(CVV, CVVError)) {
			return false;
		} else if (!CVV.matches("^[0-9]{3}$")) { 
			displayError(CVVError, "Invalid format");
			return false;
		}
		CVVError.setVisible(false);
		return true; 
	}
	
	public static boolean validateCardNumber(String cardNumber, Label cardNumberError) {
		if (!validateBlank(cardNumber, cardNumberError)) {
			return false;
		} else if (!cardNumber.matches("^(5[1-5][0-9]{14}|2(22[1-9][0-9]{12}|2[3-9][0-9]{13}|[3-6][0-9]{14}|7[0-1][0-9]{13}|720[0-9]{12}))$") && !cardNumber.matches("^4[0-9]{12}(?:[0-9]{3})?$")) { 
			displayError(cardNumberError, "Invalid format");
			return false;
		}
		cardNumberError.setVisible(false);
		return true; 
	}
	
	public static boolean validateExpirationDate(String dateMonth, String dateYear, Label dateError) {
		String date = String.join("/", dateMonth, dateYear);
		
		if (!validateBlank(dateMonth, dateError)) {
			return false;
		} else if (!validateBlank(dateYear, dateError) ) {
			return false;
		} else if (!date.matches("^((0[1-9])|(1[0-2]))[\\/\\.\\-]*((2[2-9])|(3[0-9]))$") ) { 
			displayError(dateError, "Card has expired");
			return false;
		}
		dateError.setVisible(false);
		return true; 
	}
	
	public static boolean validateCustomerICPassportNumber(String number, Label numberError) {
		if (!number.isBlank() && !number.matches("^(?!^0+$)[a-zA-Z0-9]{6,9}|\\d{6}\\-\\d{2}\\-\\d{4}")) {
			displayError(numberError, "Invalid format");
			return false;
		}
		numberError.setVisible(false);
		return true;
	}
	
	public static boolean validateCustomerContact(String contact, Label contactError) {
		if (!contact.isBlank() && !contact.matches("^(\\+\\d{1,3}( )?)?((\\(\\d{1,3}\\))|\\d{1,3})[- .]?\\d{3,4}[- .]?\\d{4}$")) {
			displayError(contactError, "Invalid format");
			return false;
		}
		contactError.setVisible(false);
		return true;
	}
}
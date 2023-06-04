package application;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CustomerProfileController implements Initializable{
	private Parent root;
	private Stage stage;
	private Scene scene;
	public boolean loginStatus = Session.status;
	public Customer customer = Session.customer;
	
	@FXML
	private GridPane carGrid;
	@FXML
	private ImageView logo;
	@FXML
    private HBox customerHBox;
	@FXML
	private Button btnLogin, btnEdit, btnUpdate, btnChangePw;
	@FXML
	private Hyperlink btnRegister;
	@FXML
	private Label customerName, fullNameError, emailError, ICPassportNumberError, contactError, oldPasswordError, newPasswordError, confirmPasswordError;
	@FXML
	private VBox dropdownMenu;
	@FXML
	private ComboBox<String> txtCountry, txtGender;
	@FXML
	private TextField txtFullName, txtEmail, txtICPassportNumber, txtContact, txtOldPassword, txtNewPassword, txtConfirmPassword;
	@FXML
	private DatePicker txtBirthDate;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Tooltip.install(logo, new Tooltip("Home"));
		
		// Check if user login to display different header
		configureHeader();
		
		// Display all cars
		loadCustomerDetails();
		
		// Render options for combo box and display prompt text
		configureComboBox();
	}
	
	public void configureHeader() { 
		if (loginStatus) {
			// Show customer name and hide login and register button
			String firstName = customer.getName().split(" ")[0];
			customerName.setText(firstName);
			customerHBox.setVisible(true);
			btnLogin.setVisible(false);
			btnRegister.setVisible(false);
		} else {
			// Show login and register button
			customerHBox.setVisible(false);
			btnLogin.setVisible(true);
			btnRegister.setVisible(true);
		}
	}
	// Render options for combo box and display prompt text	
	public void configureComboBox() {
		Functions.configureGenderComboBox(txtGender);
		Functions.configureCountryComboBox(txtCountry);
	}
	
	public void showDropdown() { 
		if (dropdownMenu.isVisible()) {
			dropdownMenu.setVisible(false);
		} else {
			dropdownMenu.setVisible(true);
		}
	}
	
	// Log out from admin account
	public void logout(ActionEvent event) throws IOException{
		Log log = new Log();
		log.setUserID(customer.getID());
		log.setTargetID(customer.getID());
		log.setType("Auth");
		log.setAction("Logout");
		log.setDateCreated(LocalDateTime.now());
		if (Session.setLogout(log)) {
			log.setStatus("Success");
			log.addNewLog(log);
			// Redirect to guest home page
			Functions.switchScene(event, root, stage, scene, "Home.fxml");
			
			// Alert message
			AlertDialog.showInfoMessage(event, "Logout successful");
		} else {
			log.setStatus("Failed");
			log.addNewLog(log);
			// Alert message
			AlertDialog.showErrorMessage(event, "Logout unsuccessful");
		}
	}
	
	public void switchToHomeMouseEvent(MouseEvent event) throws IOException {
		Functions.switchScene(event, root, stage, scene, "Home.fxml");
	}
	
	public void switchToHomeActionEvent(ActionEvent event) throws IOException {
		Functions.switchScene(event, root, stage, scene, "Home.fxml");
	}
	
	public void switchToRegistration(ActionEvent event) throws IOException {
		Functions.switchScene(event, root, stage, scene, "Registration.fxml");
	}
	
	public void switchToLogin(ActionEvent event) throws IOException {
		Functions.switchScene(event, root, stage, scene, "Login.fxml");
	}
	
	public void switchToMyBookings(ActionEvent event) throws IOException {
		Functions.switchScene(event, root, stage, scene, "MyBookings.fxml");
	}
	
	public void switchToCustomerProfile(ActionEvent event) throws IOException {
		Functions.switchScene(event, root, stage, scene, "CustomerProfile.fxml");
	}
	
	public void loadCustomerDetails() {
		Customer customer;
		try {
			customer = Customer.getSpecificCustomerDetails(this.customer.getID());
			
			txtFullName.setText(customer.getName());
			txtEmail.setText(customer.getEmail());
			if (!customer.getGender().equals("-")) {
				txtGender.setValue(customer.getGender());
			} else {
				txtGender.setValue(null);
			}
			if (!customer.getICPassportNumber().equals("-")) {
				txtICPassportNumber.setText(customer.getICPassportNumber());
			} else {
				txtICPassportNumber.setText("");
			}
			if (!customer.getBirthDate().equals("-")) {
				txtBirthDate.setValue(LocalDate.parse(customer.getBirthDate()));
			} else {
				txtBirthDate.setValue(null);
			}
			if (!customer.getCountry().equals("-")) {
				txtCountry.setValue(customer.getCountry());
			} else {
				txtCountry.setValue(null);
			}
			if (!customer.getContact().equals("-")) {
				txtContact.setText(customer.getContact());
			} else {
				txtContact.setText("");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("File not found");;
		}
	}
	
	public void edit() {
		// let user edit
		txtFullName.setDisable(false);
		txtEmail.setDisable(false);
		txtICPassportNumber.setDisable(false);
		txtContact.setDisable(false);
		txtBirthDate.setDisable(false);
		txtGender.setDisable(false);
		txtCountry.setDisable(false);
		
		// show update button
		btnEdit.setVisible(false);
		btnUpdate.setVisible(true);
		
	}
	
	public void update(ActionEvent event) throws IOException {
		int ID = this.customer.getID();
		String fullName = txtFullName.getText().trim();
		String email = txtEmail.getText().trim();
		String gender = txtGender.getValue();
		String ICPassportNumber = txtICPassportNumber.getText().trim();
		LocalDate birthDate = txtBirthDate.getValue();
		String contact = txtContact.getText().trim();
		String country = txtCountry.getValue();
		if(Validation.validateBlank(fullName, fullNameError, "Please enter full name") &
		   Validation.validateUpdateEmail(Integer.toString(ID), email, emailError) &
		   Validation.validateCustomerICPassportNumber(ICPassportNumber, ICPassportNumberError) &
		   Validation.validateCustomerContact(contact, contactError)
				) {
			//Replace with dash if no value
			gender = gender == null ? "-" : gender;
			ICPassportNumber = ICPassportNumber.isBlank() ? "-" : ICPassportNumber;
			String birthDateString = birthDate == null ? "-" : birthDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			contact = contact.isBlank() ? "-" : contact;
			country = country == null ? "-" : country;
			
			Log log = new Log();
			customer = Customer.getSpecificCustomerDetails(ID);
			// Update customer credentials into file
			if (Customer.updateCustomerCredentials(Integer.toString(ID), fullName, email, customer.getPassword(), gender, ICPassportNumber, birthDateString, contact, country, customer.getStatus(), log)) {
				AlertDialog.showInfoMessage(event, "Update successful");
				// Set log details and add to log text file
				log.setUserID(ID);
				log.setType("Profile");
				log.setTargetID(ID);
				log.setAction("Update");
				
				log.addNewLog(log);

				// Update customer details
				Session.customer.setName(fullName);
				Session.customer.setEmail(email);
				Session.customer.setGender(gender);
				Session.customer.setICPassportNumber(ICPassportNumber);
				Session.customer.setBirthDate(birthDateString);
				Session.customer.setContact(contact);
				Session.customer.setCountry(country);

				// let user edit
				txtFullName.setDisable(true);
				txtEmail.setDisable(true);
				txtICPassportNumber.setDisable(true);
				txtContact.setDisable(true);
				txtBirthDate.setDisable(true);
				txtGender.setDisable(true);
				txtCountry.setDisable(true);
				
				// show update button
				btnEdit.setVisible(true);
				btnUpdate.setVisible(false);
			} else {
				AlertDialog.showErrorMessage(event, "Update unsuccessful");
			}
		}
	}
	
	public void changePw(ActionEvent event) throws IOException {
		String oldPassword = txtOldPassword.getText();
		String newPassword = txtNewPassword.getText();
		String confirmPassword = txtConfirmPassword.getText();
		
		customer = Customer.getSpecificCustomerDetails(this.customer.getID());
		if(Validation.validateOldPassword(customer.getPassword(), oldPassword, oldPasswordError) &
		   Validation.validateNewPassword(customer.getPassword(), newPassword, newPasswordError) &
		   Validation.validateConfirmPassword(newPassword, confirmPassword, confirmPasswordError)
			) {
			Log log = new Log();
			// Update customer credentials into file
			if (Customer.updateCustomerCredentials(Integer.toString(customer.getID()), customer.getName(), customer.getEmail(), newPassword, customer.getGender(), customer.getICPassportNumber(), customer.getBirthDate(), customer.getContact(), customer.getCountry(), customer.getStatus(), log)) {
				AlertDialog.showInfoMessage(event, "Password changed successful");
				// Set log details and add to log text file
				log.setUserID(customer.getID());
				log.setType("Password");
				log.setTargetID(customer.getID());
				log.setAction("Change");
				
				log.addNewLog(log);
				
				// clear passworld fields
				txtOldPassword.setText("");
				txtNewPassword.setText("");
				txtConfirmPassword.setText("");
			} else {
				AlertDialog.showErrorMessage(event, "Password changed unsuccessful");
			}
		}
	}
	
}

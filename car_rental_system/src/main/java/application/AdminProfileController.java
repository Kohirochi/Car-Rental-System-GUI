package application;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class AdminProfileController implements Initializable{
	
	private Parent root;
	private Stage stage;
	private Scene scene;
	public Admin admin = Session.admin;
	
	@FXML
	private Button btnDashboard, btnAdmin, btnCar, btnCustomerRegistration, btnCustomer, btnInvoice, btnBookingRequest, btnUpcomingBooking, btnBookingHistory, btnReport, btnLog, btnProfile, btnEdit, btnUpdate, btnChangePw;
	@FXML
	private ImageView iconDashboard, iconAdmin, iconCar, iconCustomerRegistration, iconCustomer, iconInvoice, iconBookingRequest, iconUpcomingBooking, iconBookingHistory, iconReport, iconLog, iconProfile, carImage;
	@FXML
	private TextField txtFullName, txtEmail, txtContact, txtOldPassword, txtNewPassword, txtConfirmPassword;
	@FXML
	private ComboBox<String> txtGender;
	@FXML
	private Label customerName, fullNameError, emailError, ICPassportNumberError, contactError, oldPasswordError, newPasswordError, confirmPasswordError;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// When hover on button change color
		styleNavBar();
		
		// Render options for combo box and display prompt text	
		Functions.configureGenderComboBox(txtGender);
		
		loadAdminDetails();
	}
	
	public void styleNavBar() {
		setHoverEffect(btnDashboard, iconDashboard, "nav_dashboard_white.png", "nav_dashboard_black.png");
		setHoverEffect(btnCar, iconCar, "nav_car_white.png", "nav_car_black.png");
		setHoverEffect(btnAdmin, iconAdmin, "nav_admin_white.png", "nav_admin_black.png");
		setHoverEffect(btnCustomer, iconCustomer, "nav_customer_white.png", "nav_customer_black.png");
		setHoverEffect(btnInvoice, iconInvoice, "nav_invoice_white.png", "nav_invoice_black.png");
		setHoverEffect(btnBookingRequest, iconBookingRequest, "nav_booking_request_white.png", "nav_booking_request_black.png");
		setHoverEffect(btnUpcomingBooking, iconUpcomingBooking, "nav_upcoming_booking_white.png", "nav_upcoming_booking_black.png");
		setHoverEffect(btnBookingHistory, iconBookingHistory, "nav_booking_history_white.png", "nav_booking_history_black.png");
		setHoverEffect(btnReport, iconReport, "nav_report_white.png", "nav_report_black.png");
		setHoverEffect(btnLog, iconLog, "nav_log_white.png", "nav_log_black.png");
		setHoverEffect(btnProfile, iconProfile, "nav_profile_white.png", "nav_profile_black.png");
	}
	
	// Set hover effect on navbar button
	public void setHoverEffect(Button button, ImageView imageView, String oldIconFileName, String newIconFileName) {
		Image oldIcon = new Image("file:./src/main/resources/image/" + oldIconFileName);
		Image newIcon = new Image("file:./src/main/resources/image/" + newIconFileName);
		
		button.setOnMouseEntered(mouseEvent -> {
			imageView.setImage(newIcon);
			button.setStyle("-fx-background-color: #FFFFFF; ");
			button.setStyle("-fx-text-fill: black; ");
		});
		
		button.setOnMouseExited(mouseEvent -> {
			imageView.setImage(oldIcon);
			button.setStyle("-fx-text-fill: white; ");
			button.setStyle("-fx-background-color: #000000;");
		});
	}
	
	// Log out from admin account
	public void logOut(ActionEvent event) throws IOException{
		Log log = new Log();
		log.setUserID(Session.admin.getID());
		log.setTargetID(Session.admin.getID());
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
	
	// Show specific pane when user use the side navigation bar
	public void switchPane(ActionEvent event) throws IOException {
		if (event.getSource() == btnDashboard) {
			Functions.switchScene(event, root, stage, scene, "AdminDashboard.fxml");
		} else if (event.getSource() == btnCar) {
			Functions.switchScene(event, root, stage, scene, "AdminCar.fxml");
		} else if (event.getSource() == btnAdmin) {
			Functions.switchScene(event, root, stage, scene, "AdminAdmin.fxml");
		} else if (event.getSource() == btnCustomer) {
			Functions.switchScene(event, root, stage, scene, "AdminCustomer.fxml");
		} else if (event.getSource() == btnInvoice) {
			Functions.switchScene(event, root, stage, scene, "AdminInvoice.fxml");
		} else if (event.getSource() == btnBookingRequest) {
			Functions.switchScene(event, root, stage, scene, "AdminBookingRequest.fxml");
		} else if (event.getSource() == btnUpcomingBooking) {
			Functions.switchScene(event, root, stage, scene, "AdminUpcomingBooking.fxml");
		} else if (event.getSource() == btnBookingHistory) {
			Functions.switchScene(event, root, stage, scene, "AdminBookingHistory.fxml");
		} else if (event.getSource() == btnReport) {
			Functions.switchScene(event, root, stage, scene, "AdminReport.fxml");
		} else if (event.getSource() == btnLog) {
			Functions.switchScene(event, root, stage, scene, "AdminLog.fxml");
		} else if (event.getSource() == btnProfile) {
			Functions.switchScene(event, root, stage, scene, "AdminProfile.fxml");
		}
	}
	
	public void loadAdminDetails() {
		Admin admin;
		try {
			admin = Admin.getSpecificAdminDetails(this.admin.getID());
			
			txtFullName.setText(admin.getName());
			txtEmail.setText(admin.getEmail());
			if (!admin.getGender().equals("-")) {
				txtGender.setValue(admin.getGender());
			} else {
				txtGender.setValue(null);
			}
			if (!admin.getContact().equals("-")) {
				txtContact.setText(admin.getContact());
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
		txtContact.setDisable(false);
		txtGender.setDisable(false);
		
		// show update button
		btnEdit.setVisible(false);
		btnUpdate.setVisible(true);
		
	}
	
	public void update(ActionEvent event) throws IOException {
		int ID = this.admin.getID();
		String fullName = txtFullName.getText().trim();
		String email = txtEmail.getText().trim();
		String gender = txtGender.getValue();
		String contact = txtContact.getText().trim();
		if(Validation.validateBlank(fullName, fullNameError, "Please enter full name") &
		   Validation.validateUpdateEmail(Integer.toString(ID), email, emailError) &
		   Validation.validateCustomerContact(contact, contactError)
				) {
			//Replace with dash if no value
			gender = gender == null ? "-" : gender;
			contact = contact.isBlank() ? "-" : contact;
			
			Log log = new Log();
			admin = Admin.getSpecificAdminDetails(ID);
			// Update customer credentials into file
			if (Admin.updateAdminCredentials(Integer.toString(ID), fullName, email, admin.getPassword(), gender, contact, admin.getStatus(), log)) {
				AlertDialog.showInfoMessage(event, "Update successful");
				// Set log details and add to log text file
				log.setUserID(ID);
				log.setType("Profile");
				log.setTargetID(ID);
				log.setAction("Update");
				
				log.addNewLog(log);
				
				// let user edit
				txtFullName.setDisable(true);
				txtEmail.setDisable(true);
				txtContact.setDisable(true);
				txtGender.setDisable(true);
				
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
		
		admin = Admin.getSpecificAdminDetails(this.admin.getID());
		if(Validation.validateOldPassword(admin.getPassword(), oldPassword, oldPasswordError) &
		   Validation.validateNewPassword(admin.getPassword(), newPassword, newPasswordError) &
		   Validation.validateConfirmPassword(newPassword, confirmPassword, confirmPasswordError)
			) {
			Log log = new Log();
			// Update customer credentials into file
			if (Admin.updateAdminCredentials(Integer.toString(admin.getID()), admin.getName(), admin.getEmail(), newPassword, admin.getGender(), admin.getContact(), admin.getStatus(), log)) {
				AlertDialog.showInfoMessage(event, "Password changed successful");
				// Set log details and add to log text file
				log.setUserID(admin.getID());
				log.setType("Password");
				log.setTargetID(admin.getID());
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

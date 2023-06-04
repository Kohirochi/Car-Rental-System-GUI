package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RegistrationController implements Initializable {

	private Parent root;
	private Stage stage;
	private Scene scene;
	
	@FXML
	private TextField txtEmail, txtName;
	@FXML
	private PasswordField txtPassword, txtConfirmPassword;
	@FXML
	private Button btnSignUp;
	@FXML
	private Label emailError, nameError, passwordError, confirmPasswordError;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {		
		// Show placeholder
		Functions.showPlaceholderOnFocus(txtEmail);
		Functions.showPlaceholderOnFocus(txtName);
		Functions.showPlaceholderOnFocus(txtPassword);	
		Functions.showPlaceholderOnFocus(txtConfirmPassword);
		
		// Limit user input
		configureTextField();
	}
	
	// Limit user input using regex	
	public void configureTextField() {
		Functions.limitTextFieldInput(txtName, "[^a-zA-Z\\s]");  // Alphabets and space only
	}
	
	public void switchToLogin(ActionEvent event) throws IOException {
		Functions.switchScene(event, root, stage, scene, "Login.fxml");
	}
	
	public void switchToHome(ActionEvent event) throws IOException {
		Functions.switchScene(event, root, stage, scene, "Home.fxml");
	}
	
	public void register(ActionEvent event) throws IOException {
		String registrationEmail = txtEmail.getText().trim();
		String registrationName = txtName.getText().trim();
		String registrationPassword = txtPassword.getText();
		String registrationConfirmPassword = txtConfirmPassword.getText();
		
		Log log = new Log();
		log.setType("Auth");
		log.setAction("Register");
		
		if (Validation.validateRegistrationEmail(registrationEmail, emailError) &
			Validation.validateBlank(registrationName, nameError, "Please enter full name") &
			Validation.validatePassword(registrationPassword, passwordError) &
			Validation.validateConfirmPassword(registrationPassword, registrationConfirmPassword, confirmPasswordError)) {
				if (User.register(registrationName, registrationEmail, registrationPassword, "Active", log)) {
					AlertDialog.showInfoMessage(event, "Registration successful");
					Functions.switchScene(event, root, stage, scene, "Login.fxml");
					
					// Add to log text file					
					log.addNewLog(log);
				} else {
					AlertDialog.showErrorMessage(event, "Registration unsuccessful");
					log.addNewLog(log);
				}
		}
	}
	
}

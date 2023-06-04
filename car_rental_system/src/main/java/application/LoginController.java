package application;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController implements Initializable{
	
	private Parent root;
	private Stage stage;
	private Scene scene;	
	
	@FXML
	private TextField txtEmail;
	@FXML
	private PasswordField txtPassword;
	@FXML
	private Label emailError, passwordError;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {		
		// Show placeholder
		Functions.showPlaceholderOnFocus(txtEmail);
		Functions.showPlaceholderOnFocus(txtPassword);
	}
	
	public void switchToRegistration(ActionEvent event) throws IOException {
		Functions.switchScene(event, root, stage, scene, "Registration.fxml");
	}
	
	public void switchToForgotPassword(ActionEvent event) throws IOException {
		Functions.switchScene(event, root, stage, scene, "ForgotPassword.fxml");
	}
	
	public void switchToHome(ActionEvent event) throws IOException {
		Functions.switchScene(event, root, stage, scene, "Home.fxml");
	}
	
	// Login
	public void login(ActionEvent event) throws IOException {
		String loginEmail = txtEmail.getText().trim();
		String loginPassword = txtPassword.getText();
		
		User user = new User();
		Log log = new Log();
		log.setType("Auth");
		log.setAction("Login");
		
		if (Validation.validateLogin(user, loginEmail, loginPassword, emailError, passwordError)) {
			if (user.permission.equals("Admin")) {
			    Session.setLogin("admin", user);
			    Functions.switchScene(event, root, stage, scene, "AdminDashboard.fxml");		
			} 
			else if (user.permission.equals("Customer")) {
				Session.setLogin("customer",user);
			    Functions.switchScene(event, root, stage, scene, "Home.fxml");
			}
			// Set log details and add to log text file
			log.setDateCreated(LocalDateTime.now());
			log.setUserID(user.getID());
			log.setTargetID(user.getID());
			log.setStatus("Success");
			
			log.addNewLog(log);
		} else {
			log.setDateCreated(LocalDateTime.now());
			log.setTargetID(User.extractID(loginEmail));
			log.setStatus("Failed");
			
			log.addNewLog(log);
		}
	}

}

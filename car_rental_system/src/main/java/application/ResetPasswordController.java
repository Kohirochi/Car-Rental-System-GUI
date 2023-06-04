package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ResetPasswordController implements Initializable {

	private Parent root;
	private Stage stage;
	private Scene scene;
	
	private String validCode;
	private String verificationEmail;

	@FXML
	private TextField txtVerificationCode;
	@FXML
	private PasswordField txtPassword, txtConfirmPassword;
	@FXML
	private Label verificationCodeError, passwordError, confirmPasswordError;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// Show placeholder
		Functions.showPlaceholderOnFocus(txtVerificationCode);	
		Functions.showPlaceholderOnFocus(txtPassword);
		Functions.showPlaceholderOnFocus(txtConfirmPassword);
	}
	
	public void switchToForgotPassword(ActionEvent event) throws IOException {
		Functions.switchScene(event, root, stage, scene, "ForgotPassword.fxml");
	}
	
	public void sendEmail(ActionEvent event) {
		validCode = SMTP.sendMail(verificationEmail);
	}
	
	public void getVerificationEmail(String email) {
		verificationEmail = email;
		System.out.println(verificationEmail);
	}
	
	public void getVerificationCode(String code) {
		validCode = code;
		System.out.println(validCode);
	}
	
	public void setPassword(ActionEvent event) throws IOException {
		String verificationCode = txtVerificationCode.getText().trim();
		String newPassword = txtPassword.getText();
		String confirmNewPassword = txtConfirmPassword.getText();
		
		if (Validation.validateVerificationCode(verificationCode, validCode, verificationCodeError) & 
			Validation.validatePassword(newPassword, passwordError) &
			Validation.validateConfirmPassword(newPassword, confirmNewPassword, confirmPasswordError)) {
				// Read text file and check for the specific user				
				ObservableList<User> users = User.getUsersCredentials();
				for (User user : users) {
					if (verificationEmail.equals(user.getEmail())) {
						// Update user credentials into text file
						Log log = new Log();
						if (User.resetPassword(String.valueOf(user.getID()), user.getName(), user.getEmail(), newPassword, user.getPermission(), user.getMoreUserDetails(), user.getStatus(), log)) {
							AlertDialog.showInfoMessage(event, "Reset password successful");
							Functions.switchScene(event, root, stage, scene, "Login.fxml");
							
							// Set log details and add to log text file
							log.setUserID(user.getID());
							log.setType("Password");
							log.setTargetID(user.getID());
							log.setAction("Reset");
							
							log.addNewLog(log);
						} else {
							AlertDialog.showInfoMessage(event, "Reset password unsuccessful");
						}
						break;
					}
		        }
				
		}
		
	}
}

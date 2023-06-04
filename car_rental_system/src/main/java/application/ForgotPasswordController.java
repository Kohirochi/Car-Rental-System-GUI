package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ForgotPasswordController implements Initializable {
		
	private Parent root;
	private Stage stage;
	private Scene scene;
	
	@FXML
	private TextField txtEmail;
	@FXML
	private Label emailError;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// Show placeholder
		Functions.showPlaceholderOnFocus(txtEmail);	
	}
	
	public void switchToLogin(ActionEvent event) throws IOException {
		Functions.switchScene(event, root, stage, scene, "Login.fxml");
	}
	
	public void sendEmail(ActionEvent event) throws IOException {
		String email = txtEmail.getText().trim();
		
		if (Validation.validateVerificationEmail(email, emailError)) {
			String code = SMTP.sendMail(email);
			
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("ResetPassword.fxml"));
		    root = loader.load();
		    scene = new Scene(root);
		    
		    ResetPasswordController controller = loader.getController();
		    controller.getVerificationEmail(email);
		    controller.getVerificationCode(code);
		    
		    stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
			stage.setScene(scene);
			stage.show();
		}
	}	
	
}

package application;

import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;


public class AlertDialog {

	// Popup error message
	public static void showErrorMessage(ActionEvent event, String contentText) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle("");
		alert.setHeaderText(null);
		alert.setContentText(contentText);
		
		// Center the alert dialog in the application
		alert.initOwner(((Node)event.getSource()).getScene().getWindow());
		alert.showAndWait();
	}
	
	// Popup information message
	public static void showInfoMessage(ActionEvent event, String contentText) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("");
		alert.setHeaderText(null);
		alert.setContentText(contentText);
		
		// Center the alert dialog in the application
		alert.initOwner(((Node)event.getSource()).getScene().getWindow());
		alert.showAndWait();
	}
	
	// Popup information message
	public static void showInfoMessage(MouseEvent event, String contentText) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("");
		alert.setHeaderText(null);
		alert.setContentText(contentText);
		
		// Center the alert dialog in the application
		alert.initOwner(((Node)event.getSource()).getScene().getWindow());
		alert.showAndWait();
	}
	
	// Popup confirmation message
	public static boolean showConfirmationMessage(ActionEvent event, String contentText) {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("");
		alert.setHeaderText(null);
		alert.setContentText(contentText);
		
		// Center the alert dialog in the application
		alert.initOwner(((Node)event.getSource()).getScene().getWindow());
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			return true;
		}
		return false;
	}
}

package application;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class BookingThumbController {
	Parent root;
	Stage stage;
	Scene scene;
	private int bookingID;
	
    @FXML
    private Label carName, carPlateNumber, bookingDuration, labelStatus;
    @FXML
    private Button btnPay;
    @FXML
    private ImageView carImage;
    
    public void setData(BookingThumb bookingThumb) {
    	// Set title
		Tooltip.install(labelStatus, new Tooltip("Status"));
		
    	this.bookingID = bookingThumb.getID();
    	String status = bookingThumb.getBookingStatus();
    	Image image =  new Image(bookingThumb.getCarImagePath());
    	
    	carImage.setImage(image);
    	carName.setText(bookingThumb.getName());
    	carPlateNumber.setText(bookingThumb.getPlateNumber());
    	bookingDuration.setText(bookingThumb.getDuration());
    	labelStatus.setText(status);
    	
    	// Hide pay button for certain status
    	if (bookingThumb.getPaymentStatus().equals("Paid") || status.equals("Rejected") || status.equals("Cancelled")) {
    		btnPay.setVisible(false);
    	}
    	
    	// Change status background color
    	if (status.equals("Pending")) {
    		labelStatus.setStyle("-fx-background-color: #54C5FC; -fx-background-radius: 20px;");
    	} else if (status.equals("Pending Payment")) {
    		labelStatus.setStyle("-fx-background-color: #F5C02F; -fx-background-radius: 20px;");
    	} else if (status.equals("Confirmed")) {
    		labelStatus.setStyle("-fx-background-color: #08FF2F; -fx-background-radius: 20px;");
    	} else if (status.equals("Rented Out")) {
    		labelStatus.setStyle("-fx-background-color: #EF6C00; -fx-background-radius: 20px;");
        } else if (status.equals("Overdue")) {
    		labelStatus.setStyle("-fx-background-color: #F04337; -fx-background-radius: 20px;");
        } else if (status.equals("Completed")) {
        	labelStatus.setStyle("-fx-background-color: #3A8E5C; -fx-background-radius: 20px;");
        } else if (status.equals("Rejected")) {
        	labelStatus.setStyle("-fx-background-color: #FF3122; -fx-background-radius: 20px;");
        } else if (status.equals("Cancelled")) {
        	labelStatus.setStyle("-fx-background-color: #251E31; -fx-background-radius: 20px;");
        }
    	
    }
    
    public void switchToBookingDetails(MouseEvent event) throws IOException {
    	FXMLLoader loader = new FXMLLoader();
	    loader.setLocation(getClass().getResource("MyBookingDetails.fxml"));
	    root = loader.load();
	    scene = new Scene(root);
	    
	    // Pass specific booking ID to my bookings details page
	    MyBookingDetailsController controller = loader.getController();
	    controller.loadBookingDetails(bookingID);
	    
	    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.setScene(scene);
		stage.show();
    }
    
    public void switchToMakePayment(ActionEvent event) throws IOException {
    	FXMLLoader loader = new FXMLLoader();
	    loader.setLocation(getClass().getResource("MakePayment.fxml"));
	    root = loader.load();
	    scene = new Scene(root);
	    
	    // Pass specific booking ID to make payment page
	    MakePaymentController controller = loader.getController();
	    controller.loadBookingDetails(bookingID);
	    
	    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.setScene(scene);
		stage.show();
    }
}

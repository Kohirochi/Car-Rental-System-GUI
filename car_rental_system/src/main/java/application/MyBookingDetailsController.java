package application;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MyBookingDetailsController implements Initializable {
	private Parent root;
	private Stage stage;
	private Scene scene;
	public boolean loginStatus = Session.status;
	public Customer customer = Session.customer;
	private Booking booking = new Booking();
	
	@FXML
	private ImageView logo;
	@FXML
    private HBox customerHBox;
	@FXML
	private Label customerName;
	@FXML
	private VBox dropdownMenu;
	@FXML
	private TextField txtID, txtPickUpDateTime, txtDropOffDateTime, txtPrice, txtPaymentStatus, txtBookingStatus, txtDateCreated, txtCarName, txtPlateNumber, txtLocation, txtCustomerName, txtEmail, txtGender, txtICPassportNumber, txtContact, txtCountry;
	@FXML
	private Button btnPay, btnCancel, btnExport;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Tooltip.install(logo, new Tooltip("Home"));
		
		// Check if user login to display different header
		configureHeader();
	}
	
	public void configureHeader() { 
		// Show customer name
		String firstName = customer.getName().split(" ")[0];
		customerName.setText(firstName);
		customerHBox.setVisible(true);
	}
	
	public void showDropdown() { 
		if (dropdownMenu.isVisible()) {
			dropdownMenu.setVisible(false);
		} else {
			dropdownMenu.setVisible(true);
		}
	}
	
	// Log out from customer account
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
	
	public void switchToMakePayment(ActionEvent event) throws IOException {
    	FXMLLoader loader = new FXMLLoader();
	    loader.setLocation(getClass().getResource("MakePayment.fxml"));
	    root = loader.load();
	    scene = new Scene(root);
	    
	    // Pass specific booking ID to booking detail page
	    MakePaymentController controller = loader.getController();
	    controller.loadBookingDetails(Integer.parseInt(txtID.getText()));
	    
	    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.setScene(scene);
		stage.show();
    }
	
	public void toCancel(ActionEvent event) throws IOException {
		if (AlertDialog.showConfirmationMessage(event, "Are you sure you want to cancel the booking?")) {
			// Create new log object
			Log log = new Log();
			if (Customer.cancelBooking(Integer.parseInt(txtID.getText()), log)) {
				AlertDialog.showInfoMessage(event, "Booking cancelled successfully.");
				
				// Set log details and add to log text file
				log.setUserID(customer.getID());
				log.setType("Booking");
				log.setAction("Cancel");
				
				log.addNewLog(log);

				
				// Redirect customer to MyBookings to show cancelled booking
				Session.showCustomerBooking = "rejected/cancelled";
				Functions.switchScene(event, root, stage, scene, "MyBookings.fxml");
			} else {
				AlertDialog.showErrorMessage(event, "Booking cancelled unsuccessful. Please try again.");
			}
		}
    }
	
	public void loadBookingDetails(int bookingID) throws FileNotFoundException {
		booking = Booking.getSpecificBookingDetails(bookingID);
		
		Car car = Car.getSpecificCarDetails(booking.getCar().getID());
		Customer customer = Customer.getSpecificCustomerDetails(booking.getCustomer().getID());
		
		String paymentStatus = booking.getPaymentStatus();
		String bookingStatus = booking.getBookingStatus();
		
		txtID.setText(Integer.toString(bookingID));
		txtPickUpDateTime.setText(String.join(" ", booking.getPickUpDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), "at", booking.getPickUpTime().format(DateTimeFormatter.ofPattern("HH:mm"))));
		txtDropOffDateTime.setText(String.join(" ", booking.getDropOffDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), "at", booking.getDropOffTime().format(DateTimeFormatter.ofPattern("HH:mm"))));
		txtPrice.setText(Double.toString(booking.getPrice()));
		txtPaymentStatus.setText(paymentStatus);
		txtBookingStatus.setText(bookingStatus);
		txtDateCreated.setText(booking.getDateCreatedString());
		txtCarName.setText(car.getName());
		txtPlateNumber.setText(car.getPlateNumber());
		txtLocation.setText(Car.location);
		txtCustomerName.setText(customer.getName());
		txtEmail.setText(customer.getEmail());
		txtGender.setText(booking.getGender());
		txtICPassportNumber.setText(booking.getICPassportNumber());
		txtContact.setText(booking.getContact());
		txtCountry.setText(booking.getCountry());
		
		if (paymentStatus.equals("Paid") || bookingStatus.equals("Rejected") || bookingStatus.equals("Cancelled")) {
    		btnPay.setVisible(false);
    	}
		if (bookingStatus.equals("Rejected") || bookingStatus.equals("Cancelled")|| bookingStatus.equals("Completed")) {
			btnCancel.setVisible(false);
    	}
		
		// Show export button only if payment is made
		if (paymentStatus.equals("Paid")) {
			btnExport.setVisible(true);
		} else {
			btnExport.setVisible(false);
		}
	}
	
	
	public void exportInvoice(ActionEvent event) throws IOException {
		// Get invoice details using bookingID
		Invoice invoice = Invoice.getSpecificInvoiceDetails(Integer.parseInt(txtID.getText()));

		// Creating the PDF
		PDDocument pdf = new PDDocument();
		
		// Creating a blank page
		PDPage page = new PDPage();
		
		// Adding the blank page to our PDF
		pdf.addPage(page);
		
		// Initializing the content stream
		PDPageContentStream cs = new PDPageContentStream(pdf, page);
	
		// Invoice title
		cs.beginText();
		cs.setFont(PDType1Font.TIMES_BOLD, 20);
		cs.newLineAtOffset(210, 750);
		cs.showText("Rentio Private Limited");
		cs.endText();
		
		// Invoice subtitle
		cs.beginText();
		cs.setFont(PDType1Font.TIMES_BOLD, 18);
	    cs.newLineAtOffset(270, 690);
	    cs.showText("Invoice");
	    cs.endText();
	    
	    // Writing invoice details
 		cs.beginText();
 		cs.setFont(PDType1Font.TIMES_ROMAN, 14);
 		cs.setLeading(20f);
 	    cs.newLineAtOffset(60, 610);
 	    cs.showText("Booking ID: ");
        cs.newLine();
 	    cs.showText("Invoice ID: ");
        cs.newLine();
        cs.showText("Payment Date: ");
        cs.endText();
 	    
 	    cs.beginText();
        cs.setFont(PDType1Font.TIMES_ROMAN, 14);
        cs.setLeading(20f);
        cs.newLineAtOffset(210, 610);
        cs.showText(Integer.toString(invoice.getBookingID()));
        cs.newLine();
        cs.showText(Integer.toString(invoice.getID()));
        cs.newLine();
        cs.showText(invoice.getPaymentDateString());
        cs.endText();
	    
	    // Writing customer details
 	    cs.beginText();
 	    cs.setFont(PDType1Font.TIMES_ROMAN, 14);
        cs.setLeading(20f);
        cs.newLineAtOffset(60, 520);
        cs.showText("Customer Name: ");
        cs.newLine();
        cs.showText("IC / Passport Number: ");
        cs.newLine();
        cs.showText("Email Address: ");
        cs.newLine();
        cs.showText("Contact Number: ");
        cs.newLine();
        cs.showText("Country: ");
        cs.endText();
       
        cs.beginText();
        cs.setFont(PDType1Font.TIMES_ROMAN, 14);
        cs.setLeading(20f);
        cs.newLineAtOffset(210, 520);
        cs.showText(txtCustomerName.getText());
        cs.newLine();
        cs.showText(txtICPassportNumber.getText());
        cs.newLine();
        cs.showText(txtEmail.getText());
        cs.newLine();
        cs.showText(txtContact.getText());
        cs.newLine();
        cs.showText(txtCountry.getText());
        cs.endText();
        
        // Writing booking details
        cs.beginText();
        cs.setFont(PDType1Font.TIMES_BOLD, 14);
        cs.newLineAtOffset(30, 370);
        cs.showText("Car Name");
        cs.endText();
        
        cs.beginText();
        cs.setFont(PDType1Font.TIMES_BOLD, 14);
        cs.newLineAtOffset(170, 370);
        cs.showText("Car Plate");
        cs.endText();
        
        cs.beginText();
        cs.setFont(PDType1Font.TIMES_BOLD, 14);
        cs.newLineAtOffset(260, 370);
        cs.showText("Pick-up Date");
        cs.endText();
        
        cs.beginText();
        cs.setFont(PDType1Font.TIMES_BOLD, 14);
        cs.newLineAtOffset(400, 370);
        cs.showText("Drop-off Date");
        cs.endText();
        
        cs.beginText();
        cs.setFont(PDType1Font.TIMES_BOLD, 14);
        cs.newLineAtOffset(530, 370);
        cs.showText("Price");
        cs.endText();
        
        cs.moveTo(30, 365);
        cs.lineTo(580, 365);
        cs.stroke();
        
        cs.beginText();
        cs.setFont(PDType1Font.TIMES_ROMAN, 12);
        cs.setLeading(20f);
        cs.newLineAtOffset(30, 350);
        cs.showText(txtCarName.getText());
        cs.newLine();
        cs.endText();
        
        cs.beginText();
        cs.setFont(PDType1Font.TIMES_ROMAN, 12);
        cs.setLeading(20f);
        cs.newLineAtOffset(170, 350);
        cs.showText(txtPlateNumber.getText());
        cs.newLine();
        cs.endText();
        
        cs.beginText();
        cs.setFont(PDType1Font.TIMES_ROMAN, 12);
        cs.setLeading(20f);
        cs.newLineAtOffset(260, 350);
        cs.showText(txtPickUpDateTime.getText());
        cs.newLine();
        cs.endText();
        
        cs.beginText();
        cs.setFont(PDType1Font.TIMES_ROMAN, 12);
        cs.setLeading(20f);
        cs.newLineAtOffset(400, 350);
        cs.showText(txtDropOffDateTime.getText());
        cs.newLine();
        cs.endText();
        
        cs.beginText();
        cs.setFont(PDType1Font.TIMES_ROMAN, 12);
        cs.setLeading(20f);
        cs.newLineAtOffset(530, 350);
        cs.showText(txtPrice.getText());
        cs.newLine();
        cs.endText();
        
        //Close the content stream
        cs.close();
        //Save the PDF
        pdf.save(System.getProperty("user.home") + "\\Downloads\\Invoice.pdf");
        AlertDialog.showInfoMessage(event, "Invoice exported successfully. Please search for the PDF file in your Downloads folder");
	}
}

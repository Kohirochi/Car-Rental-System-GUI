package application;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

public class MakePaymentController implements Initializable{
	private Parent root;
	private Stage stage;
	private Scene scene;
	private Booking booking = new Booking();
	
	public boolean loginStatus = Session.status;
	public Customer customer = Session.customer;
	
	
	@FXML
	private ImageView logo, mastercard, visa;
	@FXML
    private HBox customerHBox;
	@FXML
	private Button btnLogin, btnPayNow;
	@FXML
	private Hyperlink btnRegister;
	@FXML
	private Label customerName, labelBookingDetailsHeader, labelPersonalDetailsHeader, labelPaymentDetailsHeader, labelCarPrice, labelDuration, labelTotalPrice,  expirationDateError, cardNumberError, cardholderNameError, CVVError, genderError, ICPassportNumberError, countryError, contactError, birthDateError, pickUpDateError, dropOffDateError, pickUpTimeError, dropOffTimeError, itineraryCarName, itineraryLocation, itineraryPickUpDateTime, itineraryDropOffDateTime, itineraryCustomerName, itineraryCustomerContact, itineraryPlateNumber, txtTotalPriceLabel;
	@FXML
	private Circle bookingDetailsCircle, personalDetailsCircle, paymentDetailsCircle;
	@FXML
	private Line bookingDetailsLine, personalDetailsLine, paymentDetailsLine;
	@FXML
	private VBox dropdownMenu, bookingDetails, personalDetails, paymentDetails;
    @FXML
    private TextField txtCarName, txtLocation, txtPickUpDate, txtDropOffDate, txtPickUpTime, txtDropOffTime, txtCustomerName, txtEmail, txtPlateNumber, txtICPassportNumber, txtBirthDate, txtGender, txtCountry, txtContact, txtCardholderName, txtCardNumber1, txtCardNumber2, txtCardNumber3, txtCardNumber4, txtExpirationDateMonth, txtExpirationDateYear, txtCVV;
    @FXML
    private GridPane bookingDetailsHeader, personalDetailsHeader, paymentDetailsHeader;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Tooltip.install(logo, new Tooltip("Home"));
		Tooltip.install(mastercard, new Tooltip("Mastercard"));
		Tooltip.install(visa, new Tooltip("Visa"));
		
		// Check if user login to display different header
		configureHeader();
		
		// Limit text field input
		configureTextField();
		
		// Set text field on focus
		configureTextFieldFocus();
		
		
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
	
	// Click next button on booking details page
	public void toPersonalDetails(ActionEvent event) { 
			// Show personal details close booking details
			bookingDetails.setVisible(false);
			personalDetails.setVisible(true);
			
			// Change header colour
			setActiveColour(labelPersonalDetailsHeader, personalDetailsLine, personalDetailsCircle);
			setUnactiveColour(labelBookingDetailsHeader, bookingDetailsLine, bookingDetailsCircle);
			setUnactiveColour(labelPaymentDetailsHeader, paymentDetailsLine, paymentDetailsCircle);
	}
	
	// Limit user input using regex	
	public void configureTextField() {
		Functions.limitTextFieldInput(txtCardholderName, "[^a-zA-Z\\s]");  // Alphabets and space only
		Functions.limitTextFieldInput(txtCVV, "\\D");  // Numbers only
	}
	
	// Add listeners to auto switch text field
	public void configureTextFieldFocus() {
		txtCardNumber1.textProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue.length() == 4) {
				txtCardNumber2.requestFocus();
			}
		});
		
		txtCardNumber2.textProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue.length() == 4) {
				txtCardNumber3.requestFocus();
			}
		});
		
		txtCardNumber3.textProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue.length() == 4) {
				txtCardNumber4.requestFocus();
			}
		});
		
		txtCardNumber4.textProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue.length() == 4) {
				txtExpirationDateMonth.requestFocus();
			}
		});
		
		txtExpirationDateMonth.textProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue.length() == 2) {
				txtExpirationDateYear.requestFocus();
			}
		});
		
		txtExpirationDateYear.textProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue.length() == 2) {
				txtCVV.requestFocus();
			}
		});
	}
	
	// Click next button on personal details page
	public void toPaymentDetails(ActionEvent event) { 
			// Show payment details close personal details
			personalDetails.setVisible(false);
			paymentDetails.setVisible(true);
			txtCardholderName.requestFocus();
			
			// Change header colour
			setActiveColour(labelPaymentDetailsHeader, paymentDetailsLine, paymentDetailsCircle);
			setUnactiveColour(labelPersonalDetailsHeader, personalDetailsLine, personalDetailsCircle);
			setUnactiveColour(labelBookingDetailsHeader, bookingDetailsLine, bookingDetailsCircle);
	}
	
	// Click pay now button on payment details page
	public void toPay(ActionEvent event) throws IOException { 
		String cardholderName = txtCardholderName.getText().trim();
		String cardNumber = String.join("", txtCardNumber1.getText().trim(), txtCardNumber2.getText().trim(), txtCardNumber3.getText().trim(), txtCardNumber4.getText().trim());
		String expirationDateMonth = txtExpirationDateMonth.getText().trim();
		String expirationDateYear = txtExpirationDateYear.getText().trim();
		String CVV = txtCVV.getText().trim();
		String cardType = "Credit Card";
		
		if(Validation.validateBlank(cardholderName, cardholderNameError) &
		   Validation.validateCardNumber(cardNumber, cardNumberError) &
		   Validation.validateExpirationDate(expirationDateMonth, expirationDateYear, expirationDateError) &
		   Validation.validateCVV(CVV, CVVError)
		   ) {
			
			// Create new log object
			Log log = new Log();
			if (Booking.updateBookingWithPayment(booking.getID(), log)) {
				// Create invoice
				String invoiceDetails = String.join("|", Integer.toString(booking.getID()), LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")), cardType, cardholderName, cardNumber);
				Invoice.addNewInvoice(invoiceDetails);
			
				AlertDialog.showInfoMessage(event, "Payment made successfully.");
				
				// Set log details and add to log text file
				log.setUserID(customer.getID());
				log.setType("Booking");
				log.setAction("Pay");
				
				log.addNewLog(log);
				
				// Redirect customer to MyBookings
				Functions.switchScene(event, root, stage, scene, "MyBookings.fxml");
			} else {
				AlertDialog.showErrorMessage(event, "Payment made unsuccessful. Please try again.");
			}
		}
	}
	
	public void setActiveColour(Label label, Line line, Circle circle) { 
		circle.setStroke(Color.web("#00437a"));
		circle.setFill(Color.web("#00437a"));
		line.setStyle("-fx-stroke: linear-gradient(from 0.0% 0.0% to 52.2727% 54.9242%, #00437a 0.0%, #30b3f3 100.0%)");
		label.setStyle("-fx-font-weight: bold; -fx-text-fill: #000000;");
	}
	
	public void setUnactiveColour(Label label, Line line, Circle circle) { 
		circle.setStroke(Color.web("#9b9b9b"));
		circle.setFill(Color.web("#FFFFFF"));
		line.setStyle("-fx-stroke: #9b9b9b");
		label.setStyle("-fx-font-weight: 400; -fx-text-fill: #9b9b9b;");
	}
	
	public void backToBookingDetails(MouseEvent event) {
		bookingDetails.setVisible(true);
		personalDetails.setVisible(false);
		paymentDetails.setVisible(false);
		
		setActiveColour(labelBookingDetailsHeader, bookingDetailsLine, bookingDetailsCircle);
		setUnactiveColour(labelPersonalDetailsHeader, personalDetailsLine, personalDetailsCircle);
		setUnactiveColour(labelPaymentDetailsHeader, paymentDetailsLine, paymentDetailsCircle);
	}
	
	public void backToPersonalDetails(MouseEvent event) {
		bookingDetails.setVisible(false);
		personalDetails.setVisible(true);
		paymentDetails.setVisible(false);
		
		setActiveColour(labelPersonalDetailsHeader, personalDetailsLine, personalDetailsCircle);
		setUnactiveColour(labelBookingDetailsHeader, bookingDetailsLine, bookingDetailsCircle);
		setUnactiveColour(labelPaymentDetailsHeader, paymentDetailsLine, paymentDetailsCircle);
	}
	
	public void backToPaymentDetails(MouseEvent event) {
		bookingDetails.setVisible(false);
		personalDetails.setVisible(false);
		paymentDetails.setVisible(true);
		
		setUnactiveColour(labelPersonalDetailsHeader, personalDetailsLine, personalDetailsCircle);
		setUnactiveColour(labelBookingDetailsHeader, bookingDetailsLine, bookingDetailsCircle);
		setActiveColour(labelPaymentDetailsHeader, paymentDetailsLine, paymentDetailsCircle);
	}
	
	public void loadBookingDetails(int bookingID) throws FileNotFoundException {
		booking = Booking.getSpecificBookingDetails(bookingID);
		Car car = Car.getSpecificCarDetails(booking.getCar().getID());
		Customer customer = Customer.getSpecificCustomerDetails(booking.getCustomer().getID());
		
		// Set payment amount details
		int duration = booking.getDuration();
		double totalPrice = car.getPrice()*duration; 
		labelCarPrice.setText("RM " + car.getPrice()); 
		labelDuration.setText(Integer.toString(duration));
		labelTotalPrice.setText(Functions.limitDecimalPlaces(Double.toString(totalPrice), 2));
		txtTotalPriceLabel.setText("Total price for " + duration + " days :");
		
		// Set customer details
		txtCustomerName.setText(customer.getName());
		txtEmail.setText(customer.getEmail());
		txtGender.setText(booking.getGender());
		txtICPassportNumber.setText(booking.getICPassportNumber());
		txtBirthDate.setText(booking.getBirthDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
		txtContact.setText(booking.getContact());
		txtCountry.setText(booking.getCountry());
		
		// Set car details
		txtCarName.setText(car.getName());
		txtPlateNumber.setText(car.getPlateNumber());
		txtLocation.setText(Car.location);
		txtPickUpDate.setText(booking.getPickUpDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
		txtPickUpTime.setText(booking.getPickUpTime().format(DateTimeFormatter.ofPattern("HH:mm")));
		txtDropOffDate.setText(booking.getDropOffDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
		txtDropOffTime.setText(booking.getDropOffTime().format(DateTimeFormatter.ofPattern("HH:mm")));
		
		// Set itinerary
		itineraryCarName.setText(car.getName());
		itineraryLocation.setText(Car.location);
		itineraryPlateNumber.setText(car.getPlateNumber());
		itineraryPickUpDateTime.setText(String.join(" ", booking.getPickUpDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), "at", booking.getPickUpTime().format(DateTimeFormatter.ofPattern("HH:mm"))));
		itineraryDropOffDateTime.setText(String.join(" ", booking.getDropOffDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), "at", booking.getDropOffTime().format(DateTimeFormatter.ofPattern("HH:mm"))));
		itineraryCustomerName.setText(customer.getName());
		itineraryCustomerContact.setText(customer.getContact());
	}
}

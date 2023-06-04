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

public class BookingDetailsController implements Initializable {
	private Parent root;
	private Stage stage;
	private Scene scene;
	private String page = "bookingDetails";
	private Booking booking = new Booking();
	
	public boolean loginStatus = Session.status;
	public Customer customer = Session.customer;
	public int carID;
	public Car car;
	
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
    private TextField txtCarName, txtLocation, txtCustomerName, txtEmail, txtPlateNumber, txtICPassportNumber, txtContact, txtCardholderName, txtCardNumber1, txtCardNumber2, txtCardNumber3, txtCardNumber4, txtExpirationDateMonth, txtExpirationDateYear, txtCVV;
    @FXML
    private ComboBox<LocalTime> txtPickUpTime, txtDropOffTime;
    @FXML
    private ComboBox<String> txtGender, txtCountry;
    @FXML
	private DatePicker txtPickUpDate, txtDropOffDate, txtBirthDate;
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
		
		// Generate options
		configureComboBox();
		
		// Disable date options before today
		configureDatePicker();
		
	}
	
	public void configureComboBox() { 
		showTimeOptions();
		showGenderOptions();
		showCountryOptions();
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
	
	public void showDropdown() { 
		if (dropdownMenu.isVisible()) {
			dropdownMenu.setVisible(false);
		} else {
			dropdownMenu.setVisible(true);
		}
	}
	
	public void showGenderOptions() {
		String[] gender = {"Male", "Female"};
		ObservableList<String> genderList = FXCollections.observableArrayList(gender);
		txtGender.setItems(genderList);
	}
	
	public void showCountryOptions() {
		ObservableList<String> countryList = FXCollections.observableArrayList();
		String[] countryCodes = Locale.getISOCountries();
		for (String countryCode : countryCodes) {
			Locale obj = new Locale("", countryCode);
			countryList.add(obj.getDisplayCountry());
		}
		txtCountry.setItems(countryList);
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
	
	// Display car details
	public void loadCarDetails(int carID) throws FileNotFoundException {
		this.carID = carID;
		Car car = Car.getSpecificCarDetails(carID);
		this.car = car;
		
		txtCarName.setText(String.join(" ", Integer.toString(car.getYear()), car.getBrand(), car.getModel()));
		txtPlateNumber.setText(car.getPlateNumber());
		txtLocation.setText(Car.location);
	}
	
	// Set options for combo box
	public void showTimeOptions() {
		ObservableList<LocalTime> timeFrameList = FXCollections.observableArrayList(Functions.generateTimeFrameList());
		txtPickUpTime.setItems(timeFrameList);
		txtDropOffTime.setItems(timeFrameList);
	}
	
	// Click next button on booking details page
	public void toPersonalDetails(ActionEvent event) { 
		String carName = txtCarName.getText().trim();
		String location = txtLocation.getText().trim();
		String plateNumber = txtPlateNumber.getText().trim();
		LocalDate pickUpDate = txtPickUpDate.getValue();
		LocalTime pickUpTime = txtPickUpTime.getValue();
		LocalDate dropOffDate = txtDropOffDate.getValue();
		LocalTime dropoffTime = txtDropOffTime.getValue();
		
		if(Validation.validateComboBox(pickUpTime, pickUpTimeError) &
		   Validation.validateDropOffTime(pickUpDate, dropOffDate, pickUpTime, dropoffTime, dropOffTimeError) &
		   Validation.validatePickUpDate(car, pickUpDate, pickUpDateError) &
		   Validation.validateDropOffDate(car, pickUpDate, dropOffDate, dropOffDateError)
		   ) {
			// Save booking details
			booking.setCar(car);
			booking.setDropOffDate(dropOffDate);
			booking.setDropOffTime(dropoffTime);
			booking.setPickUpDate(pickUpDate);
			booking.setPickUpTime(pickUpTime);
			
			// Show personal details close booking details
			bookingDetails.setVisible(false);
			personalDetails.setVisible(true);
			this.page = "personalDetails";
			
			// Change header colour
			setActiveColour(labelPersonalDetailsHeader, personalDetailsLine, personalDetailsCircle);
			setUnactiveColour(labelBookingDetailsHeader, bookingDetailsLine, bookingDetailsCircle);
			setUnactiveColour(labelPaymentDetailsHeader, paymentDetailsLine, paymentDetailsCircle);
			
			// Set customer details
			txtCustomerName.setText(customer.getName());
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
			
			// Set itinerary
			itineraryCarName.setText(carName);
			itineraryLocation.setText(location);
			itineraryPlateNumber.setText(plateNumber);
			itineraryPickUpDateTime.setText(String.join(" ", pickUpDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), "at", pickUpTime.format(DateTimeFormatter.ofPattern("HH:mm"))));
			itineraryDropOffDateTime.setText(String.join(" ", dropOffDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), "at", dropoffTime.format(DateTimeFormatter.ofPattern("HH:mm"))));
		}
	}
	
	// Limit user input using regex	
	public void configureTextField() {
		Functions.limitTextFieldInput(txtCardholderName, "[^a-zA-Z\\s]");  // Alphabets and space only
		Functions.limitTextFieldInput(txtCVV, "\\D");  // Numbers only
	}
	
	// Disable date options before today
	public void	configureDatePicker() {
		Functions.disableDayBefore(txtPickUpDate);
		Functions.disableDayBefore(txtDropOffDate);
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
		String customerName = txtCustomerName.getText().trim();
		String gender = txtGender.getValue();
		String ICPassportNumber = txtICPassportNumber.getText().trim();
		LocalDate birthDate = txtBirthDate.getValue();
		String contact = txtContact.getText().trim();
		String country = txtCountry.getValue();
		
		if(Validation.validateComboBox(gender, genderError) &
		   Validation.validateICPassportNumber(ICPassportNumber, ICPassportNumberError) &
		   Validation.validateComboBox(country, countryError) &
		   Validation.validateContact(contact, contactError) &
		   Validation.validateDate(birthDate, birthDateError)
		   ) {
			
			// Show payment details close personal details
			personalDetails.setVisible(false);
			paymentDetails.setVisible(true);
			this.page = "paymentDetails";
			txtCardholderName.requestFocus();
			
			// Change header colour
			setActiveColour(labelPaymentDetailsHeader, paymentDetailsLine, paymentDetailsCircle);
			setUnactiveColour(labelPersonalDetailsHeader, personalDetailsLine, personalDetailsCircle);
			setUnactiveColour(labelBookingDetailsHeader, bookingDetailsLine, bookingDetailsCircle);
			
			// Set payment amount details
			int duration = Booking.getDifferenceInDays(booking.getPickUpDate(), booking.getDropOffDate());
			double totalPrice = car.getPrice()*duration;
			labelCarPrice.setText("RM " + car.getPrice());
			labelDuration.setText(Integer.toString(duration));
			labelTotalPrice.setText(Functions.limitDecimalPlaces(Double.toString(totalPrice), 2));
			txtTotalPriceLabel.setText("Total price for " + duration + " days :");
			
			// Save personal details
			booking.setCustomer(customer);
			booking.setGender(gender);
			booking.setICPassportNumber(ICPassportNumber);
			booking.setBirthDate(birthDate);
			booking.setContact(contact);
			booking.setCountry(country);
			booking.setPrice(totalPrice);
			
			// Set itinerary
			itineraryCustomerName.setText(customerName);
			itineraryCustomerContact.setText(contact);				
		}
	}
	
	// Click pay now button on payment details page
	public void toBookWithPayment(ActionEvent event) throws IOException { 
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
			booking.setPaymentStatus("Paid");
			booking.setBookingStatus("Pending");
			
			
			// Create new log object
			Log log = new Log();
			if (Customer.addNewBooking(booking, log)) {
				// Create invoice
				String invoiceDetails = String.join("|", Integer.toString(booking.getID()), LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")), cardType, cardholderName, cardNumber);
				Invoice.addNewInvoice(invoiceDetails);
				
				AlertDialog.showInfoMessage(event, "Booking request sent");
				
				// Set log details and add to log text file
				log.setUserID(customer.getID());
				log.setType("Booking");
				log.setAction("Add");
				
				log.addNewLog(log);
				
				// Redirect customer to home
				Functions.switchScene(event, root, stage, scene, "Home.fxml");
			} else {
				AlertDialog.showErrorMessage(event, "Booking request failed to sent. Please try again");
			}
		}
	}
	
	// Click pay later button on payment details page
	public void toBookWithoutPayment(ActionEvent event) throws IOException { 
		booking.setPaymentStatus("Unpaid");
		booking.setBookingStatus("Pending");
		
		// Create new log object
		Log log = new Log();
		if (Customer.addNewBooking(booking, log)) {
			AlertDialog.showInfoMessage(event, "Booking request sent");
			
			// Set log details and add to log text file
			log.setUserID(customer.getID());
			log.setType("Booking");
			log.setAction("Add");
			
			log.addNewLog(log);
			
			// Redirect customer to home
			Functions.switchScene(event, root, stage, scene, "Home.fxml");
		} else {
			AlertDialog.showErrorMessage(event, "Booking request failed to sent. Please try again");
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
		if (this.page.equals("personalDetails") || this.page.equals("paymentDetails")) {
			this.page = "bookingDetails";
			bookingDetails.setVisible(true);
			personalDetails.setVisible(false);
			paymentDetails.setVisible(false);
			
			setActiveColour(labelBookingDetailsHeader, bookingDetailsLine, bookingDetailsCircle);
			setUnactiveColour(labelPersonalDetailsHeader, personalDetailsLine, personalDetailsCircle);
			setUnactiveColour(labelPaymentDetailsHeader, paymentDetailsLine, paymentDetailsCircle);
		} else {
			AlertDialog.showInfoMessage(event, "Please proceed by clicking 'Next' button");
		}
	}
	
	public void backToPersonalDetails(MouseEvent event) {
		if (this.page.equals("paymentDetails")) {
			this.page = "personalDetails";
			bookingDetails.setVisible(false);
			personalDetails.setVisible(true);
			paymentDetails.setVisible(false);
			
			setActiveColour(labelPersonalDetailsHeader, personalDetailsLine, personalDetailsCircle);
			setUnactiveColour(labelBookingDetailsHeader, bookingDetailsLine, bookingDetailsCircle);
			setUnactiveColour(labelPaymentDetailsHeader, paymentDetailsLine, paymentDetailsCircle);
		} else {
			AlertDialog.showInfoMessage(event, "Please proceed by clicking 'Next' button");
		}
	}

}

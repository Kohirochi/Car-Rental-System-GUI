package application;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class AdminBookingRequestController implements Initializable {
	
	private Parent root;
	private Stage stage;
	private Scene scene;
	private ObservableList<Booking> bookingRequestList;
	private ObservableList<Customer> customerList;
	private ObservableList<Car> carList; 

	
	@FXML
	private Button btnDashboard, btnAdmin, btnCar, btnCustomerRegistration, btnCustomer, btnInvoice, btnBookingRequest, btnUpcomingBooking, btnBookingHistory, btnReport, btnLog, btnProfile;
	@FXML
	private ImageView iconDashboard, iconAdmin, iconCar, iconCustomerRegistration, iconCustomer, iconInvoice, iconBookingRequest, iconUpcomingBooking, iconBookingHistory, iconReport, iconLog, iconProfile, carImage;
	@FXML
	private TableView<Booking> tableBookingRequest;
	@FXML
	private TableColumn<Booking, String> colCustomer, colGender, colICPassportNumber, colContact, colCountry, colCar; 
	@FXML
	private TableColumn<Booking, Integer> colBookingID, colPrice, colDuration;
	@FXML
	private TableColumn<Booking, LocalDate> colBirthDate, colPickUpDate, colDropOffDate;
	@FXML
	private TableColumn<Booking, LocalTime> colPickUpTime, colDropOffTime;
	@FXML
	private TextField txtBookingID, txtICPassportNumber, txtContact, txtPrice, txtSearch; 
	@FXML
	private ComboBox<String> txtGender, txtCountry;
	@FXML
	private ComboBox<LocalTime> txtPickUpTime, txtDropOffTime;
	@FXML
	private ComboBox<Customer> txtCustomer;
	@FXML
	private ComboBox<Car> txtCar;
	@FXML
	private DatePicker txtBirthDate, txtPickUpDate, txtDropOffDate;
	@FXML
	private Label customerError, customerSelectError, genderError, ICPassportNumberError, birthDateError, contactError, countryError, carError, carSelectError, pickUpDateError, pickUpTimeError, pickUpTimeSelectError, dropOffDateError, dropOffTimeError, dropOffTimeSelectError;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Functions.showPlaceholderOnFocus(txtSearch);
		
		// When hover on button change color
		styleNavBar();
		
		// Display car details to table
		displayBookingRequestList();
		
		// Render options for combo box and display prompt text
		configureComboBox();
		
		// Display label when combo box is null
		configureObjectComboBox();
		
		// Add listeners to autofill price field
		configurePriceField();
		
		// Disable date options before today
		configureDatePicker();
	}
	
	// Render options for combo box and display prompt text	
	public void configureComboBox() {
		showCustomerOptions();
		showGenderOptions();
		txtGender.setButtonCell(new PromptButtonCell<>("Please select"));
		showCarOptions();
		showCountryOptions();
		txtCountry.setButtonCell(new PromptButtonCell<>("Please select"));
		showTimeOptions();
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

	public void displayBookingRequestList() {
		bookingRequestList = Booking.getBookingDetails("pending");
		
		// Populate individual cells in each column
		colBookingID.setCellValueFactory(new PropertyValueFactory<>("ID"));
		colCustomer.setCellValueFactory(new PropertyValueFactory<>("customer"));
		colGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
		colICPassportNumber.setCellValueFactory(new PropertyValueFactory<>("ICPassportNumber"));
		colBirthDate.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
		colContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
		colCountry.setCellValueFactory(new PropertyValueFactory<>("country"));
		colCar.setCellValueFactory(new PropertyValueFactory<>("car"));
		colPickUpDate.setCellValueFactory(new PropertyValueFactory<>("pickUpDate"));
		colDropOffDate.setCellValueFactory(new PropertyValueFactory<>("dropOffDate"));
		colDuration.setCellValueFactory(new PropertyValueFactory<>("duration"));
		colPickUpTime.setCellValueFactory(new PropertyValueFactory<>("pickUpTime"));
		colDropOffTime.setCellValueFactory(new PropertyValueFactory<>("dropOffTime"));
		colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
		
		// Sets the value of the property items.
		tableBookingRequest.setItems(bookingRequestList);
		
		// Add listener to filter search result
		search();
    }
	
	public void search() {
		// Initialize a filter list
		FilteredList<Booking> filter = new FilteredList<>(bookingRequestList, condition -> true);
		
		txtSearch.textProperty().addListener((observable, oldText, newText) -> {
			
			// Sets the value of the car predicate
            filter.setPredicate(predicateBookingData -> {
            	
                if (newText == null || newText.isEmpty()) {
                    return true;
                }

                String searchKeyword = newText.toLowerCase();

                if (Integer.toString(predicateBookingData.getID()).contains(searchKeyword)) {
                    return true;
                } else if (predicateBookingData.getCustomer().toString().toLowerCase().contains(searchKeyword)) {
                    return true;
                } else if (predicateBookingData.getGender().toLowerCase().contains(searchKeyword)) {
                    return true;
                } else if (predicateBookingData.getICPassportNumber().toLowerCase().contains(searchKeyword)) {
                    return true;
                } else if (predicateBookingData.getBirthDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")).contains(searchKeyword)) {
                    return true;
                } else if (predicateBookingData.getContact().toLowerCase().contains(searchKeyword)) {
                    return true;
                } else if (predicateBookingData.getCountry().toLowerCase().contains(searchKeyword)) {
                    return true;
                } else if (predicateBookingData.getCar().toString().toLowerCase().contains(searchKeyword)) {
                    return true;
                } else if (predicateBookingData.getPickUpDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")).contains(searchKeyword)) {
                    return true;
                } else if (predicateBookingData.getDropOffDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")).contains(searchKeyword)) {
                    return true;
                } else if (Integer.toString(predicateBookingData.getDuration()).contains(searchKeyword)) {
                    return true;
                } else if (predicateBookingData.getPickUpTime().format(DateTimeFormatter.ofPattern("HH:mm")).contains(searchKeyword)) {
                    return true;
                } else if (predicateBookingData.getDropOffTime().format(DateTimeFormatter.ofPattern("HH:mm")).contains(searchKeyword)) {
                    return true;
                } else if (Double.toString(predicateBookingData.getPrice()).contains(searchKeyword)) {
                    return true;
                } else {
                    return false;
                }
            });
        });

        SortedList<Booking> sortedList = new SortedList<>(filter);

        sortedList.comparatorProperty().bind(tableBookingRequest.comparatorProperty());
        tableBookingRequest.setItems(sortedList);
	}
	
	public void showSelectedRow() {
		Booking bookingRequest = tableBookingRequest.getSelectionModel().getSelectedItem();
		int num = tableBookingRequest.getSelectionModel().getSelectedIndex();
		if (num < 0) return;
		
		txtBookingID.setText(String.valueOf(bookingRequest.getID()));
		txtCustomer.setValue(getCustomerValue(bookingRequest.getCustomer(), customerList));
		txtGender.setValue(bookingRequest.getGender());
		txtICPassportNumber.setText(bookingRequest.getICPassportNumber());
		txtBirthDate.setValue(bookingRequest.getBirthDate());
		txtContact.setText(bookingRequest.getContact());
		txtCountry.setValue(bookingRequest.getCountry());
		txtCar.setValue(getCarValue(bookingRequest.getCar(), carList));
		txtPickUpDate.setValue(bookingRequest.getPickUpDate());
		txtDropOffDate.setValue(bookingRequest.getDropOffDate());
		txtPickUpTime.setValue(bookingRequest.getPickUpTime());
		txtDropOffTime.setValue(bookingRequest.getDropOffTime());
		txtPrice.setText(String.valueOf(bookingRequest.getPrice()));
	}
	
	// Loop combo box list and set value to the matching option	
	public Customer getCustomerValue(Customer selectedCustomer, ObservableList<Customer> customerList) {
		for (Customer customer : customerList) {
			if (customer.getID() == selectedCustomer.getID()) {
				return customer;
			}
		}
		return null;
	}
	
	public Car getCarValue(Car selectedCar, ObservableList<Car> carList) {
		for (Car car : carList) {
			if (car.getID() == selectedCar.getID()) {
				return car;
			}
		}
		return null;
	}
	
	// Set options for combo box
	public void showCustomerOptions() {
		customerList = FXCollections.observableArrayList();
		ObservableList<Customer> customers = Customer.getCustomersCredentials();
		
		for (Customer customer : customers) {
			customerList.add(new Customer(customer.getID(), customer.getName()));
		}
		txtCustomer.setItems(customerList);
	}
	
	public void showCarOptions() {
		carList = FXCollections.observableArrayList();
		try {
			ObservableList<Car> cars = Car.getCarsDetails();
			
			for (Car car : cars) {
	        	carList.add(new Car(car.getID(), car.getName()));
	        }
	        txtCar.setItems(carList);
	        
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	
	public void showTimeOptions() {
		ObservableList<LocalTime> timeFrameList = FXCollections.observableArrayList(Functions.generateTimeFrameList());
		txtPickUpTime.setItems(timeFrameList);
		txtDropOffTime.setItems(timeFrameList);
	}
	
	// Display label when combo box is null
	public void configureObjectComboBox() {
		txtCustomer.valueProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue == null) {
				customerSelectError.setVisible(true);
			} else {
				customerSelectError.setVisible(false);
			}
		});
		
		txtCar.valueProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue == null) {
				carSelectError.setVisible(true);
			} else {
				carSelectError.setVisible(false);
			}
		});
		
		txtPickUpTime.valueProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue == null) {
				pickUpTimeSelectError.setVisible(true);
			} else {
				pickUpTimeSelectError.setVisible(false);
			}
		});
		
		txtDropOffTime.valueProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue == null) {
				dropOffTimeSelectError.setVisible(true);
			} else {
				dropOffTimeSelectError.setVisible(false);
			}
		});
	}
	
	// Add listeners to autofill price field
	public void configurePriceField() {
		txtCar.valueProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue == null) return;
			if ((txtPickUpDate.getValue() != null) && (txtDropOffDate.getValue() != null)) {
				Car car = newValue;
				LocalDate pickUpDate = txtPickUpDate.getValue();
				LocalDate dropOffDate = txtDropOffDate.getValue();

				int bookingDays = Booking.getDifferenceInDays(pickUpDate, dropOffDate);
				try {
					double price = Car.calculateBookingPrice(car.getID(), bookingDays);
					String formattedPrice = Functions.limitDecimalPlaces(Double.toString(price), 2);
					txtPrice.setText(formattedPrice);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				txtPrice.setText("");
			}
		});
		
		txtPickUpDate.valueProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue == null) return;
			if ((txtCar.getValue() != null) && (txtDropOffDate.getValue() != null)) {
				Car car = txtCar.getValue();
				LocalDate pickUpDate = newValue;
				LocalDate dropOffDate = txtDropOffDate.getValue();

				int bookingDays = Booking.getDifferenceInDays(pickUpDate, dropOffDate);
				try {
					double price = Car.calculateBookingPrice(car.getID(), bookingDays);
					String formattedPrice = Functions.limitDecimalPlaces(Double.toString(price), 2);
					txtPrice.setText(formattedPrice);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				txtPrice.setText("");
			}
		});
		
		txtDropOffDate.valueProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue == null) return;
			if ((txtCar.getValue() != null) && (txtPickUpDate.getValue() != null)) {
				Car car = txtCar.getValue();
				LocalDate pickUpDate = txtPickUpDate.getValue();
				LocalDate dropOffDate = newValue;

				int bookingDays = Booking.getDifferenceInDays(pickUpDate, dropOffDate);
				try {
					double price = Car.calculateBookingPrice(car.getID(), bookingDays);
					String formattedPrice = Functions.limitDecimalPlaces(Double.toString(price), 2);
					txtPrice.setText(formattedPrice);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				txtPrice.setText("");
			}
		});
	}
	
	// Disable date options before today
	public void	configureDatePicker() {
		Functions.disableDayBefore(txtPickUpDate);
		Functions.disableDayBefore(txtDropOffDate);
	}
	
	// Clear button	
	public void clear() {
		// TextField 
		txtBookingID.setText("");
		txtICPassportNumber.setText("");
		txtContact.setText("");
		txtPrice.setText("");
		
		// ComboBox
		txtCustomer.setValue(null);
		txtGender.setValue(null);
		txtCountry.setValue(null);
		txtCar.setValue(null);
		txtPickUpTime.setValue(null);
		txtDropOffTime.setValue(null);
		
		// DatePicker
		txtBirthDate.setValue(null);
		txtPickUpDate.setValue(null);
		txtDropOffDate.setValue(null);
		
		// Label Error
		customerError.setVisible(false);
		genderError.setVisible(false);
		ICPassportNumberError.setVisible(false);
		birthDateError.setVisible(false);
		contactError.setVisible(false);
		countryError.setVisible(false);
		carError.setVisible(false);
		pickUpDateError.setVisible(false);
		pickUpTimeError.setVisible(false);
		dropOffDateError.setVisible(false);
		dropOffTimeError.setVisible(false);
	}
	
	// Add button
	public void add(ActionEvent event) throws IOException {
		Customer customer = txtCustomer.getValue();
		String gender = txtGender.getValue();
		String ICPassportNumber = txtICPassportNumber.getText().trim();
		LocalDate birthDate = txtBirthDate.getValue();
		String contact = txtContact.getText().trim();
		String country = txtCountry.getValue();
		Car car = txtCar.getValue();
		LocalDate pickUpDate = txtPickUpDate.getValue();
		LocalDate dropOffDate = txtDropOffDate.getValue();
		LocalTime pickUpTime = txtPickUpTime.getValue();
		LocalTime dropoffTime = txtDropOffTime.getValue();
		String price = txtPrice.getText();
		
		// Validate car details
		if (Validation.validateComboBox(customer, customerError) &
			Validation.validateComboBox(gender, genderError) &
			Validation.validateComboBox(country, countryError) &
			Validation.validateComboBox(car, carError) &
			Validation.validateComboBox(pickUpTime, pickUpTimeError) &
			Validation.validateDropOffTime(pickUpDate, dropOffDate, pickUpTime, dropoffTime, dropOffTimeError) &
			Validation.validateICPassportNumber(ICPassportNumber, ICPassportNumberError) &
			Validation.validateContact(contact, contactError) &
			Validation.validateDate(birthDate, birthDateError) &
			Validation.validatePickUpDate(car, pickUpDate, pickUpDateError) &
			Validation.validateDropOffDate(car, pickUpDate, dropOffDate, dropOffDateError)) {
			
			// Create new line to be inserted into booking text file
			String bookingDetails = String.join("|", String.valueOf(customer.getID()), gender, ICPassportNumber, birthDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), contact, country, String.valueOf(car.getID()), pickUpDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), pickUpTime.format(DateTimeFormatter.ofPattern("HH:mm")), dropOffDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), dropoffTime.format(DateTimeFormatter.ofPattern("HH:mm")), price, "Unpaid", "Pending", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));

			Log log = new Log();
			if (Admin.addNewBooking(bookingDetails, log)) {
				AlertDialog.showInfoMessage(event, "Add successful");
				// Set log details and add to log text file
				log.setUserID(Session.admin.getID());
				log.setType("Booking");
				log.setAction("Add");
				
				log.addNewLog(log);
				
				clear();
				displayBookingRequestList();
			} else {
				AlertDialog.showErrorMessage(event, "Add unsuccessful");
			}
		}
	}
	
	// Approve button
	public void approve(ActionEvent event) throws IOException {
		String ID = txtBookingID.getText().trim();

		if (ID.isBlank()) {
			AlertDialog.showErrorMessage(event, "Please select a row before approving");
			return;
		}
		
		Log log = new Log();
		if (Admin.approveBookingRequest(ID, log)) {
			// Get customer email
			Booking booking = Booking.getSpecificBookingDetails(Integer.parseInt(ID));
			String email = Customer.getSpecificCustomerDetails(booking.getCustomer().getID()).getEmail();
			
			// Send notification as email
			SMTP.sendNotification(email, booking, "approved");
			
			AlertDialog.showInfoMessage(event, "Booking request approved");
			// Set log details and add to log text file
			log.setUserID(Session.admin.getID());
			log.setType("Booking");
			log.setTargetID(Integer.parseInt(ID));
			log.setAction("Approve");
			
			log.addNewLog(log);
			
			clear();
			displayBookingRequestList();
		} else {
			AlertDialog.showErrorMessage(event, "Failed to approve booking request");
		}
	}
	
	// Reject button
	public void reject(ActionEvent event) throws IOException {
		String ID = txtBookingID.getText().trim();
		if (ID.isBlank()) {
			AlertDialog.showErrorMessage(event, "Please select a row before rejecting");
			return;
		}
		
		Log log = new Log();
		if (Admin.rejectBookingRequest(ID, log)) {
			// Get customer email
			Booking booking = Booking.getSpecificBookingDetails(Integer.parseInt(ID));
			String email = Customer.getSpecificCustomerDetails(booking.getCustomer().getID()).getEmail();
			
			// Send notification as email
			SMTP.sendNotification(email, booking, "rejected");
			
			AlertDialog.showInfoMessage(event, "Booking request rejected");
			// Set log details and add to log text file
			log.setUserID(Session.admin.getID());
			log.setTargetID(Integer.parseInt(ID));
			log.setType("Booking");
			log.setAction("Reject");
			
			log.addNewLog(log);
						
			clear();
			displayBookingRequestList();
		} else {
			AlertDialog.showErrorMessage(event, "Failed to reject booking request");
		}
	}
	
	// Export to excel
	public void export(ActionEvent event) throws IOException {
		Workbook workbook = new HSSFWorkbook();
		Sheet spreadsheet = workbook.createSheet("Booking Request Details");

        Row row = spreadsheet.createRow(0);

        for (int j = 0; j < tableBookingRequest.getColumns().size(); j++) {
            row.createCell(j).setCellValue(tableBookingRequest.getColumns().get(j).getText());
        }

        for (int i = 0; i < tableBookingRequest.getItems().size(); i++) {
            row = spreadsheet.createRow(i + 1);
            for (int j = 0; j < tableBookingRequest.getColumns().size(); j++) {
                if(tableBookingRequest.getColumns().get(j).getCellData(i) != null) { 
                    row.createCell(j).setCellValue(tableBookingRequest.getColumns().get(j).getCellData(i).toString()); 
                }
                else {
                    row.createCell(j).setCellValue("");
                }   
            }
        }

        FileOutputStream fileOut = new FileOutputStream(System.getProperty("user.home") + "\\Downloads\\Booking_request_list.xls");
        workbook.write(fileOut);
        fileOut.close();
        AlertDialog.showInfoMessage(event, "Table exported successfully. Please search for the xls file in your Downloads folder");
	}
	
}

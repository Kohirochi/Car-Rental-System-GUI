package application;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class AdminUpcomingBookingController implements Initializable {
	
	private Parent root;
	private Stage stage;
	private Scene scene;
	private ObservableList<Booking> upcomingBookingList;
	
	@FXML
	private Button btnDashboard, btnAdmin, btnCar, btnCustomerRegistration, btnCustomer, btnInvoice, btnBookingRequest, btnUpcomingBooking, btnBookingHistory, btnReport, btnLog, btnProfile, btnRentedOut, btnCompleted;
	@FXML
	private ImageView iconDashboard, iconAdmin, iconCar, iconCustomerRegistration, iconCustomer, iconInvoice, iconBookingRequest, iconUpcomingBooking, iconBookingHistory, iconReport, iconLog, iconProfile, carImage;
	@FXML
	private TableView<Booking> tableUpcomingBooking;
	@FXML
	private TableColumn<Booking, String> colCustomer, colGender, colContact, colCar, colPaymentStatus, colBookingStatus; 
	@FXML
	private TableColumn<Booking, Integer> colBookingID, colPrice, colDuration;
	@FXML
	private TableColumn<Booking, LocalDate> colPickUpDate, colDropOffDate;
	@FXML
	private TableColumn<Booking, LocalTime> colPickUpTime, colDropOffTime;
	@FXML
	private TextField txtBookingID, txtCustomer, txtGender, txtContact, txtCar, txtPickUpDate, txtDropOffDate, txtPickUpTime, txtDropOffTime, txtPrice, txtPaymentStatus, txtBookingStatus, txtPenalty, txtSearch;
	@FXML
	private Label customerError, customerSelectError, genderError, contactError, carError, carSelectError, pickUpDateError, pickUpTimeError, pickUpTimeSelectError, dropOffDateError, dropOffTimeError, dropOffTimeSelectError, paymentStatusError, bookingStatusError, labelPenalty;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Functions.showPlaceholderOnFocus(txtSearch);
		
		// When hover on button change color
		styleNavBar();
		
		// Display car details to table
		displayUpcomingBookingList();
		
		// Scan through all bookings and convert status to overdue if applicable
		if (Session.checkedOverdue == false) {
			Booking.updateOverdueBookings();
			Session.checkedOverdue = true;
		}
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

	public void displayUpcomingBookingList() {
		upcomingBookingList = Booking.getBookingDetails("upcoming");
		
		// Populate individual cells in each column
		colBookingID.setCellValueFactory(new PropertyValueFactory<>("ID"));
		colCustomer.setCellValueFactory(new PropertyValueFactory<>("customer"));
		colGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
		colContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
		colCar.setCellValueFactory(new PropertyValueFactory<>("car"));
		colPickUpDate.setCellValueFactory(new PropertyValueFactory<>("pickUpDate"));
		colDropOffDate.setCellValueFactory(new PropertyValueFactory<>("dropOffDate"));
		colPickUpTime.setCellValueFactory(new PropertyValueFactory<>("pickUpTime"));
		colDropOffTime.setCellValueFactory(new PropertyValueFactory<>("dropOffTime"));
		colDuration.setCellValueFactory(new PropertyValueFactory<>("duration"));
		colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
		colPaymentStatus.setCellValueFactory(new PropertyValueFactory<>("paymentStatus"));
		colBookingStatus.setCellValueFactory(new PropertyValueFactory<>("bookingStatus"));
		
		// Sets the value of the property items.
		tableUpcomingBooking.setItems(upcomingBookingList);
		
		// Add listener to filter search result
		search();
    }
	
	public void search() {
		// Initialize a filter list
		FilteredList<Booking> filter = new FilteredList<>(upcomingBookingList, condition -> true);
		
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
                } else if (predicateBookingData.getContact().toLowerCase().contains(searchKeyword)) {
                    return true;
                } else if (predicateBookingData.getCar().toString().toLowerCase().contains(searchKeyword)) {
                    return true;
                } else if (predicateBookingData.getPickUpDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")).contains(searchKeyword)) {
                    return true;
                } else if (predicateBookingData.getDropOffDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")).contains(searchKeyword)) {
                    return true;
                } else if (predicateBookingData.getPickUpTime().format(DateTimeFormatter.ofPattern("HH:mm")).contains(searchKeyword)) {
                    return true;
                } else if (predicateBookingData.getDropOffTime().format(DateTimeFormatter.ofPattern("HH:mm")).contains(searchKeyword)) {
                    return true;
                } else if (Integer.toString(predicateBookingData.getDuration()).contains(searchKeyword)) {
                    return true;
                } else if (Double.toString(predicateBookingData.getPrice()).contains(searchKeyword)) {
                    return true;
                } else if (predicateBookingData.getPaymentStatus().toLowerCase().contains(searchKeyword)) {
                    return true;
                } else if (predicateBookingData.getBookingStatus().toLowerCase().contains(searchKeyword)) {
                    return true;
                } else {
                    return false;
                }
            });
        });

        SortedList<Booking> sortedList = new SortedList<>(filter);

        sortedList.comparatorProperty().bind(tableUpcomingBooking.comparatorProperty());
        tableUpcomingBooking.setItems(sortedList);
	}
	
	public void showSelectedRow() throws FileNotFoundException {
		Booking upcomingBooking = tableUpcomingBooking.getSelectionModel().getSelectedItem();
		int num = tableUpcomingBooking.getSelectionModel().getSelectedIndex();
		if (num < 0) return;
		
		txtBookingID.setText(String.valueOf(upcomingBooking.getID()));
		txtCustomer.setText(String.valueOf(upcomingBooking.getCustomer()));
		txtGender.setText(upcomingBooking.getGender());
		txtContact.setText(upcomingBooking.getContact());
		txtCar.setText(String.valueOf(upcomingBooking.getCar()));
		txtPickUpDate.setText(upcomingBooking.getPickUpDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
		txtDropOffDate.setText(upcomingBooking.getDropOffDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
		txtPickUpTime.setText(upcomingBooking.getPickUpTime().format(DateTimeFormatter.ofPattern("HH:mm")));
		txtDropOffTime.setText(upcomingBooking.getDropOffTime().format(DateTimeFormatter.ofPattern("HH:mm")));
		txtPrice.setText(String.valueOf(upcomingBooking.getPrice()));
		txtPaymentStatus.setText(upcomingBooking.getPaymentStatus());
		txtBookingStatus.setText(upcomingBooking.getBookingStatus());
		
		// Show different buttons depending on payment and booking status
		configureComponents(upcomingBooking);
	}
	
	// Show different buttons and text field depending on payment and booking status
	public void configureComponents(Booking upcomingBooking) throws FileNotFoundException {
		if (upcomingBooking.getBookingStatus().equals("Pending Payment")) {
			btnRentedOut.setVisible(false);
			btnCompleted.setVisible(false);
			
			labelPenalty.setVisible(false);
			txtPenalty.setVisible(false);
		} else if (upcomingBooking.getBookingStatus().equals("Confirmed")) {
			btnRentedOut.setVisible(true);
			btnCompleted.setVisible(false);
			
			labelPenalty.setVisible(false);
			txtPenalty.setVisible(false);
		} else if (upcomingBooking.getBookingStatus().equals("Rented Out") ) {
			labelPenalty.setVisible(false);
			txtPenalty.setVisible(false);
			btnRentedOut.setVisible(false);
			if (upcomingBooking.getDropOffDate().equals(LocalDate.now())) {
				btnCompleted.setVisible(true);				
			}			
		} else if (upcomingBooking.getBookingStatus().equals("Overdue")) {
			btnRentedOut.setVisible(false);
			btnCompleted.setVisible(true);
			
			LocalDate dropOffDate = upcomingBooking.getDropOffDate();
			int additionalDays = Booking.getDifferenceInDays(dropOffDate, LocalDate.now());
			double penalty = Car.calculateBookingPrice(upcomingBooking.getCar().getID(), additionalDays);
			String formattedPenalty = Functions.limitDecimalPlaces(Double.toString(penalty), 2);

			labelPenalty.setVisible(true);
			txtPenalty.setText(formattedPenalty);
			txtPenalty.setVisible(true);
		}
	}
	
	// Clear button	
	public void clear() {
		// TextField 
		txtBookingID.setText("");
		txtContact.setText("");
		txtPrice.setText("");
		txtPaymentStatus.setText("");
		txtBookingStatus.setText("");
		txtCustomer.setText("");
		txtGender.setText("");
		txtCar.setText("");
		txtPickUpTime.setText("");
		txtDropOffTime.setText("");
		txtPickUpDate.setText("");
		txtDropOffDate.setText("");
		txtPenalty.setText("");
	}
	
	// Rented out button
	public void markAsRentedOut(ActionEvent event) throws IOException {
		String ID = txtBookingID.getText().trim();
		if (ID.isBlank()) {
			AlertDialog.showErrorMessage(event, "Please select a row before marking");
			return;
		}
		LocalDate pickUpDate = Booking.getSpecificBookingDetails(Integer.parseInt(ID)).getPickUpDate();
		if (pickUpDate.isAfter(LocalDate.now())) {
			AlertDialog.showErrorMessage(event, "Car cannot be rented out before pick-up date");
			return;
		}
		
		Log log = new Log();
		if (Booking.markAsRentedOut(ID, log)) {
			AlertDialog.showInfoMessage(event, "Booking status converted successfully");
			// Set log details and add to log text file
			log.setUserID(Session.admin.getID());
			log.setTargetID(Integer.parseInt(ID));
			log.setType("Booking");
			log.setAction("Rented Out");
			
			log.addNewLog(log);
									
			clear();
			displayUpcomingBookingList();
		} else {
			AlertDialog.showErrorMessage(event, "Failed to convert booking status");
		}
	}
	
	// Completed button
	public void markAsCompleted(ActionEvent event) throws IOException {
		String ID = txtBookingID.getText().trim();
		String price = txtPenalty.getText();
		if (ID.isBlank()) {
			AlertDialog.showErrorMessage(event, "Please select a row before marking");
			return;
		}
		
		Log log = new Log();
		if (Booking.markAsCompleted(ID, price, log)) {
			AlertDialog.showInfoMessage(event, "Booking status converted successfully");
			// Set log details and add to log text file
			log.setUserID(Session.admin.getID());
			log.setType("Booking");
			log.setTargetID(Integer.parseInt(ID));
			log.setAction("Completed");
			
			log.addNewLog(log);
											
			clear();
			displayUpcomingBookingList();
		} else {
			AlertDialog.showErrorMessage(event, "Failed to convert booking status");
		}
	}
	
	// Export to excel
	public void export(ActionEvent event) throws IOException {
		Workbook workbook = new HSSFWorkbook();
		Sheet spreadsheet = workbook.createSheet("Upcoming Booking Details");

        Row row = spreadsheet.createRow(0);

        for (int j = 0; j < tableUpcomingBooking.getColumns().size(); j++) {
            row.createCell(j).setCellValue(tableUpcomingBooking.getColumns().get(j).getText());
        }

        for (int i = 0; i < tableUpcomingBooking.getItems().size(); i++) {
            row = spreadsheet.createRow(i + 1);
            for (int j = 0; j < tableUpcomingBooking.getColumns().size(); j++) {
                if(tableUpcomingBooking.getColumns().get(j).getCellData(i) != null) { 
                    row.createCell(j).setCellValue(tableUpcomingBooking.getColumns().get(j).getCellData(i).toString()); 
                }
                else {
                    row.createCell(j).setCellValue("");
                }   
            }
        }

        FileOutputStream fileOut = new FileOutputStream(System.getProperty("user.home") + "\\Downloads\\Upcoming_booking_list.xls");
        workbook.write(fileOut);
        fileOut.close();
        AlertDialog.showInfoMessage(event, "Table exported successfully. Please search for the xls file in your Downloads folder");
	}
}

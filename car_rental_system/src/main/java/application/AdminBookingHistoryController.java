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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class AdminBookingHistoryController implements Initializable {
	
	private Parent root;
	private Stage stage;
	private Scene scene;
	private ObservableList<Booking> bookingHistoryList;
	
	@FXML
	private Button btnDashboard, btnAdmin, btnCar, btnCustomerRegistration, btnCustomer, btnInvoice, btnBookingRequest, btnUpcomingBooking, btnBookingHistory, btnReport, btnLog, btnProfile, btnRentedOut, btnCompleted;
	@FXML
	private ImageView iconDashboard, iconAdmin, iconCar, iconCustomerRegistration, iconCustomer, iconInvoice, iconBookingRequest, iconUpcomingBooking, iconBookingHistory, iconReport, iconLog, iconProfile, carImage;
	@FXML
	private TableView<Booking> tableBookingHistory;
	@FXML
	private TableColumn<Booking, String> colCustomer, colGender, colICPassportNumber, colContact, colCountry, colCar, colPaymentStatus, colBookingStatus, colDateCreated; 
	@FXML
	private TableColumn<Booking, Integer> colBookingID, colPrice, colDuration;
	@FXML
	private TableColumn<Booking, LocalDate> colBirthDate, colPickUpDate, colDropOffDate;
	@FXML
	private TableColumn<Booking, LocalTime> colPickUpTime, colDropOffTime;
//	@FXML
//	private TableColumn<Booking, LocalDateTime> colDateCreated;
	@FXML
	private TextField txtSearch;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Functions.showPlaceholderOnFocus(txtSearch);
		
		// When hover on button change color
		styleNavBar();
		
		// Display car details to table
		displayUpcomingBookingList();
		
		// Add listener to filter search result
		search();
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
		bookingHistoryList = Booking.getBookingDetails("history");
		
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
		colPickUpTime.setCellValueFactory(new PropertyValueFactory<>("pickUpTime"));
		colDropOffTime.setCellValueFactory(new PropertyValueFactory<>("dropOffTime"));
		colDuration.setCellValueFactory(new PropertyValueFactory<>("duration"));
		colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
		colPaymentStatus.setCellValueFactory(new PropertyValueFactory<>("paymentStatus"));
		colBookingStatus.setCellValueFactory(new PropertyValueFactory<>("bookingStatus"));
		colDateCreated.setCellValueFactory(new PropertyValueFactory<>("dateCreatedString"));
		
		// Sets the value of the property items.
		tableBookingHistory.setItems(bookingHistoryList);
    }
	
	public void search() {
		// Initialize a filter list
		FilteredList<Booking> filter = new FilteredList<>(bookingHistoryList, condition -> true);
		
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
                } else if (predicateBookingData.getDateCreatedString().contains(searchKeyword)) {
                    return true;
                } else {
                    return false;
                }
            });
        });

        SortedList<Booking> sortedList = new SortedList<>(filter);

        sortedList.comparatorProperty().bind(tableBookingHistory.comparatorProperty());
        tableBookingHistory.setItems(sortedList);
	}
	
	// Export to excel
	public void export(ActionEvent event) throws IOException {
		Workbook workbook = new HSSFWorkbook();
		Sheet spreadsheet = workbook.createSheet("Booking History Details");

        Row row = spreadsheet.createRow(0);

        for (int j = 0; j < tableBookingHistory.getColumns().size(); j++) {
            row.createCell(j).setCellValue(tableBookingHistory.getColumns().get(j).getText());
        }

        for (int i = 0; i < tableBookingHistory.getItems().size(); i++) {
            row = spreadsheet.createRow(i + 1);
            for (int j = 0; j < tableBookingHistory.getColumns().size(); j++) {
                if(tableBookingHistory.getColumns().get(j).getCellData(i) != null) { 
                    row.createCell(j).setCellValue(tableBookingHistory.getColumns().get(j).getCellData(i).toString()); 
                }
                else {
                    row.createCell(j).setCellValue("");
                }   
            }
        }

        FileOutputStream fileOut = new FileOutputStream(System.getProperty("user.home") + "\\Downloads\\Booking_history_list.xls");
        workbook.write(fileOut);
        fileOut.close();
        AlertDialog.showInfoMessage(event, "Table exported successfully. Please search for the xls file in your Downloads folder");
	}
}

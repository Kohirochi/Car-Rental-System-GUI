package application;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
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

public class AdminLogController implements Initializable {
	
	private Parent root;
	private Stage stage;
	private Scene scene;
	private ObservableList<Log> logList;
	
	@FXML
	private Button btnDashboard, btnAdmin, btnCar, btnCustomerRegistration, btnCustomer, btnInvoice, btnBookingRequest, btnUpcomingBooking, btnBookingHistory, btnReport, btnLog, btnProfile;
	@FXML
	private ImageView iconDashboard, iconAdmin, iconCar, iconCustomerRegistration, iconCustomer, iconInvoice, iconBookingRequest, iconUpcomingBooking, iconBookingHistory, iconReport, iconLog, iconProfile, carImage;
	@FXML
	private TableView<Log> tableLog;
	@FXML
	private TableColumn<Booking, String> colLogPlainText, colType, colAction, colStatus, colDateCreated; 
	@FXML
	private TableColumn<Booking, Integer> colLogID, colUserID, colTargetID;
	@FXML
	private TextField txtSearch;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Functions.showPlaceholderOnFocus(txtSearch);
		
		// When hover on button change color
		styleNavBar();
		
		// Display car details to table
		displayLogList();
		
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

	public void displayLogList() {
		try {
			logList = Log.getLogsDetails();
			
			// Populate individual cells in each column
			colLogID.setCellValueFactory(new PropertyValueFactory<>("ID"));
			colLogPlainText.setCellValueFactory(new PropertyValueFactory<>("plainText"));
			colDateCreated.setCellValueFactory(new PropertyValueFactory<>("dateCreatedString"));
			colUserID.setCellValueFactory(new PropertyValueFactory<>("userID"));
			colType.setCellValueFactory(new PropertyValueFactory<>("type"));
			colTargetID.setCellValueFactory(new PropertyValueFactory<>("targetID"));
			colAction.setCellValueFactory(new PropertyValueFactory<>("action"));
			colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
			
			// Sets the value of the property items.
			tableLog.setItems(logList);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("Booking text file not found");
		}
    }
	
	public void search() {
		// Initialize a filter list
		FilteredList<Log> filter = new FilteredList<>(logList, condition -> true);
		
		txtSearch.textProperty().addListener((observable, oldText, newText) -> {
			
			// Sets the value of the car predicate
            filter.setPredicate(predicateLogData -> {
            	
                if (newText == null || newText.isEmpty()) {
                    return true;
                }

                String searchKeyword = newText.toLowerCase();

                if (Integer.toString(predicateLogData.getID()).contains(searchKeyword)) {
                    return true;
                } else if (predicateLogData.getDateCreatedString().contains(searchKeyword)) {
                    return true;
                } else if (Integer.toString(predicateLogData.getUserID()).contains(searchKeyword)) {
                    return true;
                } else if (predicateLogData.getType().toLowerCase().contains(searchKeyword)) {
                    return true;
                } else if (Integer.toString(predicateLogData.getTargetID()).contains(searchKeyword)) {
                    return true;
                } else if (predicateLogData.getAction().toLowerCase().contains(searchKeyword)) {
                    return true;
                } else if (predicateLogData.getStatus().toLowerCase().contains(searchKeyword)) {
                    return true;
                } else {
                    return false;
                }
            });
        });

        SortedList<Log> sortedList = new SortedList<>(filter);

        sortedList.comparatorProperty().bind(tableLog.comparatorProperty());
        tableLog.setItems(sortedList);
	}
	// Export to excel
	public void export(ActionEvent event) throws IOException {
		Workbook workbook = new HSSFWorkbook();
		Sheet spreadsheet = workbook.createSheet("Log Details");

        Row row = spreadsheet.createRow(0);

        for (int j = 0; j < tableLog.getColumns().size(); j++) {
            row.createCell(j).setCellValue(tableLog.getColumns().get(j).getText());
        }

        for (int i = 0; i < tableLog.getItems().size(); i++) {
            row = spreadsheet.createRow(i + 1);
            for (int j = 0; j < tableLog.getColumns().size(); j++) {
                if(tableLog.getColumns().get(j).getCellData(i) != null) { 
                    row.createCell(j).setCellValue(tableLog.getColumns().get(j).getCellData(i).toString()); 
                }
                else {
                    row.createCell(j).setCellValue("");
                }   
            }
        }

        FileOutputStream fileOut = new FileOutputStream(System.getProperty("user.home") + "\\Downloads\\Log_list.xls");
        workbook.write(fileOut);
        fileOut.close();
        AlertDialog.showInfoMessage(event, "Table exported successfully. Please search for the xls file in your Downloads folder");
	}
}

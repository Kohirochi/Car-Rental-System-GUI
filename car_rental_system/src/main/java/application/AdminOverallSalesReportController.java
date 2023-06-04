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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class AdminOverallSalesReportController implements Initializable {
	
	private Parent root;
	private Stage stage;
	private Scene scene;
	private ObservableList<Booking> bookingList;
	private ObservableList<Report> yearlyReportList;
	private ObservableList<Report> monthlyReportList;
	private String[] type = {"Yearly", "Monthly"};
	private String[] year = {"2021", "2022"};
	private boolean showYearlyReport = true;
	
	@FXML
	private Button btnDashboard, btnAdmin, btnCar, btnCustomerRegistration, btnCustomer, btnInvoice, btnBookingRequest, btnUpcomingBooking, btnBookingHistory, btnReport, btnLog, btnProfile;
	@FXML
	private ImageView iconDashboard, iconAdmin, iconCar, iconCustomerRegistration, iconCustomer, iconInvoice, iconBookingRequest, iconUpcomingBooking, iconBookingHistory, iconReport, iconLog, iconProfile, carImage;
	@FXML
	private TableView<Report> tableYearlyReport, tableMonthlyReport;
	@FXML
	private TableColumn<Report, Integer> colYear, colYearlyBookingNumber, colMonthlyBookingNumber;
	@FXML
	private TableColumn<Report, Double> colYearlySales, colMonthlySales;
	@FXML
	private TableColumn<Report, String> colMonth;
	@FXML
	private ComboBox<String> txtType, txtYear;
	@FXML
	private TextField txtSearch;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Functions.showPlaceholderOnFocus(txtSearch);
		
		// When hover on button change color
		styleNavBar();
		
		// Get booking details from text file
		bookingList = Booking.getBookingDetails("completed");
		
		// Display report details to table
		displayReport();
		
		// Render options for combo box and display prompt text	
		configureComboBox();
				
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
	
	// Render options for combo box and display prompt text	
	public void configureComboBox() {
		showTypeOptions(type);
		showYearOptions(year);
		
		txtType.setValue("Yearly");
		
		txtType.valueProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue.equals("Monthly")) {
				showYearlyReport = false;
				txtYear.setValue("2022");
				txtYear.setVisible(true);
			} else {
				showYearlyReport = true;
				txtYear.setVisible(false);
			}
			displayReport();
		});
		
		txtYear.valueProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.equals(oldValue)) {
				displayReport();
			}
		});
	}
	
	// Set options for combo box
	public void showTypeOptions(String[] type) {
		ObservableList<String> typeList = FXCollections.observableArrayList(type);
        txtType.setItems(typeList);
	}
		
	public void showYearOptions(String[] year) {
		ObservableList<String> yearList = FXCollections.observableArrayList(year);
        txtYear.setItems(yearList);
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

	public void displayReport() {
		if (showYearlyReport) {
			displayYearlyReportList();
		} else {
			displayMonthlyReportList();
		}
	}
	
	public void displayYearlyReportList() {
		tableMonthlyReport.setVisible(false);
		tableYearlyReport.setVisible(true);
		txtYear.setVisible(false);
		try {
			yearlyReportList = Report.getOverallYearlySalesReport(bookingList);
			
			// Populate individual cells in each column
			colYear.setCellValueFactory(new PropertyValueFactory<>("year"));
			colYearlyBookingNumber.setCellValueFactory(new PropertyValueFactory<>("bookingNumber"));
			colYearlySales.setCellValueFactory(new PropertyValueFactory<>("sales"));
			
			// Sets the value of the property items.
			tableYearlyReport.setItems(yearlyReportList);
			configureSearch("yearly");
			txtSearch.setText("");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("Booking text file not found");
		}
    }
	
	public void displayMonthlyReportList() {
		tableYearlyReport.setVisible(false);
		tableMonthlyReport.setVisible(true);
		try {
			int year = Integer.parseInt(txtYear.getValue());
			monthlyReportList = Report.getOverallMonthlySalesReport(bookingList, year);
			
			// Populate individual cells in each column
			colMonth.setCellValueFactory(new PropertyValueFactory<>("month"));
			colMonthlyBookingNumber.setCellValueFactory(new PropertyValueFactory<>("bookingNumber"));
			colMonthlySales.setCellValueFactory(new PropertyValueFactory<>("sales"));
			
			// Sets the value of the property items.
			tableMonthlyReport.setItems(monthlyReportList);
			configureSearch("monthly");
			txtSearch.setText("");
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("Booking text file not found");
		}
    }
	
	public void configureSearch(String type) {
		// Initialize a filter list
		if (type.equals("yearly")) {
			FilteredList<Report> filter = new FilteredList<>(yearlyReportList, condition -> true);
			
			txtSearch.textProperty().addListener((observable, oldText, newText) -> {
				
				// Sets the value of the report predicate
				filter.setPredicate(predicateReportData -> {
	            	
	                if (newText == null || newText.isEmpty()) {
	                    return true;
	                }

	                String searchKeyword = newText.toLowerCase();

	                if (Integer.toString(predicateReportData.getYear()).contains(searchKeyword)) {
	                    return true;
	                } else if (Integer.toString(predicateReportData.getBookingNumber()).contains(searchKeyword)) {
	                    return true;
	                } else if (Double.toString(predicateReportData.getSales()).contains(searchKeyword)) {
	                    return true;
	                } else {
	                    return false;
	                }
	            });
	        });

	        SortedList<Report> sortedList = new SortedList<>(filter);

	        sortedList.comparatorProperty().bind(tableYearlyReport.comparatorProperty());
            tableYearlyReport.setItems(sortedList);
            
		} else if (type.equals("monthly")) {
			FilteredList<Report> filter = new FilteredList<>(monthlyReportList, condition -> true);
			
			txtSearch.textProperty().addListener((observable, oldText, newText) -> {
				
				// Sets the value of the report predicate
				filter.setPredicate(predicateReportData -> {
	            	
	                if (newText == null || newText.isEmpty()) {
	                    return true;
	                }

	                String searchKeyword = newText.toLowerCase();

	                if (predicateReportData.getMonth().toLowerCase().contains(searchKeyword)) {
	                    return true;
	                } else if (Integer.toString(predicateReportData.getBookingNumber()).contains(searchKeyword)) {
	                    return true;
	                } else if (Double.toString(predicateReportData.getSales()).contains(searchKeyword)) {
	                    return true;
	                } else {
	                    return false;
	                }
	            });
	        });

	        SortedList<Report> sortedList = new SortedList<>(filter);

	        sortedList.comparatorProperty().bind(tableMonthlyReport.comparatorProperty());
            tableMonthlyReport.setItems(sortedList);
		}      
	}
	
	// Export to excel
	public void export(ActionEvent event) throws IOException {

		Workbook workbook = new HSSFWorkbook();
		if (tableYearlyReport.isVisible()) {
			Sheet spreadsheet = workbook.createSheet("Report Details");

	        Row row = spreadsheet.createRow(0);
	        
	        for (int j = 0; j < tableYearlyReport.getColumns().size(); j++) {
	            row.createCell(j).setCellValue(tableYearlyReport.getColumns().get(j).getText());
	        }

	        for (int i = 0; i < tableYearlyReport.getItems().size(); i++) {
	            row = spreadsheet.createRow(i + 1);
	            for (int j = 0; j < tableYearlyReport.getColumns().size(); j++) {
	                if(tableYearlyReport.getColumns().get(j).getCellData(i) != null) { 
	                    row.createCell(j).setCellValue(tableYearlyReport.getColumns().get(j).getCellData(i).toString()); 
	                }
	                else {
	                    row.createCell(j).setCellValue("");
	                }   
	            }
	        }

	        FileOutputStream fileOut = new FileOutputStream(System.getProperty("user.home") + "\\Downloads\\Overall_yearly_sales_report.xls");
	        workbook.write(fileOut);
	        fileOut.close();
		} else {
			Sheet spreadsheet = workbook.createSheet("Report Details");

	        Row row = spreadsheet.createRow(0);
	        
	        for (int j = 0; j < tableMonthlyReport.getColumns().size(); j++) {
	            row.createCell(j).setCellValue(tableMonthlyReport.getColumns().get(j).getText());
	        }

	        for (int i = 0; i < tableMonthlyReport.getItems().size(); i++) {
	            row = spreadsheet.createRow(i + 1);
	            for (int j = 0; j < tableMonthlyReport.getColumns().size(); j++) {
	                if(tableMonthlyReport.getColumns().get(j).getCellData(i) != null) { 
	                    row.createCell(j).setCellValue(tableMonthlyReport.getColumns().get(j).getCellData(i).toString()); 
	                }
	                else {
	                    row.createCell(j).setCellValue("");
	                }   
	            }
	        }

	        FileOutputStream fileOut = new FileOutputStream(System.getProperty("user.home") + "\\Downloads\\Overall_monthly_sales_report.xls");
	        workbook.write(fileOut);
	        fileOut.close();
		}
		
        AlertDialog.showInfoMessage(event, "Table exported successfully. Please search for the xls file in your Downloads folder");
	}
}

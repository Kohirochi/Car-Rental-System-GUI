package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class AdminAdminController implements Initializable {
	
	private Parent root;
	private Stage stage;
	private Scene scene;
	private ObservableList<Admin> adminList;

	@FXML
	private Button btnDashboard, btnAdmin, btnCar, btnCustomerRegistration, btnCustomer, btnInvoice, btnBookingRequest, btnUpcomingBooking, btnBookingHistory, btnReport, btnLog, btnProfile;
	@FXML
	private ImageView iconDashboard, iconAdmin, iconCar, iconCustomerRegistration, iconCustomer, iconInvoice, iconBookingRequest, iconUpcomingBooking, iconBookingHistory, iconReport, iconLog, iconProfile, customerImage;
	@FXML
	private TableView<Admin> tableAdmin;
	@FXML
	private TableColumn<Admin, String> colName, colEmail, colPassword, colGender, colContact, colStatus;
	@FXML
	private TableColumn<Admin, Integer> colAdminID;
	@FXML
	private TextField txtAdminID, txtName, txtEmail, txtPassword, txtSearch, txtContact;
	@FXML
	private ComboBox<String> txtStatus, txtGender;
	@FXML
	private Label nameError, emailError, passwordError, contactError, statusError;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Functions.showPlaceholderOnFocus(txtSearch);
		
		// When hover on button change color
		styleNavBar();
		
		// Display car details to table
		displayAdminList();
		
		// Limit user input
		configureTextField();
		
		// Render options for combo box and display prompt text			
		configureComboBox();
	}
	
	// Limit user input using regex	
	public void configureTextField() {
		Functions.limitTextFieldInput(txtName, "[^a-zA-Z\\s]");  // Alphabets and space only
	}
	
	// Render options for combo box and display prompt text	
	public void configureComboBox() {
		Functions.configureGenderComboBox(txtGender);
		Functions.configureStatusComboBox(txtStatus);
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

	public void displayAdminList() {
		adminList = Admin.getAdminsCredentials();
		
		// Populate individual cells in each column
		colAdminID.setCellValueFactory(new PropertyValueFactory<>("ID"));
		colName.setCellValueFactory(new PropertyValueFactory<>("name"));
		colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
		colPassword.setCellValueFactory(new PropertyValueFactory<>("password"));
		colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
		colGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
		colContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
		colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));	

		// Sets the value of the property items.			
		tableAdmin.setItems(adminList);
		
		// Add listener to filter search result
		search();
    }
	
	public void search() {
		// Initialize a filter list
		FilteredList<Admin> filter = new FilteredList<>(adminList, condition -> true);
		
		txtSearch.textProperty().addListener((observable, oldText, newText) -> {
			
			// Sets the value of the customer predicate
            filter.setPredicate(predicateAdminData -> {
            	
                if (newText == null || newText.isEmpty()) {
                    return true;
                }

                String searchKeyword = newText.toLowerCase();

                if (Integer.toString(predicateAdminData.getID()).contains(searchKeyword)) {
                    return true;
                } else if (predicateAdminData.getName().toLowerCase().contains(searchKeyword)) {
                    return true;
                } else if (predicateAdminData.getEmail().toLowerCase().contains(searchKeyword)) {
                    return true;
                } else if (predicateAdminData.getPassword().toLowerCase().contains(searchKeyword)) {
                    return true;
                } else if (predicateAdminData.getGender().toLowerCase().contains(searchKeyword)) {
                    return true;
                } else if (predicateAdminData.getContact().toLowerCase().contains(searchKeyword)) {
                    return true;
                } else if (predicateAdminData.getStatus().toLowerCase().contains(searchKeyword)) {
                    return true;
                } else {
                    return false;
                }
            });
        });

        SortedList<Admin> sortedList = new SortedList<>(filter);
        sortedList.comparatorProperty().bind(tableAdmin.comparatorProperty());
        tableAdmin.setItems(sortedList);
	}
	
	public void showSelectedRow() {
		Admin admin = tableAdmin.getSelectionModel().getSelectedItem();
		int num = tableAdmin.getSelectionModel().getSelectedIndex();
		if (num < 0) return;
		
		txtAdminID.setText(String.valueOf(admin.getID()));
		txtName.setText(String.valueOf(admin.getName()));
		txtEmail.setText(admin.getEmail());
		txtPassword.setText(admin.getPassword());
		txtStatus.setValue(admin.getStatus());
		
		if (!admin.getGender().equals("-")) {
			txtGender.setValue(admin.getGender());
		} else {
			txtGender.setValue(null);
		}
		if (!admin.getContact().equals("-")) {
			txtContact.setText(admin.getContact());
		} else {
			txtContact.setText("");
		}
	}
	
	// Filter the file extensions that users can upload
	public void setExtensionFilter(FileChooser file) {
		file.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("PNG Files", "*.png"),
				new FileChooser.ExtensionFilter("JPG Files", "*.jpg"),	
				new FileChooser.ExtensionFilter("JPEG Files", "*.jpeg")
		);
	}
	
	// Open file chooser for user to choose
	public void openFile(ActionEvent event) {
		FileChooser fc = new FileChooser();
		setExtensionFilter(fc);
		File selectedFile = fc.showOpenDialog(null);
		if (selectedFile != null) {
			// Get image file path
			String imagePath = selectedFile.getAbsolutePath();
			
			// Set image as image view
			customerImage.setImage(new Image(imagePath));
		}
	}
	
	// Clear button
	public void clear() {
		// TextField 
		txtAdminID.setText("");
		txtName.setText("");
		txtEmail.setText("");
		txtPassword.setText("");
		txtContact.setText("");
		
		// ComboBox
		txtStatus.setValue(null);
		txtGender.setValue(null);
		
		// Label Error
		nameError.setVisible(false);
		emailError.setVisible(false);
		passwordError.setVisible(false);
		statusError.setVisible(false);
	}
	
	// Add button
	public void add(ActionEvent event) throws IOException {
		String name = txtName.getText().trim();
		String email = txtEmail.getText().trim();
		String password = txtPassword.getText().trim();
		String gender =  txtGender.getValue();
		String contact = txtContact.getText().trim();
		String status = txtStatus.getValue();
		
		// Validate admin details
		if (Validation.validateBlank(name, nameError) &
			Validation.validateRegistrationEmail(email, emailError) &
			Validation.validatePassword(password, passwordError) &
			Validation.validateCustomerContact(contact, contactError) &
			Validation.validateComboBox(status, statusError)) {
			//Replace with dash if no value
			gender = gender == null ? "-" : gender;
			contact = contact.isBlank() ? "-" : contact;
			
			Log log = new Log();
			if (Admin.addNewAdmin(name, email, password, gender, contact, status, log)) {
				AlertDialog.showInfoMessage(event, "Add successful");
				// Set log details and add to log text file
				log.setUserID(Session.admin.getID());
				log.setType("Admin");
				log.setAction("Add");
				
				log.addNewLog(log);
				
				clear();
				displayAdminList();
			} else {
				AlertDialog.showErrorMessage(event, "Add unsuccessful");
			}
		}
	}
	
	// Update button
	public void update(ActionEvent event) throws IOException {
		String ID = txtAdminID.getText().trim();
		if (ID.isBlank()) {
			AlertDialog.showErrorMessage(event, "Please select a row before updating");
			return;
		}
		
		String name = txtName.getText().trim();
		String email = txtEmail.getText().trim();
		String password = txtPassword.getText().trim();
		String gender =  txtGender.getValue();
		String contact = txtContact.getText().trim();
		String status = txtStatus.getValue();
		
		if (Validation.validateBlank(name, nameError) &
			Validation.validateUpdateEmail(ID, email, emailError) &
			Validation.validatePassword(password, passwordError) &
			Validation.validateCustomerContact(contact, contactError) &
			Validation.validateComboBox(status, statusError)) {
			
			//Replace with dash if no value
			gender = gender == null ? "-" : gender;
			contact = contact.isBlank() ? "-" : contact;
			
			// Update admin credentials into file
			Log log = new Log();
			if (Admin.updateAdminCredentials(ID, name, email, password, gender, contact, status, log)) {
				AlertDialog.showInfoMessage(event, "Update successful");
				// Set log details and add to log text file
				log.setUserID(Session.admin.getID());
				log.setType("Admin");
				log.setTargetID(Integer.parseInt(ID));
				log.setAction("Update");
				
				log.addNewLog(log);
				
				clear();
				displayAdminList();
			} else {
				AlertDialog.showErrorMessage(event, "Update unsuccessful");
			}
		}
	}
	
	// Delete button
	public void delete(ActionEvent event) throws IOException {
		String ID = txtAdminID.getText().trim();
		if (ID.isBlank()) {
			AlertDialog.showErrorMessage(event, "Please select a row before deleting");
			return;
		}
		
		Log log = new Log();
		if (Admin.deleteAdmin(ID, log)) {
			AlertDialog.showInfoMessage(event, "Delete successful");
			// Set log details and add to log text file
			log.setUserID(Session.admin.getID());
			log.setType("Admin");
			log.setTargetID(Integer.parseInt(ID));
			log.setAction("Delete");
			
			log.addNewLog(log);
			
			clear();
			displayAdminList();
		} else {
			AlertDialog.showErrorMessage(event, "Delete unsuccessful");
		}
	}
	
	// Export to excel
	public void export(ActionEvent event) throws IOException {
		Workbook workbook = new HSSFWorkbook();
		Sheet spreadsheet = workbook.createSheet("Customer Details");

        Row row = spreadsheet.createRow(0);

        for (int j = 0; j < tableAdmin.getColumns().size(); j++) {
            row.createCell(j).setCellValue(tableAdmin.getColumns().get(j).getText());
        }

        for (int i = 0; i < tableAdmin.getItems().size(); i++) {
            row = spreadsheet.createRow(i + 1);
            for (int j = 0; j < tableAdmin.getColumns().size(); j++) {
                if(tableAdmin.getColumns().get(j).getCellData(i) != null) { 
                    row.createCell(j).setCellValue(tableAdmin.getColumns().get(j).getCellData(i).toString()); 
                }
                else {
                    row.createCell(j).setCellValue("");
                }   
            }
        }

        FileOutputStream fileOut = new FileOutputStream(System.getProperty("user.home") + "\\Downloads\\Admin_list.xls");
        workbook.write(fileOut);
        fileOut.close();
        AlertDialog.showInfoMessage(event, "Table exported successfully. Please search for the xls file in your Downloads folder");
	}
}


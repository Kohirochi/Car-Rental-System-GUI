package application;

import java.io.File;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class AdminCarController implements Initializable {
	
	private Parent root;
	private Stage stage;
	private Scene scene;
	private ObservableList<Car> carList;
	private int yearRange = 10;
	private String[] bodyType = {"Sedan", "SUV", "MPV", "Hatchback"};
	private String[] transmission = {"Automatic", "Manual"};
	private String[] fuelType = {"Petrol", "Diesel"};
	private String[] status = {"Active", "Inactive"};
	
	@FXML
	private Button btnDashboard, btnAdmin, btnCar, btnCustomerRegistration, btnCustomer, btnInvoice, btnBookingRequest, btnUpcomingBooking, btnBookingHistory, btnReport, btnLog, btnProfile;
	@FXML
	private ImageView iconDashboard, iconAdmin, iconCar, iconCustomerRegistration, iconCustomer, iconInvoice, iconBookingRequest, iconUpcomingBooking, iconBookingHistory, iconReport, iconLog, iconProfile, carImage;
	@FXML
	private TableView<Car> tableCar;
	@FXML
	private TableColumn<Car, String> colBrand, colModel, colPlateNumber, colColour, colBodyType, colTransmission, colFuelType, colStatus;
	@FXML
	private TableColumn<Car, Integer> colCarID, colYear, colSeats;
	@FXML
	private TableColumn<Car, Double> colPrice, colEngineCapacity;
	@FXML
	private TextField txtCarID, txtBrand, txtModel, txtPlateNumber, txtColour, txtEngineCapacity, txtSeats, txtPrice, txtSearch;
	@FXML
	private ComboBox<String> txtYear, txtBodyType, txtTransmission, txtFuelType, txtStatus;
	@FXML
	private Label insertImageName, yearError, brandError, modelError, plateNumberError, colourError, bodyTypeError, transmissionError, fuelTypeError, engineCapacityError, seatsError, priceError, statusError, insertImageError;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Functions.showPlaceholderOnFocus(txtSearch);
		
		// When hover on button change color
		styleNavBar();
		
		// Display car details to table
		displayCarList();
		
		// Limit user input
		configureTextField();
		
		// Render options for combo box and display prompt text			
		configureComboBox();
	}
	
	// Limit user input using regex	
	public void configureTextField() {
		Functions.limitTextFieldInput(txtSeats, "\\D");  // Numbers only
		Functions.limitTextFieldInput(txtPrice, "[^\\d\\.]");  // Numbers and dots only
		Functions.limitTextFieldInput(txtEngineCapacity, "[^\\d\\.]");  // Numbers and dots only
		Functions.limitTextFieldInput(txtColour, "[^a-zA-Z\\s]");  // Alphabets and space only
		Functions.limitTextFieldInput(txtPlateNumber, "[^a-zA-Z\\s0-9]");  // Alphabets, numbers and space only
	}
	
	// Render options for combo box and display prompt text	
	public void configureComboBox() {
		showYearOptions(yearRange);
		txtYear.setButtonCell(new PromptButtonCell<>("Please select"));
		showBodyTypeOptions(bodyType);
		txtBodyType.setButtonCell(new PromptButtonCell<>("Please select"));
		showTransmissionOptions(transmission);
		txtTransmission.setButtonCell(new PromptButtonCell<>("Please select"));
		showFuelTypeOptions(fuelType);
		txtFuelType.setButtonCell(new PromptButtonCell<>("Please select"));
		showStatusOptions(status);
		txtStatus.setButtonCell(new PromptButtonCell<>("Please select"));
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

	public void displayCarList() {
		try {
			carList = Car.getCarsDetails();
			
			// Populate individual cells in each column
			colCarID.setCellValueFactory(new PropertyValueFactory<>("ID"));
			colYear.setCellValueFactory(new PropertyValueFactory<>("year"));
			colBrand.setCellValueFactory(new PropertyValueFactory<>("brand"));
			colModel.setCellValueFactory(new PropertyValueFactory<>("model"));
			colPlateNumber.setCellValueFactory(new PropertyValueFactory<>("plateNumber"));
			colColour.setCellValueFactory(new PropertyValueFactory<>("colour"));
			colBodyType.setCellValueFactory(new PropertyValueFactory<>("bodyType"));
			colTransmission.setCellValueFactory(new PropertyValueFactory<>("transmission"));
			colFuelType.setCellValueFactory(new PropertyValueFactory<>("fuelType"));
			colEngineCapacity.setCellValueFactory(new PropertyValueFactory<>("engineCapacity"));
			colSeats.setCellValueFactory(new PropertyValueFactory<>("seats"));
			colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
			colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
			
			// Sets the value of the property items.
			tableCar.setItems(carList);
			
			// Add listener to filter search result
			search();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("Car text file not found");
		}
    }
	
	public void search() {
		// Initialize a filter list
		FilteredList<Car> filter = new FilteredList<>(carList, condition -> true);
		
		txtSearch.textProperty().addListener((observable, oldText, newText) -> {
			
			// Sets the value of the car predicate
            filter.setPredicate(predicateCarData -> {
            	
                if (newText == null || newText.isEmpty()) {
                    return true;
                }

                String searchKeyword = newText.toLowerCase();

                if (Integer.toString(predicateCarData.getID()).contains(searchKeyword)) {
                    return true;
                } else if (Integer.toString(predicateCarData.getYear()).toLowerCase().contains(searchKeyword)) {
                    return true;
                } else if (predicateCarData.getBrand().toLowerCase().contains(searchKeyword)) {
                    return true;
                } else if (predicateCarData.getModel().toLowerCase().contains(searchKeyword)) {
                    return true;
                } else if (predicateCarData.getColour().toLowerCase().contains(searchKeyword)) {
                    return true;
                } else if (predicateCarData.getPlateNumber().toLowerCase().contains(searchKeyword)) {
                    return true;
                } else if (predicateCarData.getBodyType().toLowerCase().contains(searchKeyword)) {
                    return true;
                } else if (predicateCarData.getTransmission().toString().toLowerCase().contains(searchKeyword)) {
                    return true;
                } else if (predicateCarData.getFuelType().toString().toLowerCase().contains(searchKeyword)) {
                    return true;
                } else if (Double.toString(predicateCarData.getEngineCapacity()).contains(searchKeyword)) {
                    return true;
                } else if (Integer.toString(predicateCarData.getSeats()).contains(searchKeyword)) {
                    return true;
                } else if (Double.toString(predicateCarData.getPrice()).contains(searchKeyword)) {
                    return true;
                } else if (predicateCarData.getStatus().toLowerCase().contains(searchKeyword)) {
                    return true;
                } else {
                    return false;
                }
            });
        });

        SortedList<Car> sortedList = new SortedList<>(filter);

        sortedList.comparatorProperty().bind(tableCar.comparatorProperty());
        tableCar.setItems(sortedList);
	}
	
	public void showSelectedRow() {
		Car car = tableCar.getSelectionModel().getSelectedItem();
		int num = tableCar.getSelectionModel().getSelectedIndex();
		if (num < 0) return;
		
		txtCarID.setText(String.valueOf(car.getID()));
		txtYear.setValue(String.valueOf(car.getYear()));
		txtBrand.setText(car.getBrand());
		txtModel.setText(car.getModel());
		txtPlateNumber.setText(car.getPlateNumber());
		txtColour.setText(car.getColour());
		txtBodyType.setValue(car.getBodyType());
		txtFuelType.setValue(car.getFuelType());
		txtTransmission.setValue(car.getTransmission());
		txtEngineCapacity.setText(String.valueOf(car.getEngineCapacity()));
		txtSeats.setText(String.valueOf(car.getSeats()));
		txtPrice.setText(String.valueOf(car.getPrice()));
		txtStatus.setValue(car.getStatus());
		
		String imagePath = "file:./src/main/resources/image/car/" + car.getImageName();
		Image image = new Image(imagePath, 200, 122, false, true);
		carImage.setImage(image);
	}
	
	// Set options for combo box
	public void showYearOptions(int yearRange) {
		ObservableList<String> yearList = FXCollections.observableArrayList(Functions.generateYearList(yearRange));
        txtYear.setItems(yearList);
	}
	
	public void showBodyTypeOptions(String[] bodyType) {
		ObservableList<String> bodyTypeList = FXCollections.observableArrayList(bodyType);
        txtBodyType.setItems(bodyTypeList);
	}
	
	public void showTransmissionOptions(String[] transmission) {
		ObservableList<String> transmissionList = FXCollections.observableArrayList(transmission);
        txtTransmission.setItems(transmissionList);
	}
	
	public void showFuelTypeOptions(String[] fuelType) {
		ObservableList<String> fuelTypeList = FXCollections.observableArrayList(fuelType);
        txtFuelType.setItems(fuelTypeList);
	}
	
	public void showStatusOptions(String[] status) {
		ObservableList<String> statusList = FXCollections.observableArrayList(status);
        txtStatus.setItems(statusList);
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
			String imageFileName = selectedFile.getName();
			
			// Show user the chosen file name
			insertImageName.setText(imageFileName);
			insertImageName.setVisible(true);
			
			// Get image file path
			String imagePath = selectedFile.getAbsolutePath();
			
			// Set image as image view
			carImage.setImage(new Image(imagePath));
		}
	}
	

	
	// Clear button
	public void clear() {
		// TextField 
		txtCarID.setText("");
		txtBrand.setText("");
		txtModel.setText("");
		txtPlateNumber.setText("");
		txtColour.setText("");
		txtEngineCapacity.setText("");
		txtSeats.setText("");
		txtPrice.setText("");
		
		// ComboBox
		txtYear.setValue(null);
		txtBodyType.setValue(null);
		txtTransmission.setValue(null);
		txtFuelType.setValue(null);
		txtStatus.setValue(null);
		
		// ImageView
		carImage.setImage(new Image("file:./src/main/resources/image/noImage.png"));
		
		// Label Error
		yearError.setVisible(false);
		brandError.setVisible(false);
		modelError.setVisible(false);
		colourError.setVisible(false);
		bodyTypeError.setVisible(false);
		transmissionError.setVisible(false);
		fuelTypeError.setVisible(false);
		engineCapacityError.setVisible(false);
		seatsError.setVisible(false);
		priceError.setVisible(false);
		statusError.setVisible(false);
		insertImageError.setVisible(false);
	}
	
	// Add button
	public void add(ActionEvent event) throws IOException {
		String year = txtYear.getValue();
		String brand = txtBrand.getText().trim();
		String model = txtModel.getText().trim();
		String plateNumber = txtPlateNumber.getText().trim();
		String colour = txtColour.getText().trim();
		String bodyType = txtBodyType.getValue();
		String fuelType = txtFuelType.getValue();
		String engineCapacity = txtEngineCapacity.getText().trim();
		String transmission = txtTransmission.getValue();
		String seats = txtSeats.getText().trim();
		String price = txtPrice.getText().trim();
		String status = txtStatus.getValue();
		String imageName = Functions.getImageName(carImage);
		imageName = imageName.equals("noImage.png") ? "" : imageName;
		
		// Validate car details
		if (Validation.validateComboBox(year, yearError) &
			Validation.validateComboBox(bodyType, bodyTypeError) &
			Validation.validateComboBox(fuelType, fuelTypeError) &
			Validation.validateComboBox(transmission, transmissionError) &
			Validation.validateComboBox(status, statusError) &
			Validation.validateBlank(brand, brandError) &
			Validation.validateBlank(model, modelError) &
			Validation.validateNewPlateNumber(plateNumber, plateNumberError) &
			Validation.validateBlank(colour, colourError) &
			Validation.validateBlank(seats, seatsError) &
			Validation.validateBlank(imageName, insertImageError, "Please insert an image") &
			Validation.validateDouble(price, priceError) &
			Validation.validateDouble(engineCapacity, engineCapacityError)) {
			
			// Limit the number of decimal places
			Functions.limitDecimalPlaces(engineCapacity, 1);
			Functions.limitDecimalPlaces(price, 2);
			
			// Create new line to be inserted into file
			String carDetails = String.join("|", year, brand, model, plateNumber, colour, bodyType, transmission, fuelType, engineCapacity, seats, price, status, imageName);
			
			Log log = new Log();
			if (Admin.addNewCar(carDetails, carImage.getImage(), insertImageName, log)) {
				AlertDialog.showInfoMessage(event, "Add successful");
				// Set log details and add to log text file
				log.setUserID(Session.admin.getID());
				log.setType("Car");
				log.setAction("Add");
				
				log.addNewLog(log);
				
				clear();
				displayCarList();
			} else {
				AlertDialog.showErrorMessage(event, "Add unsuccessful");
			}
		}
	}
	
	// Update button
	public void update(ActionEvent event) throws IOException {
		String ID = txtCarID.getText().trim();
		if (ID.isBlank()) {
			AlertDialog.showErrorMessage(event, "Please select a row before updating");
			return;
		}
		
		String year = txtYear.getValue();
		String brand = txtBrand.getText().trim();
		String model = txtModel.getText().trim();
		String plateNumber = txtPlateNumber.getText().trim();
		String colour = txtColour.getText().trim();
		String bodyType = txtBodyType.getValue();
		String fuelType = txtFuelType.getValue();
		String engineCapacity = txtEngineCapacity.getText().trim();
		String transmission = txtTransmission.getValue();
		String seats = txtSeats.getText().trim();
		String price = txtPrice.getText().trim();
		String status = txtStatus.getValue();
		String imageName = Functions.getImageName(carImage);
		imageName = imageName.equals("noImage.png") ? "" : imageName;
		
		// Validate car details
		if (Validation.validateComboBox(year, yearError) &
			Validation.validateComboBox(bodyType, bodyTypeError) &
			Validation.validateComboBox(fuelType, fuelTypeError) &
			Validation.validateComboBox(transmission, transmissionError) &
			Validation.validateComboBox(status, statusError) &
			Validation.validateBlank(brand, brandError) &
			Validation.validateBlank(model, modelError) &
			Validation.validateUpdatePlateNumber(ID, plateNumber, plateNumberError) &
			Validation.validateBlank(colour, colourError) &
			Validation.validateBlank(seats, seatsError) &
			Validation.validateBlank(imageName, insertImageError, "Please insert an image") &
			Validation.validateDouble(price, priceError) &
			Validation.validateDouble(engineCapacity, engineCapacityError)) {
			
			// Limit the number of decimals
			Functions.limitDecimalPlaces(engineCapacity, 1);
			Functions.limitDecimalPlaces(price, 2);
			
			// Create new line to be insert into the file
			String carDetails = String.join("|", ID, year, brand, model, plateNumber, colour, bodyType, transmission, fuelType, engineCapacity, seats, price, status, imageName);
			
			Log log = new Log();
			if (Car.updateCarDetails(ID, carDetails, carImage.getImage(), insertImageName, log)) {
				AlertDialog.showInfoMessage(event, "Update successful");
				// Set log details and add to log text file
				log.setUserID(Session.admin.getID());
				log.setType("Car");
				log.setTargetID(Integer.parseInt(ID));
				log.setAction("Update");
				
				log.addNewLog(log);
				
				clear();
				displayCarList();
			} else {
				AlertDialog.showErrorMessage(event, "Update unsuccessful");
			}
		}
	}
	
	// Delete button
	public void delete(ActionEvent event) throws IOException {
		String ID = txtCarID.getText().trim();
		if (ID.isBlank()) {
			AlertDialog.showErrorMessage(event, "Please select a row before deleting");
			return;
		}
		
		Log log = new Log();
		if (Admin.deleteCar(ID, log)) {
			AlertDialog.showInfoMessage(event, "Delete successful");
			// Set log details and add to log text file
			log.setUserID(Session.admin.getID());
			log.setType("Car");
			log.setTargetID(Integer.parseInt(ID));
			log.setAction("Delete");
			
			log.addNewLog(log);
			
			clear();
			displayCarList();
		} else {
			AlertDialog.showErrorMessage(event, "Delete unsuccessful");
		}
	}
	
	// Export to excel
	public void export(ActionEvent event) throws IOException {
		Workbook workbook = new HSSFWorkbook();
		Sheet spreadsheet = workbook.createSheet("Car Details");

        Row row = spreadsheet.createRow(0);

        for (int j = 0; j < tableCar.getColumns().size(); j++) {
            row.createCell(j).setCellValue(tableCar.getColumns().get(j).getText());
        }

        for (int i = 0; i < tableCar.getItems().size(); i++) {
            row = spreadsheet.createRow(i + 1);
            for (int j = 0; j < tableCar.getColumns().size(); j++) {
                if(tableCar.getColumns().get(j).getCellData(i) != null) { 
                    row.createCell(j).setCellValue(tableCar.getColumns().get(j).getCellData(i).toString()); 
                }
                else {
                    row.createCell(j).setCellValue("");
                }   
            }
        }

        FileOutputStream fileOut = new FileOutputStream(System.getProperty("user.home") + "\\Downloads\\Car_list.xls");
        workbook.write(fileOut);
        fileOut.close();
        AlertDialog.showInfoMessage(event, "Table exported successfully. Please search for the xls file in your Downloads folder");
	}
}

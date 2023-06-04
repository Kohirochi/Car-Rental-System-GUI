package application;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CarDetailsController implements Initializable{
	private Parent root;
	private Stage stage;
	private Scene scene;
	public boolean loginStatus = Session.status;
	public Customer customer = Session.customer;
	public int carID;
	
	@FXML
	private ImageView logo;
	@FXML
    private HBox customerHBox;
	@FXML
	private Button btnLogin;
	@FXML
	private Hyperlink btnRegister;
	@FXML
	private Label customerName;
	@FXML
	private VBox dropdownMenu;
    @FXML
    private ImageView carImage;
    @FXML
    private Label txtBodyType, txtColour, txtEngineCapacity, txtPrice, txtFuelType, txtName, txtTransmission, txtSeats;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Tooltip.install(logo, new Tooltip("Home"));
		
		// Check if user login to display different header
		configureHeader();
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
		Car car = Car.getSpecificCarDetails(carID);
		
		this.carID = carID;
		
		carImage.setImage(new Image("file:./src/main/resources/image/car/" + car.getImageName()));
		txtName.setText(String.join(" ", Integer.toString(car.getYear()), car.getBrand(), car.getModel()));
		txtPrice.setText("RM " + car.getPrice());
		txtColour.setText(car.getColour());
		txtBodyType.setText(car.getBodyType());
		txtTransmission.setText(car.getTransmission());
		txtFuelType.setText(car.getFuelType());
		txtEngineCapacity.setText(Double.toString(car.getEngineCapacity()) + "L");
		txtSeats.setText(Integer.toString(car.getSeats()));
	}
	
	// Book button
	public void bookCar(ActionEvent event) throws IOException { 
		// Check if customer login
		if (!Session.checkIfLogin(event, loginStatus)) {
			Functions.switchScene(event, root, stage, scene, "Login.fxml");
		} else {
			FXMLLoader loader = new FXMLLoader();
		    loader.setLocation(getClass().getResource("BookingDetails.fxml"));
		    root = loader.load();
		    scene = new Scene(root);
		    
		    // Pass specific car ID to car detail page
		    BookingDetailsController controller = loader.getController();
		    controller.loadCarDetails(carID);
		    
		    stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
			stage.setScene(scene);
			stage.show();			
		}
		
	}
	
}

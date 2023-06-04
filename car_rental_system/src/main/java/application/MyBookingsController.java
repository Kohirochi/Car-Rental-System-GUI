package application;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

public class MyBookingsController implements Initializable{
	private Parent root;
	private Stage stage;
	private Scene scene;
	public boolean loginStatus = Session.status;
	public Customer customer = Session.customer;
	
	@FXML
	private GridPane bookingGrid;
	@FXML
	private ImageView logo;
	@FXML
    private HBox customerHBox;
	@FXML
	private Label customerName, upcoming, awaiting, completed, rejectedAndCancelled;
	@FXML
	private VBox dropdownMenu;
	@FXML
	private Line upcomingLine, awaitingLine, completedLine, rejectedAndCancelledLine;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Tooltip.install(logo, new Tooltip("Home"));
		
		// Check if user login to display different header
		configureHeader();
		// Change color and font weight of booking header
		setActiveBookingHeader(Session.showCustomerBooking);
		// Display all upcoming booking
		loadBookingGrid(Session.showCustomerBooking);
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
	
	public void loadBookingGrid(String type) {
		// Clear element in grid pane
		bookingGrid.getChildren().clear();
		
		int column = 0;
		int row = 1;
		
		try {
			ObservableList<Booking> bookingList = Booking.getBookingDetails(type);
			ObservableList<BookingThumb> BookingThumbList = BookingThumb.getBookingThumbs(bookingList, customer.getID());
			
			for (BookingThumb bookingThumb : BookingThumbList) {
				FXMLLoader fxmlLoader = new FXMLLoader();
				fxmlLoader.setLocation(getClass().getResource("BookingThumb.fxml"));
				HBox box = fxmlLoader.load();
				BookingThumbController bookingThumbController = fxmlLoader.getController();
				bookingThumbController.setData(bookingThumb);
				
				bookingGrid.add(box, column, row++);
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("File not found");
		}
	}
	
	public void setActiveBookingHeader(String bookingType) {
		setUnactiveColour(upcoming, upcomingLine);
		setUnactiveColour(awaiting, awaitingLine);
		setUnactiveColour(completed, completedLine);
		setUnactiveColour(rejectedAndCancelled, rejectedAndCancelledLine);
		
		if (bookingType.equals("upcoming")) {
			setActiveColour(upcoming, upcomingLine);
		} else if (bookingType.equals("pending")) {
			setActiveColour(awaiting, awaitingLine);
		} else if (bookingType.equals("completed")) {
			setActiveColour(completed, completedLine);
		} else if (bookingType.equals("rejected/cancelled")) {
			setActiveColour(rejectedAndCancelled, rejectedAndCancelledLine);
		}
	}
	
	public void setActiveColour(Label label, Line line) { 
		line.setStroke(Color.web("#00437a"));
		label.setStyle("-fx-font-weight: bold; -fx-text-fill: #00437a;");
	}
	
	public void setUnactiveColour(Label label, Line line) { 
		line.setStroke(Color.web("#FFFFFF"));
		label.setStyle("-fx-font-weight: 400; -fx-text-fill: #000000;");
	}
	
	public void toUpcoming() {
		Session.showCustomerBooking = "upcoming";
		setActiveBookingHeader("upcoming");
		loadBookingGrid("upcoming");
	}
	
	public void toAwaitingRequest() {
		Session.showCustomerBooking = "pending";
		setActiveBookingHeader("pending");
		loadBookingGrid("pending");
	}
	
	public void toCompleted() {
		Session.showCustomerBooking = "completed";
		setActiveBookingHeader("completed");
		loadBookingGrid("completed");
	}
	
	public void toRejectedAndCancelled() {
		Session.showCustomerBooking = "rejected/cancelled";
		setActiveBookingHeader("rejected/cancelled");
		loadBookingGrid("rejected/cancelled");
	}
}

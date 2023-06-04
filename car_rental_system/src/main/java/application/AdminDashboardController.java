package application;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class AdminDashboardController implements Initializable {
	
	private Parent root;
	private Stage stage;
	private Scene scene;
	
	@FXML
	private Button btnDashboard, btnCar, btnAdmin, btnCustomerRegistration, btnCustomer, btnInvoice, btnBookingRequest, btnUpcomingBooking, btnBookingHistory, btnReport, btnLog, btnProfile;
	@FXML
	private ImageView iconDashboard, iconCar, iconAdmin, iconCustomerRegistration, iconCustomer, iconInvoice, iconBookingRequest, iconUpcomingBooking, iconBookingHistory, iconReport, iconLog, iconProfile;
	@FXML
	private Label txtRevenue, txtBookings, txtCars, txtCustomers;
	@FXML
	private LineChart<String, Double> revenueChart;
	@FXML
	private BarChart<Integer, String> brandChart;
	@FXML
	private PieChart genderChart, bodyTypeChart;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		styleNavBar();
		loadDashboard();
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
	
	// Load dashboard 
	public void loadDashboard() {
		txtRevenue.setText("$" + Booking.getYearToDateRevenue());
		txtBookings.setText(Integer.toString(Booking.getYearToDateBookings()));
		txtCars.setText(Integer.toString(Car.getTotalCars()));
		txtCustomers.setText(Integer.toString(Customer.getTotalCustomers()));
		
		plotRevenueChart();
		plotBrandChart();
		plotGenderChart();
		plotBodyTypeChart();
	}
	
	// Fill revenue chart with data
	public void plotRevenueChart() {
		String[] monthList = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
		double[] currentYearRevenueList = Booking.getMonthlyRevenue(LocalDate.now().getYear());
		double[] previousYearRevenueList = Booking.getMonthlyRevenue(LocalDate.now().getYear() - 1);
		
		XYChart.Series<String, Double> currentYearSeries = new XYChart.Series<String, Double>();
		XYChart.Series<String, Double> previousYearSeries = new XYChart.Series<String, Double>();
		
		for (int i = 0; i < monthList.length; i++) {
			currentYearSeries.getData().add(new XYChart.Data<String, Double>(monthList[i], currentYearRevenueList[i]));
		}
		currentYearSeries.setName(Integer.toString(LocalDate.now().getYear()) + "   ");
		
		for (int i = 0; i < monthList.length; i++) {
			previousYearSeries.getData().add(new XYChart.Data<String, Double>(monthList[i], previousYearRevenueList[i]));
		}
		previousYearSeries.setName(Integer.toString(LocalDate.now().getYear() - 1));
		
		for (int i = 0; i < monthList.length; i++) {
			previousYearSeries.getData().add(new XYChart.Data<String, Double>(monthList[i], previousYearRevenueList[i]));
		}
		
		revenueChart.getData().addAll(previousYearSeries, currentYearSeries);
	}
	
	// Fill brand chart with data
	public void plotBrandChart() {
		ArrayList<String> brandList = Car.getBrandList();
		int[] bookingList = Booking.getBookingsByBrand(brandList);
		
		XYChart.Series<Integer, String> series = new XYChart.Series<Integer, String>();
		
		for (int i = 0; i < brandList.size(); i++) {
			series.getData().add(new XYChart.Data<Integer, String>(bookingList[i], brandList.get(i)));
		}
		brandChart.getData().add(series);
	}
	
	// Fill gender chart with data
	public void plotGenderChart() {
		int[] genderDistributionList = Booking.getGenderDistribution();
				
		PieChart.Data slice1 = new PieChart.Data("Male", genderDistributionList[0]);
		PieChart.Data slice2 = new PieChart.Data("Female"  , genderDistributionList[1]);
		
		genderChart.getData().addAll(slice1, slice2);
	}
	
	// Fill body type chart with data
	public void plotBodyTypeChart() {
		int[] bodyTypeDistributionList = Booking.getBodyTypeDistribution();
		
		PieChart.Data slice1 = new PieChart.Data("Sedan", bodyTypeDistributionList[0]);
		PieChart.Data slice2 = new PieChart.Data("SUV"  , bodyTypeDistributionList[1]);
		PieChart.Data slice3 = new PieChart.Data("MPV", bodyTypeDistributionList[2]);
		PieChart.Data slice4 = new PieChart.Data("Hatchback"  , bodyTypeDistributionList[3]);
		
		bodyTypeChart.getData().addAll(slice1, slice2, slice3, slice4);
	}
}

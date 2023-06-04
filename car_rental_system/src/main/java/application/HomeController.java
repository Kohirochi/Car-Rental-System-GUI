package application;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.function.Predicate;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HomeController implements Initializable {
	private Parent root;
	private Stage stage;
	private Scene scene;
	public boolean loginStatus = Session.status;
	public Customer customer = Session.customer;
	
	private ArrayList<String> yearKeywords = new ArrayList<>();
	private ArrayList<String> brandKeywords = new ArrayList<>();
	private ArrayList<String> bodyTypeKeywords = new ArrayList<>();
	private ArrayList<String> priceKeywords = new ArrayList<>();
	private ArrayList<String> transmissionKeywords = new ArrayList<>();
	private ArrayList<String> fuelTypeKeywords = new ArrayList<>();
	
	@FXML
	private GridPane carGrid;
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
	private VBox dropdownMenu, VBoxYear, VBoxBrand;
	@FXML
	private CheckBox cbSedan, cbSUV, cbMPV, cbHatchback, cbRM0to200, cbRM200to400, cbRM400to600, cbRM600above, cbManual, cbAutomatic, cbPetrol, cbDiesel;
	
	public ObservableList<CheckBox> checkBoxes = FXCollections.observableArrayList();
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Tooltip.install(logo, new Tooltip("Home"));
		
		// Check if user login to display different header
		configureHeader();
		
		// Display all cars
		loadCarToGridPane();

		// Display all year checkboxes
		loadYearCheckbox();

		// Display all brand checkboxes
		loadBrandCheckbox();

		// Add event to all predefined checkboxes
		configureCheckbox();
	}
	
	// Display all cars
	public void loadCarToGridPane() {
		int column = 0;
		int row = 1;
		
		try {
			ObservableList<Car> carList = Car.getCarsDetails();
			
			FilteredList<Car> filteredList = new FilteredList<>(carList);
			ArrayList<Predicate<Car>> filters = new ArrayList<>();
			ArrayList<Predicate<Car>> yearFilters = new ArrayList<>();
			ArrayList<Predicate<Car>> brandFilters = new ArrayList<>(); 
			ArrayList<Predicate<Car>> bodyTypeFilters = new ArrayList<>();
			ArrayList<Predicate<Car>> priceFilters = new ArrayList<>();
			ArrayList<Predicate<Car>> transmissionFilters = new ArrayList<>();
			ArrayList<Predicate<Car>> fuelTypeFilters = new ArrayList<>();
			
			for (String keyword : yearKeywords) {
				yearFilters.add(i -> Integer.toString(i.getYear()).contains(keyword));
			}
			for (String keyword : brandKeywords) {
				brandFilters.add(i -> i.getBrand().contains(keyword));
			}
			for (String keyword : bodyTypeKeywords) {
				bodyTypeFilters.add(i -> i.getBodyType().contains(keyword));
			}
			for (String keyword : priceKeywords) {
				if (keyword.equals("0to200")) {
					priceFilters.add(i -> i.getPrice() <= 200);					
				} else if (keyword.equals("200to400")) {
					priceFilters.add(i -> i.getPrice() >= 200 && i.getPrice() <= 400);					
				} else if (keyword.equals("400to600")) {
					priceFilters.add(i -> i.getPrice() >= 400 && i.getPrice() <= 600);					
				} else if (keyword.equals("600andabove")) {
					priceFilters.add(i -> i.getPrice() >= 600);					
				}
			}
			for (String keyword : transmissionKeywords) {
				transmissionFilters.add(i -> i.getTransmission().contains(keyword));
			}
			for (String keyword : fuelTypeKeywords) {
				fuelTypeFilters.add(i -> i.getFuelType().contains(keyword));
			}
		
			Predicate<Car> filteredByYear = null;
			Predicate<Car> filteredByBrand = null;
			Predicate<Car> filteredByBodyType = null;
			Predicate<Car> filteredByPrice = null;
			Predicate<Car> filteredByTransmission = null;
			Predicate<Car> filteredByFuelType = null;
			
			if (yearFilters.size() > 0) {
				filteredByYear = yearFilters.get(0);
				if (yearFilters.size() > 1) {
					for (int i = 1; i < yearFilters.size(); i++) {
						filteredByYear = filteredByYear.or(yearFilters.get(i));
					}				
				}
				filters.add(filteredByYear);
			}
			
			if (brandFilters.size() > 0) {
				filteredByBrand = brandFilters.get(0);
				if (brandFilters.size() > 1) {
					for (int i = 1; i < brandFilters.size(); i++) {
						filteredByBrand = filteredByBrand.or(brandFilters.get(i));
					}				
				}
				filters.add(filteredByBrand);
			}
			
			if (bodyTypeFilters.size() > 0) {
				filteredByBodyType = bodyTypeFilters.get(0);
				if (bodyTypeFilters.size() > 1) {
					for (int i = 1; i < bodyTypeFilters.size(); i++) {
						filteredByBodyType = filteredByBodyType.or(bodyTypeFilters.get(i));
					}				
				}
				filters.add(filteredByBodyType);
			}
			
			if (priceFilters.size() > 0) {
				filteredByPrice = priceFilters.get(0);
				if (priceFilters.size() > 1) {
					for (int i = 1; i < priceFilters.size(); i++) {
						filteredByPrice = filteredByPrice.or(priceFilters.get(i));
					}				
				}
				filters.add(filteredByPrice);
			}
			
			if (transmissionFilters.size() > 0) {
				filteredByTransmission = transmissionFilters.get(0);
				if (transmissionFilters.size() > 1) {
					for (int i = 1; i < transmissionFilters.size(); i++) {
						filteredByTransmission = filteredByTransmission.or(transmissionFilters.get(i));
					}				
				}
				filters.add(filteredByTransmission);
			}
			
			if (fuelTypeFilters.size() > 0) {
				filteredByFuelType = fuelTypeFilters.get(0);
				if (fuelTypeFilters.size() > 1) {
					for (int i = 1; i < fuelTypeFilters.size(); i++) {
						filteredByFuelType = filteredByFuelType.or(fuelTypeFilters.get(i));
					}				
				}
				filters.add(filteredByFuelType);
			}
			
			Predicate<Car> filtersCombined = null;
			if (filters.size() > 0) {
				filtersCombined = filters.get(0);
				if (filters.size() > 1) {
					for (int i = 1; i < filters.size(); i++) {
						filtersCombined = filtersCombined.and(filters.get(i));
					}				
				}
			}
			
			filteredList.setPredicate(filtersCombined);
			
			ObservableList<CarThumb> carThumbList = CarThumb.getCarThumbs(filteredList);
			
			for (CarThumb carThumb : carThumbList) {
				FXMLLoader fxmlLoader = new FXMLLoader();
				fxmlLoader.setLocation(getClass().getResource("CarThumb.fxml"));
				VBox box = fxmlLoader.load();
				CarThumbController carThumbController = fxmlLoader.getController();
				carThumbController.setData(carThumb);
				
				if (column > 2) {
					column = 0;
					row++;
				}
				
				carGrid.add(box, column++, row);
				GridPane.setMargin(box, new Insets(10));
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("File not found");
		}
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
	
	// Display all year checkboxes
	public void loadYearCheckbox() {
		String[] yearList = Functions.generateYearList(10);
		
		// Create a checkbox for each year		
		for (String year : yearList) {
			CheckBox cb = new CheckBox(year);
			cb.setPrefHeight(24);
			VBoxYear.getChildren().add(cb);
			checkBoxes.add(cb);
			
			// Create an event handler
            EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
                public void handle(ActionEvent e) {
                    if (cb.isSelected()) {
                    	yearKeywords.add(year);
                    } else {
                    	yearKeywords.remove(year);
                    }
                    carGrid.getChildren().clear();
                    loadCarToGridPane();
                }
            };
            // Set event to checkbox
            cb.setOnAction(event);
            
		}
	}
	
	// Display all brand checkboxes
	public void loadBrandCheckbox() {
		ArrayList<String> brandList;
		
		brandList = Car.getBrandList();
		
		// Create a checkbox for each brand		
		for (String brand : brandList) {
			CheckBox cb = new CheckBox(brand);
			cb.setPrefHeight(24);
			VBoxBrand.getChildren().add(cb);
			checkBoxes.add(cb);
			
			// Create an event handler
		    EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
		        public void handle(ActionEvent e) {
		            if (cb.isSelected()) {
		            	brandKeywords.add(brand);    	
		            } else {
		            	brandKeywords.remove(brand);   
		            }
		            carGrid.getChildren().clear();
		            loadCarToGridPane();
		        }
		    };
		    // Set event to checkbox
		    cb.setOnAction(event);
		    
		}
	}
	
	public void addEventHandlerToCheckbox(CheckBox cb, ArrayList<String> keywordList, String keyword) {
		// Create an event handler
        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                if (cb.isSelected()) {
                	keywordList.add(keyword);
                } else {
                	keywordList.remove(keyword);
                }
                
                carGrid.getChildren().clear();
                loadCarToGridPane();
            }
        };
        // Set event to checkbox
        cb.setOnAction(event);        
	}
	
	// Add event to all predefined checkboxes	
	public void configureCheckbox() {
		// Add all check box
		checkBoxes.addAll(cbSedan, cbSUV, cbMPV, cbHatchback, cbRM0to200, cbRM200to400, cbRM400to600, cbRM600above, cbManual, cbAutomatic, cbPetrol, cbDiesel);
		
		// Add event to body type checkboxes
		addEventHandlerToCheckbox(cbSedan, bodyTypeKeywords, "Sedan");
		addEventHandlerToCheckbox(cbSUV, bodyTypeKeywords, "SUV");
		addEventHandlerToCheckbox(cbMPV, bodyTypeKeywords, "MPV");
		addEventHandlerToCheckbox(cbHatchback, bodyTypeKeywords, "Hatchback");
		
		// Add event to price checkboxes
		addEventHandlerToCheckbox(cbRM0to200, priceKeywords, "0to200");
		addEventHandlerToCheckbox(cbRM200to400, priceKeywords, "200to400");
		addEventHandlerToCheckbox(cbRM400to600, priceKeywords, "400to600");
		addEventHandlerToCheckbox(cbRM600above, priceKeywords, "600andabove");
		
		// Add event to transmission checkboxes
		addEventHandlerToCheckbox(cbManual, transmissionKeywords, "Manual");
		addEventHandlerToCheckbox(cbAutomatic, transmissionKeywords, "Automatic");
		
		// Add event to fuel type checkboxes
		addEventHandlerToCheckbox(cbPetrol, fuelTypeKeywords, "Petrol");
		addEventHandlerToCheckbox(cbDiesel, fuelTypeKeywords, "Diesel");
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
		log.setUserID(Session.customer.getID());
		log.setTargetID(Session.customer.getID());
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
	
	public void switchToHome(MouseEvent event) throws IOException {
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
	
	public void clearAllCheckBox() {
		for (CheckBox checkBox : checkBoxes) {
			checkBox.setSelected(false);
		}
		
		// Reset all keywords list
		yearKeywords.clear();
		brandKeywords.clear();
		bodyTypeKeywords.clear();
		priceKeywords.clear();
		transmissionKeywords.clear();
		fuelTypeKeywords.clear();
		
		carGrid.getChildren().clear();
        loadCarToGridPane();
	}
	
}

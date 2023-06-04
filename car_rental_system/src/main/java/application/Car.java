package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.image.Image;

public class Car {
	private int ID;
	private int year;
	private String brand;
	private String model;
	private String colour;
	private String bodyType;
	private String transmission;
	private String fuelType;
	private double engineCapacity;
	private int seats;
	private String plateNumber;
	private double price;
	private String status;
	private String imageName;
	public static String location = "No.2, Persiaran Kerjaya, Glenmarie Industrial Park,Section U1, 40150 Shah Alam,Selangor";
	
	private String name;
	
	Car() {
		this.ID = 0;
		this.year = 0;
		this.brand = null;
		this.model = null;
		this.plateNumber = null;
		this.colour = null;
		this.bodyType = null;
		this.transmission = null;
		this.fuelType = null;
		this.engineCapacity = 0;
		this.seats = 0;
		this.price = 0;
		this.status = null;
		this.imageName = null;
	}
	
	Car(int ID, String name) {
		this.ID = ID;
		this.name = name;
	}
	
	Car(int ID, int year, String brand, String model, String plateNumber, String colour, String bodyType, String transmission, String fuelType, double engineCapacity, int seats, double price, String status, String imageName) {
		this.ID = ID;
		this.year = year;
		this.brand = brand;
		this.model = model;
		this.colour = colour;
		this.bodyType = bodyType;
		this.transmission = transmission;
		this.fuelType = fuelType;
		this.engineCapacity = engineCapacity;
		this.seats = seats;
		this.price = price;
		this.status = status;
		this.imageName = imageName;
		this.plateNumber = plateNumber;
		
		this.name = String.join(" ", String.valueOf(year), brand, model);
	}
	
	public int getID() {
		return ID;
	}
	public void setID(int ID) {
		this.ID = ID;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getPlateNumber( ) {
		return plateNumber;
	}
	public void setPlateNumber(String plateNumber) {
		this.plateNumber = plateNumber;
	}
	public String getColour() {
		return colour;
	}
	public void setColour(String colour) {
		this.colour = colour;
	}
	public String getBodyType() {
		return bodyType;
	}
	public void setBodyType(String bodyType) {
		this.bodyType = bodyType;
	}
	public String getTransmission() {
		return transmission;
	}
	public void setTransmission(String transmission) {
		this.transmission = transmission;
	}
	public String getFuelType() {
		return fuelType;
	}
	public void setFuelType(String fuelType) {
		this.fuelType = fuelType;
	}
	public double getEngineCapacity() {
		return engineCapacity;
	}
	public void setEngineCapacity(double engineCapacity) {
		this.engineCapacity = engineCapacity;
	}
	public int getSeats() {
		return seats;
	}
	public void setSeats(int seats) {
		this.seats = seats;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getImageName() {
		return imageName;
	}
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
	    return this.getName();
	}

	// Read file and extract car details into a list
	public static ObservableList<Car> getCarsDetails() throws FileNotFoundException {
		ObservableList<Car> carList = FXCollections.observableArrayList();
		
		File file = new File("src/main/resources/txt/car.txt");
		if (file.exists()) {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String line;
			
			try {
				while((line = br.readLine()) != null) {
					String[] details = line.split("\\|");
					int ID = Integer.parseInt(details[0]);
					int year = Integer.parseInt(details[1]);
					String brand = details[2];
					String model = details[3];
					String plateNumber = details[4];
					String colour = details[5];
					String bodyType = details[6];
					String transmission = details[7];
					String fuelType = details[8];
					double engineCapacity = Double.parseDouble(details[9]);
					int seats = Integer.parseInt(details[10]);
					double price = Double.parseDouble(details[11]);
					String status = details[12];
					String imageName = details[13];
					
					if (!status.equals("Trash")) {
						Car car = new Car(ID, year, brand, model, plateNumber, colour, bodyType, transmission, fuelType, engineCapacity, seats, price, status, imageName);
						carList.add(car);
					}	
				}
				br.close();
				
				return carList;
			} catch (IOException e) {
				System.out.println("Failed to extract data from car text file");;
			}
		}
		return null;
	}
	
	
	// Read file and return specific car details
	public static Car getSpecificCarDetails(int carID) throws FileNotFoundException {
		File file = new File("src/main/resources/txt/car.txt");
		if (file.exists()) {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String line;
			Car car = null;
			try {
				while((line = br.readLine()) != null) {
					String[] details = line.split("\\|");
					int ID = Integer.parseInt(details[0]);
					if ( carID == ID) {
						int year = Integer.parseInt(details[1]);
						String brand = details[2];
						String model = details[3];
						String plateNumber = details[4];
						String colour = details[5];
						String bodyType = details[6];
						String transmission = details[7];
						String fuelType = details[8];
						double engineCapacity = Double.parseDouble(details[9]);
						int seats = Integer.parseInt(details[10]);
						double price = Double.parseDouble(details[11]);
						String status = details[12];
						String imageName = details[13];
						
						car = new Car(ID, year, brand, model, plateNumber, colour, bodyType, transmission, fuelType, engineCapacity, seats, price, status, imageName);
						break;
					}
				}
				br.close();
				return car;
			} catch (IOException e) {
				System.out.println("Failed to extract data from car text file");;
			}
		}
		return null;
	}
	
	// Calculate booking price of car
	public static double calculateBookingPrice(int carID, int days) throws FileNotFoundException {
		File file = new File("src/main/resources/txt/car.txt");
		if (file.exists()) {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String line;
			
			try {
				while((line = br.readLine()) != null) {
					String[] details = line.split("\\|");
					int ID = Integer.parseInt(details[0]);
					double price = Double.parseDouble(details[11]);
										
					if (ID == carID) {
						br.close();
						return price * days;
					}	
				}
			} catch (IOException e) {
				System.out.println("Failed to extract data from car text file");;
			}
		}
		return 0;
	}
	
	// Get all car brands in a list
	public static ArrayList<String> getBrandList() {
		ArrayList<String> brandList = new ArrayList<String>();
		
		File file = new File("src/main/resources/txt/car.txt");
		if (file.exists()) {
			FileReader fr;
			try {
				fr = new FileReader(file);
				BufferedReader br = new BufferedReader(fr);
				String line;
				while((line = br.readLine()) != null) {
					String[] details = line.split("\\|");
					String brand = details[2];
										
					if (!brandList.contains(brand)) {
						brandList.add(brand);
					}	
				}
				br.close();
				return brandList;
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e) {
				System.out.println("Failed to extract data from car text file");;
			}
		}
		return null;
	}
			
	// Update car details to text file
	public static boolean updateCarDetails(String ID, String carDetails, Image image, Label insertImageNameLabel, Log log) {
        List<String> carsDetails = new ArrayList<>();
        boolean imageUploaded = insertImageNameLabel.isVisible();
        
		if(imageUploaded) {
			String imageFileName = insertImageNameLabel.getText();
			if (!Functions.saveImageToFile(image, imageFileName, "./src/main/resources/image/car/")) return false;
		}
        try {
			for (String line : Files.readAllLines(Paths.get("src/main/resources/txt/car.txt"), StandardCharsets.UTF_8)) {
			    String[] details = line.split("\\|");
			    if (details[0].equals(ID)) {
			    	carsDetails.add(line.replace(line, carDetails));
			    } else {
			    	carsDetails.add(line);
			    }
			}
	        Files.writeString(Paths.get("src/main/resources/txt/car.txt"), String.join("\n", carsDetails), StandardCharsets.UTF_8);
	        
	        // Set log details
		    log.setDateCreated(LocalDateTime.now());
		    log.setStatus("Success");
	        return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
    }
		
	// Get total number of cars
	public static int getTotalCars() {
		int total = 0;
		
		File file = new File("src/main/resources/txt/car.txt");
		if (file.exists()) {
			FileReader fr;
			try {
				fr = new FileReader(file);
				BufferedReader br = new BufferedReader(fr);
				String line;
				while((line = br.readLine()) != null) {
					String[] details = line.split("\\|");
					String status = details[12];
										
					if (!status.equals("Trash")) {
						total = total + 1;
					}	
				}
				br.close();
				
				return total;
			} catch (IOException e) {
				System.out.println("Failed to extract data from booking text file");;
			}
		}
		return 0;
    }
}

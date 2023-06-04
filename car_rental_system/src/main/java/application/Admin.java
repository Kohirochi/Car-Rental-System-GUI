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

public class Admin extends User {
	private String gender;
	private String contact;
	
	Admin() {
		this.ID = 0;
		this.email = null;
		this.name = null;
		this.password = null;
		this.permission = null;
		this.contact = null;
		this.gender = null;
		this.status = null;
	}

	Admin (User user) {
		this.ID = user.getID();
		this.name = user.getName();
		this.email = user.getEmail();
		this.password = user.getPassword();
		this.status = user.getStatus();
		
		String[] moreDetails = user.getMoreUserDetails().split(";");
		this.gender = moreDetails[0];
		this.contact = moreDetails[1];
	}
	
	Admin(int ID, String name, String email, String password, String gender, String contact, String status) {
		this.ID = ID;
		this.name = name;
		this.email = email;
		this.password = password;
		this.gender = gender;
		this.contact = contact;
		this.status = status;
	}
	
	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}
	
	// Read file and extract admin details into an arraylist
	public static ObservableList<Admin> getAdminsCredentials() {
		ObservableList<Admin> adminList = FXCollections.observableArrayList();
		
		File file = new File("src/main/resources/txt/user.txt");
		if (file.exists()) {
			FileReader fr;
			try {
				fr = new FileReader(file);
				BufferedReader br = new BufferedReader(fr);
				String line;
				
				try {
					while((line = br.readLine()) != null) {
						String[] details = line.split("\\|");
						int ID = Integer.parseInt(details[0]);
						String name = details[1];
						String email = details[2];
						String password = details[3];
						String permission = details[4];
						String[] moreDetails = details[5].split(";");
						String status = details[6];
						
						if (permission.equals("Admin") && (!status.equals("Trash"))) {
							String gender = moreDetails[0];
							String contact = moreDetails[1];
							
							Admin admin = new Admin(ID, name, email, password, gender, contact, status);
							adminList.add(admin);
						}	
					}
					br.close();
					
					return adminList;
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			} catch (IOException e) {
				System.out.println("Failed to extract data from user text file");;
			}
		}
		return null;
	}
	
	// Read file and return specific admin details
	public static Admin getSpecificAdminDetails(int adminID) throws FileNotFoundException {
		File file = new File("src/main/resources/txt/user.txt");
		if (file.exists()) {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String line;
			Admin admin = null;
			try {
				while((line = br.readLine()) != null) {
					String[] details = line.split("\\|");
					int ID = Integer.parseInt(details[0]);
					if (adminID == ID) {
						String name = details[1];
						String email = details[2];
						String password = details[3];
						String[] moreDetails = details[5].split(";");
						String gender = moreDetails[0];
						String contact = moreDetails[1];
						String status = details[6];
						
						admin = new Admin(ID, name, email, password, gender, contact, status);
						break;
					}
				}
				br.close();
				return admin;
			} catch (IOException e) {
				System.out.println("Failed to extract data from user text file");;
			}
		}
		return null;
	}
	
	// Administrator update customer credentials to text file
	public static boolean updateAdminCredentials(String ID, String name, String email, String password, String gender, String contact, String status, Log log) {
        String moreDetails = String.join(";", gender, contact);
        String newCredentials = String.join("|", ID, name, email, password, "Admin", moreDetails, status );
        return User.updateUserCredentials(ID, newCredentials, log);
    }
	
	// Administrator add new admin credentials to text file
	public static boolean addNewAdmin(String name, String email, String password, String gender, String contact, String status, Log log) throws IOException {
		File file = new File("src/main/resources/txt/user.txt");
		if (file.exists()) {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			FileWriter fw = new FileWriter(file, true);
			BufferedWriter bw = new BufferedWriter(fw);
			String line;
			String lastID = null;
			
			while ((line=br.readLine()) != null) {
				lastID = line.split("\\|")[0];
			}
			br.close();
			int newID = Integer.parseInt(lastID) + 1;
			
			String moreDetails = String.join(";", gender, contact);
			String newLine = String.join("|", String.valueOf(newID), name, email, password, "Admin", moreDetails , status);
			
		    bw.newLine();
		    bw.append(newLine);
		    bw.close();
		    
			// Set log details
		    log.setDateCreated(LocalDateTime.now());
		    log.setTargetID(newID);
		    log.setStatus("Success");
		    return true;
		}
		return false;
	}
	
	// Administrator add new customer credentials to text file
	public static boolean addNewCustomer(String name, String email, String password, String gender, String ICPassportNumber, String birthDate, String contact, String country, String status, Log log) throws IOException {
		File file = new File("src/main/resources/txt/user.txt");
		if (file.exists()) {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			FileWriter fw = new FileWriter(file, true);
			BufferedWriter bw = new BufferedWriter(fw);
			String line;
			String lastID = null;
			
			while ((line=br.readLine()) != null) {
				lastID = line.split("\\|")[0];
			}
			br.close();
			int newID = Integer.parseInt(lastID) + 1;
			
			String moreDetails = String.join(";", gender, ICPassportNumber, birthDate, contact, country);
			String newLine = String.join("|", String.valueOf(newID), name, email, password, "Customer", moreDetails ,status );
			
		    bw.newLine();
		    bw.append(newLine);
		    bw.close();
		    
			// Set log details
		    log.setDateCreated(LocalDateTime.now());
		    log.setTargetID(newID);
		    log.setStatus("Success");
		    return true;
		}
		return false;
	}
	
	// Change status of customer to trash
	public static boolean deleteAdmin(String ID, Log log) {
        List<String> usersCredentials = new ArrayList<>();
        try {
			for (String line : Files.readAllLines(Paths.get("src/main/resources/txt/user.txt"), StandardCharsets.UTF_8)) {
			    String[] details = line.split("\\|");
			    if (details[0].equals(ID)) {
			    	usersCredentials.add(line.replace(details[6], "Trash"));
			    } else {
			    	usersCredentials.add(line);
			    }
			}
	        Files.writeString(Paths.get("src/main/resources/txt/user.txt"), String.join("\n", usersCredentials), StandardCharsets.UTF_8);
	        
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
	
	// Change status of customer to trash
	public static boolean deleteCustomer(String ID, Log log) {
        List<String> usersCredentials = new ArrayList<>();
        try {
			for (String line : Files.readAllLines(Paths.get("src/main/resources/txt/user.txt"), StandardCharsets.UTF_8)) {
			    String[] details = line.split("\\|");
			    if (details[0].equals(ID)) {
			    	usersCredentials.add(line.replace(details[6], "Trash"));
			    } else {
			    	usersCredentials.add(line);
			    }
			}
	        Files.writeString(Paths.get("src/main/resources/txt/user.txt"), String.join("\n", usersCredentials), StandardCharsets.UTF_8);
	        
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
	
	// Add new booking details to text file
	public static boolean addNewBooking(String bookingDetails, Log log) throws IOException {
		File file = new File("src/main/resources/txt/booking.txt");
		if (file.exists()) {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			FileWriter fw = new FileWriter(file, true);
			BufferedWriter bw = new BufferedWriter(fw);
			String line;
			String lastID = null;
			
			while ((line=br.readLine()) != null) {
				lastID = line.split("\\|")[0];
			}
			br.close();
			int newID = Integer.parseInt(lastID) + 1;
			String newLine = String.join("|", String.valueOf(newID), bookingDetails);
			
		    bw.newLine();
		    bw.append(newLine);
		    bw.close();
		    
		    // Set log details
		    log.setDateCreated(LocalDateTime.now());
		    log.setTargetID(newID);
		    log.setStatus("Success");
		    return true;
		}
		return false;
	}
	
	// Change status of booking to approved
	public static boolean approveBookingRequest(String ID, Log log) {
        List<String> bookingsDetails = new ArrayList<>();
        try {
			for (String line : Files.readAllLines(Paths.get("src/main/resources/txt/booking.txt"), StandardCharsets.UTF_8)) {
			    String[] details = line.split("\\|");
			    if (details[0].equals(ID)) {
			    	String paymentStatus = details[13];
			    	if (paymentStatus.equals("Unpaid")) {
				    	bookingsDetails.add(line.replace(details[14], "Pending Payment"));		
			    	} else if (paymentStatus.equals("Paid")) {
				    	bookingsDetails.add(line.replace(details[14], "Confirmed"));		
			    	}
			    } else {
			    	bookingsDetails.add(line);
			    }
			}
	        Files.writeString(Paths.get("src/main/resources/txt/booking.txt"), String.join("\n", bookingsDetails), StandardCharsets.UTF_8);
	        
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
	
	// Change status of booking to rejected
	public static boolean rejectBookingRequest(String ID, Log log) {
        List<String> bookingsDetails = new ArrayList<>();
        try {
			for (String line : Files.readAllLines(Paths.get("src/main/resources/txt/booking.txt"), StandardCharsets.UTF_8)) {
			    String[] details = line.split("\\|");
			    if (details[0].equals(ID)) {
			    	bookingsDetails.add(line.replace(details[14], "Rejected"));
			    } else {
			    	bookingsDetails.add(line);
			    }
			}
	        Files.writeString(Paths.get("src/main/resources/txt/booking.txt"), String.join("\n", bookingsDetails), StandardCharsets.UTF_8);
	        
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
	
	// Add new car details to text file
	public static boolean addNewCar(String carDetails, Image image, Label insertImageNameLabel, Log log) throws IOException {
		String imageFileName = insertImageNameLabel.getText();
		
		if(Functions.saveImageToFile(image, imageFileName, "./src/main/resources/image/car/")) {
			File file = new File("src/main/resources/txt/car.txt");
			if (file.exists()) {
				FileReader fr = new FileReader(file);
				BufferedReader br = new BufferedReader(fr);
				FileWriter fw = new FileWriter(file, true);
				BufferedWriter bw = new BufferedWriter(fw);
				String line;
				String lastID = null;
				
				while ((line=br.readLine()) != null) {
					lastID = line.split("\\|")[0];
				}
				br.close();
				int newID = Integer.parseInt(lastID) + 1;
				String newLine = String.join("|", String.valueOf(newID), carDetails);
				
			    bw.newLine();
			    bw.append(newLine);
			    bw.close();
			    
				// Set log details
			    log.setDateCreated(LocalDateTime.now());
			    log.setTargetID(newID);
			    log.setStatus("Success");
			    return true;
			}
		}
		return false;
	}
	
	// Change status of car to trash
	public static boolean deleteCar(String ID, Log log) {
        List<String> carsDetails = new ArrayList<>();
        try {
			for (String line : Files.readAllLines(Paths.get("src/main/resources/txt/car.txt"), StandardCharsets.UTF_8)) {
			    String[] details = line.split("\\|");
			    if (details[0].equals(ID)) {
			    	carsDetails.add(line.replace(details[12], "Trash"));
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
}

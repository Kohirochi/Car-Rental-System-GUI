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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Customer extends User {
	private String gender;
	private String ICPassportNumber;
	private String birthDate;
	private String country;
	private String contact;
	
	Customer() {
		this.ID = 0;
		this.email = null;
		this.name = null;
		this.password = null;
		this.permission = null;
		this.status = null;
	}
	
	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getICPassportNumber() {
		return ICPassportNumber;
	}

	public void setICPassportNumber(String iCPassportNumber) {
		ICPassportNumber = iCPassportNumber;
	}

	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	Customer (User user) {
		this.ID = user.getID();
		this.name = user.getName();
		this.email = user.getEmail();
		this.password = user.getPassword();
		this.status = user.getStatus();

		String[] moreDetails = user.getMoreUserDetails().split(";");
		this.gender = moreDetails[0];
		this.ICPassportNumber = moreDetails[1];
		this.birthDate = moreDetails[2];
		this.contact = moreDetails[3];
		this.country = moreDetails[4];
	}
	
	Customer(int ID, String name) {
		this.ID = ID;
		this.name = name;
	}
	
//	Customer(int ID, String name, String email, String password, String status) {
//		this.ID = ID;
//		this.name = name;
//		this.email = email;
//		this.password = password;
//		this.status = status;
//	}
	
	Customer(int ID, String name, String email, String password, String gender, String ICPassportNumber, String birthDate, String contact, String country, String status) {
		this.ID = ID;
		this.name = name;
		this.email = email;
		this.password = password;
		this.gender = gender;
		this.ICPassportNumber = ICPassportNumber;
		this.birthDate = birthDate;
		this.contact = contact;
		this.country = country;
		this.status = status;
	}
	
	@Override
	public String toString() {
	    return this.getName();
	}

	// Read file and extract customers details into an arraylist
	public static ObservableList<Customer> getCustomersCredentials() {
		ObservableList<Customer> customerList = FXCollections.observableArrayList();
		
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
						
						if (permission.equals("Customer") && (!status.equals("Trash"))) {
							String gender = moreDetails[0];
							String ICPassportNumber = moreDetails[1];
							String birthDate = moreDetails[2];
							String contact = moreDetails[3];
							String country = moreDetails[4];
							
							Customer customer = new Customer(ID, name, email, password, gender, ICPassportNumber, birthDate, contact, country, status );
							customerList.add(customer);
						}	
					}
					br.close();
					
					return customerList;
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
	
	// Read file and return specific customer details
	public static Customer getSpecificCustomerDetails(int customerID) throws FileNotFoundException {
		File file = new File("src/main/resources/txt/user.txt");
		if (file.exists()) {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String line;
			Customer customer = null;
			try {
				while((line = br.readLine()) != null) {
					String[] details = line.split("\\|");
					int ID = Integer.parseInt(details[0]);
					if ( customerID == ID) {
						String name = details[1];
						String email = details[2];
						String password = details[3];
						String[] moreDetails = details[5].split(";");
						String gender = moreDetails[0];
						String ICPassportNumber = moreDetails[1];
						String birthDate = moreDetails[2];
						String contact = moreDetails[3];
						String country = moreDetails[4];
						String status = details[6];
						
						customer = new Customer(ID, name, email, password, gender, ICPassportNumber, birthDate, contact, country, status );
						break;
					}
				}
				br.close();
				return customer;
			} catch (IOException e) {
				System.out.println("Failed to extract data from user text file");;
			}
		}
		return null;
	}
		
	// Update customer credentials to text file
//	public static boolean updateCustomerCredentials(String ID, String name, String email, String password, String status, Log log) {
//        List<String> usersCredentials = new ArrayList<>();
//        String newCredentials = String.join("|", ID, name, email, password, "Customer", status);
//        
//        try {
//			for (String line : Files.readAllLines(Paths.get("./src/txt/user.txt"), StandardCharsets.UTF_8)) {
//			    String[] credentials = line.split("\\|");
//			    if (credentials[0].contains(ID)) {
//			    	usersCredentials.add(line.replace(line, newCredentials));
//			    } else {
//			    	usersCredentials.add(line);
//			    }
//			}
//	        Files.writeString(Paths.get("./src/txt/user.txt"), String.join("\n", usersCredentials), StandardCharsets.UTF_8);
//	        
//	        // Set log details
//		    log.setDateCreated(LocalDateTime.now());
//		    log.setStatus("Success");
//	        return true;
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return false;
//		}
//    }
	
	// Admin update customer credentials to text file
	public static boolean updateCustomerCredentials(String ID, String name, String email, String password, String gender, String ICPassportNumber, String birthDate, String contact, String country, String status, Log log) {
        String moreDetails = String.join(";", gender, ICPassportNumber, birthDate, contact, country );
        String newCredentials = String.join("|", ID, name, email, password, "Customer", moreDetails, status );
        return User.updateUserCredentials(ID, newCredentials, log);
    }
	
	// Add new booking details to text file
	public static boolean addNewBooking(Booking booking, Log log) throws IOException {
		String customerID = Integer.toString(booking.getCustomer().getID());
		String gender = booking.getGender();
		String ICPassportNumber = booking.getICPassportNumber();
		String birthDate = booking.getBirthDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		String contact = booking.getContact();
		String country = booking.getCountry();
		String carID = Integer.toString(booking.getCar().getID());
		String pickUpDate = booking.getPickUpDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		String pickUpTime = booking.getPickUpTime().format(DateTimeFormatter.ofPattern("HH:mm"));
		String dropOffDate = booking.getDropOffDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		String dropOffTime = booking.getDropOffTime().format(DateTimeFormatter.ofPattern("HH:mm"));
		String price = String.valueOf(booking.getPrice());
		String paymentStatus = booking.getPaymentStatus();
		String bookingStatus = booking.getBookingStatus();
		String dateCreated = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
		
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
			booking.setID(newID);
			
			String newLine = String.join("|", String.valueOf(newID), customerID, gender, ICPassportNumber, birthDate, contact, country, carID, pickUpDate, pickUpTime, dropOffDate, dropOffTime, price, paymentStatus, bookingStatus, dateCreated);
			
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
	
	// Cancel booking
	public static boolean cancelBooking(int bookingID, Log log) {
		List<String> bookingsDetails = new ArrayList<>();
        try {
			for (String line : Files.readAllLines(Paths.get("src/main/resources/txt/booking.txt"), StandardCharsets.UTF_8)) {
			    String[] details = line.split("\\|");
			    int ID = Integer.parseInt(details[0]);
			    String bookingStatus = details[14];
			    
			    if (ID == bookingID) {
			    	bookingsDetails.add(line.replace(bookingStatus, "Cancelled"));
			    } else {
			    	bookingsDetails.add(line);
			    }
			}
	        Files.writeString(Paths.get("src/main/resources/txt/booking.txt"), String.join("\n", bookingsDetails), StandardCharsets.UTF_8);
	        
	        // Set log details
		    log.setDateCreated(LocalDateTime.now());
		    log.setTargetID(bookingID);
		    log.setStatus("Success");
		    return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
		
	// Get total number of customers
	public static int getTotalCustomers() {
		int total = 0;
		
		File file = new File("src/main/resources/txt/user.txt");
		if (file.exists()) {
			FileReader fr;
			try {
				fr = new FileReader(file);
				BufferedReader br = new BufferedReader(fr);
				String line;
				while((line = br.readLine()) != null) {
					String[] details = line.split("\\|");
					String permission = details[4];
					String status = details[6];
										
					if (!status.equals("Trash") && permission.equals("Customer")) {
						total = total + 1;
					}	
				}
				br.close();
				
				return total;
			} catch (IOException e) {
				System.out.println("Failed to extract data from user text file");;
			}
		}
		return 0;
    }
}

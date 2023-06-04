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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Booking {

	private int ID;
	private Customer customer;
	private String gender;
	private String ICPassportNumber;
	private LocalDate birthDate;
	private String contact;
	private String country;
	private Car car;
	private LocalDate pickUpDate;
	private LocalTime pickUpTime;
	private LocalDate dropOffDate;
	private LocalTime dropOffTime;
	private double price;
	private String paymentStatus;
	private String bookingStatus;
	private LocalDateTime dateCreated;
	
	private int duration;
	
	Booking() {
		this.ID = 0;
		this.customer = null;
		this.gender = null;
		this.ICPassportNumber = null;
		this.birthDate = null;
		this.contact = null;
		this.country = null;
		this.car = null;
		this.pickUpDate = null;
		this.pickUpTime = null;
		this.dropOffDate = null;
		this.dropOffTime = null;
		this.price = 0;
		this.paymentStatus = null;
		this.bookingStatus = null;
		this.dateCreated = null;
		this.duration = 0;
	}
	
	Booking(int ID, Customer customer, String gender, String ICPassportNumber, LocalDate birthDate, String contact, String country, Car car, LocalDate pickUpDate, LocalTime pickupTime, LocalDate dropOffDate, LocalTime dropoffTime, double price, String paymentStatus, String bookingStatus, LocalDateTime dateCreated) throws FileNotFoundException {
		this.ID = ID;
		this.customer = customer;
		this.gender = gender;
		this.ICPassportNumber = ICPassportNumber;
		this.birthDate = birthDate;
		this.contact = contact;
		this.country = country;
		this.car = car;
		this.pickUpDate = pickUpDate;
		this.pickUpTime = pickupTime;
		this.dropOffDate = dropOffDate;
		this.dropOffTime = dropoffTime;
		this.price = price;
		this.paymentStatus = paymentStatus;
		this.bookingStatus = bookingStatus;
		this.dateCreated = dateCreated;
		
		int days = getDifferenceInDays(pickUpDate, dropOffDate);
		this.duration = days;
	}

	public int getID() {
		return ID;
	}

	public void setID(int ID) {
		this.ID = ID;
	}
	
	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
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

	public void setICPassportNumber(String ICPassportNumber) {
		this.ICPassportNumber = ICPassportNumber;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Car getCar() {
		return car;
	}

	public void setCar(Car car) {
		this.car = car;
	}

	public LocalDate getPickUpDate() {
		return pickUpDate;
	}

	public void setPickUpDate(LocalDate pickUpDate) {
		this.pickUpDate = pickUpDate;
	}
	
	public LocalTime getPickUpTime() {
		return pickUpTime;
	}

	public void setPickUpTime(LocalTime pickUpTime) {
		this.pickUpTime = pickUpTime;
	}

	public LocalDate getDropOffDate() {
		return dropOffDate;
	}

	public void setDropOffDate(LocalDate dropOffDate) {
		this.dropOffDate = dropOffDate;
	}
	
	public LocalTime getDropOffTime() {
		return dropOffTime;
	}

	public void setDropOffTime(LocalTime dropOffTime) {
		this.dropOffTime = dropOffTime;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public String getBookingStatus() {
		return bookingStatus;
	}

	public void setBookingStatus(String bookingStatus) {
		this.bookingStatus = bookingStatus;
	}

	public LocalDateTime getDateCreated() {
		return dateCreated;
	}
	
	public String getDateCreatedString() {
		return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(dateCreated);
	}

	public void setDateCreated(LocalDateTime dateCreated) {
		this.dateCreated = dateCreated;
	}
	
	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}
	
	private static Customer extractCustomer(int customerID) throws FileNotFoundException {
		File file = new File("src/main/resources/txt/user.txt");
		if (file.exists()) {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String line;
			
			try {
				while((line = br.readLine()) != null) {
					String[] details = line.split("\\|");
					int ID = Integer.parseInt(details[0]);
					String name = details[1];
					
					if (ID == customerID) {
						Customer customer = new Customer(ID, name);
						br.close();
						return customer;
					}	
				}
			} catch (IOException e) {
				System.out.println("Failed to extract data from user text file");;
			}
		}
		return null;
	}
	
	private static Car extractCar(int carID) throws FileNotFoundException {
		File file = new File("src/main/resources/txt/car.txt");
		if (file.exists()) {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String line;
			
			try {
				while((line = br.readLine()) != null) {
					String[] details = line.split("\\|");
					int ID = Integer.parseInt(details[0]);
					String year = details[1];
					String brand = details[2];
					String model = details[3];
										
					if (ID == carID) {
						String name = String.join(" ", year, brand, model);
						Car car = new Car(ID, name);
						br.close();
						return car;
					}	
				}
			} catch (IOException e) {
				System.out.println("Failed to extract data from car text file");;
			}
		}
		return null;
	}
	
	public static int getDifferenceInDays(LocalDate pickUpDate, LocalDate dropOffDate) {
		long days = ChronoUnit.DAYS.between(pickUpDate, dropOffDate) + 1;
		return (int) days;
	}
	
	// Read file and extract booking details into a list
	public static ObservableList<Booking> getBookingDetails(String type) {
		ObservableList<Booking> bookingList = FXCollections.observableArrayList();
		
		File file = new File("src/main/resources/txt/booking.txt");
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
						int customerID = Integer.parseInt(details[1]);
						String gender = details[2];
						String ICPassportNumber = details[3];
						LocalDate birthDate = LocalDate.parse(details[4]);
						String contact = details[5];
						String country = details[6];
						int carID = Integer.parseInt(details[7]);
						LocalDate pickUpDate = LocalDate.parse(details[8]);
						LocalTime pickUpTime = LocalTime.parse(details[9]);
						LocalDate dropOffDate = LocalDate.parse(details[10]);
						LocalTime dropoffTime = LocalTime.parse(details[11]);
						double price = Double.parseDouble(details[12]);
						String paymentStatus = details[13];
						String bookingStatus = details[14];
						LocalDateTime dateCreated = LocalDateTime.parse(details[15]);
						
						Customer customer = extractCustomer(customerID);
						Car car = extractCar(carID);
						
						if (type.equals("pending")) {
							if (bookingStatus.equals("Pending")) {
								Booking bookingRequest = new Booking(ID, customer, gender, ICPassportNumber, birthDate, contact, country, car, pickUpDate, pickUpTime, dropOffDate, dropoffTime, price, paymentStatus, bookingStatus, dateCreated);
								bookingList.add(bookingRequest);
							}
						} else if (type.equals("upcoming")) {
							if (!bookingStatus.equals("Pending") && !bookingStatus.equals("Rejected") && !bookingStatus.equals("Cancelled") && !bookingStatus.equals("Completed")) {
								Booking upcomingBooking = new Booking(ID, customer, gender, ICPassportNumber, birthDate, contact, country, car, pickUpDate, pickUpTime, dropOffDate, dropoffTime, price, paymentStatus, bookingStatus, dateCreated);
								bookingList.add(upcomingBooking);
							}
						} else if (type.equals("history")) {
							if (bookingStatus.equals("Rejected") || bookingStatus.equals("Cancelled") || bookingStatus.equals("Completed")) {
								Booking bookingHistory = new Booking(ID, customer, gender, ICPassportNumber, birthDate, contact, country, car, pickUpDate, pickUpTime, dropOffDate, dropoffTime, price, paymentStatus, bookingStatus, dateCreated);
								bookingList.add(bookingHistory);
							}
						} else if (type == "completed") {
							if (bookingStatus.equals("Completed")) {
								Booking bookingHistory = new Booking(ID, customer, gender, ICPassportNumber, birthDate, contact, country, car, pickUpDate, pickUpTime, dropOffDate, dropoffTime, price, paymentStatus, bookingStatus, dateCreated);
								bookingList.add(bookingHistory);
							}
						} else if (type == "rejected/cancelled") {
							if (bookingStatus.equals("Rejected") || bookingStatus.equals("Cancelled")) {
								Booking bookingHistory = new Booking(ID, customer, gender, ICPassportNumber, birthDate, contact, country, car, pickUpDate, pickUpTime, dropOffDate, dropoffTime, price, paymentStatus, bookingStatus, dateCreated);
								bookingList.add(bookingHistory);
							}
						}
							
					}
					br.close();
					
					return bookingList;
				} catch (IOException e) {
					System.out.println("Failed to extract data from booking text file");
				}
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
		return null;
	}
	
	// Read file and extract specific booking details
	public static Booking getSpecificBookingDetails(int bookingID) throws FileNotFoundException {
		File file = new File("src/main/resources/txt/booking.txt");
		if (file.exists()) {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String line;
			Booking booking = new Booking();
			try {
				while((line = br.readLine()) != null) {
					String[] details = line.split("\\|");
					int ID = Integer.parseInt(details[0]);
					if (ID == bookingID) {
						int customerID = Integer.parseInt(details[1]);
						String gender = details[2];
						String ICPassportNumber = details[3];
						LocalDate birthDate = LocalDate.parse(details[4]);
						String contact = details[5];
						String country = details[6];
						int carID = Integer.parseInt(details[7]);
						LocalDate pickUpDate = LocalDate.parse(details[8]);
						LocalTime pickUpTime = LocalTime.parse(details[9]);
						LocalDate dropOffDate = LocalDate.parse(details[10]);
						LocalTime dropoffTime = LocalTime.parse(details[11]);
						double price = Double.parseDouble(details[12]);
						String paymentStatus = details[13];
						String bookingStatus = details[14];
						LocalDateTime dateCreated = LocalDateTime.parse(details[15]);
						
						Customer customer = extractCustomer(customerID);
						Car car = extractCar(carID);
						
						booking = new Booking(ID, customer, gender, ICPassportNumber, birthDate, contact, country, car, pickUpDate, pickUpTime, dropOffDate, dropoffTime, price, paymentStatus, bookingStatus, dateCreated);
						break;
					}
				}
				br.close();
				
				return booking;
			} catch (IOException e) {
				System.out.println("Failed to extract data from booking text file");
			}
		}
		return null;
	}

	// Scan through all bookings and convert status to overdue if car is not returned on time and to cancelled if payment is not made prior to drop off date
	public static void updateOverdueBookings() {
		List<String> bookingsDetails = new ArrayList<>();
        try {
			for (String line : Files.readAllLines(Paths.get("src/main/resources/txt/booking.txt"), StandardCharsets.UTF_8)) {
			    String[] details = line.split("\\|");
			    
			    LocalDate dropOffDate = LocalDate.parse(details[10]);
			    String bookingStatus = details[14];
			    if (bookingStatus.equals("Rented Out") && (dropOffDate.isBefore(LocalDate.now()))) {
			    	bookingsDetails.add(line.replace(details[14], "Overdue"));
			    } else if (bookingStatus.equals("Pending Payment") && (dropOffDate.isBefore(LocalDate.now()))) {
			    	bookingsDetails.add(line.replace(details[14], "Cancelled"));
			    }  else {
			    	bookingsDetails.add(line);
			    }
			}
	        Files.writeString(Paths.get("src/main/resources/txt/booking.txt"), String.join("\n", bookingsDetails), StandardCharsets.UTF_8);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// when customer press pay now button update booking details
	public static boolean updateBookingWithPayment(int bookingID, Log log) {
		List<String> bookingsDetails = new ArrayList<>();
        try {
			for (String line : Files.readAllLines(Paths.get("src/main/resources/txt/booking.txt"), StandardCharsets.UTF_8)) {
			    String[] details = line.split("\\|");
			    int ID = Integer.parseInt(details[0]);
			    String bookingStatus = details[14];
			    String paymentStatus = details[13];
			    
			    if (ID == bookingID) {
			    	if (bookingStatus.equals("Pending Payment")) {
				    	line = line.replace(bookingStatus, "Confirmed");
			    	}
			    	line = line.replace(paymentStatus, "Paid");
			    	bookingsDetails.add(line);
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
				
	// Change status of booking to rented out
	public static boolean markAsRentedOut(String ID, Log log) {
        List<String> bookingsDetails = new ArrayList<>();
        try {
			for (String line : Files.readAllLines(Paths.get("src/main/resources/txt/booking.txt"), StandardCharsets.UTF_8)) {
			    String[] details = line.split("\\|");
			    if (details[0].equals(ID)) {
			    	bookingsDetails.add(line.replace(details[14], "Rented Out"));
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
	
	// Change status of booking to completed
	public static boolean markAsCompleted(String ID, String price, Log log) {
        List<String> bookingsDetails = new ArrayList<>();
        try {
			for (String line : Files.readAllLines(Paths.get("src/main/resources/txt/booking.txt"), StandardCharsets.UTF_8)) {
			    String[] details = line.split("\\|");
			    if (details[0].equals(ID)) {
			    	if (details[14].equals("Overdue")) {
			    		String initialPrice = details[12];
			    		double updatedPrice = Double.parseDouble(initialPrice) + Double.parseDouble(price);
			    		line = line.replace(details[12], String.valueOf(updatedPrice));
			    		line = line.replace(details[14], "Completed");
				    	bookingsDetails.add(line);
			    	} else {
				    	bookingsDetails.add(line.replace(details[14], "Completed"));
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
	
	// Get revenue of each month for current year
	public static double[] getMonthlyRevenue(int year) {
		double[] revenueList = new double[12];
		
		File file = new File("src/main/resources/txt/booking.txt");
		if (file.exists()) {
			FileReader fr;
			try {
				fr = new FileReader(file);
				BufferedReader br = new BufferedReader(fr);
				String line;
				while((line = br.readLine()) != null) {
					String[] details = line.split("\\|");
					double price = Double.parseDouble(details[12]);
					String bookingStatus = details[14];
					LocalDateTime dateCreated = LocalDateTime.parse(details[15]);
										
					if (bookingStatus.equals("Completed")) {
						if (dateCreated.getYear() == year) {
							int index = dateCreated.getMonthValue() - 1;
							revenueList[index] = revenueList[index] + price;
						}
					}	
				}
				br.close();
				
				return revenueList;
			} catch (IOException e) {
				System.out.println("Failed to extract data from booking text file");;
			}
		}
		return null;
    }
	
	// Get revenue of each month using year
	public static int[] getBookingsByBrand(ArrayList<String> brandList) {
		int[] bookingList = new int[brandList.size()];
		
		File file = new File("src/main/resources/txt/booking.txt");
		if (file.exists()) {
			FileReader fr;
			try {
				fr = new FileReader(file);
				BufferedReader br = new BufferedReader(fr);
				String line;
				while((line = br.readLine()) != null) {
					String[] details = line.split("\\|");
					int carID = Integer.parseInt(details[7]);
					Car car = extractCar(carID);
					String brand = car.getName().split(" ")[1];
					
					for (int i = 0; i < brandList.size(); i++) {
						if (brandList.get(i).equals(brand)) {
							bookingList[i] = bookingList[i] + 1;
						}
					}
				}
				br.close();
				
				return bookingList;
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e) {
				System.out.println("Failed to extract data from booking text file");;
			}
		}
		return null;
    }
	
	// Get customer gender for all completed bookings
	public static int[] getGenderDistribution() {
		int[] genderDistributionList = new int[2]; // int[maleCount, femaleCount]
		
		File file = new File("src/main/resources/txt/booking.txt");
		if (file.exists()) {
			FileReader fr;
			try {
				fr = new FileReader(file);
				BufferedReader br = new BufferedReader(fr);
				String line;
				while((line = br.readLine()) != null) {
					String[] details = line.split("\\|");
					String gender = details[2];
					String bookingStatus = details[14];
					
					if (bookingStatus.equals("Completed")) {
						if (gender.equals("Male")) {
							genderDistributionList[0] = genderDistributionList[0] + 1;
						} else if (gender.equals("Female")) {
							genderDistributionList[1] = genderDistributionList[1] + 1;
						}						
					}
				}
				br.close();
				
				return genderDistributionList;
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e) {
				System.out.println("Failed to extract data from booking text file");;
			}
		}
		return null;
    }
	
	// Get body type of car for all completed bookings
	public static int[] getBodyTypeDistribution() {
		int[] bodyTypeDistributionList = new int[4]; // int[sedanCount, SUVCount, MPVCount, hatchbackCount]
		
		File file = new File("src/main/resources/txt/booking.txt");
		if (file.exists()) {
			FileReader fr;
			try {
				fr = new FileReader(file);
				BufferedReader br = new BufferedReader(fr);
				String line;
				while((line = br.readLine()) != null) {
					String[] details = line.split("\\|");
					int carID = Integer.parseInt(details[7]);
					String bookingStatus = details[14];
					
					String bodyType = Car.getSpecificCarDetails(carID).getBodyType();
					
					if (bookingStatus.equals("Completed")) {
						if (bodyType.equals("Sedan")) {
							bodyTypeDistributionList[0] = bodyTypeDistributionList[0] + 1;
						} else if (bodyType.equals("SUV")) {
							bodyTypeDistributionList[1] = bodyTypeDistributionList[1] + 1;
						} else if (bodyType.equals("MPV")) {
							bodyTypeDistributionList[2] = bodyTypeDistributionList[2] + 1;
						} else if (bodyType.equals("Hatchback")) {
							bodyTypeDistributionList[3] = bodyTypeDistributionList[3] + 1;
						}						
					}
				}
				br.close();
				
				return bodyTypeDistributionList;
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e) {
				System.out.println("Failed to extract data from booking text file");;
			}
		}
		return null;
    }
	
	// Get total revenue year-to-date
	public static String getYearToDateRevenue() {
		double total = 0;
		int year = LocalDate.now().getYear();
		
		File file = new File("src/main/resources/txt/booking.txt");
		if (file.exists()) {
			FileReader fr;
			try {
				fr = new FileReader(file);
				BufferedReader br = new BufferedReader(fr);
				String line;
				while((line = br.readLine()) != null) {
					String[] details = line.split("\\|");
					double price = Double.parseDouble(details[12]);
					String bookingStatus = details[14];
					LocalDateTime dateCreated = LocalDateTime.parse(details[15]);
										
					if (bookingStatus.equals("Completed")) {
						if (dateCreated.getYear() == year) {
							total = total + price;
						}
					}	
				}
				br.close();
				
				String formattedTotal = Functions.limitDecimalPlaces(Double.toString(total) , 2);
				return formattedTotal;
			} catch (IOException e) {
				System.out.println("Failed to extract data from booking text file");;
			}
		}
		return null;
    }
	
	// Get total bookings year-to-date
	public static int getYearToDateBookings() {
		int total = 0;
		int year = LocalDate.now().getYear();
		
		File file = new File("src/main/resources/txt/booking.txt");
		if (file.exists()) {
			FileReader fr;
			try {
				fr = new FileReader(file);
				BufferedReader br = new BufferedReader(fr);
				String line;
				while((line = br.readLine()) != null) {
					String[] details = line.split("\\|");
					String bookingStatus = details[14];
					LocalDateTime dateCreated = LocalDateTime.parse(details[15]);
										
					if (bookingStatus.equals("Completed")) {
						if (dateCreated.getYear() == year) {
							total = total + 1;
						}
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

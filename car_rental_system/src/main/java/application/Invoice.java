package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Invoice {
	
	private int ID;
	private int bookingID;
	private LocalDateTime paymentDate;
	private String paymentType;
	private String cardholderName;
	private String cardNumber;
	
	Invoice() {
		this.ID = 0;
		this.bookingID = 0;
		this.paymentDate = null;
		this.paymentType = null;
		this.cardholderName = null;
		this.cardNumber = null;
	}
	
	Invoice(int ID, int bookingID, LocalDateTime paymentDate, String paymentType, String cardholderName, String cardNumber) {
		this.ID = ID;
		this.bookingID = bookingID;
		this.paymentDate = paymentDate;
		this.paymentType = paymentType;
		this.cardholderName = cardholderName;
		this.cardNumber = cardNumber;
	}

	public int getID() {
		return ID;
	}

	public void setID(int ID) {
		this.ID = ID;
	}

	public int getBookingID() {
		return bookingID;
	}

	public void setBookingID(int bookingID) {
		this.bookingID = bookingID;
	}
	
	public String getCustomerName() {
		Booking booking;
		String customerName = "";
		try {
			booking = Booking.getSpecificBookingDetails(bookingID);
			customerName = booking.getCustomer().toString();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return customerName;
	}
	
	public LocalDateTime getPaymentDate() {
		return paymentDate;
	}
	
	public String getPaymentDateString() {
		return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(paymentDate);
	}
	
	public void setPaymentDate(LocalDateTime paymentDate) {
		this.paymentDate = paymentDate;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getCardholderName() {
		return cardholderName;
	}

	public void setCardholderName(String cardholderName) {
		this.cardholderName = cardholderName;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
	
	// Add new invoice details to text file
	public static boolean addNewInvoice(String invoiceDetails) throws IOException {
		File file = new File("src/main/resources/txt/invoice.txt");
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
			String newLine = String.join("|", String.valueOf(newID), invoiceDetails);
			
		    bw.newLine();
		    bw.append(newLine);
		    bw.close();
		    
		    return true;
		}
		return false;
	}
	
	// Read file and extract invoice details into a list
	public static ObservableList<Invoice> getInvoicesDetails() throws FileNotFoundException {
		ObservableList<Invoice> invoiceList = FXCollections.observableArrayList();
		
		File file = new File("src/main/resources/txt/invoice.txt");
		if (file.exists()) {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String line;
			
			try {
				while((line = br.readLine()) != null) {
					String[] details = line.split("\\|");
					int ID = Integer.parseInt(details[0]);
					int BookingID = Integer.parseInt(details[1]);
					LocalDateTime paymentDate = LocalDateTime.parse(details[2]);
					String paymentType = details[3];
					String cardholderName = details[4];
					String cardNumber = details[5];
					
					Invoice invoice = new Invoice(ID, BookingID, paymentDate, paymentType, cardholderName, cardNumber);
					invoiceList.add(invoice);
				}
				br.close();
				
				return invoiceList;
			} catch (IOException e) {
				System.out.println("Failed to extract data from invoice text file");;
			}
		}
		return null;
	}
	
	// Read file and extract invoice details into a list
	public static Invoice getSpecificInvoiceDetails(int bookingID) throws FileNotFoundException {
		File file = new File("src/main/resources/txt/invoice.txt");
		if (file.exists()) {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String line;
			Invoice invoice = new Invoice();
			
			try {
				while((line = br.readLine()) != null) {
					String[] details = line.split("\\|");
					int BookingID = Integer.parseInt(details[1]);
					
					if (bookingID == BookingID) {
						int ID = Integer.parseInt(details[0]);
						LocalDateTime paymentDate = LocalDateTime.parse(details[2]);
						String paymentType = details[3];
						String cardholderName = details[4];
						String cardNumber = details[5];
						
						invoice = new Invoice(ID, BookingID, paymentDate, paymentType, cardholderName, cardNumber);
						break;
					}
				}
				br.close();
				
				return invoice;
			} catch (IOException e) {
				System.out.println("Failed to extract data from invoice text file");;
			}
		}
		return null;
	}
}

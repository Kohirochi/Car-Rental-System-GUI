package application;

import java.io.FileNotFoundException;
import java.time.format.DateTimeFormatter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class BookingThumb {
	private int ID;
	private String carImagePath;
	private String name;
	private String plateNumber;
	private String duration;
	private String paymentStatus;
	private String bookingStatus;
	
	BookingThumb() {
		this.ID = 0;
		this.carImagePath = null;
		this.name = null;
		this.plateNumber = null;
		this.duration = null;
		this.paymentStatus = null;
		this.bookingStatus = null;
	}
	
	BookingThumb(int ID, String carImagePath, String name, String plateNumber, String duration, String paymentStatus, String bookingStatus) {
		this.ID = ID;
		this.carImagePath = carImagePath;
		this.name = name;
		this.plateNumber = plateNumber;
		this.duration = duration;
		this.paymentStatus = paymentStatus;
		this.bookingStatus = bookingStatus;
	}
	
	
	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getCarImagePath() {
		return carImagePath;
	}

	public void setCarImagePath(String carImagePath) {
		this.carImagePath = carImagePath;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPlateNumber() {
		return plateNumber;
	}

	public void setPlateNumber(String plateNumber) {
		this.plateNumber = plateNumber;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
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
	
	public String getYear() {
		return this.name.split(" ")[0];
	}
	
	public static ObservableList<BookingThumb> getBookingThumbs(ObservableList<Booking> bookingList, int customerID) throws FileNotFoundException {
		// Initialize car thumb list
		ObservableList<BookingThumb> bookingThumbList = FXCollections.observableArrayList();

		for (Booking booking : bookingList) {
			if (booking.getCustomer().getID() == customerID) {
				int ID = booking.getID();
				Car car = Car.getSpecificCarDetails(booking.getCar().getID());
				String carImagePath = "file:./src/main/resources/image/car/" + car.getImageName();
				String name = String.join(" ", Integer.toString(car.getYear()), car.getBrand(), car.getModel());
				String plateNumber = car.getPlateNumber();
				String duration = String.join(" ", booking.getPickUpDate().format(DateTimeFormatter.ofPattern("MMM dd, YYYY")), "-", booking.getDropOffDate().format(DateTimeFormatter.ofPattern("MMM dd, YYYY")));
				String paymentStatus = booking.getPaymentStatus();
				String bookingStatus = booking.getBookingStatus();
				
				BookingThumb bookingThumb = new BookingThumb(ID, carImagePath, name, plateNumber, duration, paymentStatus, bookingStatus);
				bookingThumbList.add(bookingThumb);
			}
		}
		return bookingThumbList;
	}
}

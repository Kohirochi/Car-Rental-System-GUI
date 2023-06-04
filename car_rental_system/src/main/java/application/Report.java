package application;

import java.io.FileNotFoundException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Report {
	Customer customer;
	Car car;
	int year;
	String month;
	int bookingNumber;
	double sales;
	
	Report() {
		this.year = 0;
		this.sales = 0;
	}
	
	Report(int year, double sales, int bookingNumber) {
		this.year = year;
		this.sales = sales;
		this.bookingNumber = bookingNumber;
	}
	
	Report(String month, double sales, int bookingNumber) {
		this.month = month;
		this.sales = sales;
		this.bookingNumber = bookingNumber;
	}
	
	Report(Customer customer, int year, double sales, int bookingNumber) {
		this.customer = customer;
		this.year = year;
		this.sales = sales;
		this.bookingNumber = bookingNumber;
	}
	
	Report(Customer customer, String month, double sales, int bookingNumber) {
		this.customer = customer;
		this.month = month;
		this.sales = sales;
		this.bookingNumber = bookingNumber;
	}
	
	Report(Car car, int year, double sales, int bookingNumber) {
		this.car = car;
		this.year = year;
		this.sales = sales;
		this.bookingNumber = bookingNumber;
	}
	
	Report(Car car, String month, double sales, int bookingNumber) {
		this.car = car;
		this.month = month;
		this.sales = sales;
		this.bookingNumber = bookingNumber;
	}
	
	public Customer getCustomer() {
		return customer;
	}
	
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
	public int getCustomerID() {
		return customer.getID();
	}
	
	public String getCustomerName() {
		return customer.getName();
	}

	public Car getCar() {
		return car;
	}

	public void setCar(Car car) {
		this.car = car;
	}
	
	public int getCarID() {
		return car.getID();
	}
	
	public String getCarName() {
		return car.getName();
	}
	
	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public double getSales() {
		return sales;
	}
	
	public void setSales(double sales) {
		this.sales = sales;
	}
	
	public int getBookingNumber() {
		return bookingNumber;
	}

	public void setBookingNumber(int bookingNumber) {
		this.bookingNumber = bookingNumber;
	}

	// Read booking list and extract report details into an arraylist
	public static ObservableList<Report> getOverallYearlySalesReport(ObservableList<Booking> bookingList) throws FileNotFoundException {
		ObservableList<Report> reportList = FXCollections.observableArrayList();
		double sales2021 = 0;
		double sales2022 = 0;
		
		int bookingNumber2021 = 0;
		int bookingNumber2022 = 0;
		
		for (Booking booking : bookingList) {
			if (booking.getDateCreated().getYear() == 2022) {
				sales2022 = sales2022 + booking.getPrice();
				bookingNumber2022 += 1;
			} else if (booking.getDateCreated().getYear() == 2021) {
				sales2021 = sales2021 + booking.getPrice();
				bookingNumber2021 += 1;
			}
		}
		reportList.add(new Report(2022, Math.round(sales2022 * 100) / 100D, bookingNumber2022));
		reportList.add(new Report(2021, Math.round(sales2021 * 100) / 100D, bookingNumber2021));
		
		return reportList;
	}
	
	// Read booking list and extract report details into an arraylist
	public static ObservableList<Report> getOverallMonthlySalesReport(ObservableList<Booking> bookingList, int year) throws FileNotFoundException {
		ObservableList<Report> reportList = FXCollections.observableArrayList();
		double salesJan = 0;
		double salesFeb = 0;
		double salesMar = 0;
		double salesApr = 0;
		double salesMay = 0;
		double salesJun = 0;
		double salesJul = 0;
		double salesAug = 0;
		double salesSep = 0;
		double salesOct = 0;
		double salesNov = 0;
		double salesDec = 0;
		
		int bookingNumberJan = 0;
		int bookingNumberFeb = 0;
		int bookingNumberMar = 0;
		int bookingNumberApr = 0;
		int bookingNumberMay = 0;
		int bookingNumberJun = 0;
		int bookingNumberJul = 0;
		int bookingNumberAug = 0;
		int bookingNumberSep = 0;
		int bookingNumberOct = 0;
		int bookingNumberNov = 0;
		int bookingNumberDec = 0;
		
		for (Booking booking : bookingList) {
			if (booking.getDateCreated().getYear() == year) {
				if (booking.getDateCreated().getMonthValue() == 1) {
					salesJan = salesJan + booking.getPrice();
					bookingNumberJan += 1;
				} else if (booking.getDateCreated().getMonthValue() == 2) {
					salesFeb = salesFeb + booking.getPrice();
					bookingNumberFeb += 1;
				} else if (booking.getDateCreated().getMonthValue() == 3) {
					salesMar = salesMar + booking.getPrice();
					bookingNumberMar += 1;
				} else if (booking.getDateCreated().getMonthValue() == 4) {
					salesApr = salesApr + booking.getPrice();
					bookingNumberApr += 1;
				} else if (booking.getDateCreated().getMonthValue() == 5) {
					salesMay = salesMay + booking.getPrice();
					bookingNumberMay += 1;
				} else if (booking.getDateCreated().getMonthValue() == 6) {
					salesJun = salesJun + booking.getPrice();
					bookingNumberJun += 1;
				} else if (booking.getDateCreated().getMonthValue() == 7) {
					salesJul = salesJul + booking.getPrice();
					bookingNumberJul += 1;
				} else if (booking.getDateCreated().getMonthValue() == 8) {
					salesAug = salesAug + booking.getPrice();
					bookingNumberAug += 1;
				} else if (booking.getDateCreated().getMonthValue() == 9) {
					salesSep = salesSep + booking.getPrice();
					bookingNumberSep += 1;
				} else if (booking.getDateCreated().getMonthValue() == 10) {
					salesOct = salesOct + booking.getPrice();
					bookingNumberOct += 1;
				} else if (booking.getDateCreated().getMonthValue() == 11) {
					salesNov = salesNov + booking.getPrice();
					bookingNumberNov += 1;
				} else if (booking.getDateCreated().getMonthValue() == 12) {
					salesDec = salesDec + booking.getPrice();
					bookingNumberDec += 1;
				}
			}
		}
		reportList.add(new Report("January", Math.round(salesJan * 100) / 100D, bookingNumberJan));
		reportList.add(new Report("February", Math.round(salesFeb * 100) / 100D, bookingNumberFeb));
		reportList.add(new Report("March", Math.round(salesMar * 100) / 100D, bookingNumberMar));
		reportList.add(new Report("April", Math.round(salesApr * 100) / 100D, bookingNumberApr));
		reportList.add(new Report("May", Math.round(salesMay * 100) / 100D, bookingNumberMay));
		reportList.add(new Report("June", Math.round(salesJun * 100) / 100D, bookingNumberJun));
		reportList.add(new Report("July", Math.round(salesJul * 100) / 100D, bookingNumberJul));
		reportList.add(new Report("August", Math.round(salesAug * 100) / 100D, bookingNumberAug));
		reportList.add(new Report("September", Math.round(salesSep * 100) / 100D, bookingNumberSep));
		reportList.add(new Report("October", Math.round(salesOct * 100) / 100D, bookingNumberOct));
		reportList.add(new Report("November", Math.round(salesNov * 100) / 100D, bookingNumberNov));
		reportList.add(new Report("December", Math.round(salesDec * 100) / 100D, bookingNumberDec));
		
		return reportList;
	}
	
	// Read booking list and extract report details into an arraylist
	public static ObservableList<Report> getCustomerYearlySalesReport(ObservableList<Booking> bookingList) throws FileNotFoundException {
		ObservableList<Report> reportList = FXCollections.observableArrayList();
		ObservableList<Integer> customerIDList = FXCollections.observableArrayList();
		
		for (Booking booking : bookingList) {
			if (!customerIDList.contains(booking.getCustomer().getID())) {
				customerIDList.add(booking.getCustomer().getID());
			}
		}
		
		for (Integer customerID : customerIDList) {
			Customer customer = null;
			double sales2021 = 0;
			double sales2022 = 0;
			
			int bookingNumber2021 = 0;
			int bookingNumber2022 = 0;
			
			for (Booking booking : bookingList) {
				if (booking.getCustomer().getID() == customerID) {
					customer = Customer.getSpecificCustomerDetails(customerID);
					
					if (booking.getDateCreated().getYear() == 2022) {
						sales2022 = sales2022 + booking.getPrice();
						bookingNumber2022 += 1;
					} else if (booking.getDateCreated().getYear() == 2021) {
						sales2021 = sales2021 + booking.getPrice();
						bookingNumber2021 += 1;
					}
				}
			}

			if (customer != null) {
				reportList.add(new Report(customer, 2022, Math.round(sales2022 * 100) / 100D, bookingNumber2022));
				reportList.add(new Report(customer, 2021, Math.round(sales2021 * 100) / 100D, bookingNumber2021));
			}
		}
		
		return reportList;
	}
	
	// Read booking list and extract report details into an arraylist
	public static ObservableList<Report> getCustomerMonthlySalesReport(ObservableList<Booking> bookingList, int year) throws FileNotFoundException {
		ObservableList<Report> reportList = FXCollections.observableArrayList();
		ObservableList<Integer> customerIDList = FXCollections.observableArrayList();
		
		for (Booking booking : bookingList) {
			if (!customerIDList.contains(booking.getCustomer().getID())) {
				customerIDList.add(booking.getCustomer().getID());
			}
		}
		
		for (Integer customerID : customerIDList) {
			Customer customer = null;
			double salesJan = 0;
			double salesFeb = 0;
			double salesMar = 0;
			double salesApr = 0;
			double salesMay = 0;
			double salesJun = 0;
			double salesJul = 0;
			double salesAug = 0;
			double salesSep = 0;
			double salesOct = 0;
			double salesNov = 0;
			double salesDec = 0;
			
			int bookingNumberJan = 0;
			int bookingNumberFeb = 0;
			int bookingNumberMar = 0;
			int bookingNumberApr = 0;
			int bookingNumberMay = 0;
			int bookingNumberJun = 0;
			int bookingNumberJul = 0;
			int bookingNumberAug = 0;
			int bookingNumberSep = 0;
			int bookingNumberOct = 0;
			int bookingNumberNov = 0;
			int bookingNumberDec = 0;
			
			for (Booking booking : bookingList) {
				if (booking.getCustomer().getID() == customerID) {
					customer = Customer.getSpecificCustomerDetails(customerID);
					
					if (booking.getDateCreated().getYear() == year) {
						if (booking.getDateCreated().getMonthValue() == 1) {
							salesJan = salesJan + booking.getPrice();
							bookingNumberJan += 1;
						} else if (booking.getDateCreated().getMonthValue() == 2) {
							salesFeb = salesFeb + booking.getPrice();
							bookingNumberFeb += 1;
						} else if (booking.getDateCreated().getMonthValue() == 3) {
							salesMar = salesMar + booking.getPrice();
							bookingNumberMar += 1;
						} else if (booking.getDateCreated().getMonthValue() == 4) {
							salesApr = salesApr + booking.getPrice();
							bookingNumberApr += 1;
						} else if (booking.getDateCreated().getMonthValue() == 5) {
							salesMay = salesMay + booking.getPrice();
							bookingNumberMay += 1;
						} else if (booking.getDateCreated().getMonthValue() == 6) {
							salesJun = salesJun + booking.getPrice();
							bookingNumberJun += 1;
						} else if (booking.getDateCreated().getMonthValue() == 7) {
							salesJul = salesJul + booking.getPrice();
							bookingNumberJul += 1;
						} else if (booking.getDateCreated().getMonthValue() == 8) {
							salesAug = salesAug + booking.getPrice();
							bookingNumberAug += 1;
						} else if (booking.getDateCreated().getMonthValue() == 9) {
							salesSep = salesSep + booking.getPrice();
							bookingNumberSep += 1;
						} else if (booking.getDateCreated().getMonthValue() == 10) {
							salesOct = salesOct + booking.getPrice();
							bookingNumberOct += 1;
						} else if (booking.getDateCreated().getMonthValue() == 11) {
							salesNov = salesNov + booking.getPrice();
							bookingNumberNov += 1;
						} else if (booking.getDateCreated().getMonthValue() == 12) {
							salesDec = salesDec + booking.getPrice();
							bookingNumberDec += 1;
						}
					}
				}
			}

			if (customer != null) {
				reportList.add(new Report(customer, "January", Math.round(salesJan * 100) / 100D, bookingNumberJan));
				reportList.add(new Report(customer, "February", Math.round(salesFeb * 100) / 100D, bookingNumberFeb));
				reportList.add(new Report(customer, "March", Math.round(salesMar * 100) / 100D, bookingNumberMar));
				reportList.add(new Report(customer, "April", Math.round(salesApr * 100) / 100D, bookingNumberApr));
				reportList.add(new Report(customer, "May", Math.round(salesMay * 100) / 100D, bookingNumberMay));
				reportList.add(new Report(customer, "June", Math.round(salesJun * 100) / 100D, bookingNumberJun));
				reportList.add(new Report(customer, "July", Math.round(salesJul * 100) / 100D, bookingNumberJul));
				reportList.add(new Report(customer, "August", Math.round(salesAug * 100) / 100D, bookingNumberAug));
				reportList.add(new Report(customer, "September", Math.round(salesSep * 100) / 100D, bookingNumberSep));
				reportList.add(new Report(customer, "October", Math.round(salesOct * 100) / 100D, bookingNumberOct));
				reportList.add(new Report(customer, "November", Math.round(salesNov * 100) / 100D, bookingNumberNov));
				reportList.add(new Report(customer, "December", Math.round(salesDec * 100) / 100D, bookingNumberDec));
			}
		}
		
		return reportList;
	}
	
	// Read booking list and extract report details into an arraylist
	public static ObservableList<Report> getCarYearlySalesReport(ObservableList<Booking> bookingList) throws FileNotFoundException {
		ObservableList<Report> reportList = FXCollections.observableArrayList();
		ObservableList<Integer> carIDList = FXCollections.observableArrayList();
		
		for (Booking booking : bookingList) {
			if (!carIDList.contains(booking.getCar().getID())) {
				carIDList.add(booking.getCar().getID());
			}
		}
		
		for (Integer carID : carIDList) {
			Car car = null;
			double sales2021 = 0;
			double sales2022 = 0;
			
			int bookingNumber2021 = 0;
			int bookingNumber2022 = 0;
			
			for (Booking booking : bookingList) {
				if (booking.getCar().getID() == carID) {
					car = Car.getSpecificCarDetails(carID);
					
					if (booking.getDateCreated().getYear() == 2022) {
						sales2022 = sales2022 + booking.getPrice();
						bookingNumber2022 += 1;
					} else if (booking.getDateCreated().getYear() == 2021) {
						sales2021 = sales2021 + booking.getPrice();
						bookingNumber2021 += 1;
					}
				}
			}

			if (car != null) {
				reportList.add(new Report(car, 2022, Math.round(sales2022 * 100) / 100D, bookingNumber2022));
				reportList.add(new Report(car, 2021, Math.round(sales2021 * 100) / 100D, bookingNumber2021));
			}
		}
		
		return reportList;
	}
	
	// Read booking list and extract report details into an arraylist
	public static ObservableList<Report> getCarMonthlySalesReport(ObservableList<Booking> bookingList, int year) throws FileNotFoundException {
		ObservableList<Report> reportList = FXCollections.observableArrayList();
		ObservableList<Integer> carIDList = FXCollections.observableArrayList();
		
		for (Booking booking : bookingList) {
			if (!carIDList.contains(booking.getCar().getID())) {
				carIDList.add(booking.getCar().getID());
			}
		}
		
		for (Integer carID : carIDList) {
			Car car = null;
			double salesJan = 0;
			double salesFeb = 0;
			double salesMar = 0;
			double salesApr = 0;
			double salesMay = 0;
			double salesJun = 0;
			double salesJul = 0;
			double salesAug = 0;
			double salesSep = 0;
			double salesOct = 0;
			double salesNov = 0;
			double salesDec = 0;
			
			int bookingNumberJan = 0;
			int bookingNumberFeb = 0;
			int bookingNumberMar = 0;
			int bookingNumberApr = 0;
			int bookingNumberMay = 0;
			int bookingNumberJun = 0;
			int bookingNumberJul = 0;
			int bookingNumberAug = 0;
			int bookingNumberSep = 0;
			int bookingNumberOct = 0;
			int bookingNumberNov = 0;
			int bookingNumberDec = 0;
			
			for (Booking booking : bookingList) {
				if (booking.getCar().getID() == carID) {
					car = Car.getSpecificCarDetails(carID);
					
					if (booking.getDateCreated().getYear() == year) {
						if (booking.getDateCreated().getMonthValue() == 1) {
							salesJan = salesJan + booking.getPrice();
							bookingNumberJan += 1;
						} else if (booking.getDateCreated().getMonthValue() == 2) {
							salesFeb = salesFeb + booking.getPrice();
							bookingNumberFeb += 1;
						} else if (booking.getDateCreated().getMonthValue() == 3) {
							salesMar = salesMar + booking.getPrice();
							bookingNumberMar += 1;
						} else if (booking.getDateCreated().getMonthValue() == 4) {
							salesApr = salesApr + booking.getPrice();
							bookingNumberApr += 1;
						} else if (booking.getDateCreated().getMonthValue() == 5) {
							salesMay = salesMay + booking.getPrice();
							bookingNumberMay += 1;
						} else if (booking.getDateCreated().getMonthValue() == 6) {
							salesJun = salesJun + booking.getPrice();
							bookingNumberJun += 1;
						} else if (booking.getDateCreated().getMonthValue() == 7) {
							salesJul = salesJul + booking.getPrice();
							bookingNumberJul += 1;
						} else if (booking.getDateCreated().getMonthValue() == 8) {
							salesAug = salesAug + booking.getPrice();
							bookingNumberAug += 1;
						} else if (booking.getDateCreated().getMonthValue() == 9) {
							salesSep = salesSep + booking.getPrice();
							bookingNumberSep += 1;
						} else if (booking.getDateCreated().getMonthValue() == 10) {
							salesOct = salesOct + booking.getPrice();
							bookingNumberOct += 1;
						} else if (booking.getDateCreated().getMonthValue() == 11) {
							salesNov = salesNov + booking.getPrice();
							bookingNumberNov += 1;
						} else if (booking.getDateCreated().getMonthValue() == 12) {
							salesDec = salesDec + booking.getPrice();
							bookingNumberDec += 1;
						}
					}
				}
			}

			if (car != null) {
				reportList.add(new Report(car, "January", Math.round(salesJan * 100) / 100D, bookingNumberJan));
				reportList.add(new Report(car, "February", Math.round(salesFeb * 100) / 100D, bookingNumberFeb));
				reportList.add(new Report(car, "March", Math.round(salesMar * 100) / 100D, bookingNumberMar));
				reportList.add(new Report(car, "April", Math.round(salesApr * 100) / 100D, bookingNumberApr));
				reportList.add(new Report(car, "May", Math.round(salesMay * 100) / 100D, bookingNumberMay));
				reportList.add(new Report(car, "June", Math.round(salesJun * 100) / 100D, bookingNumberJun));
				reportList.add(new Report(car, "July", Math.round(salesJul * 100) / 100D, bookingNumberJul));
				reportList.add(new Report(car, "August", Math.round(salesAug * 100) / 100D, bookingNumberAug));
				reportList.add(new Report(car, "September", Math.round(salesSep * 100) / 100D, bookingNumberSep));
				reportList.add(new Report(car, "October", Math.round(salesOct * 100) / 100D, bookingNumberOct));
				reportList.add(new Report(car, "November", Math.round(salesNov * 100) / 100D, bookingNumberNov));
				reportList.add(new Report(car, "December", Math.round(salesDec * 100) / 100D, bookingNumberDec));
			}
		}
		
		return reportList;
	}
	
}

package application;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

public class CarThumb {
	private int ID;
	private String thumbPath;
	private String name;
	private String price;
	
	CarThumb() {
		this.ID = 0;
		this.thumbPath = null;
		this.name = null;
		this.price = null;
	}
	
	CarThumb(int ID, String thumbPath, String name, String price) {
		this.ID = ID;
		this.thumbPath = thumbPath;
		this.name = name;
		this.price = price;
	}
	
	public int getID() {
		return ID;
	}
	
	public void setID(int ID) {
		this.ID = ID;
	}
	
	public String getThumbPath() {
		return thumbPath;
	}
	
	public void setThumbPath(String thumbpath) {
		this.thumbPath = thumbpath;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getPrice() {
		return price;
	}
	
	public void setPrice(String price) {
		this.price = price;
	}
	
	public String getYear() {
		return this.name.split(" ")[0];
	}
	
	public static ObservableList<CarThumb> getCarThumbs(FilteredList<Car> carList) throws FileNotFoundException {
		// Initialize car thumb list
		ObservableList<CarThumb> carThumbList = FXCollections.observableArrayList();

		for (Car car : carList) {
			if (car.getStatus().equals("Active")) {
				int ID = car.getID();
				String imagePath = "file:./src/main/resources/image/car/" + car.getImageName();
				String name = String.join(" ", Integer.toString(car.getYear()), car.getBrand(), car.getModel());
				String price = "RM " + Double.toString(car.getPrice());
				
				CarThumb carThumb = new CarThumb(ID, imagePath, name, price);
				carThumbList.add(carThumb);
			}
		}
		return carThumbList;
	}
}

package application;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import javax.imageio.ImageIO;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class Functions {
	
	public static void switchScene(ActionEvent event, Parent root, Stage stage, Scene scene, String fxml) throws IOException {
		root = FXMLLoader.load(Functions.class.getResource(fxml));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public static void switchScene(MouseEvent event, Parent root, Stage stage, Scene scene, String fxml) throws IOException {
		root = FXMLLoader.load(Functions.class.getResource(fxml));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public static void showPlaceholderOnFocus(TextField textfield) {
		textfield.styleProperty().bind(
                Bindings
                        .when(textfield.focusedProperty())
                        .then("-fx-prompt-text-fill: derive(-fx-control-inner-background, -30%);")
                        .otherwise("-fx-prompt-text-fill: derive(-fx-control-inner-background, -30%);"));
	}
	
	public static void showPlaceholderOnFocus(PasswordField passwordfield) {
		passwordfield.styleProperty().bind(
                Bindings
                        .when(passwordfield.focusedProperty())
                        .then("-fx-prompt-text-fill: derive(-fx-control-inner-background, -30%);")
                        .otherwise("-fx-prompt-text-fill: derive(-fx-control-inner-background, -30%);"));
	}
	
	public static String[] generateYearList(int number) {
		String[] yearList = new String[number];
		int year = Calendar.getInstance().get(Calendar.YEAR);
		
		for (int i = 0 ; i< number; i++) {
			yearList[i] = String.valueOf(year);
			year--;
		}
		return yearList;
	}
	
	public static String padLeftZeros(String inputString, int length) {
	    if (inputString.length() >= length) {
	        return inputString;
	    }
	    StringBuilder sb = new StringBuilder();
	    while (sb.length() < length - inputString.length()) {
	        sb.append('0');
	    }
	    sb.append(inputString);

	    return sb.toString();
	}
	
	// Change opacity of button when hover
	public static void setChangeOpacity(Button button) {
		button.setOnMouseEntered(mouseEvent -> {
			button.setOpacity(0.7);
		});
		button.setOnMouseExited(mouseEvent -> {
			button.setOpacity(1);
		});
	}
	
	public static ArrayList<LocalTime> generateTimeFrameList() {
		ArrayList<LocalTime> timeFrames = new ArrayList<LocalTime>() ;
		LocalTime time = LocalTime.parse("09:00");
		
		do {
			timeFrames.add(time);
			time = time.plusMinutes(30);
		} while(time != LocalTime.parse("19:00"));
		
		return timeFrames;
	}
	
	
	// Extract image name from image path
	public static String getImageName(ImageView ImageView) {
		String imagePath = ImageView.getImage().getUrl();
		String imageName = "";
		if (imagePath.contains("/")) {
			imageName = imagePath.substring(imagePath.lastIndexOf("/") + 1, imagePath.length());
		} else if (imagePath.contains("\\")) {
			imageName = imagePath.substring(imagePath.lastIndexOf("\\") + 1, imagePath.length());
		}
		
		return imageName;
	}
	
	// Limit user input when typing into text field
	public static void limitTextFieldInput(TextField textField, String regex) {
		textField.textProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		    	textField.setText(newValue.replaceAll(regex, ""));
		    }
		});
	}
	
	// Save the image user select to a file
	public static boolean saveImageToFile(Image image, String fileName, String saveLocation) {		
		// Extract file extension from file name
		String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
		
		// Create new file to be save
		File file = new File(saveLocation + fileName);
		
		// ImageIO.write : Writes an image using an arbitrary ImageWriter that supports the given format to a File
		// SwingFXUtils.fromFXImage : Snapshots the specified JavaFX Image object and stores a copy of its pixels into a BufferedImage object
		try {
			ImageIO.write(SwingFXUtils.fromFXImage(image, null), fileExtension, file);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	// Limit the number of decimal places
	public static String limitDecimalPlaces(String value, int decimalPlaces) { 
		return String.format("%."+decimalPlaces+"f",Double.parseDouble(value));
	}
	
	
	public static void configureGenderComboBox(ComboBox<String> comboBox) {
		// Set options for combo box
		String[] gender = {"Male", "Female"};
		ObservableList<String> genderList = FXCollections.observableArrayList(gender);
		comboBox.setItems(genderList);
		
		// display prompt text	
		comboBox.setButtonCell(new PromptButtonCell<>("Please select"));
	}
	
	
	public static void configureCountryComboBox(ComboBox<String> comboBox) {
		// Set options for combo box
		ObservableList<String> countryList = FXCollections.observableArrayList();
		String[] countryCodes = Locale.getISOCountries();
		for (String countryCode : countryCodes) {
			Locale obj = new Locale("", countryCode);
			countryList.add(obj.getDisplayCountry());
		}
		comboBox.setItems(countryList);
		
		// display prompt text	
		comboBox.setButtonCell(new PromptButtonCell<>("Please select"));
	}
	
	// Set options for combo box
	public static void configureStatusComboBox(ComboBox<String> comboBox) {
		String[] status = {"Active", "Inactive"};
		ObservableList<String> statusList = FXCollections.observableArrayList(status);
		comboBox.setItems(statusList);
		
		// display prompt text	
		comboBox.setButtonCell(new PromptButtonCell<>("Please select"));
	}
	
	// Disable date options before today
	public static void disableDayBefore(DatePicker datePicker) {
		datePicker.setDayCellFactory(picker -> new DateCell() {
	        public void updateItem(LocalDate date, boolean empty) {
	            super.updateItem(date, empty);
	            LocalDate tomorrow = LocalDate.now().plusDays(1);

	            setDisable(empty || date.compareTo(tomorrow) < 0 );
	        }
	    });
	}
	
	public static String getMonthString(int monthInt) {
		String month = "";
		if(monthInt == 1) {
			month = "January";
		} else if (monthInt == 2) {
			month = "February";
		} else if (monthInt == 3) {
			month = "March";
		} else if (monthInt == 4) {
			month = "April";
		} else if (monthInt == 5) {
			month = "May";
		} else if (monthInt == 6) {
			month = "June";
		} else if (monthInt == 7) {
			month = "July";
		} else if (monthInt == 8) {
			month = "August";
		} else if (monthInt == 9) {
			month = "September";
		} else if (monthInt == 10) {
			month = "October";
		} else if (monthInt == 11) {
			month = "November";
		} else if (monthInt == 12) {
			month = "December";
		}
		return month;
	}

}

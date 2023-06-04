package application;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class CarThumbController {
	Parent root;
	Stage stage;
	Scene scene;
	private int carID;
	
    @FXML
    private Label carName;

    @FXML
    private Label carPrice;

    @FXML
    private ImageView carImage;
    
    public void setData(CarThumb carThumb) {
    	this.carID = carThumb.getID();
    	
    	Image image =  new Image(carThumb.getThumbPath());
    	carImage.setImage(image);
    	
    	carName.setText(carThumb.getName());
    	carPrice.setText(carThumb.getPrice());
    }
    
    public void switchToCarDetails(MouseEvent event) throws IOException {
    	FXMLLoader loader = new FXMLLoader();
	    loader.setLocation(getClass().getResource("CarDetails.fxml"));
	    root = loader.load();
	    scene = new Scene(root);
	    
	    // Pass specific car ID to car detail page
	    CarDetailsController controller = loader.getController();
	    controller.loadCarDetails(carID);
	    
	    stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
		stage.setScene(scene);
		stage.show();
    }
}

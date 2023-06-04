module application.car_rental_system {
    requires javafx.controls;
    requires javafx.fxml;
    requires poi;
    requires java.desktop;
    requires javafx.swing;
    requires java.mail;
    requires org.apache.pdfbox;


    opens application to javafx.fxml;
    exports application;
}
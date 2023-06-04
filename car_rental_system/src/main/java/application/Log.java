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

public class Log {
	
	private int ID;
	private LocalDateTime dateCreated;
	private int userID; // 0 indicates guest
	private String type;
	private String action;
	private String status;
	private int targetID;
	
	public Log() {
		this.ID = 0;
		this.dateCreated = null;
		this.userID = 0;
		this.type = null;
		this.action = null;
		this.status = null;
		this.targetID = 0;
	}
	
	public Log(int ID, LocalDateTime dateCreated, int userID, String type, int targetID, String action, String status) {
		this.ID = ID;
		this.dateCreated = dateCreated;
		this.userID = userID;
		this.type = type;
		this.targetID = targetID;
		this.action = action;
		this.status = status;
	}

	public int getID() {
		return ID;
	}

	public void setID(int ID) {
		this.ID = ID;
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

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getTargetID() {
		return targetID;
	}

	public void setTargetID(int targetID) {
		this.targetID = targetID;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getPlainText() throws FileNotFoundException {
		String plainText = "";
		String user = "";
		String logStatus = "";
		String logAction = "";
		String target = "";
		String logType = "";
		
		// User
		if(userID > 0) {
			user = User.getSpecificUserCredentials(userID).getName();
		} else {
			user = "Guest";
		}
		
		// Status
		if (status.equals("Success")) {
			logStatus = "succeed to";
		} else {
			logStatus = status.toLowerCase() + " to";
		}
		
		// Action
		if (action.equals("Login")) {
			logAction = "log in to";
		} else if (action.equals("Logout")) {
			logAction = "log out from";
		} else if (action.equals("Pay")) {
			logAction = "pay for";
		} else if (action.equals("Rented Out")) {
			logAction = "rent out";
		}  else if (action.equals("Completed")) {
			logAction = "complete";
		} else {
			logAction = action.toLowerCase();
		}
		
		// Target
		if (type.equals("Auth") | type.equals("Profile")) {
			if(targetID > 0) {
				target = User.getSpecificUserCredentials(targetID).getName() + "'s";
			} else {
				target = "an";
			}
		} else if (type.equals("Car")) {
			target = Car.getSpecificCarDetails(targetID).getName();
		} else if (type.equals("Customer")) {
			target = User.getSpecificUserCredentials(targetID).getName() + "'s";
		} else if (type.equals("Booking")) {
			if (action.equals("Rented Out")) {
				target = Booking.getSpecificBookingDetails(targetID).getCar().getName() + " to " + Booking.getSpecificBookingDetails(targetID).getCustomer().getName();
			} else {
				target = Booking.getSpecificBookingDetails(targetID).getCustomer().getName() + "'s";
			}
		}
		
		if (type.equals("Auth")) {
			logType = "account";
		} else if (type.equals("Customer")){
			logType = "details";
		} else if (type.equals("Car") & action.equals("Update")){
			logType = "details";
		} else if (action.equals("Rented Out")){
			logType = "";
		} else {
			logType = type.toLowerCase();
		}
		
		if (type.equals("Booking")) {
			plainText = String.join(" ", user, logStatus, logAction, target, logType , "on " + DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(dateCreated));
		} else {
			plainText = String.join(" ", user, logStatus, logAction, target, logType , "on " + DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(dateCreated));
		}
		return plainText;
	}
	
	// Read file and extract log details into a list
	public static ObservableList<Log> getLogsDetails() throws FileNotFoundException {
		ObservableList<Log> logList = FXCollections.observableArrayList();
		
		File file = new File("src/main/resources/txt/log.txt");
		if (file.exists()) {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String line;
			
			try {
				while((line = br.readLine()) != null) {
					String[] details = line.split("\\|");
					int ID = Integer.parseInt(details[0]);
					LocalDateTime dateCreated = LocalDateTime.parse(details[1]);
					int userID = Integer.parseInt(details[2]);
					String type = details[3];
					int targetID = Integer.parseInt(details[4]);
					String action = details[5];
					String status = details[6];
					
					Log log = new Log(ID, dateCreated, userID, type, targetID, action, status);
					logList.add(log);	
				}
				br.close();
				
				return logList;
			} catch (IOException e) {
				System.out.println("Failed to extract data from log text file");;
			}
		}
		return null;
	}
	
	// Add new log to text file
	public boolean addNewLog(Log log) throws IOException {
		File file = new File("src/main/resources/txt/log.txt");
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
			String newLine = String.join("|", String.valueOf(newID), log.getDateCreated().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")), String.valueOf(log.getUserID()), log.getType(), String.valueOf(log.getTargetID()), log.getAction(), log.getStatus());
			
		    bw.newLine();
		    bw.append(newLine);
		    bw.close();
		    return true;
		}
		return false;
	}
}

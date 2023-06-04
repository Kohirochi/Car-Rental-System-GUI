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
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class User {
	int ID;
	String name;
	String email;
	String password;
	String permission;
	String status;
	String moreUserDetails;
	
	User() {
		this.ID = 0;
		this.email = null;
		this.name = null;
		this.password = null;
		this.permission = null;
		this.status = null;
		this.moreUserDetails = null;
	}
	
	User (int ID, String name, String email, String password, String permission, String moreUserDetails, String status){
		this.ID = ID;
		this.email = email;
		this.name = name;
		this.password = password;
		this.permission = permission;
		this.status = status;
		this.moreUserDetails = moreUserDetails;
	}
	
	// Getters & Setters
	public int getID() {
		return ID;
	}

	public void setID(int ID) {
		this.ID = ID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}
	
	public String getMoreUserDetails() {
		return moreUserDetails;
	}

	public void setMoreUserDetails(String moreUserDetails) {
		this.moreUserDetails = moreUserDetails;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	// Set specific user details
	public void setAttributes(User user){
		setID(user.getID());
		setEmail(user.getEmail());
		setName(user.getName());
		setPassword(user.getPassword());
		setPermission(user.getPermission());
		setMoreUserDetails(user.getMoreUserDetails());
		setStatus(user.getStatus());
	}
	
	// Add new customer credentials to text file when register
	public static boolean register(String name, String email, String password, String status, Log log) throws IOException {
		File file = new File("src/main/resources/txt/user.txt");
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
			String moreDetails = String.join(";", "-", "-", "-", "-", "-");
			String newLine = String.join("|", String.valueOf(newID), name, email, password, "Customer", moreDetails, status );
			
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

	// Read file and extract users details into an arraylist
	public static ObservableList<User> getUsersCredentials() throws FileNotFoundException{
		ObservableList<User> userList = FXCollections.observableArrayList();
		
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
					String email = details[2];
					String password = details[3];
					String permission = details[4];
					String moreDetails = details[5];
					String status = details[6];
					
					if (!status.equals("Trash")) {
						User user = new User(ID, name, email, password, permission, moreDetails, status);
						userList.add(user);
					}
				}
				br.close();
				
				return userList;
			} catch (IOException e) {
				System.out.println("Failed to extract data from user text file");;
			}
		}
		return null;
	}
	
	// Read file and extract specific user details
	public static User getSpecificUserCredentials(int userID) throws FileNotFoundException{		
		File file = new File("src/main/resources/txt/user.txt");
		if (file.exists()) {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String line;
			User user = null;
			try {
				while((line = br.readLine()) != null) {
					String[] details = line.split("\\|");
					int ID = Integer.parseInt(details[0]);
					if (ID == userID) {
						String name = details[1];
						String email = details[2];
						String password = details[3];
						String permission = details[4];
						String moreDetails = details[5];
						String status = details[6];
						user = new User(ID, name, email, password, permission, moreDetails, status);
						break;
					}
					
				}
				br.close();
				return user;
			} catch (IOException e) {
				System.out.println("Failed to extract data from user text file");;
			}
		}
		return null;
	}
		
	public static int extractID(String loginEmail) throws FileNotFoundException {
		File file = new File("src/main/resources/txt/user.txt");
		if (file.exists()) {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String line;
			
			try {
				while((line = br.readLine()) != null) {
					String[] details = line.split("\\|");
					int ID = Integer.parseInt(details[0]);
					String email = details[2];
					String status = details[6];
					
					if (!status.equals("Trash")) {
						if (email.equals(loginEmail)) {
							br.close();
							return ID;
						}
					}
				}
			} catch (IOException e) {
				System.out.println("Failed to extract data from user text file");;
			}
		}
		return 0;
	}

	// Reset password
	public static boolean resetPassword(String ID, String name, String email, String password, String permission, String userDetails, String status, Log log) {
        List<String> usersCredentials = new ArrayList<>();
        String newCredentials = String.join("|", ID, name, email, password, permission, userDetails, status);
        
        try {
			for (String line : Files.readAllLines(Paths.get("src/main/resources/txt/user.txt"), StandardCharsets.UTF_8)) {
			    String[] credentials = line.split("\\|");
			    if (credentials[0].equals(ID)) {
			    	usersCredentials.add(line.replace(line, newCredentials));
			    } else {
			    	usersCredentials.add(line);
			    }
			}
	        Files.writeString(Paths.get("src/main/resources/txt/user.txt"), String.join("\n", usersCredentials), StandardCharsets.UTF_8);
	        
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
	
	// Administrator update user credentials 
	public static boolean updateUserCredentials(String ID, String newCredentials, Log log) {
        List<String> usersCredentials = new ArrayList<>();        
        try {
			for (String line : Files.readAllLines(Paths.get("src/main/resources/txt/user.txt"), StandardCharsets.UTF_8)) {
			    String[] credentials = line.split("\\|");
			    if (credentials[0].equals(ID)) {
			    	usersCredentials.add(line.replace(line, newCredentials));
			    } else {
			    	usersCredentials.add(line);
			    }
			}
	        Files.writeString(Paths.get("src/main/resources/txt/user.txt"), String.join("\n", usersCredentials), StandardCharsets.UTF_8);
	        
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
}

package application;

import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.Properties;
import java.util.Random;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SMTP {
	
	public static String sendMail(String receiverEmail) {
		
		final String senderEmail = "rentio2022@gmail.com";
		final String password = "dztenjpgyoztudbq";
		
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com"); // SMTP host
		props.put("mail.smtp.port", "587"); // TLS port
		props.put("mail.smtp.auth", "true"); // Enable authentication
		props.put("mail.smtp.starttls.enable", "true"); // Enable STARTTLS
		
		Authenticator auth = new Authenticator() {
			// Override the getPasswordAuthentication method
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(senderEmail, password);
			}
		};
		Session session = Session.getInstance(props, auth);
		
		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(senderEmail));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiverEmail));
			message.setSubject("Password Reset");
			String otp = generateOTP();
			message.setText("The OTP to reset your password is: " + otp);
			
			Transport.send(message);
			
			return otp;
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static boolean sendNotification(String receiverEmail, Booking booking, String status) throws FileNotFoundException {
		
		final String senderEmail = "rentio2022@gmail.com";
		final String password = "dztenjpgyoztudbq";
		
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com"); // SMTP host
		props.put("mail.smtp.port", "587"); // TLS port
		props.put("mail.smtp.auth", "true"); // Enable authentication
		props.put("mail.smtp.starttls.enable", "true"); // Enable STARTTLS
		
		Authenticator auth = new Authenticator() {
			// Override the getPasswordAuthentication method
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(senderEmail, password);
			}
		};
		Session session = Session.getInstance(props, auth);
		
		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(senderEmail));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiverEmail));
			message.setSubject("Notification");
			String text = "The booking that was made on " + booking.getDateCreatedString() + " has been " + status + ".\n" + 
							"Below are the booking details\n" +
							"Booking ID: " + booking.getID() + "\n" +
							"Car Name: " + booking.getCar().getName() + "\n" + 
							"Car Plate Number: " + Car.getSpecificCarDetails(booking.getCar().getID()).getPlateNumber() + "\n" +
							"Pick-up Date: " + booking.getPickUpDate() + " at " + booking.getPickUpTime() + "\n" +
							"Drop-off Date: " + booking.getDropOffDate() + " at " + booking.getDropOffTime() + "\n" +
							"Price: RM" + booking.getPrice() ;
			message.setText(text);
			Transport.send(message);
			
			return true;
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
	
	private static String generateOTP() {
		String otp = new DecimalFormat("000000").format(new Random().nextInt(999999));
		return otp;
	}
}

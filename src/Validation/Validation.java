package Validation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Formatter;
import java.util.HashMap;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

import Database.ConnectToDB;

/*
 *  Use for:
 * + Creating Email automatically (all the email is ended with @thuvienhanoi.com
 * + Normalize name
 * + Check valid password, phone number with some constraints
 * + Check variable int, double,...
 * 
 * 
 * 
 * 
 */


//For creating BookID



public class Validation {
	//For creating BookID
	private static HashMap<String, Integer> id_Counter = new HashMap<>();
	private static HashMap<String, String> id_Prefix = new HashMap<>();
	
	static {
		id_Counter.put("Fiction", 0);
        id_Counter.put("Non-Fiction", 0);
        id_Counter.put("Science Fiction", 0);
        id_Counter.put("Fantasy", 0);
        id_Counter.put("Romance", 0);
        id_Counter.put("Mystery", 0);
        id_Counter.put("Technology", 0);
        id_Counter.put("Education", 0);
        id_Counter.put("Children's Books", 0);
        id_Counter.put("Self-Help", 0);
        
        id_Prefix.put("Fiction", "F");
        id_Prefix.put("Non-Fiction", "NF");
        id_Prefix.put("Science Fiction", "SF");
        id_Prefix.put("Fantasy", "FA");
        id_Prefix.put("Romance", "R");
        id_Prefix.put("Mystery", "M");
        id_Prefix.put("Technology", "T");
        id_Prefix.put("Education", "E");
        id_Prefix.put("Children's Books", "CB");
        id_Prefix.put("Self-Help", "SH");
	}
	
	// Check if a string can be parsed to an int
	public static boolean isValidInt(String str) {
		try {
			Integer.parseInt(str);
			return true;
		} catch (NumberFormatException e) {
			// Show error message if the string is not a valid integer
			JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid integer.", "Input Error",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}

	// Check if the age is valid (within the range)
	public static boolean isValidAge(String ageStr, int min, int max) {
		if (isValidInt(ageStr)) {
			int age = Integer.parseInt(ageStr);

			// If age is out of range, display relevant error message
			if (age < min) {
				JOptionPane.showMessageDialog(null, "You are too young. Age must be at least " + min + ".",
						"Input Error", JOptionPane.ERROR_MESSAGE);
				return false;
			} else if (age > max) {
				JOptionPane.showMessageDialog(null, "You are too old. Age must be less than or equal to " + max + ".",
						"Input Error", JOptionPane.ERROR_MESSAGE);
				return false;
			}
			return true;
		}
		return false;
	}

	// Check if password is valid
	public static boolean isValidPassWord(String pass) {
		if (pass.length() < 8) {
			JOptionPane.showMessageDialog(null, "Password must be at least 8 characters long.", "Password Error",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if (!pass.matches(".*[A-Z].*")) {
			JOptionPane.showMessageDialog(null, "Password must have at least 1 uppercase characters.", "Password Error",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if (!pass.matches(".*[a-z].*")) {
			JOptionPane.showMessageDialog(null, "Password must have at least 1 lowercase characters.", "Password Error",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if (!pass.matches(".*\\d.*")) {
			JOptionPane.showMessageDialog(null, "Password must have at least 1 digit.", "Password Error",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}

		return true;
	}

	// Check ì phone number is valid
	public static boolean isValidPhoneNo(String phone) {
		String PHONE_REGEX = "\\d{10}";
		if (!phone.matches(PHONE_REGEX)) {
			JOptionPane.showMessageDialog(null, "Phone number is invalid.", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}

	public static boolean isValidCharacter(String string, String object) {
		String REGEX = "^[A-Za-z\\s]+$";
		if (!string.matches(REGEX)) {
			JOptionPane.showMessageDialog(null, object + " is invalid.", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}

	public static String normalizeName(String name) {
		StringBuilder nameBuilder = new StringBuilder();
		String[] nameElement = name.trim().split("\\s+");
		for (int i = 0; i < nameElement.length; i++) {

			nameBuilder.append(Character.toUpperCase(nameElement[i].charAt(0)))
					.append(nameElement[i].substring(1).toLowerCase());
			nameBuilder.append(" ");

		}
		return nameBuilder.toString().trim();
	}

	public static String createEmail(String name, String age) {
		String[] nameElement = name.trim().split("\\s+");
		StringBuilder mailBuilder = new StringBuilder();
		mailBuilder.append(nameElement[nameElement.length - 1].toLowerCase());
		for (int i = 0; i < nameElement.length - 1; i++) {
			mailBuilder.append(Character.toLowerCase(nameElement[i].charAt(0)));
		}
		mailBuilder.append(age);
		mailBuilder.append("@thuvienhanoi.com");
		return mailBuilder.toString().trim();
	}
	
	 // Method to check the administrator password (use in librarian login section)
    public static boolean checkAdminPassword() {
        String password = JOptionPane.showInputDialog(null, "Enter administrator password to view librarian list:");

        if (password == null || !password.equals("thuvienhanoi")) {
            JOptionPane.showMessageDialog(null, "Incorrect or invalid password!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }
    
    //Method to automatically create a BookID based on the categories 
    public static String createBookID(String categories) {
    	int counter = id_Counter.get(categories) + 1;
    	id_Counter.put(categories, counter);
    	String prefix = id_Prefix.get(categories);
    	return prefix + String.format("%03d", counter);
    }
    
    
    //Method to modify date form
    public static String getCurrentDate() {
    	Calendar now = Calendar.getInstance();
        Formatter formatter = new Formatter();
        
        // Format the current time
        String formattedTime = formatter.format("%1$tY-%1$tm-%1$td %1$tH:%1$tM", now).toString();
        formatter.close();
        return formattedTime;
	
    }
    
 // Method to validate if any text fields are empty
    public static boolean validateTextFields(JTextField... textFields) {
        for (JTextField textField : textFields) {
            if (textField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "All fields must be filled.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        return true;
    }
    
 // Method to ensure the studentID digit part is in valid form
    public static boolean isValidStudentID(String digit) {
        // Regular expression for a 6-digit number
        String StuID_REGEX = "\\d{6}";
        
        // Check if the length of the digit is exactly 6
        if (digit.length() != 6) {
            JOptionPane.showMessageDialog(null, "StudentID digit part should be exactly 6 digits long.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // Check if the digit matches the regex for a 6-digit number
        if (!digit.matches(StuID_REGEX)) {
            JOptionPane.showMessageDialog(null, "StudentID digit part should contain only numeric characters.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
    
    // Method to check if BookID (use in BorrowBookSection and Return Book Section) is valid
    public static boolean haveBookID(String bookID, String table) throws ClassNotFoundException, SQLException {
		Connection connection = ConnectToDB.getConnection();
		boolean isValid = false;

		if (connection != null) {
			try {
				String query = "SELECT * FROM " + table + " WHERE BookId = ?";
				PreparedStatement stm = connection.prepareStatement(query);
				stm.setString(1, bookID);
				ResultSet result = stm.executeQuery();

				if (result.next()) {
					isValid = true;
				}
				System.out.println("Connected to Database Successfully");

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				ConnectToDB.closeConnection(connection);
			}
		}
		return isValid;
	}
    
    // Method to check if StudentID (use in Return Book Section) is valid
    public static boolean haveStudentID(String StudentID) throws ClassNotFoundException, SQLException {
    	Connection connection = ConnectToDB.getConnection();
		boolean isValid = false;

		if (connection != null) {
			try {
				String query = "SELECT * FROM borrowedbook WHERE StudentID = ?";
				PreparedStatement stm = connection.prepareStatement(query);
				stm.setString(1, StudentID);
				ResultSet result = stm.executeQuery();

				if (result.next()) {
					isValid = true;
				}
				System.out.println("Connected to Database Successfully");

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				ConnectToDB.closeConnection(connection);
			}
		}
		return isValid;
    }



}

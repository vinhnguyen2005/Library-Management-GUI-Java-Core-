package Admin;

import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.border.EmptyBorder;

import Database.ConnectToDB;
import Validation.Validation;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddLibrarian extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField nameField;
	private JPasswordField passwordField;
	private JTextField phoneNumberField;
	private JTextField addressField;
	private JTextField cityField;
	private JTextField ageField;

	public AddLibrarian(AdminSection adminsection) {
		setTitle("Add Librarian");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 435, 548);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// Add components
		addComponent("Name", 30, 44, nameField = new JTextField(), 136, 44);
		addComponent("Age", 30, 101, ageField = new JTextField(), 136, 101);
		addComponent("Password", 30, 159, passwordField = new JPasswordField(), 136, 163);
		addComponent("Phone Number", 30, 220, phoneNumberField = new JTextField(), 155, 224);
		addComponent("Address", 30, 283, addressField = new JTextField(), 136, 287);
		addComponent("City", 30, 339, cityField = new JTextField(), 136, 343);

		JButton btnAdd = new JButton("ADD");
		btnAdd.setFont(new Font("Arial", Font.BOLD, 15));
		btnAdd.setBounds(147, 402, 109, 31);
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name = nameField.getText().trim();
				String age = ageField.getText().trim();
				String pass = new String(passwordField.getPassword()).trim();
				String phoneNumber = phoneNumberField.getText().trim();
				String address = addressField.getText().trim();
				String city = cityField.getText().trim();

				try {
					if (canAdd(name, age, pass, phoneNumber, address, city)) {
						JOptionPane.showMessageDialog(null, "Add librarian successful", "Success",
								JOptionPane.INFORMATION_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(null, "Add librarian failed", "Error", JOptionPane.ERROR_MESSAGE);
					}
				} catch (ClassNotFoundException | SQLException ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage(), "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		contentPane.add(btnAdd);

		JButton btnBack = new JButton("Back");
		btnBack.setFont(new Font("Arial", Font.PLAIN, 13));
		btnBack.setBounds(113, 464, 73, 27);
		btnBack.addActionListener(e -> {
			dispose(); // Close current frame
			adminsection.setVisible(true); // Return to the main admin section menu
		});
		contentPane.add(btnBack);
		setResizable(false);

		JButton btnClear = new JButton("Clear");
		btnClear.setFont(new Font("Arial", Font.PLAIN, 13));
		btnClear.setBounds(222, 464, 73, 27);
		btnClear.addActionListener(e -> {
			nameField.setText("");
			ageField.setText("");
			passwordField.setText("");
			phoneNumberField.setText("");
			addressField.setText("");
			cityField.setText("");
		});
		contentPane.add(btnClear);

		btnAdd.setFocusable(false);
		btnClear.setFocusable(false);
		btnBack.setFocusable(false);
	}

	private void addComponent(String labelText, int labelX, int labelY, JTextField textField, int fieldX, int fieldY) {
		JLabel label = new JLabel(labelText);
		label.setFont(new Font("Tahoma", Font.BOLD, 15));
		label.setBounds(labelX, labelY, 150, 31);
		contentPane.add(label);

		textField.setBounds(fieldX, fieldY, 236, 27);
		contentPane.add(textField);
	}

	private boolean canAdd(String name, String age, String pass, String phoneNumber, String address, String city)
			throws ClassNotFoundException, SQLException {
		Connection connection = ConnectToDB.getConnection();
		boolean isValid = false;

		boolean check = Validation.isValidCharacter(name, "Name Field") && Validation.isValidAge(age, 18, 100)
				&& Validation.isValidPassWord(pass) && Validation.isValidPhoneNo(phoneNumber)
				&& Validation.isValidCharacter(city, "City Field");

		if (check) {
			if (connection != null) {
				System.out.println("Connected to Database Successfully");

				String modifiedName = Validation.normalizeName(name);
				String email = Validation.createEmail(modifiedName, age);

				String query = "INSERT INTO librariantable (name, age, email, password, phonenumber, address, city) VALUES (?, ?, ?, ?, ?, ?, ?)";
				try (PreparedStatement stm = connection.prepareStatement(query)) {
					stm.setString(1, modifiedName);
					stm.setString(2, age);
					stm.setString(3, email);
					stm.setString(4, pass);
					stm.setString(5, phoneNumber);
					stm.setString(6, address);
					stm.setString(7, city);

					int rowsUpdated = stm.executeUpdate();
					isValid = rowsUpdated > 0;
				} finally {
					ConnectToDB.closeConnection(connection);
				}
			} else {
				System.out.println("Failed to connect to the database.");
			}
		} else {
			System.out.println("Validation failed. Please correct the input.");
		}

		return isValid;
	}
}

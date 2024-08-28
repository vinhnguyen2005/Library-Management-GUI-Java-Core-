package Librarian;

import java.awt.Color;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import Database.ConnectToDB;
import Validation.Validation;

public class BorrowBookSection extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel mainPanel;
	private JTextField studentIDField;
	private JTextField bookIDField;
	private JTextField studentNameField;
	private JTextField phoneNumberField;
	private JLabel idLabel;
	private JComboBox<String> categoryComboBox;

	public BorrowBookSection(Librariansection librarianSection) {
		setTitle("Borrow Book");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 501, 488);
		mainPanel = new JPanel();
		mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(mainPanel);
		mainPanel.setLayout(null);

		// Labels
		JLabel studentIDLabel = new JLabel("Student ID:");
		studentIDLabel.setFont(new Font("Tahoma", Font.BOLD, 17));
		studentIDLabel.setBounds(44, 92, 103, 45);
		mainPanel.add(studentIDLabel);

		JLabel bookIDLabel = new JLabel("Book ID:");
		bookIDLabel.setFont(new Font("Tahoma", Font.BOLD, 17));
		bookIDLabel.setBounds(44, 30, 103, 45);
		mainPanel.add(bookIDLabel);

		JLabel studentNameLabel = new JLabel("Student Name:");
		studentNameLabel.setFont(new Font("Tahoma", Font.BOLD, 17));
		studentNameLabel.setBounds(44, 167, 131, 45);
		mainPanel.add(studentNameLabel);

		JLabel phoneNumberLabel = new JLabel("Phone Number:");
		phoneNumberLabel.setFont(new Font("Tahoma", Font.BOLD, 17));
		phoneNumberLabel.setBounds(44, 237, 140, 45);
		mainPanel.add(phoneNumberLabel);

		// Category ComboBox
		String[] bookCategories = { "HE", "HA", "HS", "HB" };
		categoryComboBox = new JComboBox<>(bookCategories);
		categoryComboBox.setFont(new Font("Tahoma", Font.PLAIN, 12));
		categoryComboBox.setBounds(211, 105, 71, 22);
		mainPanel.add(categoryComboBox);

		// TextFields
		studentIDField = new JTextField();
		studentIDField.setBounds(292, 106, 158, 22);
		mainPanel.add(studentIDField);
		studentIDField.setColumns(10);

		bookIDField = new JTextField();
		bookIDField.setColumns(10);
		bookIDField.setBounds(211, 44, 239, 22);
		mainPanel.add(bookIDField);

		studentNameField = new JTextField();
		studentNameField.setColumns(10);
		studentNameField.setBounds(211, 181, 239, 22);
		mainPanel.add(studentNameField);

		phoneNumberField = new JTextField();
		phoneNumberField.setColumns(10);
		phoneNumberField.setBounds(211, 251, 239, 22);
		mainPanel.add(phoneNumberField);

		// Buttons
		JButton borrowButton = new JButton("Borrow");
		borrowButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Code for borrowing book
				String bookID = bookIDField.getText().toUpperCase();
				String digitStudentID = studentIDField.getText();
				String prefixStudentID = (String) categoryComboBox.getSelectedItem();
				String studentName = studentNameField.getText();
				String phoneNumber = phoneNumberField.getText();

				try {
					if (canBorrowBook(bookID, digitStudentID, prefixStudentID, studentName, phoneNumber)) {
						JOptionPane.showMessageDialog(null, "Borrow book successful", "Success",
								JOptionPane.INFORMATION_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(null, "Borrow book failed", "Error", JOptionPane.ERROR_MESSAGE);
					}
				} catch (HeadlessException | ClassNotFoundException | SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		borrowButton.setFont(new Font("Tahoma", Font.BOLD, 14));
		borrowButton.setBounds(189, 340, 103, 37);
		mainPanel.add(borrowButton);

		JButton backButton = new JButton("Back");
		backButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				librarianSection.setVisible(true);
				dispose();
			}
		});
		backButton.setBounds(129, 399, 77, 37);
		mainPanel.add(backButton);

		JButton clearButton = new JButton("Clear");
		clearButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
		clearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				studentIDField.setText("");
				bookIDField.setText("");
				studentNameField.setText("");
				phoneNumberField.setText("");
			}
		});
		clearButton.setBounds(278, 399, 77, 37);
		mainPanel.add(clearButton);

		idLabel = new JLabel("Your ID:");
		idLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
		idLabel.setBounds(211, 142, 195, 14);
		idLabel.setForeground(Color.BLUE);
		mainPanel.add(idLabel);
		
		JLabel lblNewLabel = new JLabel("( Ex: HE190300, ...)");
		lblNewLabel.setBounds(44, 130, 103, 14);
		mainPanel.add(lblNewLabel);
		
		//Use documentlistener to provide instant feedback based on the current content of the text field
		studentIDField.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				updateIDLabel();
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				updateIDLabel();
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				updateIDLabel();
			}
		});

		// Add ActionListener to JComboBox
		categoryComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Get the selected category
				updateIDLabel();
			}
		});
	}

	private void updateIDLabel() {
		String selectedCategory = (String) categoryComboBox.getSelectedItem();
		idLabel.setText("Your ID: " + selectedCategory + studentIDField.getText());
		idLabel.setForeground(Color.BLUE);
		
	}
	

	private boolean canBorrowBook(String bookID, String digitStudentID, String prefixStudentID, String studentName, String phoneNumber) throws ClassNotFoundException, SQLException {
		Connection connection = ConnectToDB.getConnection();
		boolean isValid = false;

		if(!Validation.haveBookID(bookID, "booklist")) {
			return false;
		}

		boolean check = Validation.validateTextFields(bookIDField, studentNameField, phoneNumberField) 
			&& Validation.isValidPhoneNo(phoneNumber) 
			&& Validation.isValidStudentID(digitStudentID) 
			&& Validation.isValidCharacter(studentName, "Student Name");

		if (check) {
			if (connection != null) {
				System.out.println("Connected to Database Successfully");
				String studentID = prefixStudentID + digitStudentID;
				String normalizedStudentName = Validation.normalizeName(studentName);
				String borrowedDate = Validation.getCurrentDate();

				String query = "INSERT INTO borrowedbook (BookId, StudentID, StudentName, StudentPhoneNo, BorrowedDate) VALUES (?, ?, ?, ?, ?)";
				try (PreparedStatement stm = connection.prepareStatement(query)) {
					stm.setString(1, bookID.toUpperCase());
					stm.setString(2, studentID);
					stm.setString(3, normalizedStudentName);
					stm.setString(4, phoneNumber);
					stm.setString(5, borrowedDate);

					int rowsUpdated = stm.executeUpdate();
					isValid = rowsUpdated > 0;
					if (isValid) {
						updateBookList(bookID);
					}
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
	
	//Method to update borrowed book column in AddBook section
	private void updateBookList(String bookID) throws ClassNotFoundException, SQLException {
		Connection connection = ConnectToDB.getConnection();
		if(connection != null) {
			String query = "UPDATE booklist SET Borrowed = Borrowed + 1 WHERE BookId = ?";
			try (PreparedStatement stm = connection.prepareStatement(query)) {
				stm.setString(1, bookID);
				int rowsUpdated = stm.executeUpdate();
				if (rowsUpdated > 0) {
					System.out.println("Book borrowed count updated successfully.");
				} else {
					System.out.println("Failed to update the borrowed count.");
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				ConnectToDB.closeConnection(connection);
			}
		}
	}

}

package Librarian;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Database.ConnectToDB;
import Validation.Validation;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.HeadlessException;

import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.awt.event.ActionEvent;

public class ReturnBookSection extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel mainPanel;
	private JTextField bookIdField;
	private JTextField studentIdField;

	public ReturnBookSection(Librariansection librarianSection) {
		setTitle("Return Book");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 420, 332);
		mainPanel = new JPanel();
		mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(mainPanel);
		mainPanel.setLayout(null);

		JLabel bookIdLabel = new JLabel("BookID");
		bookIdLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		bookIdLabel.setBounds(46, 43, 62, 32);
		mainPanel.add(bookIdLabel);

		JLabel studentIdLabel = new JLabel("StudentID");
		studentIdLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		studentIdLabel.setBounds(46, 126, 80, 32);
		mainPanel.add(studentIdLabel);

		bookIdField = new JTextField();
		bookIdField.setBounds(154, 51, 199, 20);
		mainPanel.add(bookIdField);
		bookIdField.setColumns(10);

		studentIdField = new JTextField();
		studentIdField.setColumns(10);
		studentIdField.setBounds(154, 134, 199, 20);
		mainPanel.add(studentIdField);

		JButton returnBookButton = new JButton("Return Book");
		returnBookButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Add your logic here
				String bookId = bookIdField.getText().toUpperCase();
				String studentId = studentIdField.getText().toUpperCase();
				try {
					if (canReturn(bookId, studentId)) {
						JOptionPane.showMessageDialog(null, "Return book successful", "Success", JOptionPane.INFORMATION_MESSAGE);
					}
					else {
						JOptionPane.showMessageDialog(null, "Return book failed", "Error", JOptionPane.ERROR_MESSAGE);
					}
				} catch (HeadlessException | ClassNotFoundException | SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});
		returnBookButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
		returnBookButton.setBounds(69, 211, 105, 32);
		mainPanel.add(returnBookButton);

		JButton backButton = new JButton("Back");
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				librarianSection.setVisible(true);
				dispose();
			}
		});
		backButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
		backButton.setBounds(248, 211, 105, 32);
		mainPanel.add(backButton);
	}

	// Method to check if user can return book
	private boolean canReturn(String bookId, String studentId) throws ClassNotFoundException, SQLException {
		Connection connection = ConnectToDB.getConnection();
		boolean check = Validation.validateTextFields(bookIdField, studentIdField)
				&& Validation.haveBookID(bookId, "borrowedbook") && Validation.haveStudentID(studentId);
		boolean isValid = false;
		if (check) {
			if (connection != null) {
				// Delete borrowed book from database
				String query = "DELETE FROM borrowedbook WHERE BookId = ? AND StudentID = ?";
				try (PreparedStatement stm = connection.prepareStatement(query)) {
					stm.setString(1, bookId);
					stm.setString(2, studentId);
					int rowUpdated = stm.executeUpdate();
					isValid = rowUpdated > 0;
					if (isValid) {
						updateBookList(bookId);
					}
				}
				finally {
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

	// Method to update borrowed book column in AddBook section
	private void updateBookList(String bookID) throws ClassNotFoundException, SQLException {
		Connection connection = ConnectToDB.getConnection();
		if (connection != null) {
			String query = "UPDATE booklist SET Borrowed = Borrowed - 1 WHERE BookId = ?";
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

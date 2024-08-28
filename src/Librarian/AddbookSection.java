package Librarian;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Database.ConnectToDB;
import Validation.Validation;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JButton;

public class AddbookSection extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField nameField;
	private JTextField authorField;
	private JTextField publisherField;
	private JTextField quantityField;
	private JLabel lblCategoryPrompt;

	public AddbookSection(Librariansection librasection) {
		setTitle("Add Book");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 550, 528);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// Labels
		JLabel lblCategories = new JLabel("Categories:");
		lblCategories.setFont(new Font("Tahoma", Font.BOLD, 17));
		lblCategories.setBounds(44, 32, 103, 45);
		contentPane.add(lblCategories);

		JLabel lblName = new JLabel("Name:");
		lblName.setFont(new Font("Tahoma", Font.BOLD, 17));
		lblName.setBounds(44, 99, 103, 45);
		contentPane.add(lblName);

		JLabel lblAuthor = new JLabel("Author:");
		lblAuthor.setFont(new Font("Tahoma", Font.BOLD, 17));
		lblAuthor.setBounds(44, 155, 103, 45);
		contentPane.add(lblAuthor);

		JLabel lblPublisher = new JLabel("Publisher:");
		lblPublisher.setFont(new Font("Tahoma", Font.BOLD, 17));
		lblPublisher.setBounds(44, 218, 103, 45);
		contentPane.add(lblPublisher);

		JLabel lblQuantity = new JLabel("Quantity:");
		lblQuantity.setFont(new Font("Tahoma", Font.BOLD, 17));
		lblQuantity.setBounds(44, 274, 103, 45);
		contentPane.add(lblQuantity);

		// Category ComboBox
		String[] categories = { "Fiction", "Non-Fiction", "Science Fiction", "Fantasy", "Romance", "Mystery",
				"Technology", "Education", "Children's Books", "Self-Help" };
		JComboBox<String> comboBox = new JComboBox<>(categories);
		comboBox.setFont(new Font("Tahoma", Font.PLAIN, 12));
		comboBox.setBounds(194, 46, 88, 22);
		contentPane.add(comboBox);

		// Category Label
		lblCategoryPrompt = new JLabel("You have chosen:");
		lblCategoryPrompt.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblCategoryPrompt.setBounds(292, 44, 229, 22);
		lblCategoryPrompt.setForeground(Color.BLUE);
		contentPane.add(lblCategoryPrompt);

		// TextFields
		nameField = new JTextField();
		nameField.setBounds(194, 113, 239, 22);
		contentPane.add(nameField);
		nameField.setColumns(10);

		authorField = new JTextField();
		authorField.setColumns(10);
		authorField.setBounds(194, 170, 239, 22);
		contentPane.add(authorField);

		publisherField = new JTextField();
		publisherField.setColumns(10);
		publisherField.setBounds(194, 233, 239, 22);
		contentPane.add(publisherField);

		quantityField = new JTextField();
		quantityField.setColumns(10);
		quantityField.setBounds(194, 289, 239, 22);
		contentPane.add(quantityField);

		// Buttons
		JButton btnAdd = new JButton("ADD");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String categories = (String) comboBox.getSelectedItem();
				String name = nameField.getText().trim();
				String author = authorField.getText().trim();
				String publisher = publisherField.getText().trim();
				String quantity = quantityField.getText().trim();
				
				try {
					if(canAdd(categories, name, author, publisher, quantity)) {
						JOptionPane.showMessageDialog(null, "Add book successful", "Success", JOptionPane.INFORMATION_MESSAGE);
					}
					else {
						JOptionPane.showMessageDialog(null, "Add book failed", "Error", JOptionPane.ERROR_MESSAGE);
					}
				} catch (ClassNotFoundException | SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					JOptionPane.showMessageDialog(null, "Database error: " + e1.getMessage(), "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnAdd.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnAdd.setBounds(227, 346, 103, 37);
		contentPane.add(btnAdd);

		JButton btnBack = new JButton("Back");
		btnBack.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				librasection.setVisible(true);
				dispose();
			}
		});
		btnBack.setBounds(165, 406, 77, 37);
		contentPane.add(btnBack);

		JButton btnHome = new JButton("Clear");
		btnHome.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnHome.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				nameField.setText("");
				authorField.setText("");
				publisherField.setText("");
				quantityField.setText("");
			}
		});
		btnHome.setBounds(321, 406, 77, 37);
		contentPane.add(btnHome);

		// Add ActionListener to JComboBox
		comboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Get the selected category
				String selectedCategory = (String) comboBox.getSelectedItem();
				lblCategoryPrompt.setText("You have chosen: " + selectedCategory);
				lblCategoryPrompt.setForeground(Color.BLUE);
			}
		});
	}
	
	private boolean canAdd(String catgories, String name, String author, String publisher, String quantity) throws ClassNotFoundException, SQLException {
		Connection connection = ConnectToDB.getConnection();
		boolean isValid = false;
		boolean check = Validation.validateTextFields(nameField, authorField, publisherField, quantityField) && Validation.isValidCharacter(publisher, "Publisher") && Validation.isValidInt(quantity);
		String bookID = Validation.createBookID(catgories);
		if (!isValidBookId(bookID)) {
			JOptionPane.showMessageDialog(null, "The book is already added", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		if (check) {
			if (connection != null) {
				System.out.println("Connected to Database Successfully");
				String normalizedName = Validation.normalizeName(name);
				String normalizedAuthor = Validation.normalizeName(author);
				int borrowed = 0;
				String addedDate = Validation.getCurrentDate();

				String query = "INSERT INTO booklist (BookID, Name, Author, Publisher, Quantity, Borrowed, Added_Date) VALUES (?, ?, ?, ?, ?, ?, ?)";
				try (PreparedStatement stm = connection.prepareStatement(query)) {
					stm.setString(1, bookID);
					stm.setString(2, normalizedName);
					stm.setString(3, normalizedAuthor);
					stm.setString(4, publisher);
					stm.setString(5, quantity);
					stm.setInt(6, borrowed);
					stm.setString(7, addedDate);

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
	
	//Method to check if book is already added or not
	private boolean isValidBookId(String bookID) throws ClassNotFoundException, SQLException {
		Connection connection = ConnectToDB.getConnection();
	    boolean isValid= false;
	    try {
	        if (connection != null) {
	            System.out.println("Connected to Database Successfully");
	            String query = "SELECT COUNT(*) FROM booklist WHERE BookId = ?";
	            PreparedStatement stm = connection.prepareStatement(query);
	            stm.setString(1, bookID);
	            ResultSet rs = stm.executeQuery();
	            if (rs.next()) {
	                isValid = rs.getInt(1) > 0;
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        ConnectToDB.closeConnection(connection);
	    }
	    return isValid;
	}
}

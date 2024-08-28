package Librarian;

import Admin.*;
import Database.ConnectToDB;
import Validation.Validation;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class LibrarianLoginForm extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField usernameTextField;
	private JPasswordField passwordField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LibrarianLoginForm frame = new LibrarianLoginForm();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public LibrarianLoginForm() {
		setTitle("Admin Registration");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 497, 310);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel titleLabel = new JLabel("Librarian Login Form");
		titleLabel.setFont(new Font("Times New Roman", Font.BOLD, 20));
		titleLabel.setBounds(148, 11, 194, 29);
		contentPane.add(titleLabel);

		JLabel usernameLabel = new JLabel("Enter Email");
		usernameLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		usernameLabel.setBounds(54, 66, 96, 29);
		contentPane.add(usernameLabel);

		JLabel passwordLabel = new JLabel("Enter Password");
		passwordLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		passwordLabel.setBounds(54, 138, 115, 29);
		contentPane.add(passwordLabel);

		usernameTextField = new JTextField();
		usernameTextField.setBounds(196, 66, 204, 26);
		contentPane.add(usernameTextField);
		usernameTextField.setColumns(10);

		passwordField = new JPasswordField();
		passwordField.setBounds(196, 138, 204, 26);
		contentPane.add(passwordField);

		JButton loginButton = new JButton("Login");
		loginButton.setFont(new Font("Tahoma", Font.BOLD, 11));
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String email = usernameTextField.getText();
				String pass = new String(passwordField.getPassword()); // Use getPassword() to retrieve the password
				try {
					if (validLogin(email, pass)) {
						JOptionPane.showMessageDialog(null, "Login Successful", "Success",
								JOptionPane.INFORMATION_MESSAGE);
						Librariansection librariansection = new Librariansection();
						librariansection.setVisible(true);

					} else {
						JOptionPane.showMessageDialog(null, "Login Unsuccessful", "Failed",
								JOptionPane.INFORMATION_MESSAGE);
					}
				} catch (HeadlessException | ClassNotFoundException | SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		loginButton.setBounds(133, 191, 80, 29);
		contentPane.add(loginButton);
		loginButton.setFocusable(false);

		JButton homeButton = new JButton("Home");
		homeButton.setFont(new Font("Tahoma", Font.BOLD, 11));
		homeButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				RegistrationForm register = new RegistrationForm();
				register.setVisible(true);
				dispose();
			}

		});
		homeButton.setBounds(203, 231, 96, 29);
		homeButton.setFocusable(false);
		contentPane.add(homeButton);
		JButton btnForgot = new JButton("Forgot");
		btnForgot.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		    	 // Call Validation to check admin password
		        if (Validation.checkAdminPassword()) {
		            try {
		                // Show the librarian list if password is correct
		                new ViewLibrarianList(LibrarianLoginForm.this).setVisible(true);
		            } catch (ClassNotFoundException | SQLException ex) {
		                ex.printStackTrace();
		            }
		        }
		    }
		});

		btnForgot.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnForgot.setBounds(284, 191, 80, 29);
		btnForgot.setFocusable(false);
		contentPane.add(btnForgot);
	}
	

	private boolean validLogin(String email, String pass) throws ClassNotFoundException, SQLException {
		Connection connection = ConnectToDB.getConnection();
		boolean isValid = false;

		if (connection != null) {
			try {
				String query = "SELECT * FROM librariantable WHERE email = ? AND password = ?";
				PreparedStatement stm = connection.prepareStatement(query);
				stm.setString(1, email);
				stm.setString(2, pass);
				ResultSet result = stm.executeQuery();

				if (result.next()) {
					isValid = true;
				}
				System.out.println("Connect to Database Successfully");

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				ConnectToDB.closeConnection(connection);
			}
		}
		return isValid;
	}
}

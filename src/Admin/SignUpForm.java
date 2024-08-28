package Admin;

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


import Database.ConnectToDB;

public class SignUpForm extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField usernameTextField;
	private JPasswordField passwordField; 

	public SignUpForm(AdminLoginForm loginform) {
		setTitle("Admin Registration");
		setResizable(false);
		setEnabled(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 497, 310);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel titleLabel = new JLabel("Admin SignUp Form");
		titleLabel.setFont(new Font("Times New Roman", Font.BOLD, 20));
		titleLabel.setBounds(148, 11, 194, 29);
		contentPane.add(titleLabel);

		JLabel usernameLabel = new JLabel("Enter Name");
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

		JButton signUpButton = new JButton("Sign Up");
		signUpButton.setBounds(169, 206, 89, 23);
		getContentPane().add(signUpButton);

		signUpButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String username = usernameTextField.getText().trim();
				String password = new String(passwordField.getPassword()).trim(); // Use getPassword() to retrieve the password
				try {
					if (registerUser(username, password)) {
						JOptionPane.showMessageDialog(null, "Sign-Up Successful", "Success",
								JOptionPane.INFORMATION_MESSAGE);
						dispose(); // close the window
						loginform.setVisible(true); // Login with the account created
					} else {
						JOptionPane.showMessageDialog(null, "Sign-Up Failed", "Failed",
								JOptionPane.INFORMATION_MESSAGE);
					}
				} catch (HeadlessException | ClassNotFoundException | SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		signUpButton.setFocusable(false);
	}

	private boolean registerUser(String username, String password) throws ClassNotFoundException, SQLException {
	    if (username.trim().isEmpty() || password.trim().isEmpty()) {
	        // Username or Password cannot be null or empty
	        JOptionPane.showMessageDialog(null, "Username or Password cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
	        return false;
	    }

	    // Check if username already exists
	    if (isLogin(username, "Admin_username")) {
	        JOptionPane.showMessageDialog(null, "Username already exists. Please choose a different username.", "Error", JOptionPane.ERROR_MESSAGE);
	        return false;
	    }

	    Connection connection = ConnectToDB.getConnection();
	    boolean isRegistered = false;
	    try {
	        if (connection != null) {
	            System.out.println("Connected to Database Successfully");
	            String query = "INSERT INTO loginadmin (Admin_username, Admin_password) VALUES (?, ?)";
	            PreparedStatement stm = connection.prepareStatement(query);
	            stm.setString(1, username);
	            stm.setString(2, password);
	            int rowsUpdated = stm.executeUpdate();
	            isRegistered = (rowsUpdated > 0);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        ConnectToDB.closeConnection(connection);
	    }
	    return isRegistered;
	}

	private boolean isLogin(String value, String column) throws ClassNotFoundException, SQLException {
	    Connection connection = ConnectToDB.getConnection();
	    boolean isLogin = false;
	    try {
	        if (connection != null) {
	            System.out.println("Connected to Database Successfully");
	            String query = "SELECT COUNT(*) FROM loginadmin WHERE Admin_username = ?";
	            PreparedStatement stm = connection.prepareStatement(query);
	            stm.setString(1, value);
	            ResultSet rs = stm.executeQuery();
	            if (rs.next()) {
	                isLogin = rs.getInt(1) > 0;
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        ConnectToDB.closeConnection(connection);
	    }
	    return isLogin;
	}
}
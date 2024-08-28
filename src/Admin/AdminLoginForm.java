package Admin;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Database.ConnectToDB;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Font;
import java.awt.HeadlessException;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.event.ActionEvent;

public class AdminLoginForm extends JFrame {

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
					AdminLoginForm frame = new AdminLoginForm();
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
	public AdminLoginForm() {
		setTitle("Admin Registration");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 497, 326);
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel titleLabel = new JLabel("Admin Login Form");
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

		JButton loginButton = new JButton("Login");
		loginButton.setFont(new Font("Tahoma", Font.BOLD, 11));
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String username = usernameTextField.getText().trim();
				String pass = new String(passwordField.getPassword()).trim(); // Use getPassword() to retrieve the password
				try {
					if (validLogin(username, pass)) {
						JOptionPane.showMessageDialog(null, "Login Successful", "Success",
								JOptionPane.INFORMATION_MESSAGE);
						AdminSection admin = new AdminSection();
						admin.setVisible(true);

					} else {
						JOptionPane.showMessageDialog(null, "Login Unsuccessful", "Failed",
								JOptionPane.INFORMATION_MESSAGE);
					}
				} catch (HeadlessException | ClassNotFoundException | SQLException e1) {
					e1.printStackTrace();
				}
				dispose();
			}
		});
		loginButton.setBounds(109, 207, 89, 29);
		contentPane.add(loginButton);

		JButton signUpButton = new JButton("Sign up");
		signUpButton.setFont(new Font("Tahoma", Font.BOLD, 11));
		signUpButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				new SignUpForm(AdminLoginForm.this).setVisible(true);
			}
		});
		signUpButton.setBounds(272, 207, 89, 29);
		contentPane.add(signUpButton);
		
		JButton homeButton = new JButton("Home");
		homeButton.setFont(new Font("Tahoma", Font.BOLD, 11));
		homeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RegistrationForm register = new RegistrationForm();
				register.setVisible(true);
				dispose();
			}
		});
		homeButton.setBounds(190, 247, 96, 29);
		contentPane.add(homeButton);
	}

	private boolean validLogin(String username, String pass) throws ClassNotFoundException, SQLException {
		Connection connection = ConnectToDB.getConnection();
		boolean isValid = false;

		if (connection != null) {
			try {
				String query = "SELECT * FROM loginadmin WHERE Admin_username = ? AND Admin_password = ?";
				PreparedStatement stm = connection.prepareStatement(query);
				stm.setString(1, username);
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

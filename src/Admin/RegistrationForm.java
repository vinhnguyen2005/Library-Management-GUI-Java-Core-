package Admin;

import java.awt.EventQueue;
import Librarian.LibrarianLoginForm;
import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class RegistrationForm extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RegistrationForm frame = new RegistrationForm();
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
	public RegistrationForm() {
		setTitle("Management Software");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 509, 376);
		contentPane = new JPanel();
		setResizable(false);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Library Management System");
		lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 25));
		lblNewLabel.setBounds(88, 24, 336, 56);
		contentPane.add(lblNewLabel);
		
		JButton btnAdminAccess = new JButton("Admin Login");
		btnAdminAccess.addActionListener(new ActionListener() {
			// Create the Admin Registration Form
			public void actionPerformed(ActionEvent e) {
				AdminLoginForm adminLogin = new AdminLoginForm();
				adminLogin.setVisible(true);
				dispose();
			}
		});
		btnAdminAccess.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnAdminAccess.setBounds(153, 104, 178, 69);
		contentPane.add(btnAdminAccess);
		
		
		JButton btnLibrarianAccess = new JButton("Librarian Login");
		btnLibrarianAccess.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LibrarianLoginForm librarianLogin = new LibrarianLoginForm();
				librarianLogin.setVisible(true);
				dispose();
			}
		});
		btnLibrarianAccess.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnLibrarianAccess.setBounds(153, 207, 178, 69);
		contentPane.add(btnLibrarianAccess);
		
		btnAdminAccess.setFocusable(false);
		btnLibrarianAccess.setFocusable(false);
	}
}

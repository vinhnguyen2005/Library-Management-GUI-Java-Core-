package Admin;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.awt.event.ActionEvent;

public class AdminSection extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AdminSection frame = new AdminSection();
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
	public AdminSection() {
		setResizable(false);
		setTitle("Admin");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 499);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Admin Section");
		lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 25));
		lblNewLabel.setBounds(131, 11, 172, 51);
		contentPane.add(lblNewLabel);
		
		JButton btnAddLibrarian = new JButton("Add Librarian");
		btnAddLibrarian.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				new AddLibrarian(AdminSection.this).setVisible(true);
			}
		});
		btnAddLibrarian.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnAddLibrarian.setBounds(131, 73, 159, 60);
		contentPane.add(btnAddLibrarian);
		
		JButton btnViewLibrarian = new JButton("View Librarian");
		btnViewLibrarian.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				try {
					new ViewLibrarian(AdminSection.this).setVisible(true);
				} catch (ClassNotFoundException | SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnViewLibrarian.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnViewLibrarian.setBounds(131, 157, 159, 60);
		contentPane.add(btnViewLibrarian);
		
		JButton btnLogOut = new JButton("Log Out");
		btnLogOut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AdminLoginForm login = new AdminLoginForm();
				dispose();
				login.setVisible(true);
			}
		});
		btnLogOut.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnLogOut.setBounds(131, 250, 159, 60);
		contentPane.add(btnLogOut);
		
		btnAddLibrarian.setFocusable(false);
		btnLogOut.setFocusable(false);
		btnViewLibrarian.setFocusable(false); 
		
		JButton btnReturn = new JButton("Home");
		btnReturn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RegistrationForm register = new RegistrationForm();
				dispose();
				register.setVisible(true);
				
			}
		});
		btnReturn.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnReturn.setFocusable(false);
		btnReturn.setBounds(131, 340, 159, 60);
		contentPane.add(btnReturn);
		
	}
}

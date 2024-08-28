package Librarian;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Admin.AddLibrarian;
import Admin.RegistrationForm;

import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.awt.event.ActionEvent;

public class Librariansection extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Librariansection frame = new Librariansection();
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
	public Librariansection() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 549, 681);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblLibrarianSection = new JLabel("Librarian Section");
		lblLibrarianSection.setFont(new Font("Times New Roman", Font.BOLD, 27));
		lblLibrarianSection.setBounds(156, 23, 220, 57);
		contentPane.add(lblLibrarianSection);

		JButton btnAddBook = new JButton("Add Book");
		btnAddBook.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new AddbookSection(Librariansection.this).setVisible(true);
				dispose();
			}
		});
		btnAddBook.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnAddBook.setBounds(65, 129, 171, 63);
		btnAddBook.setFocusable(false);
		contentPane.add(btnAddBook);

		JButton btnViewBooks = new JButton("View Book");
		btnViewBooks.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					new ViewBookList(Librariansection.this).setVisible(true);
					dispose();
				} catch (ClassNotFoundException | SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnViewBooks.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnViewBooks.setBounds(295, 129, 171, 63);
		btnViewBooks.setFocusable(false);
		contentPane.add(btnViewBooks);

		JButton btnBorrowBook = new JButton("Borrow");
		btnBorrowBook.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new BorrowBookSection(Librariansection.this).setVisible(true);
				dispose();
			}
		});
		btnBorrowBook.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnBorrowBook.setBounds(65, 270, 171, 63);
		btnBorrowBook.setFocusable(false);
		contentPane.add(btnBorrowBook);

		JButton btnBorrowedList = new JButton("Borrowed Book List");
		btnBorrowedList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					new ViewBorrowedBookList(Librariansection.this).setVisible(true);
				} catch (ClassNotFoundException | SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				dispose();
			}
		});
		btnBorrowedList.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnBorrowedList.setBounds(295, 270, 171, 63);
		btnBorrowedList.setFocusable(false);
		contentPane.add(btnBorrowedList);

		JButton btnReturnBook = new JButton("Return Book");
		btnReturnBook.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new ReturnBookSection(Librariansection.this).setVisible(true);
				dispose();
			}
		});
		btnReturnBook.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnReturnBook.setBounds(65, 405, 171, 63);
		btnReturnBook.setFocusable(false);
		contentPane.add(btnReturnBook);

		JButton btnHome = new JButton("Home");
		btnHome.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RegistrationForm register = new RegistrationForm();
				register.setVisible(true);
				dispose();
			}
		});
		btnHome.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnHome.setBounds(295, 405, 171, 63);
		btnHome.setFocusable(false);
		contentPane.add(btnHome);
		
		JButton btnLogOut = new JButton("Log Out");
		btnLogOut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LibrarianLoginForm libraLogin = new LibrarianLoginForm();
				libraLogin.setVisible(true);
				dispose();
			}
		});
		btnLogOut.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnLogOut.setFocusable(false);
		btnLogOut.setBounds(182, 527, 171, 63);
		contentPane.add(btnLogOut);

	}
}

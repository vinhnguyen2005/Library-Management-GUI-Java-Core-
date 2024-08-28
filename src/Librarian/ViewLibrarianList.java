package Librarian;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;

import Database.ConnectToDB;

import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.awt.event.ActionEvent;

public class ViewLibrarianList extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable table;

	public ViewLibrarianList(LibrarianLoginForm librarianlogin) throws ClassNotFoundException, SQLException {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 866, 486);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 827, 370);
		contentPane.add(scrollPane);

		table = new JTable();
		scrollPane.setViewportView(table);
		table.setModel(new DefaultTableModel(new Object[][] {},
				new String[] { "Name", "Age", "Email", "Password", "Phone Number", "Address", "City" }));
		table.getColumnModel().getColumn(0).setPreferredWidth(150);
		table.getColumnModel().getColumn(1).setPreferredWidth(47);
		table.getColumnModel().getColumn(2).setPreferredWidth(135);
		table.getColumnModel().getColumn(3).setPreferredWidth(111);
		table.getColumnModel().getColumn(4).setPreferredWidth(120);
		table.getColumnModel().getColumn(5).setPreferredWidth(115);

		scrollPane.setViewportView(table);
		loadLibrarianData();

		JButton btnNewButton = new JButton("Back");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				librarianlogin.setVisible(true);
				dispose();
			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnNewButton.setBounds(364, 400, 106, 36);
		contentPane.add(btnNewButton);

	}

	private void loadLibrarianData() throws ClassNotFoundException, SQLException {
		Connection connection = ConnectToDB.getConnection();
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		model.setRowCount(0);
		if (connection != null) {
			try {
				String query = "SELECT * FROM librariantable";
				Statement statement = connection.createStatement();
				ResultSet result = statement.executeQuery(query);

				while (result.next()) {
					String name = result.getString(1);
					String age = result.getString(2);
					String email = result.getString(3);
					String password = result.getString(4);
					String phonenumber = result.getString(5);
					String address = result.getString(6);
					String city = result.getString(7);

					model.addRow(new Object[] { name, age, email, password, phonenumber, address, city });
				}

				System.out.println("Connect to Database Successfully");

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				ConnectToDB.closeConnection(connection);

			}
		}
	}
}

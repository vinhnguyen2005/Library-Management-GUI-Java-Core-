package Admin;

import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import Database.ConnectToDB;

import javax.swing.JScrollPane;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;

public class ViewLibrarian extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable table;

	public ViewLibrarian(AdminSection adminsection) throws ClassNotFoundException, SQLException {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 870, 492);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 827, 362);
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

		JButton btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				adminsection.setVisible(true);
			}
		});
		btnBack.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnBack.setBounds(666, 407, 111, 23);
		contentPane.add(btnBack);

		JComboBox<String> comboBox = new JComboBox<>();
		comboBox.setToolTipText("");
		comboBox.setBounds(189, 408, 89, 22);
		contentPane.add(comboBox);
		comboBox.addItem("Name");
		comboBox.addItem("Age");

		JButton btnSort = new JButton("Sort");
		btnSort.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String selectedoption = (String) comboBox.getSelectedItem();
				sortData(selectedoption);
			}
		});
		btnSort.setFocusable(false);
		btnSort.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnSort.setBounds(65, 407, 89, 23);
		contentPane.add(btnSort);

		JButton btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					deleteLibrarian();
				} catch (ClassNotFoundException | SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnDelete.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnDelete.setBounds(369, 407, 111, 23);
		contentPane.add(btnDelete);

		loadLibrarianData();

	}

	private void deleteLibrarian() throws ClassNotFoundException, SQLException {
		int rowDelete = table.getSelectedRow();
		if (rowDelete != -1) {
			DefaultTableModel model = (DefaultTableModel) table.getModel();
			model.removeRow(rowDelete);
			String nametoDelete = (String) model.getValueAt(rowDelete, 0);
			deleterecordfromDB(nametoDelete);
		} else {
			JOptionPane.showMessageDialog(null, "Please select a row to delete.", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void deleterecordfromDB(String name) throws ClassNotFoundException, SQLException {
		Connection connection = ConnectToDB.getConnection();
		 try {
		        String query = "DELETE FROM librariantable WHERE name = ?"; 
		        PreparedStatement stm = connection.prepareStatement(query);
		        stm.setString(1, name); 
		        int rowsUpdated = stm.executeUpdate();
		        if (rowsUpdated > 0) {
		            System.out.println("Record deleted successfully.");
		        } else {
		            System.out.println("No record found with the provided name.");
		        }
		    } catch (Exception e) {
		        e.printStackTrace();
		    } finally {
				ConnectToDB.closeConnection(connection);
			}
	}


	private void sortData(String columnName) {
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(model);
		List<RowSorter.SortKey> sortedList = new ArrayList<RowSorter.SortKey>();

		if (columnName.equals("Name")) {
			sorter.setComparator(getColumnIndex(columnName), new Comparator<String>() {

				@Override
				public int compare(String o1, String o2) {
					String[] name1 = o1.split("\\s+");
					String[] name2 = o2.split("\\s+");
					if (!name1[name1.length - 1].equalsIgnoreCase(name2[name2.length - 1])) {
						return name1[name1.length - 1].compareToIgnoreCase(name2[name2.length - 1]);
					}
					return -1;
				}
			});
		} else if (columnName.equals("Age")) {
			sorter.setComparator(getColumnIndex(columnName), new Comparator<String>() {

				@Override
				public int compare(String o1, String o2) {
					// TODO Auto-generated method stub
					int age1 = Integer.parseInt(o1);
					int age2 = Integer.parseInt(o2);
					return Integer.compare(age1, age2);
				}
			});
		}
		sortedList.add(new RowSorter.SortKey(getColumnIndex(columnName), SortOrder.ASCENDING));
		sorter.setSortKeys(sortedList);
		table.setRowSorter(sorter);

	}

	private int getColumnIndex(String columnName) {
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		for (int i = 0; i < model.getColumnCount(); i++) {
			if (model.getColumnName(i).equals(columnName)) {
				return i;
			}
		}
		return -1;
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

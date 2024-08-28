package Librarian;

import java.awt.EventQueue;

import javax.swing.JFrame;
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
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;

public class ViewBorrowedBookList extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable table;
	private JButton btnSort;
	private JComboBox comboBox;

	public ViewBorrowedBookList(Librariansection librarianSection) throws ClassNotFoundException, SQLException {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 867, 507);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 831, 386);
		contentPane.add(scrollPane);
		
		table = new JTable();
		scrollPane.setViewportView(table);
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"BookID", "Student ID", "Student Name", "Phone Number", "Borrowed Date"
			}
		));
		
		JButton btnNewButton = new JButton("Back");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				librarianSection.setVisible(true);
				dispose();
			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnNewButton.setBounds(528, 416, 108, 29);
		contentPane.add(btnNewButton);
		
		btnSort = new JButton("Sort");
		btnSort.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String selectedoption = (String) comboBox.getSelectedItem();
				sortData(selectedoption);
			}
		});
		btnSort.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnSort.setBounds(171, 416, 108, 29);
		contentPane.add(btnSort);
		
		comboBox = new JComboBox<>();
		comboBox.setFont(new Font("Tahoma", Font.PLAIN, 12));
		comboBox.setBounds(289, 418, 94, 27);
		comboBox.addItem("BookID");
		comboBox.addItem("Student Name");
		contentPane.add(comboBox);
		table.getColumnModel().getColumn(0).setPreferredWidth(89);
		table.getColumnModel().getColumn(1).setPreferredWidth(99);
		table.getColumnModel().getColumn(2).setPreferredWidth(152);
		table.getColumnModel().getColumn(3).setPreferredWidth(162);
		table.getColumnModel().getColumn(4).setPreferredWidth(121);
		
		loadBorrowedList();
	}
	
	private void sortData(String columnName) {
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(model);
		List<RowSorter.SortKey> sortedList = new ArrayList<RowSorter.SortKey>();

		if (columnName.equals("Student Name")) {
			sorter.setComparator(getColumnIndex(columnName), new Comparator<String>() {

				@Override
				public int compare(String o1, String o2) {
					String[] name1 = o1.split("\\s+");
					String[] name2 = o2.split("\\s+");
					if (!name1[name1.length - 1].equalsIgnoreCase(name2[name2.length - 1])) {
						return name1[name1.length - 1].compareToIgnoreCase(name2[name2.length - 1]);
					}
					return o1.compareToIgnoreCase(o2);
				}
			});
		} else {
			sorter.setComparator(2, new Comparator<String>() {

				@Override
				public int compare(String o1, String o2) {
					// TODO Auto-generated method stub
					return o2.compareTo(o2);
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
	
	private void loadBorrowedList() throws ClassNotFoundException, SQLException {
		Connection connection = ConnectToDB.getConnection();
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		model.setRowCount(0); // Clear existing data

		if (connection != null) {
			try {
				String query = "SELECT * FROM borrowedbook";
				Statement statement = connection.createStatement();
				ResultSet result = statement.executeQuery(query);

				while (result.next()) {
					String bookId = result.getString("BookId");
					String studentID = result.getString("StudentID");
					String studentName = result.getString("StudentName");
					String PhoneNo = result.getString("StudentPhoneNo");
					String borrowedDate = result.getString("BorrowedDate");

					model.addRow(new Object[] { bookId, studentID, studentName, PhoneNo, borrowedDate});
				}

				System.out.println("Data loaded successfully.");
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("Error loading data.");
			} finally {
				ConnectToDB.closeConnection(connection);
			}
		} else {
			System.out.println("Database connection is null.");
		}
	}
}

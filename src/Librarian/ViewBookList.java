package Librarian;

import java.awt.EventQueue;

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
import javax.swing.ComboBoxEditor;
import javax.swing.JButton;
import java.awt.Font;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;

public class ViewBookList extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable table;


	public ViewBookList(Librariansection librarianSection) throws ClassNotFoundException, SQLException {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 870, 505);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 835, 373);
		contentPane.add(scrollPane);

		table = new JTable();
		scrollPane.setViewportView(table);
		table.setModel(new DefaultTableModel(new Object[][] {},
				new String[] { "BookID", "Name", "Author", "Publisher", "Quantity", "Borrowed", "Added Date" }));

		JButton btnNewButton = new JButton("Back");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				librarianSection.setVisible(true);
				dispose();
			}
		});
		btnNewButton.setFocusable(false);
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnNewButton.setBounds(366, 405, 99, 37);
		contentPane.add(btnNewButton);

		JButton btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					deleteBook();
				} catch (ClassNotFoundException | SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnDelete.setFocusable(false);
		btnDelete.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnDelete.setBounds(667, 405, 99, 37);
		contentPane.add(btnDelete);

		String[] field = { "Name", "Quantity", "Borrowed" };
		JComboBox<String> comboBox = new JComboBox<>(field);
		comboBox.setFont(new Font("Tahoma", Font.PLAIN, 14));
		comboBox.setBounds(160, 410, 99, 31);
		contentPane.add(comboBox);
		table.getColumnModel().getColumn(0).setPreferredWidth(66);
		table.getColumnModel().getColumn(1).setPreferredWidth(128);
		table.getColumnModel().getColumn(2).setPreferredWidth(116);
		table.getColumnModel().getColumn(3).setPreferredWidth(116);
		table.getColumnModel().getColumn(4).setPreferredWidth(65);
		table.getColumnModel().getColumn(5).setPreferredWidth(62);
		table.getColumnModel().getColumn(6).setPreferredWidth(97);

		JButton btnSt = new JButton("Sort");
		btnSt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String selectedoption = (String) comboBox.getSelectedItem();
				sortData(selectedoption);
			}
		});
		btnSt.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnSt.setBounds(62, 410, 76, 32);
		btnSt.setFocusable(rootPaneCheckingEnabled);
		contentPane.add(btnSt);

		loadBookList();
	}

	private void sortData(String columnName) {
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(model);
		List<RowSorter.SortKey> sortedList = new ArrayList<RowSorter.SortKey>();
		int columnIndex = getColumnIndex(columnName);
		if (columnName.equals("Quantity") || columnName.equals("Borrowed")) {
			sorter.setComparator(columnIndex, (o1, o2) -> {
				try {
					int num1 = Integer.parseInt(o1.toString());
					int num2 = Integer.parseInt(o2.toString());
					return Integer.compare(num1, num2);
				} catch (NumberFormatException e) {
					return 0;
				}
			});
		}

		else {
			sorter.setComparator(columnIndex, new Comparator<String>() {
				@Override
				public int compare(String o1, String o2) {
					return o1.compareToIgnoreCase(o2);
				}
			});
		}
		sortedList.add(new RowSorter.SortKey(columnIndex, SortOrder.ASCENDING));
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

	private void deleteBook() throws ClassNotFoundException, SQLException {
		int rowDelete = table.getSelectedRow();
		if (rowDelete != -1) {
			DefaultTableModel model = (DefaultTableModel) table.getModel();
			String nametoDelete = (String) model.getValueAt(rowDelete, 0);
			model.removeRow(rowDelete);
			deleteBookFromDB(nametoDelete);
		} else {
			JOptionPane.showMessageDialog(null, "Please select a row to delete.", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void deleteBookFromDB(String bookID) throws ClassNotFoundException, SQLException {
		Connection connection = ConnectToDB.getConnection();
		try {
			if (connection != null) {
				String query = "DELETE FROM booklist WHERE BookId = ?";
				PreparedStatement stm = connection.prepareStatement(query);
				stm.setString(1, bookID);
				int rowsUpdated = stm.executeUpdate();
				if (rowsUpdated > 0) {
					System.out.println("Record deleted successfully.");
				} else {
					System.out.println("No record found with the provided name.");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectToDB.closeConnection(connection);
		}

	}

	private void loadBookList() throws ClassNotFoundException, SQLException {
		Connection connection = ConnectToDB.getConnection();
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		model.setRowCount(0); // Clear existing data

		if (connection != null) {
			try {
				String query = "SELECT * FROM booklist";
				Statement statement = connection.createStatement();
				ResultSet result = statement.executeQuery(query);

				while (result.next()) {
					String bookId = result.getString("BookId");
					String name = result.getString("Name");
					String author = result.getString("Author");
					String publisher = result.getString("Publisher");
					int quantity = result.getInt("Quantity");
					int borrowed = result.getInt("Borrowed");
					String addedDate = result.getString("Added_Date");

					model.addRow(new Object[] { bookId, name, author, publisher, quantity, borrowed, addedDate });
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
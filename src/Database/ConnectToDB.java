package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectToDB {
	private static String url = "jdbc:mysql://localhost:3306/librarymanagement?autoReconnect=true&useSSL=false&serverTimezone=UTC";
	private static String username = "root";
	private static String password = "Vinh2005";

	public static Connection getConnection() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		return DriverManager.getConnection(url, username, password);
	}

	public static void closeConnection(Connection connection) throws SQLException {
		if (connection != null && !connection.isClosed()) {
			connection.close();
			System.out.println("Database connection closed");
		}
	}
}

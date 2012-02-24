package util.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionHolder {
	private static Connection connection = null;

	public static Connection getConnection() throws SQLException {
		if (connection == null) {
			buildConnection();
		}

		return connection;
	}

	private static void buildConnection() throws SQLException {
		// FIXME corrigir host e database
		connection = DriverManager.getConnection(
				"jdbc:postgresql://pinguin-29fe670/financeiro", "postgres",
				"1234");
	}
}

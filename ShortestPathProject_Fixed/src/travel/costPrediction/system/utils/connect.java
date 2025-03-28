package travel.costPrediction.system.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class connect {
    private Connection connection;
    private Statement statement;

    public connect() {
        try {
            connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/costPrediction", "root", "password"
            );
            statement = connection.createStatement();
            System.out.println("Database connected successfully.");
        } catch (SQLException e) {
            System.err.println("Database connection failed.");
            e.printStackTrace();
        }
    }

    public Statement getStatement() {
        return statement;
    }

    public Connection getConnection() {
        return connection;
    }

    // ✅ Method to close the connection
    public void close() {
        try {
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

package Services;

import java.sql.*;

public class DatabaseService {
    private static DatabaseService INSTANCE;

    static Connection connection = null;

    public DatabaseService() {
    }

    public static DatabaseService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DatabaseService();
        }

        return INSTANCE;
    }

    public Connection getConnection() {
        if (connection != null) return connection;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3310/gamestore", "user", "mysql123");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return connection;
    }

    public void closeConnection() {
        if (connection == null) return;

        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void closeStatement(Statement statement) {
        if (statement == null) return;

        try {
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void closeResultSet(ResultSet resultSet) {
        if (resultSet == null) return;

        try {
            resultSet.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
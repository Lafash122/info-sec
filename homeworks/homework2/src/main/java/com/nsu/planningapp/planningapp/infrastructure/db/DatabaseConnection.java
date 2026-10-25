package com.nsu.planningapp.planningapp.infrastructure.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DatabaseConnection {
    static String dbName = "socialist_country_example";

    static String JDBC_URL = String.format(
            "jdbc:postgresql://localhost:5432/" + dbName + "?currentSchema=public&user=%s&password=%s",
            System.getenv("DB_USER"),
            System.getenv("DB_PASSWORD")
    );

    public static String getDBName() {
        return dbName;
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL);
    }

    public static void closeConnection(Connection conn) throws SQLException {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Connection closure error: " + e.getMessage());
            }
        }
    }
}

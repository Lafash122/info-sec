package com.nsu.planningapp.planningapp.infrastructure.db;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DatabaseUtils {
    public static List<Long> fetchIds(Connection conn, String tableName, String columnName)
        throws SQLException {
        List<Long> ids = new ArrayList<>();
        String sql = "SELECT " + columnName + " FROM " + tableName;

        try (Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Long id = rs.getLong(columnName);
                ids.add(id);
            }
        }
        return ids;
    }

    public static void executeSqlScript(Connection conn, String filePath) throws Exception {
        Path path = Paths.get(filePath);
        String content = Files.readString(path);

        List<String> statements = splitSqlStatements(content);

        try (Statement stmt = conn.createStatement()) {
            for (String sql : statements) {
                //System.out.println(sql); -- debug
                try {
                    stmt.execute(sql);
                } catch (SQLException e) {
                    System.err.println("Error in the statement: " + e.getMessage());
                }
            }
        }
    }

    public static List<String> splitSqlStatements(String content) {
        return Arrays.stream(content.split(";"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }
}

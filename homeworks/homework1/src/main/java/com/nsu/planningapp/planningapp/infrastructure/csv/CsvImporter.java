package com.nsu.planningapp.planningapp.infrastructure.csv;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.sql.*;

public class CsvImporter {
    static String delimiter = ";";

    // Resources and settlements (cities, towns, etc.)

    public void importResources(Connection conn, String filePath) throws IOException, SQLException {
        String sql = "INSERT INTO RESOURCES (name, unit) VALUES (?, ?)";
        importResourcesTable(conn, filePath, sql);
        //System.out.println("importResources done");
    }

    public void importSettlements(Connection conn, String filePath) throws IOException, SQLException {
        String sql = "INSERT INTO SETTLEMENTS (name) VALUES (?)";
        importSettlementsTable(conn, filePath, sql);
        //System.out.println("importSettlements done");
    }

    // Blueprints

    public void importBuildingBlueprints(Connection conn, String filePath) throws IOException, SQLException {
        String sqlQuery = "INSERT INTO BUILDING_BLUEPRINTS (name, service_life, " +
                "number_of_workdays, daily_water_consumption, daily_energy_consumption, blueprint_type) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        importBuildingBlueprints(conn, filePath, sqlQuery);
        //System.out.println("importBuildingBlueprints done");
    }

    public void importTransportBlueprints(Connection conn, String filePath) throws IOException, SQLException {
        String sqlQuery = "INSERT INTO TRANSPORT_BLUEPRINTS (name, velocity, " +
                "number_of_workdays, type) " +
                "VALUES (?, ?, ?, ?)";
        importTransportBlueprints(conn, filePath, sqlQuery);
        //System.out.println("importTransportBlueprints done");
    }

    // Subtypes of building blueprints

    public void importFactoryBlueprints(Connection conn, String filePath) throws IOException, SQLException {
        String sqlQuery = "INSERT INTO FACTORY_BLUEPRINTS (id, number_of_jobs, " +
                "number_of_jobs_with_higher_education) " +
                "VALUES (?, ?, ?)";
        String findBlueprintId = "SELECT id FROM BUILDING_BLUEPRINTS WHERE name = ?";
        importFactoryBlueprints(conn, filePath, sqlQuery, findBlueprintId);
        //System.out.println("importFactoryBlueprints done");
    }

    public void importPublicBlueprints(Connection conn, String filePath) throws IOException, SQLException {
        String sqlQuery = "INSERT INTO PUBLIC_FACILITY_BLUEPRINTS (id, number_of_jobs, " +
                "number_of_jobs_with_higher_education, number_of_visitors, number_of_parking_spaces) " +
                "VALUES (?, ?, ?, ?, ?)";
        String findBlueprintId = "SELECT id FROM BUILDING_BLUEPRINTS WHERE name = ?";
        importPublicBlueprints(conn, filePath, sqlQuery, findBlueprintId);
        //System.out.println("importPublicBlueprints done");
    }

    public void importResidentialBlueprints(Connection conn, String filePath) throws IOException, SQLException {
        String sqlQuery = "INSERT INTO RESIDENTIAL_BUILDING_BLUEPRINTS (id, number_of_residents, " +
                "quality_of_housing) " +
                "VALUES (?, ?, ?)";
        String findBlueprintId = "SELECT id FROM BUILDING_BLUEPRINTS WHERE name = ?";
        importResidentialBlueprints(conn, filePath, sqlQuery, findBlueprintId);
        //System.out.println("importResidentialBlueprints done");
    }

    public void importTemporaryBlueprints(Connection conn, String filePath) throws IOException, SQLException {
        String sqlQuery = "INSERT INTO TEMPORARY_RESIDENCE_BUILDING_BLUEPRINTS (id, number_of_jobs, " +
                "number_of_residents) " +
                "VALUES (?, ?, ?)";
        String findBlueprintId = "SELECT id FROM BUILDING_BLUEPRINTS WHERE name = ?";
        importTemporaryBlueprints(conn, filePath, sqlQuery, findBlueprintId);
        //System.out.println("importTemporaryBlueprints done");
    }

    // Specimens

    public void importBuildings(Connection conn, String filePath) throws IOException, SQLException {
        String insertSql = "INSERT INTO BUILDINGS (settlement, blueprint) VALUES (?, ?)";
        String findBlueprintId = "SELECT id FROM BUILDING_BLUEPRINTS WHERE name = ?";
        String findSettlementId = "SELECT id FROM SETTLEMENTS WHERE name = ?";
        importBuildings(conn, filePath, insertSql, findBlueprintId, findSettlementId);
        //System.out.println("importBuildings done");
    }

    public void importTransport(Connection conn, String filePath) throws IOException, SQLException  {
        String insertSql = "INSERT INTO TRANSPORT (blueprint) VALUES (?)";
        String findBlueprintId = "SELECT id FROM TRANSPORT_BLUEPRINTS WHERE name = ?";
        importTransport(conn, filePath, insertSql, findBlueprintId);
        //System.out.println("importTransport done");
    }

    // Resources for construction / production according to blueprints

    public void importBuildingBlueprintResources(Connection conn, String filePath) throws SQLException, IOException {
        String findBlueprintId = "SELECT id FROM BUILDING_BLUEPRINTS WHERE name = ?";
        String insertSql = "INSERT INTO BUILDING_CONSTRUCTION_RESOURCES " +
                "(building_blueprint_id, resource_id, quantity) VALUES (?, ?, ?)";

        importBlueprintResources(conn, filePath, findBlueprintId, insertSql);
        //System.out.println("importBuildingBlueprintResources done");
    }

    public void importTransportBlueprintResources(Connection conn, String filePath) throws SQLException, IOException {
        String findBlueprintId = "SELECT id FROM TRANSPORT_BLUEPRINTS WHERE name = ?";
        String insertSql = "INSERT INTO TRANSPORT_CONSTRUCTION_RESOURCES " +
                "(transport_blueprint_id, resource_id, quantity) VALUES (?, ?, ?)";

        importBlueprintResources(conn, filePath, findBlueprintId, insertSql);
        //System.out.println("importTransportBlueprintResources done");
    }

    // Resources produced and consumed in factories

    public void importConsumedResources(Connection conn, String filePath) throws SQLException, IOException {
        String findBlueprintId = "SELECT id FROM BUILDING_BLUEPRINTS WHERE name = ?";
        String insertSql = "INSERT INTO AMOUNT_OF_RESOURCES_CONSUMED " +
                "(factory_blueprint_id, resource_id, quantity) VALUES (?, ?, ?)";

        importBlueprintResources(conn, filePath, findBlueprintId, insertSql);
        //System.out.println("importConsumedBlueprint done");
    }

    public void importProducesBlueprint(Connection conn, String filePath) throws SQLException, IOException {
        String findBlueprintId = "SELECT id FROM BUILDING_BLUEPRINTS WHERE name = ?";
        String insertSql = "INSERT INTO AMOUNT_OF_RESOURCES_PRODUCED " +
                "(factory_blueprint_id, resource_id, quantity) VALUES (?, ?, ?)";

        importBlueprintResources(conn, filePath, findBlueprintId, insertSql);
        //System.out.println("importProducesBlueprint done");
    }

    // Storage resources in buildings

    public void importResourcesStorage(Connection conn, String filePath) throws SQLException, IOException {
        String findBlueprintId = "SELECT id FROM BUILDING_BLUEPRINTS WHERE name = ?";
        String insertSql = "INSERT INTO RESOURCE_STORAGE_SIZES " +
                "(building_blueprint_id, resource_id, quantity) VALUES (?, ?, ?)";

        importBlueprintResources(conn, filePath, findBlueprintId, insertSql);
        //System.out.println("importResourcesStorage done");
    }


    // Filling in tables containing only the ID and the name of the object
    private static void importSettlementsTable(Connection conn, String filePath, String sqlQuery) throws IOException,
                                                                                                        SQLException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath));
             PreparedStatement pstmt = conn.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS))
        {
            String line;
            boolean isHeader = true;
            while((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                String[] parts = line.split(delimiter);
                String name = parts[0];
                pstmt.setString(1, name);

                pstmt.executeUpdate();
            }
        }
    }

    private static void importResourcesTable(Connection conn, String filePath, String sqlQuery) throws IOException,
            SQLException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath));
             PreparedStatement pstmt = conn.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS))
        {
            String line;
            boolean isHeader = true;
            while((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                String[] parts = line.split(delimiter);
                String name = parts[0];
                String unit = switch (name.toLowerCase()) {
                    case "вода", "сточные воды" -> "м3";
                    case "электричество" -> "МВт*ч";
                    case "тепло" -> "ГДж";
                    default -> "т";
                };


                pstmt.setString(1, name);
                pstmt.setString(2, unit);

                pstmt.executeUpdate();
            }
        }
    }

    private static void importBuildingBlueprints(Connection conn, String filePath, String sqlQuery)
            throws IOException, SQLException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath));
             PreparedStatement pstmt = conn.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS))
        {
            String line;
            boolean isHeader = true;
            while((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                String[] parts = line.split(delimiter);

                String name = parts[0];
                int service_life = Integer.parseInt(parts[1]);
                int number_of_workdays = Integer.parseInt(parts[2]);
                float daily_water_consumption = Float.parseFloat(parts[3].replace(',', '.'));
                float daily_energy_consumption = Float.parseFloat(parts[4].replace(',', '.'));
                String blueprint_type = parts[5];

                pstmt.setString(1, name);
                pstmt.setInt(2, service_life);
                pstmt.setInt(3, number_of_workdays);
                pstmt.setFloat(4, daily_water_consumption);
                pstmt.setFloat(5, daily_energy_consumption);
                pstmt.setString(6, blueprint_type);

                pstmt.executeUpdate();
            }
        }
    }

    private static void importTransportBlueprints(Connection conn, String filePath, String sqlQuery) throws IOException, SQLException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath));
             PreparedStatement pstmt = conn.prepareStatement(sqlQuery))
        {
            String line;
            boolean isHeader = true;
            while((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                String[] parts = line.split(delimiter);

                String name = parts[0];
                float velocity = Float.parseFloat(parts[1].replace(',', '.'));
                int number_of_workdays = Integer.parseInt(parts[2]);
                String type = parts[3];

                pstmt.setString(1, name);
                pstmt.setFloat(2, velocity);
                pstmt.setInt(3, number_of_workdays);
                pstmt.setString(4, type);

                pstmt.executeUpdate();
            }
        }
    }

    // Subtypes of building blueprints

    private static void importFactoryBlueprints(Connection conn, String filePath, String sqlQuery, String findBlueprintId)
            throws IOException, SQLException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath));
             PreparedStatement pstmt = conn.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement findBlueprintStmt = conn.prepareStatement(findBlueprintId)
        )
        {
            String line;
            boolean isHeader = true;
            while((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                String[] parts = line.split(delimiter);

                String name = parts[0];
                int number_of_jobs = Integer.parseInt(parts[1]);
                int number_of_jobs_with_higher_education = Integer.parseInt(parts[2]);

                findBlueprintStmt.setString(1, name);
                ResultSet resultBlueprintSet = findBlueprintStmt.executeQuery();

                int id;
                if (resultBlueprintSet.next()) {
                    id = resultBlueprintSet.getInt("id");
                } else {
                    // TODO: заменить на исключение
                    System.err.println("Blueprint not found: " + name);
                    resultBlueprintSet.close();
                    continue;
                }

                pstmt.setInt(1, id);
                pstmt.setInt(2, number_of_jobs);
                pstmt.setInt(3, number_of_jobs_with_higher_education);

                pstmt.executeUpdate();
            }
        }
    }

    private static void importPublicBlueprints(Connection conn, String filePath, String sqlQuery, String findBlueprintId) throws IOException, SQLException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath));
             PreparedStatement pstmt = conn.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement findBlueprintStmt = conn.prepareStatement(findBlueprintId)
        )
        {
            String line;
            boolean isHeader = true;
            while((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                //System.out.println(line);

                String[] parts = line.split(delimiter);

                String name = parts[0];
                int number_of_jobs = Integer.parseInt(parts[1]);
                int number_of_jobs_with_higher_education = Integer.parseInt(parts[2]);
                int number_of_visitors = Integer.parseInt(parts[3]);
                int number_of_parking_spaces = Integer.parseInt(parts[4]);

                //System.out.println(name);

                findBlueprintStmt.setString(1, name);
                ResultSet resultBlueprintSet = findBlueprintStmt.executeQuery();

                int id;
                if (resultBlueprintSet.next()) {
                    id = resultBlueprintSet.getInt("id");
                } else {
                    // TODO: заменить на исключение
                    System.err.println("Blueprint not found: " + name);
                    resultBlueprintSet.close();
                    continue;
                }

                //System.out.println(name + "; " + id);

                pstmt.setInt(1, id);
                pstmt.setInt(2, number_of_jobs);
                pstmt.setInt(3, number_of_jobs_with_higher_education);
                pstmt.setInt(4, number_of_visitors);
                pstmt.setInt(5, number_of_parking_spaces);

                pstmt.executeUpdate();
            }
        }
    }

    private static void importResidentialBlueprints(Connection conn, String filePath, String sqlQuery, String findBlueprintId) throws IOException, SQLException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath));
             PreparedStatement pstmt = conn.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement findBlueprintStmt = conn.prepareStatement(findBlueprintId)
        )
        {
            String line;
            boolean isHeader = true;
            while((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                String[] parts = line.split(delimiter);

                String name = parts[0];
                int number_of_residents = Integer.parseInt(parts[1]);
                int quality_of_housing = Integer.parseInt(parts[2]);

                findBlueprintStmt.setString(1, name);
                ResultSet resultBlueprintSet = findBlueprintStmt.executeQuery();

                int id;
                if (resultBlueprintSet.next()) {
                    id = resultBlueprintSet.getInt("id");
                } else {
                    // TODO: заменить на исключение
                    System.err.println("Blueprint not found: " + name);
                    resultBlueprintSet.close();
                    continue;
                }

                pstmt.setInt(1, id);
                pstmt.setInt(2, number_of_residents);
                pstmt.setInt(3, quality_of_housing);

                pstmt.executeUpdate();
            }
        }
    }

    private static void importTemporaryBlueprints(Connection conn, String filePath, String sqlQuery, String findBlueprintId) throws IOException, SQLException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath));
             PreparedStatement pstmt = conn.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement findBlueprintStmt = conn.prepareStatement(findBlueprintId)
        )
        {
            String line;
            boolean isHeader = true;
            while((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                String[] parts = line.split(delimiter);

                String name = parts[0];
                int number_of_jobs = Integer.parseInt(parts[1]);
                int number_of_residents = Integer.parseInt(parts[2]);

                findBlueprintStmt.setString(1, name);
                ResultSet resultBlueprintSet = findBlueprintStmt.executeQuery();

                int id;
                if (resultBlueprintSet.next()) {
                    id = resultBlueprintSet.getInt("id");
                } else {
                    // TODO: заменить на исключение
                    System.err.println("Blueprint not found: " + name);
                    resultBlueprintSet.close();
                    continue;
                }

                pstmt.setInt(1, id);
                pstmt.setInt(2, number_of_jobs);
                pstmt.setInt(3, number_of_residents);

                pstmt.executeUpdate();
            }
        }
    }

    // Specimens

    private static void importBuildings(Connection conn, String filePath,
                                        String insertSql, String findBlueprintId, String findSettlementId)
            throws IOException, SQLException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath));
             PreparedStatement findBlueprintStmt = conn.prepareStatement(findBlueprintId);
             PreparedStatement findSettlementStmt = conn.prepareStatement(findSettlementId);
             PreparedStatement insertStmt = conn.prepareStatement(insertSql)
        ) {
            String line;
            boolean isHeader = true;
            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                String[] parts = line.split(delimiter);

                String settlementName = parts[0];
                String blueprintName = parts[1];

                int settlementId;
                int blueprintId;

                findSettlementStmt.setString(1, settlementName);
                ResultSet resultSettlementSet = findSettlementStmt.executeQuery();

                findBlueprintStmt.setString(1, blueprintName);
                ResultSet resultBlueprintSet = findBlueprintStmt.executeQuery();

                if (resultBlueprintSet.next()) {
                    blueprintId = resultBlueprintSet.getInt("id");
                } else {
                    // TODO: заменить на исключение
                    System.err.println("Blueprint not found: " + blueprintName);
                    resultBlueprintSet.close();
                    resultSettlementSet.close();
                    continue;
                }

                if (resultSettlementSet.next()) {
                    settlementId = resultSettlementSet.getInt("id");
                } else {
                    // TODO: заменить на исключение
                    System.err.println("Resource not found: " + settlementName);
                    resultBlueprintSet.close();
                    resultSettlementSet.close();
                    continue;
                }

                insertStmt.setInt(1, settlementId);
                insertStmt.setInt(2, blueprintId);
                insertStmt.executeUpdate();

                resultBlueprintSet.close();
                resultSettlementSet.close();
            }
        }
    }

    private static void importTransport(Connection conn, String filePath,
                                        String insertSql, String findBlueprintId) throws IOException, SQLException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath));
             PreparedStatement findBlueprintStmt = conn.prepareStatement(findBlueprintId);
             PreparedStatement insertStmt = conn.prepareStatement(insertSql)
        ) {
            String line;
            boolean isHeader = true;
            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }
                String[] parts = line.split(delimiter);
                String blueprintName = parts[0];
                int blueprintId;

                findBlueprintStmt.setString(1, blueprintName);
                ResultSet resultBlueprintSet = findBlueprintStmt.executeQuery();

                if (resultBlueprintSet.next()) {
                    blueprintId = resultBlueprintSet.getInt("id");
                } else {
                    // TODO: заменить на исключение
                    System.err.println("Blueprint not found: " + blueprintName);
                    resultBlueprintSet.close();
                    continue;
                }

                insertStmt.setInt(1, blueprintId);
                insertStmt.executeUpdate();

                resultBlueprintSet.close();
            }
        }
    }

    // Resources for construction according to blueprints

    private static void importBlueprintResources(Connection conn,
                                                 String filePath,
                                                 String findBlueprintId,
                                                 String insertSql) throws IOException, SQLException {
        String findResourceSql = "SELECT id FROM RESOURCES WHERE name = ?";
        try (BufferedReader bf = new BufferedReader(new FileReader(filePath));
            PreparedStatement findResourceStmt = conn.prepareStatement(findResourceSql);
            PreparedStatement findBlueprintStmt = conn.prepareStatement(findBlueprintId);
            PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
            String line;
            boolean isHeader = true;
            while((line = bf.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                String[] parts = line.split(delimiter);
                String blueprintName = parts[0];
                String resourceName = parts[1];
                float quantity = Float.parseFloat(parts[2].replace(',', '.'));

                // Requesting a resource ID from the database
                findBlueprintStmt.setString(1, blueprintName);
                findResourceStmt.setString(1, resourceName);

                ResultSet resultBlueprintSet = findBlueprintStmt.executeQuery();
                ResultSet resultResourceSet = findResourceStmt.executeQuery();

                int blueprintId;
                int resourceId;

                if (resultBlueprintSet.next()) {
                    blueprintId = resultBlueprintSet.getInt("id");
                } else {
                    // TODO: заменить на исключение
                    System.err.println("Blueprint not found: " + blueprintName);
                    resultBlueprintSet.close();
                    resultResourceSet.close();
                    continue;
                }

                if (resultResourceSet.next()) {
                    resourceId = resultResourceSet.getInt("id");
                } else {
                    // TODO: заменить на исключение
                    System.err.println("Resource not found: " + resourceName);
                    resultBlueprintSet.close();
                    resultResourceSet.close();
                    continue;
                }

                insertStmt.setInt(1, blueprintId);
                insertStmt.setInt(2,resourceId);
                insertStmt.setFloat(3, quantity);
                insertStmt.executeUpdate();

                resultBlueprintSet.close();
                resultResourceSet.close();
            }
        }
    }
}

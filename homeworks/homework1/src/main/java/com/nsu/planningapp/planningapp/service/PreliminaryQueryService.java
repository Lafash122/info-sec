package com.nsu.planningapp.planningapp.service;

import com.nsu.planningapp.planningapp.infrastructure.db.DatabaseConnection;
import com.nsu.planningapp.planningapp.dto.BuildingInfoDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PreliminaryQueryService {
    // Settlements
    public static List<String> getAllSettlements() throws SQLException {
        String sql = "SELECT name FROM SETTLEMENTS ORDER BY name";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            List<String> cities = new ArrayList<>();
            while (rs.next()) {
                cities.add(rs.getString("name"));
            }
            return cities;
        }
    }

    public static Integer getSettlementId(String cityName) throws SQLException {
        String sql = "SELECT id FROM SETTLEMENTS WHERE name = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cityName);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getInt("id") : null;
        }
    }

    // Transport blueprints

    public static List<String> getAllTransportNames() throws SQLException {
        String sql = "SELECT name FROM TRANSPORT_BLUEPRINTS ORDER BY name";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            List<String> transports = new ArrayList<>();
            while (rs.next()) {
                transports.add(rs.getString("name"));
            }
            return transports;
        }
    }

    public static Integer getTransportBlueprintId(String transportName) throws SQLException {
        String sql = "SELECT id FROM TRANSPORT_BLUEPRINTS WHERE name = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, transportName);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getInt("id") : null;
        }
    }

    public static List<String> getAllTransportBlueprintNames() throws SQLException {
        String sql = "SELECT name FROM TRANSPORT_BLUEPRINTS ORDER BY name";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            List<String> names = new ArrayList<>();
            while (rs.next())
                names.add(rs.getString("name"));

            return names;
        }
    }

    // Resources

    public static List<String> getAllResourcesNames() throws SQLException {
        String sql = "SELECT name FROM RESOURCES ORDER BY name";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            List<String> resources = new ArrayList<>();
            while (rs.next()) {
                resources.add(rs.getString("name"));
            }
            return resources;
        }
    }

    public static Integer getResourceId(String resourceName) throws SQLException {
        String sql = "SELECT id FROM RESOURCES WHERE name = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, resourceName);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getInt("id") : null;
        }
    }

    // Building blueprint types
    public static List<String> getAllBlueprintTypes() throws SQLException {
        String sql = "SELECT DISTINCT blueprint_type FROM BUILDING_BLUEPRINTS ORDER BY blueprint_type";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            List<String> types = new ArrayList<>();
            while (rs.next()) {
                types.add(rs.getString("blueprint_type"));
            }
            return types;
        }
    }

    // Constructed buildings
    public static List<String> getAllBuildings() throws SQLException {
        String sql = "SELECT DISTINCT bb.name FROM BUILDINGS b " +
                     "JOIN BUILDING_BLUEPRINTS bb ON b.blueprint = bb.id ORDER BY bb.name";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            List<String> buildings = new ArrayList<>();
            while (rs.next()) {
                buildings.add(rs.getString("name"));
            }
            return buildings;
        }
    }

    public static List<String> getBuildingsBySettlement(String settlementName) throws SQLException {
        String sql = "SELECT bb.name FROM BUILDINGS b " +
                     "JOIN SETTLEMENTS s ON b.settlement = s.id " +
                     "JOIN BUILDING_BLUEPRINTS bb ON b.blueprint = bb.id " +
                     "WHERE s.name = ? ORDER BY bb.name";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, settlementName);
            ResultSet rs = stmt.executeQuery();

            List<String> buildings = new ArrayList<>();
            while (rs.next()) {
                buildings.add(rs.getString("name"));
            }
            return buildings;
        }
    }

    public static List<String> getAllBuildingBlueprintNames() throws SQLException {
        String sql = "SELECT name FROM BUILDING_BLUEPRINTS ORDER BY name";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            List<String> names = new ArrayList<>();
            while (rs.next())
                names.add(rs.getString("name"));

            return names;
        }
    }

    public static Integer getBuildingBlueprintId(String name) throws SQLException {
        String sql = "SELECT id FROM BUILDING_BLUEPRINTS WHERE name = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getInt("id") : null;
        }
    }

    public static Integer getBuildingId(String buildingName) throws SQLException {
        String sql = "SELECT b.id FROM BUILDINGS b " +
                     "JOIN BUILDING_BLUEPRINTS bb ON b.blueprint = bb.id WHERE bb.name = ? LIMIT 1";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, buildingName);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getInt("id") : null;
        }
    }

    public static List<BuildingInfoDto> getBuildingsWithParking() throws SQLException {
        String sql = "SELECT DISTINCT b.id, bb.name AS blueprint_name, s.name AS settlement_name, bb.blueprint_type " +
                        "FROM BUILDINGS b JOIN BUILDING_BLUEPRINTS bb ON b.blueprint = bb.id " +
                        "JOIN SETTLEMENTS s ON b.settlement = s.id " +
                        "JOIN PUBLIC_FACILITY_BLUEPRINTS pf ON bb.id = pf.id " +
                        "WHERE pf.number_of_parking_spaces > 0 ORDER BY s.name, bb.name";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            List<BuildingInfoDto> list = new ArrayList<>();
            while (rs.next())
                list.add(new BuildingInfoDto(
                    rs.getInt("id"),
                    rs.getString("blueprint_name"),
                    rs.getString("settlement_name"),
                    rs.getString("blueprint_type")
                ));

            return list;
        }
    }

    // Constructed vehicles
    public static Integer getTransportId(String transportName) throws SQLException {
        String sql = "SELECT id FROM TRANSPORT WHERE blueprint = (SELECT id FROM TRANSPORT_BLUEPRINTS WHERE name = ?) LIMIT 1";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, transportName);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getInt("id") : null;
        }
    }

    public static List<Integer> getTransportIdsByName(List<String> transportNames) throws SQLException {
        List<Integer> ids = new ArrayList<>();
        for (String name : transportNames) {
            Integer id = getTransportId(name);
            if (id != null) ids.add(id);
        }
        return ids;
    }
}

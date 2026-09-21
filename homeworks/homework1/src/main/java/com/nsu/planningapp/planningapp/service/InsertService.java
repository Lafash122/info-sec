package com.nsu.planningapp.planningapp.service;

import com.nsu.planningapp.planningapp.infrastructure.db.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class InsertService {
    public void addSettlement(String name) throws Exception {
        String sql = "INSERT INTO SETTLEMENTS (name) VALUES (?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.executeUpdate();
        }
    }

    public void addBuilding(String settlementName, String blueprintName) throws Exception {
        Integer settlementId = PreliminaryQueryService.getSettlementId(settlementName);
        if (settlementId == null)
            throw new Exception("Город не найден: " + settlementName);
        Integer blueprintId = PreliminaryQueryService.getBuildingBlueprintId(blueprintName);
        if (blueprintId == null)
            throw new Exception("Чертёж здания не найден: " + blueprintName);

        String sql = "INSERT INTO BUILDINGS (settlement, blueprint) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, settlementId);
            stmt.setInt(2, blueprintId);
            stmt.executeUpdate();
        }
    }

    public void addTransport(String blueprintName) throws Exception {
        Integer blueprintId = PreliminaryQueryService.getTransportBlueprintId(blueprintName);
        if (blueprintId == null)
            throw new Exception("Чертёж транспорта не найден: " + blueprintName);

        String sql = "INSERT INTO TRANSPORT (blueprint) VALUES (?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, blueprintId);
            stmt.executeUpdate();
        }
    }
}
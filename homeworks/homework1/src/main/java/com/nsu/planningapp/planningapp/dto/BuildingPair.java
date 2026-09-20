package com.nsu.planningapp.planningapp.dto;

public record BuildingPair(String settlementName, String blueprintName) {
    @Override
    public String toString() {
        return settlementName + ";" + blueprintName;
    }
}

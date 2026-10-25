package com.nsu.planningapp.planningapp.infrastructure.csv;

import com.nsu.planningapp.planningapp.dto.BuildingPair;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;

// For exporting data into CSV files (like settlements, buildings, etc.)
public class CsvExporter {
    public static void exportSettlementsToCsv(Collection<String> items, String filePath) throws IOException {
        exportSettlementsToCsv(items, filePath, false);
    }

    public static void exportSettlementsToCsv(Collection<String> items, String filePath, boolean includeHeader)
            throws IOException {

        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath))) {
            if (includeHeader) {
                writer.write("Название населенного пункта");
                writer.newLine();
            }

            for (String item : items) {
                writer.write(item);
                writer.newLine();
            }
        }
    }

    public static void exportBuildingsToCsv(Collection<BuildingPair> items, String filePath, boolean includeHeader)
        throws IOException{
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath))) {
            if (includeHeader) {
                writer.write("Населенный пункт;Чертеж");
                writer.newLine();
            }

            for (BuildingPair item : items) {
                writer.write(item.toString());
                writer.newLine();
            }
        }
    }

    public static void exportTransportToCsv(Collection<String> items, String filePath, boolean includeHeader)
            throws IOException{
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath))) {
            if (includeHeader) {
                writer.write("Название транспорта");
                writer.newLine();
            }

            for (String item : items) {
                writer.write(item);
                writer.newLine();
            }
        }
    }
}
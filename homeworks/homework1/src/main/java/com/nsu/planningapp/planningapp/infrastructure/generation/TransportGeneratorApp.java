package com.nsu.planningapp.planningapp.infrastructure.generation;

import com.nsu.planningapp.planningapp.infrastructure.csv.CsvExporter;

import java.util.List;

public class TransportGeneratorApp {
    public static void generate(List<String> blueprintNames, String outputPath) throws Exception {
        try {
            TransportGenerator generator = new TransportGenerator(blueprintNames);
            List<String> transportList = generator.generateList(42);
            CsvExporter.exportTransportToCsv(transportList, outputPath, true);

            //System.out.println("\nTransport exported to: " + outputPath);
        } catch (Exception e) {
            System.err.println("Transport generation error: " + e.getMessage());
            throw e;
        }
    }
}

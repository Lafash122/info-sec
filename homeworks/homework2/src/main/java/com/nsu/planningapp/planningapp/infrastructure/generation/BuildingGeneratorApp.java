package com.nsu.planningapp.planningapp.infrastructure.generation;

import com.nsu.planningapp.planningapp.infrastructure.csv.CsvExporter;
import com.nsu.planningapp.planningapp.dto.BuildingPair;

import java.util.List;
import java.util.Set;


public class BuildingGeneratorApp {
    public static void generate(List<String> settlementsNames, List<String> blueprintsNames, String outputPath)
            throws Exception {
        try {
            BuildingGenerator generator = new BuildingGenerator(settlementsNames, blueprintsNames);
            Set<BuildingPair> buildingsSet = generator.generateUniqueSet(40);
            CsvExporter.exportBuildingsToCsv(buildingsSet, outputPath, true);

            System.out.println("\nBuildings exported to: " + outputPath);
        } catch (Exception e) {
            System.err.println("Buildings generation error: " + e.getMessage());
            throw e;
        }
    }
}

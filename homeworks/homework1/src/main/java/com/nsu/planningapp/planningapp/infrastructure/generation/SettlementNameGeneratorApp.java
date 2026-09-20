package com.nsu.planningapp.planningapp.infrastructure.generation;

import com.nsu.planningapp.planningapp.infrastructure.csv.CsvExporter;

import java.util.Set;

public class SettlementNameGeneratorApp {
    public static void generate(String outputPath) throws Exception {
        try {
            SettlementNameGenerator generator = new SettlementNameGenerator(
                    SettlementNameDictionary.getRussianPrefixes(),
                    SettlementNameDictionary.getRussianPostfixes(),
                    SettlementNameDictionary.getRussianConnectors()
            );

            Set<String> settlementNames = generator.generateUniqueSet(10);

            //System.out.println("Generated " + settlementNames.size() + " settlement names:");
            //cityNames.forEach(System.out::println);

            CsvExporter.exportSettlementsToCsv(settlementNames, outputPath, true);

            System.out.println("\nSettlements exported to: " + outputPath);
        } catch (Exception e) {
            System.err.println("Settlement generation error: " + e.getMessage());
            throw e;
        }
    }
}
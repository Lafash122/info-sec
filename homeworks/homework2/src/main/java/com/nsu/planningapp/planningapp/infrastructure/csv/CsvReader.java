package com.nsu.planningapp.planningapp.infrastructure.csv;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CsvReader {
    public static List<String> readNamesFromCsv(String filePath, String delimiter) throws IOException {
        return readNamesFromCsv(filePath, delimiter, true);
    }

    public static List<String> readNamesFromCsv(String filePath, String delimiter, boolean hasHeader) throws IOException {
        List<String> names = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstLine = true;

            while ((line = br.readLine()) != null) {
                if (hasHeader && isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                String[] parts = line.split(delimiter);
                String name = parts[0];

                if (!name.isEmpty()) {
                    names.add(name);
                }
            }
        }
        return names;
    }
}

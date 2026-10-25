package com.nsu.planningapp.planningapp.infrastructure.generation;

import java.util.*;

public class TransportGenerator {
    private final Random random = new Random();
    private final List<String> blueprintsNames;

    public TransportGenerator(List<String> blueprintsNames) {
        this.blueprintsNames = blueprintsNames;
    }

    public String generateOneBuilding() throws Exception {
        return getRandomElement(blueprintsNames);
    }

    public List<String> generateList(int count) throws Exception {
        List<String> names = new ArrayList<>();
        while (names.size() < count) {
            names.add(generateOneBuilding());
        }
        return names;
    }

    private String getRandomElement(List<String> list) throws Exception {
        if (list.isEmpty()) {
            throw new IllegalStateException("List is empty");
        }
        return list.get(random.nextInt(list.size()));
    }
}

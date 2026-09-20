package com.nsu.planningapp.planningapp.infrastructure.generation;

import com.nsu.planningapp.planningapp.dto.BuildingPair;

import java.util.*;

public class BuildingGenerator {
    private final Random random = new Random();
    private final List<String> settlementsNames;
    private final List<String> blueprintsNames;

    public BuildingGenerator(List<String> settlementsNames, List<String> blueprintsNames){
        this.settlementsNames = settlementsNames;
        this.blueprintsNames = blueprintsNames;
    }

    public BuildingPair generateOneBuilding() throws Exception {
        String settlementName = getRandomElement(settlementsNames);
        String buildingName = getRandomElement(blueprintsNames);

        return new BuildingPair(settlementName, buildingName);
    }

    public Set<BuildingPair> generateUniqueSet(int count) throws Exception {
        Set<BuildingPair> pairs = new LinkedHashSet<>();
        if (count > settlementsNames.size() * blueprintsNames.size()) {
            throw new IllegalArgumentException("Too many pairs, max number is " +
                    blueprintsNames.size() * blueprintsNames.size());
        }
        while(pairs.size() < count) {
            pairs.add(generateOneBuilding());
        }
        return pairs;
    }

    public List<BuildingPair> generateList(int count, boolean allowDuplicates) throws Exception {
        if (allowDuplicates) {
            List<BuildingPair> names = new ArrayList<>();
            while (names.size() < count) {
                names.add(generateOneBuilding());
            }
            return names;
        } else {
            return new ArrayList<>(generateUniqueSet(count));
        }
    }

    private String getRandomElement(List<String> list) throws Exception {
        if (list.isEmpty()) {
            throw new IllegalStateException("List is empty");
        }
        return list.get(random.nextInt(list.size()));
    }
}


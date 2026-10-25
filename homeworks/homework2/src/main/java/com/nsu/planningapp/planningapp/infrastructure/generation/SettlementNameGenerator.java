package com.nsu.planningapp.planningapp.infrastructure.generation;

import java.util.*;

public class SettlementNameGenerator {
    private final Random random = new Random();
    private final List<String> prefixes;
    private final List<String> postfixes;
    private final List<String> connectors;

    public SettlementNameGenerator(List<String> prefixes, List<String> postfixes) {
        this.prefixes = prefixes;
        this.postfixes = postfixes;
        this.connectors = new ArrayList<>();

        this.connectors.add("");
        this.connectors.add(" ");
        this.connectors.add("-");
    }

    public SettlementNameGenerator(List<String> prefixes, List<String> postfixes, List<String> connectors) {
        this.prefixes = prefixes;
        this.postfixes = postfixes;
        this.connectors = connectors;
    }

    public String generateOneName() throws Exception {
        String prefix = getRandomElement(prefixes);
        String postfix = getRandomElement(postfixes);
        if (!connectors.isEmpty() && random.nextBoolean()) {
            String connector = getRandomElement(connectors);
            if (Objects.equals(connector, "")) {
                return firstUpperCase(prefix) + postfix.toLowerCase();
            }
            return firstUpperCase(prefix) + connector + firstUpperCase(postfix);
        }
        return firstUpperCase(prefix) + postfix.toLowerCase();
    }

    public Set<String> generateUniqueSet(int count) throws Exception {
        Set<String> names = new LinkedHashSet<>();
        while (names.size() < count){
            names.add(generateOneName());
        }
        return names;
    }

    public List<String> generateList(int count, boolean allowDublicates) throws Exception {
        if (allowDublicates) {
            List<String> names = new ArrayList<>();
            while (names.size() < count) {
                names.add(generateOneName());
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

    private String firstUpperCase(String word) {
        if (word == null || word.isEmpty()) {
            return "";
        }
        return word.substring(0, 1).toUpperCase() + word.substring(1);
    }
}

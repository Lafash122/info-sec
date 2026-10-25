package com.nsu.planningapp.planningapp.infrastructure.generation;

import java.util.Arrays;
import java.util.List;

public class SettlementNameDictionary {
    // Russian prefixes
    public static List<String> getRussianPrefixes() {
        return Arrays.asList(
                "Нижне", "Верхне", "Ново", "Старо", "Красн", "Черн", "Бел",
                "Мал", "Велико", "Свято", "Троицк", "Петр", "Александр",
                "Екатерин", "Владимир", "Дмитров", "Южн", "Север"
        );
    }

    // Russian postfixes
    public static List<String> getRussianPostfixes() {
        return Arrays.asList(
                "камск", "уральск", "град", "город", "полис", "дар", "бург",
                "ов", "ев", "ин", "ск", "вольск", "дон", "реченск",
                "горск", "славль", "мерь", "яр", "слав"
        );
    }

    // Russian connectors
    public static List<String> getRussianConnectors() {
        return Arrays.asList("-", " ", "");
    }
}

package com.example.recommendation.config;

import java.util.Map;
import java.util.HashMap;

public enum DeltaDays {
    DAY_BEFORE,
    DAY_AFTER,
    TODAY;

    private static final Map<String, DeltaDays> lookup = new HashMap<>();

    static {
        for (DeltaDays deltaDay : DeltaDays.values()) {
            lookup.put(deltaDay.name(), deltaDay);
        }
    }

    public static Map<String, DeltaDays> lookup() {
        return lookup;
    }
}

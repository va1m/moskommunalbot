package com.va1m.moskommunalbot.interaction;

import java.util.stream.Stream;

/** Possible states during interaction with user */
public enum State {

    START,
    USE_LAST_METERS,
    LAST_COLD_WATER_METERS,
    CURRENT_COLD_WATER_METERS,
    LAST_HOT_WATER_METERS,
    CURRENT_HOT_WATER_METERS,
    LAST_ELECTRICITY_METERS,
    CURRENT_ELECTRICITY_METERS,
    SHOWING_RESULTS;

    /** Returns a constant by its name or {@link #START} if nothing was found */
    public static State of(String str) {
        return Stream.of(values())
            .filter(val -> val.name().equals(str))
            .findAny()
            .orElse(START);
    }
}

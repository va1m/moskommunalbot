package com.va1m.komunalko.parsers;

import java.time.LocalDate;

/** Perces the page and founds prices and dates since they have been set */
public final class ColdWaterParser {

    private ColdWaterParser() {
    }

    public static Price[] parse() {
        return new Price[] {
                Price.of(LocalDate.now().minusDays(1), 100)
        };
    }
}

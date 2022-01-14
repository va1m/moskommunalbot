package com.va1m.moskommunalbot.interaction;

import java.time.LocalDate;

/**
 * Just returns current day.
 * Clients using this service can mock current day in their tests with any other static date.
 */
public class TimeService {

    /** Returns result of {@link LocalDate#now} */
    public LocalDate getToday() {
        return LocalDate.now();
    }
}

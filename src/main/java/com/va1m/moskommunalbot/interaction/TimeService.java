package com.va1m.moskommunalbot.interaction;

import dagger.Reusable;

import java.time.LocalDate;

import javax.inject.Inject;

/**
 * Just returns current day.
 * Clients using this service can mock current day in their tests with any other static date.
 */
@Reusable
public class TimeService {

    @Inject
    public TimeService() {
        // necessary to be injectable for Dagger DI
    }

    /** Returns result of {@link LocalDate#now} */
    public LocalDate getToday() {
        return LocalDate.now();
    }
}

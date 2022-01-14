package com.va1m.moskommunalbot.interaction;

import static org.assertj.core.api.Assertions.assertThat;

import com.va1m.moskommunalbot.model.State;

import org.junit.jupiter.api.Test;

/** Tests {@link State} */
class StateTest {

    @Test
    void found() {
        assertThat(State.of("LAST_COLD_WATER_METERS")).isSameAs(State.LAST_COLD_WATER_METERS);
    }

    @Test
    void notFound() {
        assertThat(State.of("NOT_EXISTING_CONSTANT")).isSameAs(State.START);
    }
}
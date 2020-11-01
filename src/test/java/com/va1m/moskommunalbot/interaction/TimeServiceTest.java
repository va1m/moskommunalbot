package com.va1m.moskommunalbot.interaction;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

/** Tests {@link TimeService} */
class TimeServiceTest {

    private static final TimeService TIME_SERVICE = new TimeService();

    @Test
    void today() {
        assertThat(TIME_SERVICE.getToday()).isEqualTo(LocalDate.now());
    }
}
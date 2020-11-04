package com.va1m.moskommunalbot.interaction.stateprocessors;

import static org.assertj.core.api.Assertions.assertThat;

import com.va1m.moskommunalbot.interaction.State;
import org.junit.jupiter.api.Test;

/** Tests {@link StartStateProcessor} */
class StartStateProcessorTest {

    private final StateProcessor stateProcessor = new StartStateProcessor();

    @Test
    void forState() {
        assertThat(stateProcessor.forState()).isSameAs(State.START);
    }

    @Test
    void processOutput() {
        assertThat(stateProcessor.buildMessageForUser(null)).isNull();
    }
}
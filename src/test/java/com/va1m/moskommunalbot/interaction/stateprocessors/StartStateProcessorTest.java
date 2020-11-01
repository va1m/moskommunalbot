package com.va1m.moskommunalbot.interaction.stateprocessors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;

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
        assertThatIllegalStateException()
            .isThrownBy(() -> stateProcessor.processOutput(null))
            .withMessage("Shouldn't be called");
    }
}
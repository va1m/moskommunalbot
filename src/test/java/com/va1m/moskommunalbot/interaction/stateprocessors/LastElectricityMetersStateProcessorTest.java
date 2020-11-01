package com.va1m.moskommunalbot.interaction.stateprocessors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import com.va1m.moskommunalbot.interaction.InteractionContext;
import com.va1m.moskommunalbot.interaction.InvalidInputException;
import com.va1m.moskommunalbot.interaction.State;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/** Tests {@link LastElectricityMetersStateProcessor} */
class LastElectricityMetersStateProcessorTest {

    private final StateProcessor stateProcessor = new LastElectricityMetersStateProcessor();

    @Test
    void forState() {
        assertThat(stateProcessor.forState()).isSameAs(State.WAITING_FOR_LAST_ELECTRICITY_METERS);
    }

    @ParameterizedTest
    @CsvSource({"10000", "10000.0", "10000,0", "010000,0", "10000.09"})
    void validInput(String currentMeters) {
        final var interactionContext = new InteractionContext();
        stateProcessor.processInput(currentMeters, interactionContext);
        assertThat(interactionContext.getLastElectricityMeters()).isEqualTo(100000);
    }

    @Test
    void invalidInput() {
        final var interactionContext = new InteractionContext();
        assertThatExceptionOfType(InvalidInputException.class)
            .isThrownBy(() -> stateProcessor.processInput("abc", interactionContext))
            .withMessage("Только цифры, запятую или точку, пожалуйста.");
    }

    @Test
    void processOutput() {
        final var expected = "Введите показание счетчика электричества на начало периода. "
            + "В киловаттах, например: 23395,1.";
        assertThat(stateProcessor.processOutput(null)).isEqualTo(expected);
    }
}
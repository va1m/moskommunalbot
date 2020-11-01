package com.va1m.moskommunalbot.interaction.stateprocessors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import com.va1m.moskommunalbot.interaction.InteractionContext;
import com.va1m.moskommunalbot.interaction.InvalidInputException;
import com.va1m.moskommunalbot.interaction.State;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/** Tests {@link LastHotWaterMetersStateProcessor} */
class LastHotWaterMetersStateProcessorTest {

    private final StateProcessor stateProcessor = new LastHotWaterMetersStateProcessor();

    @Test
    void forState() {
        assertThat(stateProcessor.forState()).isSameAs(State.WAITING_FOR_LAST_HOT_WATER_METERS);
    }

    @ParameterizedTest
    @CsvSource({"100", "100.0", "100,0", "0100,0", "100.0009"})
    void validInput(String currentMeters) {
        final var interactionContext = new InteractionContext();
        stateProcessor.processInput(currentMeters, interactionContext);
        assertThat(interactionContext.getLastHotWaterMeters()).isEqualTo(100000);
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
        final var expected = "Введите показание счетчика горячей воды на начало периода. "
            + "В метрах кубических, например: 618,471.";
        assertThat(stateProcessor.processOutput(null)).isEqualTo(expected);
    }
}
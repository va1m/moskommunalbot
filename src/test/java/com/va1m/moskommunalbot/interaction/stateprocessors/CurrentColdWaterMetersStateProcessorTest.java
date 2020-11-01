package com.va1m.moskommunalbot.interaction.stateprocessors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import com.va1m.moskommunalbot.interaction.InteractionContext;
import com.va1m.moskommunalbot.interaction.InvalidInputException;
import com.va1m.moskommunalbot.interaction.State;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/** Tests {@link CurrentColdWaterMetersStateProcessor} */
class CurrentColdWaterMetersStateProcessorTest {

    private final StateProcessor stateProcessor = new CurrentColdWaterMetersStateProcessor();

    @Test
    void forState() {
        assertThat(stateProcessor.forState()).isSameAs(State.WAITING_FOR_CURRENT_COLD_WATER_METERS);
    }

    @ParameterizedTest
    @CsvSource({"100", "101"})
    void currentInputMoreOrEqualToLast(String currentMeters) {
        final var interactionContext = new InteractionContext();
        interactionContext.setLastColdWaterMeters(100);
        stateProcessor.processInput(currentMeters, interactionContext);
        assertThat(interactionContext.getCurrentColdWaterMeters()).isEqualTo(Integer.parseInt(currentMeters));
    }

    @Test
    void currentInputLessThenLast() {
        final var interactionContext = new InteractionContext();
        interactionContext.setLastColdWaterMeters(101);
        assertThatExceptionOfType(InvalidInputException.class)
            .isThrownBy(() -> stateProcessor.processInput("100", interactionContext))
            .withMessage("Текущее показание счётчика не может быть меньше предыдущего показания. Введите корректное показание счётчика.");
    }

    @Test
    void currentInputIsNotNumber() {
        final var interactionContext = new InteractionContext();
        interactionContext.setLastColdWaterMeters(101);
        assertThatExceptionOfType(InvalidInputException.class)
            .isThrownBy(() -> stateProcessor.processInput("abc", interactionContext))
            .withMessage("Только цифры, пожалуйста");
    }

    @Test
    void processOutput() {
        assertThat(stateProcessor.processOutput(null)).isEqualTo("Теперь, пожалуйста, введите текущее показание счетчика холодной воды.");
    }
}
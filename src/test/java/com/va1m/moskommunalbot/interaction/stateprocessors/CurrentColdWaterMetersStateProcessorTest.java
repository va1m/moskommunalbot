package com.va1m.moskommunalbot.interaction.stateprocessors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import com.va1m.moskommunalbot.model.Calculation;
import com.va1m.moskommunalbot.interaction.InvalidInputException;
import com.va1m.moskommunalbot.model.State;
import com.va1m.moskommunalbot.model.InteractionMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/** Tests {@link CurrentColdWaterMetersStateProcessor} */
class CurrentColdWaterMetersStateProcessorTest {

    private final StateProcessor stateProcessor = new CurrentColdWaterMetersStateProcessor();

    @Test
    void forState() {
        assertThat(stateProcessor.forState()).isSameAs(State.CURRENT_COLD_WATER_METERS);
    }

    @ParameterizedTest
    @CsvSource({
        "100", "100.0", "100,0", "0100,0", "100.0009",
        "101", "100.001", "100,001"})
    void currentInputMoreOrEqualToLast(String currentMeters) {
        final var interactionContext = new Calculation();
        interactionContext.setLastColdWaterMeters(100000);
        stateProcessor.processAnswer(currentMeters, interactionContext);
        final var expected = (int) (Double.parseDouble(currentMeters.replace(",", ".")) * 1000.0D);
        assertThat(interactionContext.getCurrentColdWaterMeters()).isEqualTo(expected);
    }

    @Test
    void currentInputLessThenLast() {
        final var interactionContext = new Calculation();
        interactionContext.setLastColdWaterMeters(100001);
        assertThatExceptionOfType(InvalidInputException.class)
            .isThrownBy(() -> stateProcessor.processAnswer("100.000", interactionContext))
            .withMessage("Текущее показание счётчика не может быть меньше предыдущего показания. Введите корректное показание счётчика.");
    }

    @Test
    void currentInputIsNotNumber() {
        final var interactionContext = new Calculation();
        assertThatExceptionOfType(InvalidInputException.class)
            .isThrownBy(() -> stateProcessor.processAnswer("abc", interactionContext))
            .withMessage("Только цифры, запятую или точку, пожалуйста.");
    }

    @Test
    void processOutput() {
        final var expected = "Пожалуйста, введите *текущее* показание счетчика холодной воды. "
            + "В метрах кубических, например: 410,127.";
        assertThat(stateProcessor.buildMessageForUser(null))
            .usingRecursiveComparison()
            .isEqualTo(InteractionMessage.of(expected));
    }
}
package com.va1m.moskommunalbot.interaction.stateprocessors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import com.va1m.moskommunalbot.interaction.InvalidInputException;
import com.va1m.moskommunalbot.interaction.State;
import com.va1m.moskommunalbot.model.Calculation;
import com.va1m.moskommunalbot.model.InteractionMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/** Tests {@link CurrentElectricityMetersStateProcessor} */
class CurrentElectricityMetersStateProcessorTest {

    private final StateProcessor stateProcessor = new CurrentElectricityMetersStateProcessor();

    @Test
    void forState() {
        assertThat(stateProcessor.forState()).isSameAs(State.CURRENT_ELECTRICITY_METERS);
    }

    @ParameterizedTest
    @CsvSource({
        "10000", "10000.0", "10000,0", "010000,0", "10000.09",
        "10001", "10000.1", "10000,1"})
    void currentInputMoreOrEqualToLast(String currentMeters) {
        final var interactionContext = new Calculation();
        interactionContext.setLastElectricityMeters(100000);
        stateProcessor.processAnswer(currentMeters, interactionContext);
        final var expected = (int) (Double.parseDouble(currentMeters.replace(",", ".")) * 10.0D);
        assertThat(interactionContext.getCurrentElectricityMeters()).isEqualTo(expected);
    }

    @Test
    void currentInputLessThenLast() {
        final var interactionContext = new Calculation();
        interactionContext.setLastElectricityMeters(100001);
        assertThatExceptionOfType(InvalidInputException.class)
            .isThrownBy(() -> stateProcessor.processAnswer("10000.0", interactionContext))
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
        final var expected = "Введите *текущее* показание счетчика электричества. "
            + "В киловаттах, например: 23451,7.";
        assertThat(stateProcessor.buildMessageForUser(null))
            .usingRecursiveComparison()
            .isEqualTo(InteractionMessage.of(expected));
    }
}
package com.va1m.moskommunalbot.interaction.stateprocessors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import com.va1m.moskommunalbot.interaction.InvalidInputException;
import com.va1m.moskommunalbot.model.State;
import com.va1m.moskommunalbot.model.Calculation;
import com.va1m.moskommunalbot.model.InteractionMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/** Tests {@link CurrentHotWaterMetersStateProcessor} */
class CurrentHotWaterMetersStateProcessorTest {

    private final StateProcessor stateProcessor = new CurrentHotWaterMetersStateProcessor();

    @Test
    void forState() {
        assertThat(stateProcessor.forState()).isSameAs(State.CURRENT_HOT_WATER_METERS);
    }

    @ParameterizedTest
    @CsvSource({
        "100", "100.0", "100,0", "0100,0", "100.0009",
        "101", "100.001", "100,001"})
    void currentInputMoreOrEqualToLast(String currentMeters) {
        final var interactionContext = new Calculation();
        interactionContext.setLastHotWaterMeters(100000);
        stateProcessor.processAnswer(currentMeters, interactionContext);
        final var expected = (int) (Double.parseDouble(currentMeters.replace(",", ".")) * 1000.0D);
        assertThat(interactionContext.getCurrentHotWaterMeters()).isEqualTo(expected);
    }

    @Test
    void currentInputLessThenLast() {
        final var interactionContext = new Calculation();
        interactionContext.setLastHotWaterMeters(100001);
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
        final var expected = "Введите *текущее* показание счетчика горячей воды. "
            + "В метрах кубических, например: 621,334.";
        assertThat(stateProcessor.buildMessageForUser(null))
            .usingRecursiveComparison()
            .isEqualTo(InteractionMessage.of(expected));
    }
}
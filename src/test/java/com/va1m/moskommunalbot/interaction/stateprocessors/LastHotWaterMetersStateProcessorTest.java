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

/** Tests {@link LastHotWaterMetersStateProcessor} */
class LastHotWaterMetersStateProcessorTest {

    private final StateProcessor stateProcessor = new LastHotWaterMetersStateProcessor();

    @Test
    void forState() {
        assertThat(stateProcessor.forState()).isSameAs(State.LAST_HOT_WATER_METERS);
    }

    @ParameterizedTest
    @CsvSource({"100", "100.0", "100,0", "0100,0", "100.0009"})
    void validInput(String currentMeters) {
        final var interactionContext = new Calculation();
        stateProcessor.processAnswer(currentMeters, interactionContext);
        assertThat(interactionContext.getLastHotWaterMeters()).isEqualTo(100000);
    }

    @Test
    void invalidInput() {
        final var interactionContext = new Calculation();
        assertThatExceptionOfType(InvalidInputException.class)
            .isThrownBy(() -> stateProcessor.processAnswer("abc", interactionContext))
            .withMessage("Только цифры, запятую или точку, пожалуйста.");
    }

    @Test
    void processOutputWithLastMeters() {
        final var calculation = new Calculation()
            .setLastHotWaterMeters(100);
        assertThat(stateProcessor.buildMessageForUser(calculation)).isNull();
    }

    @Test
    void processOutputWithoutLastMeters() {
        final var expected = "Введите показание счетчика горячей воды *на начало периода*. "
            + "В метрах кубических, например: 618,471.";
        assertThat(stateProcessor.buildMessageForUser(new Calculation()))
            .usingRecursiveComparison()
            .isEqualTo(InteractionMessage.of(expected));
    }
}
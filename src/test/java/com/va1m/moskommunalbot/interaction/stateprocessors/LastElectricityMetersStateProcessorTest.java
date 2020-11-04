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

/** Tests {@link LastElectricityMetersStateProcessor} */
class LastElectricityMetersStateProcessorTest {

    private final StateProcessor stateProcessor = new LastElectricityMetersStateProcessor();

    @Test
    void forState() {
        assertThat(stateProcessor.forState()).isSameAs(State.LAST_ELECTRICITY_METERS);
    }

    @ParameterizedTest
    @CsvSource({"10000", "10000.0", "10000,0", "010000,0", "10000.09"})
    void validInput(String currentMeters) {
        final var interactionContext = new Calculation();
        stateProcessor.processAnswer(currentMeters, interactionContext);
        assertThat(interactionContext.getLastElectricityMeters()).isEqualTo(100000);
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
            .setLastElectricityMeters(100);
        assertThat(stateProcessor.buildMessageForUser(calculation)).isNull();
    }

    @Test
    void processOutputWithoutLastMeters() {
        final var expected = "Введите показание счетчика электричества *на начало периода*. "
            + "В киловаттах, например: 23395,1.";
        assertThat(stateProcessor.buildMessageForUser(new Calculation()))
            .usingRecursiveComparison()
            .isEqualTo(InteractionMessage.of(expected));
    }
}
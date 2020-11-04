package com.va1m.moskommunalbot.interaction.stateprocessors;

import static org.assertj.core.api.Assertions.assertThat;

import com.va1m.moskommunalbot.model.InteractionMessage;
import com.va1m.moskommunalbot.model.Calculation;
import com.va1m.moskommunalbot.interaction.State;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/** Tests {@link UseLastMetersStateProcessor} */
class UseLastMetersStateProcessorTest {

    private final static StateProcessor STATE_PROCESSOR = new UseLastMetersStateProcessor();

    @Test
    void forState() {
        assertThat(STATE_PROCESSOR.forState()).isSameAs(State.USE_LAST_METERS);
    }

    @Test
    void buildMessageForUser() {
        final var calculation = new Calculation()
            .setCurrentColdWaterMeters(100)
            .setLastColdWaterMeters(150)
            .setCurrentHotWaterMeters(200)
            .setLastHotWaterMeters(250)
            .setCurrentElectricityMeters(3000)
            .setLastElectricityMeters(3500);

        final var interactionMessage = STATE_PROCESSOR.buildMessageForUser(calculation);

        final var expected = InteractionMessage.of("Привет! Начинаем новый расчёт за коммунальные услуги.\n"
            + "Хотите использовать показания счётчиков из предыдущего расчёта?\n"
            + "Холодная вода: *0.100*\n"
            + "Горячая вода: *0.200*\n"
            + "Электричество: *300.0*.",
            new String[]{"Да", "Нет"});

        assertThat(interactionMessage)
            .usingRecursiveComparison()
            .isEqualTo(expected);
    }

    @Test
    void buildMessageForUserWithNoPrevCalc() {
        final var calculation = new Calculation();
        final var interactionMessage = STATE_PROCESSOR.buildMessageForUser(calculation);
        assertThat(interactionMessage).isNull();
    }

    @ParameterizedTest
    @CsvSource({
        "Yes", "yeS", "yes", "Да", "да", "дА"
    })
    void processAnswerWithYes(String input) {
        final var calculation = new Calculation()
            .setCurrentColdWaterMeters(100)
            .setLastColdWaterMeters(150)
            .setCurrentHotWaterMeters(200)
            .setLastHotWaterMeters(250)
            .setCurrentElectricityMeters(3000)
            .setLastElectricityMeters(3500);

        STATE_PROCESSOR.processAnswer(input, calculation);

        final var expected = new Calculation()
            .setLastColdWaterMeters(100)
            .setLastHotWaterMeters(200)
            .setLastElectricityMeters(3000);

        assertThat(calculation)
            .usingRecursiveComparison()
            .isEqualTo(expected);
    }

    @Test
    void processAnswerWithNo() {
        final var calculation = new Calculation()
            .setCurrentColdWaterMeters(100)
            .setLastColdWaterMeters(150)
            .setCurrentHotWaterMeters(200)
            .setLastHotWaterMeters(250)
            .setCurrentElectricityMeters(3000)
            .setLastElectricityMeters(3500);

        STATE_PROCESSOR.processAnswer("No", calculation);

        final var expected = new Calculation();

        assertThat(calculation)
            .usingRecursiveComparison()
            .isEqualTo(expected);
    }
}
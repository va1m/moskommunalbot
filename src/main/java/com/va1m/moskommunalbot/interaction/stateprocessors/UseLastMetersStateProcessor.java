package com.va1m.moskommunalbot.interaction.stateprocessors;

import com.va1m.moskommunalbot.model.InteractionMessage;
import com.va1m.moskommunalbot.model.Calculation;
import com.va1m.moskommunalbot.interaction.State;

/** Prepare output message text for {@link State#USE_LAST_METERS} */
public class UseLastMetersStateProcessor implements StateProcessor {

    @Override
    public State forState() {
        return State.USE_LAST_METERS;
    }

    @Override
    public InteractionMessage buildMessageForUser(Calculation calculation) {

        if (calculation.getCurrentColdWaterMeters() == null
            || calculation.getCurrentHotWaterMeters() == null
            || calculation.getCurrentElectricityMeters() == null) {
            return null;
        }

        final var template = "Привет! Начинаем новый расчёт за коммунальные услуги.\n"
            + "Хотите использовать показания счётчиков из предыдущего расчёта?\n"
            + "Холодная вода: *%.3f*\n"
            + "Горячая вода: *%.3f*\n"
            + "Электричество: *%.1f*.";

        final var text = String.format(template,
            calculation.getCurrentColdWaterMeters() / 1000.0D,
            calculation.getCurrentHotWaterMeters() / 1000.0D,
            calculation.getCurrentElectricityMeters() / 10.0D);

        return InteractionMessage.of(text, new String[]{"Да", "Нет"});
    }

    @Override
    public void processAnswer(String input, Calculation calculation) {
        calculation.setLastColdWaterMeters(null);
        calculation.setLastHotWaterMeters(null);
        calculation.setLastElectricityMeters(null);
        if ("да".equalsIgnoreCase(input) || "yes".equalsIgnoreCase(input)) {
            calculation.setLastColdWaterMeters(calculation.getCurrentColdWaterMeters());
            calculation.setLastHotWaterMeters(calculation.getCurrentHotWaterMeters());
            calculation.setLastElectricityMeters(calculation.getCurrentElectricityMeters());
        }
        calculation.setCurrentColdWaterMeters(null);
        calculation.setCurrentHotWaterMeters(null);
        calculation.setCurrentElectricityMeters(null);
    }
}

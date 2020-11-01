package com.va1m.moskommunalbot.interaction.stateprocessors;

import com.va1m.moskommunalbot.interaction.InteractionContext;
import com.va1m.moskommunalbot.interaction.State;

/** Prepare output message text for {@link State#WAITING_FOR_LAST_COLD_WATER_METERS} state and then handles user input data */
public class LastColdWaterMetersStateProcessor implements StateProcessor {

    @Override
    public State forState() {
        return State.WAITING_FOR_LAST_COLD_WATER_METERS;
    }

    @Override
    public void processInput(String input, InteractionContext interactionContext) {
        storeIfValid(input, 3, interactionContext::setLastColdWaterMeters);
    }

    @Override
    public String processOutput(InteractionContext interactionContext) {
        return "Привет! Начинаем новый расчет за коммунальные услуги.\n\n"
            + "Пожалуйста, введите показание счетчика холодной воды на начало периода. "
            + "В метрах кубических, например: 394,630.";
    }
}

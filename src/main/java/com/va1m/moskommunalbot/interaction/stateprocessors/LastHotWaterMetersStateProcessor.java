package com.va1m.moskommunalbot.interaction.stateprocessors;

import com.va1m.moskommunalbot.interaction.InteractionContext;
import com.va1m.moskommunalbot.interaction.State;

/** Prepare output message text for {@link State#WAITING_FOR_LAST_HOT_WATER_METERS} state and then handles user input data */
public class LastHotWaterMetersStateProcessor implements StateProcessor {

    @Override
    public State forState() {
        return State.WAITING_FOR_LAST_HOT_WATER_METERS;
    }

    @Override
    public void processInput(String input, InteractionContext interactionContext) {
        storeIfValid(input, 3, interactionContext::setLastHotWaterMeters);
    }

    @Override
    public String processOutput(InteractionContext interactionContext) {
        return "Введите показание счетчика горячей воды на начало периода. "
            + "В метрах кубических, например: 618,471.";
    }
}

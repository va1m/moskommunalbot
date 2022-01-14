package com.va1m.moskommunalbot.interaction.stateprocessors;

import com.va1m.moskommunalbot.model.InteractionMessage;
import com.va1m.moskommunalbot.model.Calculation;
import com.va1m.moskommunalbot.model.State;

/** Prepare output message text for {@link State#LAST_HOT_WATER_METERS} state and then handles user input data */
public class LastHotWaterMetersStateProcessor implements StateProcessor {

    @Override
    public State forState() {
        return State.LAST_HOT_WATER_METERS;
    }

    @Override
    public InteractionMessage buildMessageForUser(Calculation calculation) {
        if (calculation.getLastHotWaterMeters() != null) {
            return null;
        }
        return InteractionMessage.of("Введите показание счетчика горячей воды *на начало периода*. "
            + "В метрах кубических, например: 618,471.");
    }

    @Override
    public void processAnswer(String input, Calculation calculation) {
        storeIfValid(input, 3, calculation::setLastHotWaterMeters);
    }
}

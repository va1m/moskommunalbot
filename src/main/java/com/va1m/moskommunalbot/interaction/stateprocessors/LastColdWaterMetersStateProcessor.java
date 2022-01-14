package com.va1m.moskommunalbot.interaction.stateprocessors;

import com.va1m.moskommunalbot.model.InteractionMessage;
import com.va1m.moskommunalbot.model.Calculation;
import com.va1m.moskommunalbot.model.State;

/** Prepare output message text for {@link State#LAST_COLD_WATER_METERS} state and then handles user input data */
public class LastColdWaterMetersStateProcessor implements StateProcessor {

    @Override
    public State forState() {
        return State.LAST_COLD_WATER_METERS;
    }

    @Override
    public InteractionMessage buildMessageForUser(Calculation calculation) {
        if (calculation.getLastColdWaterMeters() != null) {
            return null; //go to next step
        }
        return InteractionMessage.of("Пожалуйста, введите показание счетчика холодной воды *на начало периода*. "
            + "В метрах кубических, например: 394,630.");
    }

    @Override
    public void processAnswer(String input, Calculation calculation) {
        storeIfValid(input, 3, calculation::setLastColdWaterMeters);
    }
}

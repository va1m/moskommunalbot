package com.va1m.moskommunalbot.interaction.stateprocessors;

import com.va1m.moskommunalbot.model.InteractionMessage;
import com.va1m.moskommunalbot.model.Calculation;
import com.va1m.moskommunalbot.interaction.State;

/** Prepare output message text for {@link State#LAST_ELECTRICITY_METERS} state and then handles user input data */
public class LastElectricityMetersStateProcessor implements StateProcessor {

    @Override
    public State forState() {
        return State.LAST_ELECTRICITY_METERS;
    }

    @Override
    public InteractionMessage buildMessageForUser(Calculation calculation) {
        if (calculation.getLastElectricityMeters() != null) {
            return null;
        }
        return InteractionMessage.of("Введите показание счетчика электричества *на начало периода*. "
            + "В киловаттах, например: 23395,1.");
    }

    @Override
    public void processAnswer(String input, Calculation calculation) {
        storeIfValid(input, 1, calculation::setLastElectricityMeters);
    }
}

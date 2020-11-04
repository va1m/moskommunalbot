package com.va1m.moskommunalbot.interaction.stateprocessors;

import com.va1m.moskommunalbot.model.InteractionMessage;
import com.va1m.moskommunalbot.model.Calculation;
import com.va1m.moskommunalbot.interaction.State;

/** Prepare output message text for {@link State#CURRENT_ELECTRICITY_METERS} state and then handles user input data */
public class CurrentElectricityMetersStateProcessor implements StateProcessor {

    @Override
    public State forState() {
        return State.CURRENT_ELECTRICITY_METERS;
    }

    @Override
    public InteractionMessage buildMessageForUser(Calculation calculation) {
        return InteractionMessage.of("Введите *текущее* показание счетчика электричества. "
            + "В киловаттах, например: 23451,7.");
    }

    @Override
    public void processAnswer(String input, Calculation calculation) {
        storeIfValid(input, 1, calculation::getLastElectricityMeters, calculation::setCurrentElectricityMeters);
    }
}

package com.va1m.moskommunalbot.interaction.stateprocessors;

import com.va1m.moskommunalbot.model.InteractionMessage;
import com.va1m.moskommunalbot.model.Calculation;
import com.va1m.moskommunalbot.interaction.State;

/** Prepare output message text for {@link State#CURRENT_HOT_WATER_METERS} state and then handles user input data */
public class CurrentHotWaterMetersStateProcessor implements StateProcessor {

    @Override
    public State forState() {
        return State.CURRENT_HOT_WATER_METERS;
    }

    @Override
    public InteractionMessage buildMessageForUser(Calculation calculation) {
        return InteractionMessage.of("Введите *текущее* показание счетчика горячей воды. "
            + "В метрах кубических, например: 621,334.");
    }

    @Override
    public void processAnswer(String input, Calculation calculation) {
        storeIfValid(input, 3, calculation::getLastHotWaterMeters, calculation::setCurrentHotWaterMeters);
    }
}

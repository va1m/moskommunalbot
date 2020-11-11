package com.va1m.moskommunalbot.interaction.stateprocessors;

import com.va1m.moskommunalbot.model.InteractionMessage;
import com.va1m.moskommunalbot.model.Calculation;
import com.va1m.moskommunalbot.interaction.State;

/** Prepare output message text for {@link State#START} */
public class StartStateProcessor implements StateProcessor {

    @Override
    public State forState() {
        return State.START;
    }

    @Override
    public InteractionMessage buildMessageForUser(Calculation calculation) {
        return null;
    }

    @Override
    public void processAnswer(String input, Calculation calculation) {
        // Do nothing here
    }
}

package com.va1m.moskommunalbot.interaction.stateprocessors;

import com.va1m.moskommunalbot.interaction.InteractionContext;
import com.va1m.moskommunalbot.interaction.State;

/** Prepare output message text for {@link State#START} */
public class StartStateProcessor implements StateProcessor {

    @Override
    public State forState() {
        return State.START;
    }

    @Override
    public void processInput(String input, InteractionContext interactionContext) {
        // Nothing to do here
    }

    @Override
    public String processOutput(InteractionContext interactionContext) {
        throw new IllegalStateException("Shouldn't be called");
    }
}

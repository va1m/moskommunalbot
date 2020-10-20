package com.va1m.moskommunalbot.interaction.stateprocessors;

import com.va1m.moskommunalbot.interaction.InteractionContext;
import com.va1m.moskommunalbot.interaction.InvalidInputException;
import com.va1m.moskommunalbot.interaction.State;

import java.util.function.IntConsumer;

/** Base interface for all state processors handling user inputs and preparing output text messages for him */
public interface StateProcessor {

    /** For which state the processor is */
    State forState();

    /** Processes user input and put valid data into {@link InteractionContext} */
    void processInput(String input, InteractionContext interactionContext);

    /** Provides methods (processors) to build output text messages for user */
    String processOutput(InteractionContext interactionContext);

    default void storeIfValid(String input, IntConsumer setter) {
        try {
            final var intInput = Integer.parseInt(input.trim());
            setter.accept(intInput);
        } catch (NumberFormatException e) {
            throw new InvalidInputException("Только цифры, пожалуйста");
        }
    }
}

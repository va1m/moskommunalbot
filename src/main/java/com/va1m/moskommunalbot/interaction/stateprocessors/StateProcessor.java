package com.va1m.moskommunalbot.interaction.stateprocessors;

import com.va1m.moskommunalbot.interaction.InteractionContext;
import com.va1m.moskommunalbot.interaction.InvalidInputException;
import com.va1m.moskommunalbot.interaction.State;

import java.util.function.IntConsumer;
import java.util.function.IntSupplier;

/** Base interface for all state processors handling user inputs and preparing output text messages for him */
public interface StateProcessor {

    /** For which state the processor is */
    State forState();

    /** Processes user input and put valid data into {@link InteractionContext} */
    void processInput(String input, InteractionContext interactionContext);

    /** Provides methods (processors) to build output text messages for user */
    String processOutput(InteractionContext interactionContext);

    default void storeIfValid(String input, int accuracy, IntConsumer setter) {
        final int intInput = getInputAsInt(input, accuracy);
        setter.accept(intInput);
    }

    default void storeIfValid(String input, int accuracy, IntSupplier getter, IntConsumer setter) {
        final int intInput = getInputAsInt(input, accuracy);
        final var lastMeterValue = getter.getAsInt();
        if (intInput < lastMeterValue) {
            throw new InvalidInputException("Текущее показание счётчика не может быть меньше предыдущего показания. "
                + "Введите корректное показание счётчика.");
        }
        setter.accept(intInput);
    }

    private static int getInputAsInt(String input, int accuracy) {
        final int intInput;
        try {
            intInput = (int)(Double.parseDouble(input.replace(",", ".").trim()) * Math.pow(10.0D, accuracy));
        } catch (NumberFormatException e) {
            throw new InvalidInputException("Только цифры, запятую или точку, пожалуйста.");
        }
        return intInput;
    }
}

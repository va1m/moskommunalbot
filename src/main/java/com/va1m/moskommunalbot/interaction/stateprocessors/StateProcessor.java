package com.va1m.moskommunalbot.interaction.stateprocessors;

import com.va1m.moskommunalbot.model.InteractionMessage;
import com.va1m.moskommunalbot.model.Calculation;
import com.va1m.moskommunalbot.interaction.InvalidInputException;
import com.va1m.moskommunalbot.model.State;

import java.util.function.IntConsumer;
import java.util.function.IntSupplier;

/** Base interface for all state processors handling user inputs and preparing output text messages for him */
public interface StateProcessor {

    /** For which state the processor is */
    State forState();

    /** Returns a {@link InteractionMessage} to send to user */
    InteractionMessage buildMessageForUser(Calculation calculation);

    /** Processes user answer and put valid data into {@link Calculation} */
    void processAnswer(String input, Calculation calculation);

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

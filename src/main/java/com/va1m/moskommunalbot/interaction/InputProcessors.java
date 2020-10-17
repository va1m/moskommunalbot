package com.va1m.moskommunalbot.interaction;

import com.va1m.moskommunalbot.interaction.processors.InvalidInputException;

import java.util.function.IntConsumer;

/** Processes user input and put valid data into {@link InteractionContext} */
public final class InputProcessors {

    private InputProcessors() {
    }

    /** Validates input user data for last cold water meters and stores the valid data in {@link InteractionContext} */
    public static void lastColdWaterMeters(String input, InteractionContext interactionContext) {
        storeIfValid(input, interactionContext::setLatestColdWaterMeters);
    }

    /** Validates input user data for current cold water meters and stores the valid data in {@link InteractionContext} */
    public static void currentColdWaterMeters(String input, InteractionContext interactionContext) {
        storeIfValid(input, interactionContext::setCurrentColdWaterMeters);
    }

    /** Validates input user data for last hot water meters and stores the valid data in {@link InteractionContext} */
    public static void lastHotWaterMeters(String input, InteractionContext interactionContext) {
        storeIfValid(input, interactionContext::setLatestHotWaterMeters);
    }

    /**
     * Validates input user data for current hot water meters and stores the valid data in {@link InteractionContext}
     */
    public static void currentHotWaterMeters(String input, InteractionContext interactionContext) {
        storeIfValid(input, interactionContext::setCurrentHotWaterMeters);
    }

    /** Validates input user data for last electricity meters and stores the valid data in {@link InteractionContext} */
    public static void lastElectricityMeters(String input, InteractionContext interactionContext) {
        storeIfValid(input, interactionContext::setLatestElectricityMeters);
    }

    /**
     * Validates input user data for current electricity meters and stores the valid data in {@link InteractionContext}
     */
    public static void currentElectricityMeters(String input, InteractionContext interactionContext) {
        storeIfValid(input, interactionContext::setCurrentElectricityMeters);
    }

    private static void storeIfValid(String input, IntConsumer setter) {
        try {
            final var intInput = Integer.parseInt(input.trim());
            setter.accept(intInput);
        } catch (NumberFormatException e) {
            throw new InvalidInputException("Только цифры, пожалуйста");
        }
    }
}

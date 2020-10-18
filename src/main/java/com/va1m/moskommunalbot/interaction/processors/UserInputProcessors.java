package com.va1m.moskommunalbot.interaction.processors;

import com.va1m.moskommunalbot.interaction.InteractionContext;

import java.util.function.IntConsumer;

/** Processes user input and put valid data into {@link InteractionContext} */
public final class UserInputProcessors {

    private UserInputProcessors() {
    }

    /** Validates input user data for last cold water meters and stores the valid data in {@link InteractionContext} */
    public static void coldWaterLastMeters(String input, InteractionContext interactionContext) {
        storeIfValid(input, interactionContext::setLatestColdWaterMeters);
    }

    /** Validates input user data for current cold water meters and stores the valid data in {@link InteractionContext} */
    public static void coldWaterCurrentMeters(String input, InteractionContext interactionContext) {
        storeIfValid(input, interactionContext::setCurrentColdWaterMeters);
    }

    /** Validates input user data for last hot water meters and stores the valid data in {@link InteractionContext} */
    public static void hotWaterLastMeters(String input, InteractionContext interactionContext) {
        storeIfValid(input, interactionContext::setLatestHotWaterMeters);
    }

    /**
     * Validates input user data for current hot water meters and stores the valid data in {@link InteractionContext}
     */
    public static void hotWaterCurrentMeters(String input, InteractionContext interactionContext) {
        storeIfValid(input, interactionContext::setCurrentHotWaterMeters);
    }

    /** Validates input user data for last electricity meters and stores the valid data in {@link InteractionContext} */
    public static void electricityLastMeters(String input, InteractionContext interactionContext) {
        storeIfValid(input, interactionContext::setLatestElectricityMeters);
    }

    /**
     * Validates input user data for current electricity meters and stores the valid data in {@link InteractionContext}
     */
    public static void electricityCurrentMeters(String input, InteractionContext interactionContext) {
        storeIfValid(input, interactionContext::setCurrentElectricityMeters);
    }

    /** Does nothing, just a stub to avoid null checkings */
    public static void dummy(String input, InteractionContext interactionContext) {
        // do nothing here
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

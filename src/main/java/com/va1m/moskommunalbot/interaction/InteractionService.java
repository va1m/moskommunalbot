package com.va1m.moskommunalbot.interaction;

import com.va1m.moskommunalbot.interaction.processors.InvalidInputException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

/** Manages states to process response messages */
public class InteractionService {

    private static final Logger LOGGER = LogManager.getLogger(InteractionService.class);

    private static final Map<State, State> STATE_MACHINE = Map.of(
            State.START, State.WAITING_FOR_LAST_COLD_WATER_METERS,
            State.WAITING_FOR_LAST_COLD_WATER_METERS, State.WAITING_FOR_CURRENT_COLD_WATER_METERS,
            State.WAITING_FOR_CURRENT_COLD_WATER_METERS, State.WAITING_FOR_LAST_HOT_WATER_METERS,
            State.WAITING_FOR_LAST_HOT_WATER_METERS, State.WAITING_FOR_CURRENT_HOT_WATER_METERS,
            State.WAITING_FOR_CURRENT_HOT_WATER_METERS, State.WAITING_FOR_LAST_ELECTRICITY_METERS,
            State.WAITING_FOR_LAST_ELECTRICITY_METERS, State.WAITING_FOR_CURRENT_ELECTRICITY_METERS,
            State.WAITING_FOR_CURRENT_ELECTRICITY_METERS, State.SHOWING_RESULTS,
            State.SHOWING_RESULTS, State.WAITING_FOR_LAST_COLD_WATER_METERS
    );

    private final InteractionDao interactionDao;

    /** Constructor */
    public InteractionService(InteractionDao interactionDao) {
        this.interactionDao = interactionDao;
    }

    /** Returns a reply according to current state and input data for a specific chat */
    public String getReply(long chatId, String input) {

        final var interactionContext = interactionDao.read(chatId)
                .orElseGet(() -> {
                    final var newInteractionContext = new InteractionContext();
                    newInteractionContext.setLastState(State.START);
                    return newInteractionContext;
                });

        try {
            final var currentStep = STATE_MACHINE.get(interactionContext.getLastState());
            currentStep.getInputProcessor().accept(input, interactionContext);
            interactionContext.setLastState(currentStep);
            interactionDao.write(chatId, interactionContext);
            return currentStep.getOutputProcessor().apply(interactionContext);
        } catch (InvalidInputException iie) {
            return iie.getMessage();
        } catch (Exception e) {
            LOGGER.error("", e);
            return "Ups... Something went wrong.\n" +
                    "See log.";
        }
    }
}

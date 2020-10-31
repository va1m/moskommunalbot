package com.va1m.moskommunalbot.interaction;

import com.google.inject.Inject;
import com.va1m.moskommunalbot.interaction.stateprocessors.StateProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/** Manages states to process response messages */
public class InteractionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(InteractionService.class);

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

    private final Map<State, StateProcessor> stateProcessors;
    private final InteractionDao interactionDao;

    /** Constructor */
    @Inject
    public InteractionService(InteractionDao interactionDao, Set<StateProcessor> stateProcessors) {
        this.interactionDao = interactionDao;

        this.stateProcessors = stateProcessors.stream()
            .collect(Collectors.toUnmodifiableMap(StateProcessor::forState, Function.identity()));
    }

    /** Returns a reply according to current state and input data for a specific chat */
    public String getReply(long chatId, String input) {

        final var interactionContext = interactionDao.read(chatId)
            .orElseGet(() -> {
                final var newInteractionContext = new InteractionContext();
                newInteractionContext.setNextState(State.START);
                return newInteractionContext;
            });

        try {
            final var currentStep = interactionContext.getNextState();
            stateProcessors.get(currentStep).processInput(input, interactionContext);

            final var nextStep = STATE_MACHINE.get(interactionContext.getNextState());
            interactionContext.setNextState(nextStep);
            interactionDao.write(chatId, interactionContext);

            return stateProcessors.get(nextStep).processOutput(interactionContext);

        } catch (InvalidInputException iie) {
            return iie.getMessage();
        } catch (Exception e) {
            LOGGER.error("", e);
            return "Ups...\n"
                + "Something went wrong.\n"
                + "See log.";
        }
    }
}

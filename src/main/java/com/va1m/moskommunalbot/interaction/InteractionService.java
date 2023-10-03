package com.va1m.moskommunalbot.interaction;

import com.google.inject.Inject;
import com.va1m.moskommunalbot.model.Calculation;
import com.va1m.moskommunalbot.model.InteractionMessage;
import com.va1m.moskommunalbot.interaction.stateprocessors.StateProcessor;
import com.va1m.moskommunalbot.model.State;
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
        State.START, State.USE_LAST_METERS,
        State.USE_LAST_METERS, State.LAST_COLD_WATER_METERS,
        State.LAST_COLD_WATER_METERS, State.CURRENT_COLD_WATER_METERS,
        State.CURRENT_COLD_WATER_METERS, State.LAST_HOT_WATER_METERS,
        State.LAST_HOT_WATER_METERS, State.CURRENT_HOT_WATER_METERS,
        State.CURRENT_HOT_WATER_METERS, State.LAST_ELECTRICITY_METERS,
        State.LAST_ELECTRICITY_METERS, State.CURRENT_ELECTRICITY_METERS,
        State.CURRENT_ELECTRICITY_METERS, State.SHOWING_RESULTS,
        State.SHOWING_RESULTS, State.START
    );

    private final Map<State, StateProcessor> stateProcessors;
    private final CalculationDao calculationDao;

    @Inject
    public InteractionService(CalculationDao calculationDao, Set<StateProcessor> stateProcessors) {
        this.calculationDao = calculationDao;

        this.stateProcessors = stateProcessors.stream()
            .collect(Collectors.toUnmodifiableMap(StateProcessor::forState, Function.identity()));
    }

    /** Returns a reply according to current state and input data for a specific chat */
    public InteractionMessage processState(long chatId, String input) {

        try {
            final var calculation = calculationDao.read(chatId)
                .orElseGet(() -> {
                    final var newInteractionContext = new Calculation();
                    newInteractionContext.setState(State.START);
                    return newInteractionContext;
                });

            var state = calculation.getState();
            stateProcessors.get(state).processAnswer(input, calculation);

            InteractionMessage interactionMessage;
            do {
                state = STATE_MACHINE.get(state);
                calculation.setState(state);
                calculationDao.write(chatId, calculation);

                interactionMessage = stateProcessors.get(state).buildMessageForUser(calculation);

            } while (interactionMessage == null);
            return interactionMessage;

        } catch (InvalidInputException iie) {
            return InteractionMessage.of(iie.getMessage());
        } catch (Exception e) {
            LOGGER.error("", e);
            return InteractionMessage.of("""
                Ups...
                Something went wrong.
                See log.
                """);
        }
    }
}

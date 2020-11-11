package com.va1m.moskommunalbot.interaction;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.va1m.moskommunalbot.model.Calculation;
import com.va1m.moskommunalbot.model.InteractionMessage;
import com.va1m.moskommunalbot.interaction.stateprocessors.StateProcessor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

/** Test for {@link InteractionService} */
@ExtendWith(MockitoExtension.class)
class InteractionServiceTest {

    @Mock
    private CalculationDao calculationDao;

    @Mock
    private StateProcessor firstStateProcessor;

    @Mock
    private StateProcessor secondStateProcessor;

    @Test
    void getReplyForInitState() {
        final var chatId = 100L;
        final var hello = InteractionMessage.of("Hello");
        final var interactionContext = new Calculation();
        interactionContext.setState(State.START);

        when(firstStateProcessor.forState()).thenReturn(State.START);
        when(secondStateProcessor.forState()).thenReturn(State.USE_LAST_METERS);
        when(calculationDao.read(chatId)).thenReturn(Optional.of(interactionContext));
        when(secondStateProcessor.buildMessageForUser(interactionContext)).thenReturn(hello);

        final var stateProcessors = Set.of(firstStateProcessor, secondStateProcessor);
        InteractionService interactionService = new InteractionService(calculationDao, stateProcessors);

        final var reply = interactionService.processState(chatId, "Hi");

        assertThat(reply).isEqualTo(hello);

        verify(calculationDao).write(chatId, interactionContext);
        verify(firstStateProcessor).processAnswer(eq("Hi"), any());
        verifyNoMoreInteractions(calculationDao, firstStateProcessor);

        final var expectedInteractionContext = new Calculation();
        expectedInteractionContext.setState(State.USE_LAST_METERS);

        assertThat(interactionContext)
            .usingRecursiveComparison()
            .isEqualTo(expectedInteractionContext);
    }

    @Test
    void getReplyWithInvalidInputException() {

        final var errorMessage = "Only numbers, please.";

        when(firstStateProcessor.forState()).thenReturn(State.START);
        when(secondStateProcessor.forState()).thenReturn(State.LAST_COLD_WATER_METERS);

        doThrow(new InvalidInputException(errorMessage))
            .when(firstStateProcessor).processAnswer(eq("Hi"), any());

        final var stateProcessors = Set.of(firstStateProcessor, secondStateProcessor);
        InteractionService interactionService = new InteractionService(calculationDao, stateProcessors);

        final var reply = interactionService.processState(100L, "Hi");

        final var expected = InteractionMessage.of(errorMessage);

        assertThat(reply)
            .usingRecursiveComparison()
            .isEqualTo(expected);
    }

    @Test
    void getReplyWithException() {

        when(firstStateProcessor.forState()).thenReturn(State.START);
        when(secondStateProcessor.forState()).thenReturn(State.LAST_COLD_WATER_METERS);

        doThrow(IllegalStateException.class)
            .when(calculationDao).write(anyLong(), any());

        final var stateProcessors = Set.of(firstStateProcessor, secondStateProcessor);
        InteractionService interactionService = new InteractionService(calculationDao, stateProcessors);

        final var reply = interactionService.processState(100L, "Hi");

        final var expected = InteractionMessage.of("Ups...\nSomething went wrong.\nSee log.");

        assertThat(reply)
            .usingRecursiveComparison()
            .isEqualTo(expected);
    }
}
package com.va1m.moskommunalbot.interaction;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.va1m.moskommunalbot.interaction.stateprocessors.StateProcessor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

/** Test for {@link InteractionService} */
@ExtendWith(MockitoExtension.class)
class InteractionServiceTest {

    @Mock
    private InteractionDao interactionDao;

    @Mock
    private StateProcessor startStateProcessor;

    @Mock
    private StateProcessor lastColdWaterStateProcessor;

    @Test
    void getReplyForInitState() {
        final var chatId = 100L;
        final var hello = "Hello";
        final var interactionContext = new InteractionContext();
        interactionContext.setNextState(State.START);

        when(startStateProcessor.forState()).thenReturn(State.START);
        when(lastColdWaterStateProcessor.forState()).thenReturn(State.WAITING_FOR_LAST_COLD_WATER_METERS);
        when(interactionDao.read(chatId)).thenReturn(Optional.of(interactionContext));
        when(lastColdWaterStateProcessor.processOutput(interactionContext)).thenReturn(hello);

        final var stateProcessors = new StateProcessor[] {
            startStateProcessor,
            lastColdWaterStateProcessor
        };
        InteractionService interactionService = new InteractionService(interactionDao, stateProcessors);

        final var reply = interactionService.getReply(chatId, "Hi");

        assertThat(reply).isEqualTo(hello);

        verify(interactionDao).write(chatId, interactionContext);
        verify(startStateProcessor).processInput(eq("Hi"), any());
        verifyNoMoreInteractions(interactionDao, startStateProcessor);

        final var expectedInteractionContext = new InteractionContext();
        expectedInteractionContext.setNextState(State.WAITING_FOR_LAST_COLD_WATER_METERS);

        assertThat(interactionContext)
            .usingRecursiveComparison()
            .isEqualTo(expectedInteractionContext);
    }

    @Test
    void getReplyWithInvalidInputException() {

        final var errorMessage = "Only numbers, please.";

        when(startStateProcessor.forState()).thenReturn(State.START);
        when(lastColdWaterStateProcessor.forState()).thenReturn(State.WAITING_FOR_LAST_COLD_WATER_METERS);

        doThrow(new InvalidInputException(errorMessage))
            .when(startStateProcessor).processInput(eq("Hi"), any());

        final var stateProcessors = new StateProcessor[] {
            startStateProcessor,
            lastColdWaterStateProcessor
        };
        InteractionService interactionService = new InteractionService(interactionDao, stateProcessors);

        final var reply = interactionService.getReply(100L, "Hi");

        assertThat(reply).isEqualTo(errorMessage);
    }

    @Test
    void getReplyWithException() {

        when(startStateProcessor.forState()).thenReturn(State.START);
        when(lastColdWaterStateProcessor.forState()).thenReturn(State.WAITING_FOR_LAST_COLD_WATER_METERS);

        doThrow(IllegalStateException.class)
            .when(interactionDao).write(anyLong(), any());

        final var stateProcessors = new StateProcessor[] {
            startStateProcessor,
            lastColdWaterStateProcessor
        };
        InteractionService interactionService = new InteractionService(interactionDao, stateProcessors);

        final var reply = interactionService.getReply(100L, "Hi");

        assertThat(reply).isEqualTo("Ups...\nSomething went wrong.\nSee log.");
    }
}
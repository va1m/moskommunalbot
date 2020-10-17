package com.va1m.moskommunalbot.interaction;

import com.va1m.moskommunalbot.interaction.processors.OutputProcessors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.BiConsumer;
import java.util.function.Function;

@Getter
@RequiredArgsConstructor
public enum State {

    START((a, b) -> {}, null),

    WAITING_FOR_LAST_COLD_WATER_METERS(
            InputProcessors::lastColdWaterMeters,
            OutputProcessors::lastColdWater),

    WAITING_FOR_CURRENT_COLD_WATER_METERS(
            InputProcessors::currentColdWaterMeters,
            OutputProcessors::currentColdWater),

    WAITING_FOR_LAST_HOT_WATER_METERS(
            InputProcessors::lastHotWaterMeters,
            OutputProcessors::lastHotWater),

    WAITING_FOR_CURRENT_HOT_WATER_METERS(
            InputProcessors::currentHotWaterMeters,
            OutputProcessors::currentHotWater),

    WAITING_FOR_LAST_ELECTRICITY_METERS(
            InputProcessors::lastElectricityMeters,
            OutputProcessors::lastElectricity),

    WAITING_FOR_CURRENT_ELECTRICITY_METERS(
            InputProcessors::currentElectricityMeters,
            OutputProcessors::currentElectricity),

    SHOWING_RESULTS((a, b) -> {}, OutputProcessors::results);

    private final BiConsumer<String, InteractionContext> inputProcessor;
    private final Function<InteractionContext, String> outputProcessor;
}

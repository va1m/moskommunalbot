package com.va1m.moskommunalbot.interaction;

import lombok.Getter;
import lombok.Setter;

/** Collects data entered by user during interaction with the bot */
@Getter
@Setter
public class InteractionContext {

    private State nextState;

    private Integer lastColdWaterMeters;
    private Integer currentColdWaterMeters;

    private Integer lastHotWaterMeters;
    private Integer currentHotWaterMeters;

    private Integer lastElectricityMeters;
    private Integer currentElectricityMeters;
}

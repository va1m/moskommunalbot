package com.va1m.moskommunalbot.interaction;

import lombok.Getter;
import lombok.Setter;

/** Collects data entered by user during interaction with the bot */
@Getter
@Setter
public class InteractionContext {

    private State nextState;

    private Integer latestColdWaterMeters;
    private Integer currentColdWaterMeters;

    private Integer latestHotWaterMeters;
    private Integer currentHotWaterMeters;

    private Integer latestElectricityMeters;
    private Integer currentElectricityMeters;
}

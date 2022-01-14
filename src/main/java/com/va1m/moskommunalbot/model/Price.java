package com.va1m.moskommunalbot.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

/** Price with duration of validity */
@Getter
@RequiredArgsConstructor(staticName = "of")
public class Price {

    /** Since which date the price has set (inclusive) */
    private final LocalDate since;

    /** Till which date the price is valid (inclusive) */
    private final LocalDate till;

    /** Sum in coins */
    private final int value;
}

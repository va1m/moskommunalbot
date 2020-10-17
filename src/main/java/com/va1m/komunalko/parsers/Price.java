package com.va1m.komunalko.parsers;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

/** The price with the date of set */
@Getter
@RequiredArgsConstructor(staticName = "of")
public class Price {

    /** Since which date the price has set */
    private final LocalDate since;

    /** A sum in coins */
    private final int amount;
}

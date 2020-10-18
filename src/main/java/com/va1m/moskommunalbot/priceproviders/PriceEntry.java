package com.va1m.moskommunalbot.priceproviders;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

/** The price with the duration of validity */
@Getter
@RequiredArgsConstructor(staticName = "of")
public class PriceEntry {

    /** Since which date the price has set (inclusive) */
    private final LocalDate since;

    /** Till which date the price is valid (inclusive) */
    private final LocalDate till;

    /** A sum in coins */
    private final int price;
}

package com.va1m.moskommunalbot.priceproviders;

import java.time.LocalDate;

/**
 * Provides prices for disposing water with durations during which the prices are valid.
 *
 * http://www.mosvodokanal.ru/forabonents/tariffs/
 */
public class WaterDisposingPricesProvider {

    /** Provides prices with the durations they are being applying */
    public PriceEntry[] provide() {
        return new PriceEntry[] {
                PriceEntry.of(LocalDate.of(2020, 7, 1), LocalDate.of(2020, 12, 31), 3090)
        };
    }
}

package com.va1m.moskommunalbot.priceproviders;

import java.time.LocalDate;

/**
 * Provides prices for cold water with durations during which the prices are valid
 *
 * https://online.moek.ru/clients/tarify-i-raschety/tarify
 */
public class HotWaterPricesProvider {

    /** Provides prices with the durations they are being applying */
    public PriceEntry[] provide() {
        return new PriceEntry[]{
            PriceEntry.of(LocalDate.of(2020, 1, 1), LocalDate.of(2020, 7, 1).minusDays(1), 19819),
            PriceEntry.of(LocalDate.of(2020, 7, 1), LocalDate.of(2021, 7, 1).minusDays(1), 20515),
            PriceEntry.of(LocalDate.of(2021, 7, 1), LocalDate.of(2022, 7, 1).minusDays(1), 20696),
            PriceEntry.of(LocalDate.of(2022, 7, 1), LocalDate.of(2023, 7, 1).minusDays(1), 22285),
            PriceEntry.of(LocalDate.of(2023, 7, 1), LocalDate.of(2024, 1, 1).minusDays(1), 22482)
        };
    }
}

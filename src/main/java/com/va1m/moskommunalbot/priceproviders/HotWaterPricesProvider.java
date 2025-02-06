package com.va1m.moskommunalbot.priceproviders;

import com.va1m.moskommunalbot.model.Price;
import dagger.Reusable;

import java.time.LocalDate;

import javax.inject.Inject;

/**
 * Provides prices for cold water with durations during which the prices are valid
 * <p>
 * @see <a href="https://online.moek.ru/clients/tarify-i-raschety/tarify">tariffs online</a>
 */
@Reusable
public class HotWaterPricesProvider {

    private static final Price[] PRICES = {
        Price.of(LocalDate.of(2020, 1, 1), LocalDate.of(2020, 6, 30), 19819),
        Price.of(LocalDate.of(2020, 7, 1), LocalDate.of(2021, 6, 30), 20515),
        Price.of(LocalDate.of(2021, 7, 1), LocalDate.of(2022, 6, 30), 20696),
        Price.of(LocalDate.of(2022, 7, 1), LocalDate.of(2024, 6, 30), 24316),
        Price.of(LocalDate.of(2024, 7, 1), LocalDate.of(2025, 6, 30), 27214),
        Price.of(LocalDate.of(2025, 7, 1), LocalDate.of(2026, 6, 30), 27876),
        Price.of(LocalDate.of(2026, 7, 1), LocalDate.of(2027, 6, 30), 28156),
        Price.of(LocalDate.of(2027, 7, 1), LocalDate.of(2028, 6, 30), 29387),
        Price.of(LocalDate.of(2028, 7, 1), LocalDate.of(2029, 6, 30), 29471)
    };

    @Inject
    public HotWaterPricesProvider() {
        // necessary to be injectable for Dagger DI
    }

    /** Provides prices with the durations they are being applying */
    public Price[] provide() {
        return PRICES;
    }
}

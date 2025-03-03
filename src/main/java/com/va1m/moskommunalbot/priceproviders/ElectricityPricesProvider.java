package com.va1m.moskommunalbot.priceproviders;

import com.va1m.moskommunalbot.model.Price;
import dagger.Reusable;

import java.time.LocalDate;

import javax.inject.Inject;

/**
 * Provides prices for electricity with durations during which the prices are valid
 * <p>
 * <a href="https://www.mosenergosbyt.ru/individuals/tariffs-n-payments/tariffs-msk/polnaya-versiya-tarifov.php">tariffs online</a>
 * "Однотарифный учёт с применением одноставочного тарифа"
 */
@Reusable
public class ElectricityPricesProvider {

    private static final Price[] PRICES = {
        Price.of(LocalDate.of(2020, 7, 1), LocalDate.of(2021, 7, 1).minusDays(1), 487),
        Price.of(LocalDate.of(2021, 7, 1), LocalDate.of(2022, 7, 1).minusDays(1), 515),
        Price.of(LocalDate.of(2022, 7, 1), LocalDate.of(2022, 12, 31), 543),
        Price.of(LocalDate.of(2023, 1, 1), LocalDate.of(2024, 6, 30), 566),
        Price.of(LocalDate.of(2024, 7, 1), LocalDate.of(2024, 12, 31), 615),
        Price.of(LocalDate.of(2025, 1, 1), LocalDate.of(2025, 12, 31), 699),
    };

    @Inject
    public ElectricityPricesProvider() {
        // necessary to be injectable for Dagger DI
    }

    /** Provides prices with the durations they are being applying */
    public Price[] provide() {
        return PRICES;
    }
}

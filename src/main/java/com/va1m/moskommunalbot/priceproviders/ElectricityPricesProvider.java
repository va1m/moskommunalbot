package com.va1m.moskommunalbot.priceproviders;

import com.va1m.moskommunalbot.model.Price;

import java.time.LocalDate;

/**
 * Provides prices for electricity with durations during which the prices are valid
 *
 * https://www.mosenergosbyt.ru/individuals/tariffs-n-payments/tariffs-msk/kvartiry-i-doma-s-elektricheskimi-plitami.php
 * "Однотарифный учёт с применением одноставочного тарифа"
 */
public class ElectricityPricesProvider {

    /** Provides prices with the durations they are being applying */
    public Price[] provide() {
        return new Price[]{
            Price.of(LocalDate.of(2020, 7, 1), LocalDate.of(2021, 7, 1).minusDays(1), 487),
            Price.of(LocalDate.of(2021, 7, 1), LocalDate.of(2022, 7, 1).minusDays(1), 515),
            Price.of(LocalDate.of(2022, 7, 1), LocalDate.of(2023, 7, 1).minusDays(1), 543)
        };
    }
}

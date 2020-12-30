package com.va1m.moskommunalbot.priceproviders;

import java.time.LocalDate;

/**
 * Provides prices for electricity with durations during which the prices are valid
 *
 * https://www.mosenergosbyt.ru/individuals/tariffs-n-payments/tariffs-msk/kvartiry-i-doma-s-elektricheskimi-plitami.php
 */
public class ElectricityPricesProvider {

    /** Provides prices with the durations they are being applying */
    public PriceEntry[] provide() {
        return new PriceEntry[]{
            PriceEntry.of(LocalDate.of(2020, 7, 1), LocalDate.of(2021, 7, 1).minusDays(1), 487),
            PriceEntry.of(LocalDate.of(2021, 7, 1), LocalDate.of(2022, 1, 1).minusDays(1), 515)
        };
    }
}

package com.va1m.moskommunalbot.priceproviders;

import com.va1m.moskommunalbot.model.Price;
import dagger.Reusable;

import java.time.LocalDate;

import javax.inject.Inject;

/**
 * Provides single-rate electricity prices for urban residential consumers with stationary electric stoves,
 * including СВАО, without differentiation by time of day.
 * <p>
 * Moscow Department of Economic Policy and Development order № ДПР-ТР-407/25 of 24.12.2025,
 * effective 01.01.2026, appendix № 1, rows 2.1 and 3.1, «Одноставочный тариф», PDF pages 4–5.
 * Prices include VAT and are stored in kopecks per kWh.
 *
 * @see <a href="https://www.mos.ru/upload/documents/docs/5117/Prikazot24122025_DPR-TR-407_25-ghvb1.pdf">Official tariff order (PDF)</a>
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
        Price.of(LocalDate.of(2026, 1, 1), LocalDate.of(2026, 9, 30), 728),
        Price.of(LocalDate.of(2026, 10, 1), LocalDate.of(2026, 12, 31), 846),
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

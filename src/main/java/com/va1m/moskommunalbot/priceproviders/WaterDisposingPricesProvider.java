package com.va1m.moskommunalbot.priceproviders;

import com.va1m.moskommunalbot.model.Price;
import dagger.Reusable;

import java.time.LocalDate;

import javax.inject.Inject;

/**
 * Provides residential wastewater disposal prices for АО «Мосводоканал» consumers, including СВАО.
 * <p>
 * Moscow Department of Economic Policy and Development order № ДПР-ТР-334/25 of 16.12.2025,
 * effective 01.01.2026, amends order № ДПР-ТР-290/23 of 15.12.2023.
 * Appendix № 4 (replacing appendix № 5), row 1, «Население (с учётом НДС)», PDF page 7.
 * Prices include VAT and are stored in kopecks per m³.
 *
 * @see <a href="https://www.mos.ru/upload/documents/docs/4677/Prikazot16122025_DPR-TR-334_25-ghvb2.pdf">Official tariff order (PDF)</a>
 */
@Reusable
public class WaterDisposingPricesProvider {

    private static final Price[] PRICES = {
        Price.of(LocalDate.of(2020, 7, 1), LocalDate.of(2021, 6, 30), 3090),
        Price.of(LocalDate.of(2021, 7, 1), LocalDate.of(2022, 6, 30), 3202),
        Price.of(LocalDate.of(2022, 7, 1), LocalDate.of(2022, 12, 31), 3553),
        Price.of(LocalDate.of(2023, 1, 1), LocalDate.of(2024, 6, 30), 3997),
        Price.of(LocalDate.of(2024, 7, 1), LocalDate.of(2025, 6, 30), 4591),
        Price.of(LocalDate.of(2025, 7, 1), LocalDate.of(2025, 12, 31), 5162),
        Price.of(LocalDate.of(2026, 1, 1), LocalDate.of(2026, 9, 30), 5248),
        Price.of(LocalDate.of(2026, 10, 1), LocalDate.of(2026, 12, 31), 6024),
        Price.of(LocalDate.of(2027, 1, 1), LocalDate.of(2027, 6, 30), 6024),
        Price.of(LocalDate.of(2027, 7, 1), LocalDate.of(2027, 12, 31), 6265),
        Price.of(LocalDate.of(2028, 1, 1), LocalDate.of(2028, 6, 30), 6265),
        Price.of(LocalDate.of(2028, 7, 1), LocalDate.of(2028, 12, 31), 6516),
    };

    @Inject
    public WaterDisposingPricesProvider() {
        // necessary to be injectable for Dagger DI
    }

    /** Provides prices with the durations they are being applying */
    public Price[] provide() {
        return PRICES;
    }
}

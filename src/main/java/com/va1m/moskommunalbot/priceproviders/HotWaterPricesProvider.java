package com.va1m.moskommunalbot.priceproviders;

import com.va1m.moskommunalbot.model.Price;
import dagger.Reusable;

import java.time.LocalDate;

import javax.inject.Inject;

/**
 * Provides residential hot water prices for ПАО «МОЭК» consumers in Moscow outside ТиНАО, including СВАО.
 * <p>
 * Moscow Department of Economic Policy and Development order № ДПР-ТР-316/25 of 15.12.2025,
 * effective 01.01.2026, amends order № ДПР-ТР-289/23 of 15.12.2023.
 * Its appendix (replacing appendix № 2), row 1, column «Население (с НДС)», PDF page 3,
 * covers all system types within the closed hot water supply system.
 * The published tariff is a single price per m³, including VAT, stored in kopecks per m³.
 *
 * @see <a href="https://www.mos.ru/upload/documents/docs/9077/Prikazot15122025_DPR-TR-316_25-ikuj8.pdf">Official tariff order (PDF)</a>
 */
@Reusable
public class HotWaterPricesProvider {

    private static final Price[] PRICES = {
        Price.of(LocalDate.of(2020, 1, 1), LocalDate.of(2020, 6, 30), 19819),
        Price.of(LocalDate.of(2020, 7, 1), LocalDate.of(2021, 6, 30), 20515),
        Price.of(LocalDate.of(2021, 7, 1), LocalDate.of(2022, 6, 30), 20696),
        Price.of(LocalDate.of(2022, 7, 1), LocalDate.of(2024, 6, 30), 24316),
        Price.of(LocalDate.of(2024, 7, 1), LocalDate.of(2025, 6, 30), 27214),
        Price.of(LocalDate.of(2025, 7, 1), LocalDate.of(2025, 12, 31), 31250),
        Price.of(LocalDate.of(2026, 1, 1), LocalDate.of(2026, 9, 30), 31771),
        Price.of(LocalDate.of(2026, 10, 1), LocalDate.of(2026, 12, 31), 37780),
        Price.of(LocalDate.of(2027, 1, 1), LocalDate.of(2027, 6, 30), 37780),
        Price.of(LocalDate.of(2027, 7, 1), LocalDate.of(2027, 12, 31), 40172),
        Price.of(LocalDate.of(2028, 1, 1), LocalDate.of(2028, 6, 30), 40172),
        Price.of(LocalDate.of(2028, 7, 1), LocalDate.of(2028, 12, 31), 42482)
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

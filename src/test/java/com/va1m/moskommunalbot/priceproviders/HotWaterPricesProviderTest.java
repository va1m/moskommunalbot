package com.va1m.moskommunalbot.priceproviders;

import static org.assertj.core.api.Assertions.assertThat;

import com.va1m.moskommunalbot.model.Price;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDate;
import java.util.stream.Stream;

class HotWaterPricesProviderTest {

    private final HotWaterPricesProvider provider = new HotWaterPricesProvider();

    @ParameterizedTest
    @CsvSource({
        "2025-06-30, 27214",
        "2025-07-01, 31250",
        "2025-12-31, 31250",
        "2026-01-01, 31771",
        "2026-09-30, 31771",
        "2026-10-01, 37780",
        "2026-10-16, 37780",
        "2026-12-31, 37780",
        "2027-01-01, 37780",
        "2027-06-30, 37780",
        "2027-07-01, 40172",
        "2027-12-31, 40172",
        "2028-01-01, 40172",
        "2028-06-30, 40172",
        "2028-07-01, 42482",
        "2028-12-31, 42482"
    })
    void shouldProvideApprovedResidentialTariff(LocalDate date, int expectedKopecks) {
        // Order ДПР-ТР-316/25, appendix, row 1, residential column (VAT included).
        assertThat(Stream.of(provider.provide())
            .filter(price -> !date.isBefore(price.getSince()) && !date.isAfter(price.getTill())))
            .singleElement()
            .extracting(Price::getValue)
            .isEqualTo(expectedKopecks);
    }

    @Test
    void shouldHaveConnectedDurations() {

        Price[] prices = provider.provide();

        Price previous = null;
        for (Price current : prices) {
            if (previous != null) {
                assertThat(current.getSince()).isEqualTo(previous.getTill().plusDays(1));
            }
            previous = current;
        }
    }
}

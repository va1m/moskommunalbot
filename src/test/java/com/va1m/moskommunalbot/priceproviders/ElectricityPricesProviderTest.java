package com.va1m.moskommunalbot.priceproviders;

import static org.assertj.core.api.Assertions.assertThat;

import com.va1m.moskommunalbot.model.Price;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDate;
import java.util.stream.Stream;

class ElectricityPricesProviderTest {

    private final ElectricityPricesProvider provider = new ElectricityPricesProvider();

    @ParameterizedTest
    @CsvSource({
        "2026-01-01, 728",
        "2026-09-30, 728",
        "2026-10-01, 846",
        "2026-10-16, 846",
        "2026-12-31, 846"
    })
    void shouldProvideApprovedSingleRateElectricStoveTariff(LocalDate date, int expectedKopecks) {
        // Order ДПР-ТР-407/25, appendix 1, rows 2.1 and 3.1 (VAT included).
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

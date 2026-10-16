package com.va1m.moskommunalbot.priceproviders;

import static org.assertj.core.api.Assertions.assertThat;

import com.va1m.moskommunalbot.model.Price;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDate;
import java.util.stream.Stream;

class WaterDisposingPricesProviderTest {

    private final WaterDisposingPricesProvider provider = new WaterDisposingPricesProvider();

    @ParameterizedTest
    @CsvSource({
        "2026-01-01, 5248",
        "2026-09-30, 5248",
        "2026-10-01, 6024",
        "2026-10-16, 6024",
        "2026-12-31, 6024",
        "2027-01-01, 6024",
        "2027-06-30, 6024",
        "2027-07-01, 6265",
        "2027-12-31, 6265",
        "2028-01-01, 6265",
        "2028-06-30, 6265",
        "2028-07-01, 6516",
        "2028-12-31, 6516"
    })
    void shouldProvideApprovedResidentialTariff(LocalDate date, int expectedKopecks) {
        // Order ДПР-ТР-334/25, appendix 4, row 1 (VAT included).
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

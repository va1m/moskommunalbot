package com.va1m.moskommunalbot.priceproviders;

import static org.assertj.core.api.Assertions.assertThat;

import com.va1m.moskommunalbot.model.Price;
import org.junit.jupiter.api.Test;

class WaterDisposingPricesProviderTest {

    private final WaterDisposingPricesProvider provider = new WaterDisposingPricesProvider();

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
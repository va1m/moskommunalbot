package com.va1m.moskommunalbot.priceproviders;

import com.va1m.moskommunalbot.model.Price;
import dagger.Reusable;

import java.time.LocalDate;

import javax.inject.Inject;

/**
 * Provides prices for disposing water with durations during which the prices are valid.
 * <p>
 * <a href="http://www.mosvodokanal.ru/forabonents/tariffs/">tariffs online</a>
 * "ТАРИФЫ
 * на питьевую воду (питьевое водоснабжение) и водоотведение,
 * осуществляемые акционерным обществом "Мосводоканал" на территории города Москвы
 * (за исключением Троицкого и Новомосковского административных округов)"
 */
@Reusable
public class WaterDisposingPricesProvider {

    private static final Price[] PRICES = {
        Price.of(LocalDate.of(2020, 7, 1), LocalDate.of(2021, 6, 30), 3090),
        Price.of(LocalDate.of(2021, 7, 1), LocalDate.of(2022, 6, 30), 3202),
        Price.of(LocalDate.of(2022, 7, 1), LocalDate.of(2022, 12, 31), 3553),
        Price.of(LocalDate.of(2023, 1, 1), LocalDate.of(2024, 6, 30), 3997),
        Price.of(LocalDate.of(2024, 7, 1), LocalDate.of(2024, 12, 31), 4591)
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

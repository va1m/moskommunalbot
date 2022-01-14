package com.va1m.moskommunalbot.priceproviders;

import com.va1m.moskommunalbot.model.Price;

import java.time.LocalDate;

/**
 * Provides prices for cold water with durations during which the prices are valid
 *
 * http://www.mosvodokanal.ru/forabonents/tariffs/
 * "ТАРИФЫ
 * на питьевую воду (питьевое водоснабжение) и водоотведение,
 * осуществляемые акционерным обществом "Мосводоканал" на территории города Москвы
 * (за исключением Троицкого и Новомосковского административных округов)"
 */
public class ColdWaterPricesProvider {

    /** Provides prices with the durations they are being applying */
    public Price[] provide() {
        return new Price[]{
            Price.of(LocalDate.of(2020, 7, 1), LocalDate.of(2021, 7, 1).minusDays(1), 4230),
            Price.of(LocalDate.of(2021, 7, 1), LocalDate.of(2022, 7, 1).minusDays(1), 4357),
            Price.of(LocalDate.of(2022, 7, 1), LocalDate.of(2023, 7, 1).minusDays(1), 4588)
        };
    }
}

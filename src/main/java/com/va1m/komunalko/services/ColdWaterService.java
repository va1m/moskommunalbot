package com.va1m.komunalko.services;

import com.va1m.komunalko.parsers.ColdWaterParser;

import java.time.LocalDate;
import java.util.stream.Stream;

/**
 * Builds the part of response message for user containing information about
 * the price for cold water and since when it has been set.
 * For example:
 * "
 * Cold water:
 *     123RUB for 100L (by 1.23 RUB/L since 1 July 2020)
 * "
 */
public class ColdWaterService {

    public String calc(LocalDate date, int consumed) {
        // TODO
        final var prices = ColdWaterParser.parse();
        return Stream.of(prices)
                .filter(price -> date.compareTo(price.getSince()) >= 0)
                .sorted()
                .findFirst()
                .map(price -> {
                    final var dblPrice = ((double) price.getAmount()) / 100.0D;
                    final var totalAmount = ((double) (consumed * price.getAmount())) / 100.0D;
                    return String.format("Cold water:%n\t%.2f RUB for %dL (by %.2f RUB/L since %s)",
                            totalAmount, consumed, dblPrice, price.getSince().toString());
                })
                .orElseThrow();
    }
}

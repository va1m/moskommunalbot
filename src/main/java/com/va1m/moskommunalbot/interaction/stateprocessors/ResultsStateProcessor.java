package com.va1m.moskommunalbot.interaction.stateprocessors;

import com.va1m.moskommunalbot.interaction.TimeService;
import com.va1m.moskommunalbot.model.Calculation;
import com.va1m.moskommunalbot.model.InteractionMessage;
import com.va1m.moskommunalbot.model.Price;
import com.va1m.moskommunalbot.model.State;
import com.va1m.moskommunalbot.priceproviders.ColdWaterPricesProvider;
import com.va1m.moskommunalbot.priceproviders.ElectricityPricesProvider;
import com.va1m.moskommunalbot.priceproviders.HotWaterPricesProvider;
import com.va1m.moskommunalbot.priceproviders.WaterDisposingPricesProvider;
import lombok.RequiredArgsConstructor;

import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

/** Prepare output message text for {@link State#SHOWING_RESULTS} state and then handles user input data */
public class ResultsStateProcessor implements StateProcessor {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private final ColdWaterPricesProvider coldWater;
    private final HotWaterPricesProvider hotWater;
    private final WaterDisposingPricesProvider waterDisposing;
    private final ElectricityPricesProvider electricity;
    private final TimeService timeService;

    @RequiredArgsConstructor(staticName = "of")
    private static class ExpenseEntry {
        private final double amount;
        private final String formattedText;
    }

    /** Constructor */
    public ResultsStateProcessor(ColdWaterPricesProvider coldWater, HotWaterPricesProvider hotWater,
        WaterDisposingPricesProvider waterDisposing, ElectricityPricesProvider electricity, TimeService timeService) {

        this.coldWater = coldWater;
        this.hotWater = hotWater;
        this.waterDisposing = waterDisposing;
        this.electricity = electricity;
        this.timeService = timeService;
    }

    @Override
    public State forState() {
        return State.SHOWING_RESULTS;
    }

    @Override
    public InteractionMessage buildMessageForUser(Calculation calculation) {

        String template;
        double current;
        double last;

        var totalAmount = 0.0D;

        template = "- Холодная вода: *%.2f*₽%n"
            + "%.3f - %.3f = %.3fм³%n"
            + "Тариф: %.2f₽/м³ с %s%n%n";
        current = calculation.getCurrentColdWaterMeters() / 1000.0D;
        last = calculation.getLastColdWaterMeters() / 1000.0D;
        final var consumedColdWater = current - last;
        final var coldWaterEntry = getExpenseEntry(coldWater.provide(), current, last, consumedColdWater, template);
        totalAmount += coldWaterEntry.amount;

        template = "- Горячая вода: *%.2f*₽%n"
            + "%.3f - %.3f = %.3fм³%n"
            + "Тариф: %.2f₽/м³ с %s%n%n";
        current = calculation.getCurrentHotWaterMeters() / 1000.0D;
        last = calculation.getLastHotWaterMeters() / 1000.0D;
        final var consumedHotWater = current - last;
        final var hotWaterEntry = getExpenseEntry(hotWater.provide(), current, last, consumedHotWater, template);
        totalAmount += hotWaterEntry.amount;

        template = "- Водоотвод (канализация): *%.2f*₽%n"
            + "%.3f + %.3f = %.3fм³%n"
            + "Тариф: %.2f₽/м³ с %s%n%n";
        final var disposedWater = consumedColdWater + consumedHotWater;
        final var disposedWaterEntry = getExpenseEntry(waterDisposing.provide(), consumedColdWater, consumedHotWater, disposedWater, template);
        totalAmount += disposedWaterEntry.amount;

        template = "- Электричество: *%.2f*₽%n"
            + "%.3f - %.3f = %.1fкВт·ч%n"
            + "Тариф: %.2f₽/кВт·ч с %s%n%n";
        current = calculation.getCurrentElectricityMeters() / 10.0D;
        last = calculation.getLastElectricityMeters() / 10.0D;
        final var consumedElectricity = current - last;
        final var consumedElectricityEntry =
            getExpenseEntry(electricity.provide(), current, last, consumedElectricity, template);
        totalAmount += consumedElectricityEntry.amount;

        final var formattedTotalAmount = String.format("ВСЕГО: *%.2f*₽%n%n", totalAmount);

        final var text = "Стоимость коммунальных услуг:\n"
            + '\n'
            + coldWaterEntry.formattedText
            + hotWaterEntry.formattedText
            + disposedWaterEntry.formattedText
            + consumedElectricityEntry.formattedText
            + formattedTotalAmount
            + "Цены взяты из следующих источников:\n"
            + "- [МосВодоКанал](https://www.mos.ru/upload/documents/docs/4677/Prikazot16122025_DPR-TR-334_25-ghvb2.pdf)\n"
            + "- [МОЭК](https://www.mos.ru/upload/documents/docs/9077/Prikazot15122025_DPR-TR-316_25-ikuj8.pdf)\n"
            + "- [МосЭнергоСбыт](https://www.mos.ru/upload/documents/docs/5117/Prikazot24122025_DPR-TR-407_25-ghvb1.pdf)\n"
            + "\n"
            + "Для начала нового расчета наберите /new.";
        return InteractionMessage.of(text);
    }

    private ExpenseEntry getExpenseEntry(Price[] prices, double a, double b, double consumed, String template) {

        final var today = timeService.getToday();

        return Stream.of(prices)
            .filter(price -> !today.isBefore(price.getSince()))
            .filter(price -> !today.isAfter(price.getTill()))
            .findFirst()
            .map(price -> {
                final var dblPrice = price.getValue() / 100.0D;
                final var amount = consumed * dblPrice;
                final var formattedText = String.format(template,
                    amount, a, b, consumed, dblPrice, price.getSince().format(FORMATTER));
                return ExpenseEntry.of(amount, formattedText);
            })
            .orElseThrow();
    }

    @Override
    public void processAnswer(String input, Calculation calculation) {
        // Nothing to do here
    }
}

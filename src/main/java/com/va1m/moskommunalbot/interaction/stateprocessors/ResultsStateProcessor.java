package com.va1m.moskommunalbot.interaction.stateprocessors;

import com.google.inject.Inject;
import com.va1m.moskommunalbot.model.InteractionMessage;
import com.va1m.moskommunalbot.model.Calculation;
import com.va1m.moskommunalbot.interaction.State;
import com.va1m.moskommunalbot.interaction.TimeService;
import com.va1m.moskommunalbot.priceproviders.ColdWaterPricesProvider;
import com.va1m.moskommunalbot.priceproviders.ElectricityPricesProvider;
import com.va1m.moskommunalbot.priceproviders.HotWaterPricesProvider;
import com.va1m.moskommunalbot.priceproviders.PriceEntry;
import com.va1m.moskommunalbot.priceproviders.WaterDisposingPricesProvider;
import lombok.RequiredArgsConstructor;

import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

/** Prepare output message text for {@link State#SHOWING_RESULTS} state and then handles user input data */
public class ResultsStateProcessor implements StateProcessor {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    final ColdWaterPricesProvider coldWater;
    final HotWaterPricesProvider hotWater;
    final WaterDisposingPricesProvider waterDisposing;
    final ElectricityPricesProvider electricity;
    final TimeService timeService;

    @RequiredArgsConstructor(staticName = "of")
    private static class ExpenseEntry {
        private final double amount;
        private final String formattedText;
    }

    /** Constructor */
    @Inject
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

        var totalAmount = 0.0D;

        var template = "- Холодная вода:%n"
            + "*%.2f руб.* за %.3f кб.м.%n"
            + "%.2f руб/кб.м. с %s%n%n";
        final var consumedColdWater =
            ((double)(calculation.getCurrentColdWaterMeters() - calculation.getLastColdWaterMeters())) / 1000.0D;
        final var coldWaterEntry = getExpenseEntry(coldWater.provide(), consumedColdWater, template);
        totalAmount += coldWaterEntry.amount;

        template = "- Горячая вода:%n"
            + "*%.2f руб.* за %.3f кб.м.%n"
            + "%.2f руб/кб.м. с %s%n%n";
        final var consumedHotWater =
            ((double)(calculation.getCurrentHotWaterMeters() - calculation.getLastHotWaterMeters())) / 1000.0D;
        final var hotWaterEntry = getExpenseEntry(hotWater.provide(), consumedHotWater, template);
        totalAmount += hotWaterEntry.amount;

        template = "- Водоотвод (канализация):%n"
            + "*%.2f руб.* за %.3f кб.м.%n"
            + "%.2f руб/кб.м. с %s%n%n";
        final var disposedWater = consumedColdWater + consumedHotWater;
        final var disposedWaterEntry = getExpenseEntry(waterDisposing.provide(), disposedWater, template);
        totalAmount += disposedWaterEntry.amount;

        template = "- Электричество:%n"
            + "*%.2f руб.* за %.1f КВт%n"
            + "%.2f руб/КВт с %s%n%n";
        final var consumedElectricity =
            ((double)(calculation.getCurrentElectricityMeters() - calculation.getLastElectricityMeters())) / 10.0D;
        final var consumedElectricityEntry =
            getExpenseEntry(electricity.provide(), consumedElectricity, template);
        totalAmount += consumedElectricityEntry.amount;

        final var formattedTotalAmount = String.format("ВСЕГО: *%.2f руб.*%n%n", totalAmount);

        final var text = "Стоимость коммунальных услуг:\n"
            + '\n'
            + coldWaterEntry.formattedText
            + hotWaterEntry.formattedText
            + disposedWaterEntry.formattedText
            + consumedElectricityEntry.formattedText
            + formattedTotalAmount
            + "Цены взяты из следующих источников:\n"
            + "- [МосВодоКанал](http://www.mosvodokanal.ru/forabonents/tariffs/)\n"
            + "- [МОЭК](https://online.moek.ru/clients/tarify-i-raschety/tarify)\n"
            + "- [МосЭнергоСбыт](https://www.mosenergosbyt.ru/individuals/tariffs-n-payments/tariffs-msk/kvartiry-i-doma-s-elektricheskimi-plitami.php)\n"
            + "\n"
            + "Для начала нового расчета наберите /new.";
        return InteractionMessage.of(text);
    }

    private ExpenseEntry getExpenseEntry(PriceEntry[] priceEntries, double consumed, String template) {
        final var today = timeService.getToday();

        return Stream.of(priceEntries)
            .filter(price -> today.compareTo(price.getSince()) >= 0)
            .filter(price -> today.compareTo(price.getTill()) <= 0)
            .findFirst()
            .map(priceEntry -> {
                final var dblPrice = ((double) priceEntry.getPrice()) / 100.0D;
                final var amount = consumed * dblPrice;
                final var formattedText = String.format(template,
                    amount, consumed, dblPrice, priceEntry.getSince().format(FORMATTER));
                return ExpenseEntry.of(amount, formattedText);
            })
            .orElseThrow();
    }

    @Override
    public void processAnswer(String input, Calculation calculation) {
        // Nothing to do here
    }
}

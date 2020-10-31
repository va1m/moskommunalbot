package com.va1m.moskommunalbot.interaction.stateprocessors;

import com.google.inject.Inject;
import com.va1m.moskommunalbot.interaction.InteractionContext;
import com.va1m.moskommunalbot.interaction.State;
import com.va1m.moskommunalbot.priceproviders.ColdWaterPricesProvider;
import com.va1m.moskommunalbot.priceproviders.ElectricityPricesProvider;
import com.va1m.moskommunalbot.priceproviders.HotWaterPricesProvider;
import com.va1m.moskommunalbot.priceproviders.PriceEntry;
import com.va1m.moskommunalbot.priceproviders.WaterDisposingPricesProvider;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

/** Prepare output message text for {@link State#SHOWING_RESULTS} state and then handles user input data */
public class ResultsStateProcessor implements StateProcessor {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    final ColdWaterPricesProvider coldWater;
    final HotWaterPricesProvider hotWater;
    final WaterDisposingPricesProvider waterDisposing;
    final ElectricityPricesProvider electricity;

    @RequiredArgsConstructor(staticName = "of")
    private static class ExpenseEntry {
        private final double amount;
        private final String formattedText;
    }

    /** Constructor */
    @Inject
    public ResultsStateProcessor(ColdWaterPricesProvider coldWater, HotWaterPricesProvider hotWater,
        WaterDisposingPricesProvider waterDisposing, ElectricityPricesProvider electricity) {

        this.coldWater = coldWater;
        this.hotWater = hotWater;
        this.waterDisposing = waterDisposing;
        this.electricity = electricity;
    }

    @Override
    public State forState() {
        return State.SHOWING_RESULTS;
    }

    @Override
    public void processInput(String input, InteractionContext interactionContext) {
        // Nothing to do here
    }

    @Override
    public String processOutput(InteractionContext interactionContext) {

        var totalAmount = 0.0D;

        var template = "- Холодная вода:%n"
            + "%.2f руб. за %d кб.м.%n"
            + "%.2f руб/кб.м. с %s%n%n";
        final var consumedColdWater =
            interactionContext.getCurrentColdWaterMeters() - interactionContext.getLatestColdWaterMeters();
        final var coldWaterEntry = getExpenseEntry(this.coldWater.provide(), consumedColdWater, template);
        totalAmount += coldWaterEntry.amount;

        template = "- Горячая вода:%n"
            + "%.2f руб. за %d кб.м.%n"
            + "%.2f руб/кб.м. с %s%n%n";
        final var consumedHotWater =
            interactionContext.getCurrentHotWaterMeters() - interactionContext.getLatestHotWaterMeters();
        final var hotWaterEntry = getExpenseEntry(hotWater.provide(), consumedHotWater, template);
        totalAmount += hotWaterEntry.amount;

        template = "- Водоотвод (канализация):%n"
            + "%.2f руб. за %d кб.м.%n"
            + "%.2f руб/кб.м. с %s%n%n";
        final var disposedWater = consumedColdWater + consumedHotWater;
        final var disposedWaterEntry = getExpenseEntry(waterDisposing.provide(), disposedWater, template);
        totalAmount += disposedWaterEntry.amount;

        template = "- Электричество:%n"
            + "%.2f руб. за %d КВт%n"
            + "%.2f руб/КВт с %s%n%n";
        final var consumedElectricity =
            interactionContext.getCurrentElectricityMeters() - interactionContext.getLatestElectricityMeters();
        final var consumedElectricityEntry =
            getExpenseEntry(electricity.provide(), consumedElectricity, template);
        totalAmount += consumedElectricityEntry.amount;

        final var formattedTotalAmount = String.format("ВСЕГО: *%.2f руб.*%n%n", totalAmount);

        return "Стоимость коммунальных услуг:\n"
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
    }

    private static ExpenseEntry getExpenseEntry(PriceEntry[] priceEntries, int consumed, String template) {
        final var today = LocalDate.now();

        return Stream.of(priceEntries)
            .filter(price -> today.compareTo(price.getSince()) >= 0)
            .filter(price -> today.compareTo(price.getTill()) <= 0)
            .findFirst()
            .map(priceEntry -> {
                final var dblPrice = ((double) priceEntry.getPrice()) / 100.0D;
                final var amount = ((double) (consumed * priceEntry.getPrice())) / 100.0D;
                final var formattedText = String.format(template,
                    amount, consumed, dblPrice, priceEntry.getSince().format(FORMATTER));
                return ExpenseEntry.of(amount, formattedText);
            })
            .orElseThrow();
    }
}
